/*******************************************************************************
 * Copyright (c) 2010 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Eclipse Foundation - initial API and implementation
 *******************************************************************************/
package org.eclipse.epp.internal.mpc.core.transport.httpclient;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.cache.HttpCacheEntrySerializer;
import org.apache.http.client.cache.HttpCacheUpdateCallback;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.DefaultHttpCacheEntrySerializer;
import org.apache.http.impl.client.cache.ManagedHttpCacheStorage;
import org.eclipse.epp.internal.mpc.core.util.TextUtil;

public class PersistentHttpCacheStorage extends ManagedHttpCacheStorage {

	private static final String DATA_FILE_EXTENSION = ".data";

	private final File cacheDir;

	private final MessageDigest digest;

	private final AtomicBoolean active;

	private final Map<String, HttpCacheEntry> entries;

	private final int limit;

	private final HttpCacheEntrySerializer serializer;

	public PersistentHttpCacheStorage(CacheConfig config, File cacheDir)
			throws UnsupportedOperationException {
		this(config, cacheDir, new DefaultHttpCacheEntrySerializer());
	}

	public PersistentHttpCacheStorage(CacheConfig config, File cacheDir, HttpCacheEntrySerializer serializer)
			throws UnsupportedOperationException {
		super(config);
		this.limit = config.getMaxCacheEntries();
		this.cacheDir = cacheDir;
		this.digest = createDigest();
		this.active = new AtomicBoolean(true);
		this.entries = accessEntries();
		this.serializer = serializer;
	}

	@SuppressWarnings("unchecked")
	private Map<String, HttpCacheEntry> accessEntries() {
		try {
			Field entriesField = ManagedHttpCacheStorage.class.getDeclaredField("entries"); //$NON-NLS-1$
			entriesField.setAccessible(true);
			return (Map<String, HttpCacheEntry>) entriesField.get(this);
		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}

	private static MessageDigest createDigest() throws UnsupportedOperationException {
		MessageDigest digest = null;
		NoSuchAlgorithmException error = null;
		for (String algo : new String[] { "SHA-256", "SHA-1", "MD5" }) {
			try {
				digest = MessageDigest.getInstance(algo);
			} catch (NoSuchAlgorithmException e) {
				if (error == null) {
					error = e;
				} else {
					error.addSuppressed(e);
				}
			}
		}
		if (error != null && digest == null) {
			throw new UnsupportedOperationException(error);
		}
		return digest;
	}

	@Override
	public synchronized void putEntry(String url, HttpCacheEntry entry) throws IOException {
		putEntry(url, entry, true);
	}

	private void putEntry(String url, HttpCacheEntry entry, boolean update) throws IOException {
		Entry<String, HttpCacheEntry> removeCandidate = getRemoveCandidate(url);
		HttpCacheEntry existingEntry = super.getEntry(url);//don't load needlessly
		super.putEntry(url, entry);
		try {
			if (update) {
				if (existingEntry == null) {
					didAdd(url, entry);
				} else if (existingEntry != entry) {
					didUpdate(url, existingEntry, entry);
				}
			}
		} finally {
			handleRemoveOldest(removeCandidate);
		}
	}

	private void handleRemoveOldest(Entry<String, HttpCacheEntry> removeCandidate) {
		if (removeCandidate != null) {
			HttpCacheEntry potentiallyRemovedEntry = entries.get(removeCandidate.getKey());
			if (potentiallyRemovedEntry == null) {
				didRemove(removeCandidate.getKey(), removeCandidate.getValue());
			}
		}
	}

	private Entry<String, HttpCacheEntry> getRemoveCandidate(String url) {
		Entry<String, HttpCacheEntry> removeCandidate = null;
		if (entries.size()==limit && !entries.isEmpty()) {
			for (Entry<String, HttpCacheEntry> oldEntry : entries.entrySet()) {
				if (!url.equals(oldEntry.getKey())) {
					removeCandidate = oldEntry;
					break;
				}
			}
		}
		return removeCandidate;
	}

	private void didUpdate(String url, HttpCacheEntry existingEntry, HttpCacheEntry entry) throws IOException {
		if (existingEntry != entry) {
			saveEntry(url, entry);
		}
	}

	private void saveEntry(String url, HttpCacheEntry entry) throws IOException {
		File file = getFile(url);
		saveEntry(file, entry);
	}

	private void saveEntry(File file, HttpCacheEntry entry) throws IOException {
		file.getParentFile().mkdirs();
		try (FileOutputStream fos = new FileOutputStream(file)) {
			serializer.writeTo(entry, fos);
		}
	}

	private void didAdd(String url, HttpCacheEntry entry) throws IOException {
		saveEntry(url, entry);
	}

	private void didRemove(String key, HttpCacheEntry value) {
		deleteEntry(key);
	}

	private void deleteEntry(String key) {
		File file = getFile(key);
		if (file.exists()) {
			delete(file);
		}
	}

	@Override
	public synchronized HttpCacheEntry getEntry(String url) throws IOException {
		HttpCacheEntry entry = super.getEntry(url);
		if (entry == null) {
			entry = loadEntry(url);
			if (entry != null) {
				putEntry(url, entry, false);
			}
		}
		return entry;
	}

	private HttpCacheEntry loadEntry(String url) throws IOException {
		File file = getFile(url);
		if (file.isFile()) {
			return loadEntry(file);
		}
		return null;
	}

	private HttpCacheEntry loadEntry(File file) throws IOException {
		IOException error = null;
		try (FileInputStream fis = new FileInputStream(file)) {
			return serializer.readFrom(fis);
		} catch (IOException ex) {
			error = ex;
			throw error;
		} finally {
			if (error != null) {
				delete(file);
			}
		}
	}

	@Override
	public synchronized void removeEntry(String url) throws IOException {
		HttpCacheEntry entry = super.getEntry(url);//don't unnecessarily load the entry
		super.removeEntry(url);
		didRemove(url, entry);//even if entry is null, it might exist on disk...
	}

	@Override
	public void updateEntry(String url, HttpCacheUpdateCallback callback) throws IOException {
		Entry<String, HttpCacheEntry> removeCandidate = getRemoveCandidate(url);
		HttpCacheEntry oldEntry = null;
		try {
			oldEntry = getEntry(url);
		} catch (IOException e) {
			//ignore
		}
		try {
			super.updateEntry(url, callback);
			HttpCacheEntry newEntry = super.getEntry(url);//get updated entry
			if (newEntry == null) {
				didRemove(url, oldEntry);
			} else if (oldEntry == null) {
				didAdd(url, newEntry);
			} else if (newEntry != oldEntry) {
				didUpdate(url, oldEntry, newEntry);
			}
		} finally {
			handleRemoveOldest(removeCandidate);
		}
	}

	@Override
	public void shutdown() {
		if (this.active.compareAndSet(true, false)) {
			synchronized (this) {
				removeEntryFiles();
			}
			super.shutdown();
		}
	}

	@Override
	public void cleanResources() {
		super.cleanResources();
		synchronized (this) {
			Set<File> usedFiles = new HashSet<File>();
			for (String key : entries.keySet()) {
				usedFiles.add(getFile(key));
			}
			File[] dataFiles = listDataFiles();
			if (dataFiles == null || dataFiles.length == 0) {
				return;
			}
			for (File file : dataFiles) {
				if (usedFiles.contains(file)) {
					continue;
				}
				delete(file);
			}
		}
	}

	private File getFile(String requestId) {
		final StringBuilder buffer = new StringBuilder();
		final int len = Math.min(requestId.length(), 80);
		boolean didDash = false;
		for (int i = 0; i < len; i++) {
			final char ch = requestId.charAt(i);
			if (Character.isLetterOrDigit(ch) || ch == '.' || ch == '_') {
				didDash = false;
				buffer.append(ch);
			} else if (ch == '?' || ch == '#') {
				break;
			} else if (!didDash) {
				didDash = true;
				buffer.append('-');
			}
		}
		buffer.append('.');
		synchronized (digest) {
			digest.reset();
			byte[] hash = digest.digest(requestId.getBytes(StandardCharsets.UTF_8));
			TextUtil.toHexString(hash, buffer);
		}
		buffer.append(DATA_FILE_EXTENSION);
		return new File(cacheDir, buffer.toString());
	}

	private static void delete(File file) {
		if (!file.delete()) {
			file.deleteOnExit();
		}
	}

	private File[] listDataFiles() {
		File[] dataFiles = cacheDir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isFile() && !pathname.getName().startsWith(".") //$NON-NLS-1$
						&& pathname.getName().endsWith(DATA_FILE_EXTENSION);
			}
		});
		return dataFiles;
	}

	private void removeEntryFiles() {
		File[] dataFiles = listDataFiles();
		if (dataFiles == null || dataFiles.length == 0) {
			return;
		}
		for (File file : dataFiles) {
			delete(file);
		}
	}

	@Override
	public void close() {
		if (this.active.compareAndSet(true, false)) {
			super.close();
		}
	}
}

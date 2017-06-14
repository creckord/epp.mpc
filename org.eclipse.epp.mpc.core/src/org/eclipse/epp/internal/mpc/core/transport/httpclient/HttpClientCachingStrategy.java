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
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.cache.HeaderConstants;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.cache.HttpCacheStorage;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHeaderElement;
import org.apache.http.message.BasicHeaderValueFormatter;
import org.apache.http.protocol.HttpContext;

public class HttpClientCachingStrategy {

	private HttpCacheStorage storage;

	private final File cacheDir;

	protected HttpClientCachingStrategy(File cacheDir) {
		this(null, cacheDir);
	}

	protected HttpClientCachingStrategy(HttpCacheStorage storage, File cacheDir) {
		super();
		this.storage = storage;
		this.cacheDir = cacheDir;
	}

	public synchronized HttpCacheStorage getStorage() {
		if (storage == null) {
			storage = createStorage();
		}
		return storage;
	}

	protected HttpCacheStorage createStorage() {
		return new PersistentHttpCacheStorage(createCacheConfig(), cacheDir);
	}

	public boolean needsRefresh(String url) throws IOException {
		return false;
	}

	public Object getCacheHandle(String url) {
		try {
			HttpCacheEntry entry = getStorage().getEntry(url);
			if (entry == null) {
				return null;
			}
			Header eTagHeader = entry.getFirstHeader(HeaderConstants.ETAG);
			Header lastModifiedHeader = entry.getFirstHeader(HeaderConstants.LAST_MODIFIED);

		} catch (IOException ex) {
			return null;
		}
		return null;
	}

	public File getCacheDir() {
		return cacheDir;
	}

	public void configure(CachingHttpClientBuilder cachingBuilder) {
		CacheConfig config = createCacheConfig();
		File cacheDir = getCacheDir();

		cachingBuilder.setDeleteCache(false)
		.setCacheDir(cacheDir)
		.setHttpCacheStorage(getStorage())
		.setCacheConfig(config)
		.addInterceptorLast(new HttpResponseInterceptor() {

			@Override
			public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
				long maxStale = getMaxStaleTime();
				if (maxStale == -1) {
					return;
				}
				for (final Header h : response.getHeaders(HeaderConstants.CACHE_CONTROL)) {
					for (final HeaderElement elt : h.getElements()) {
						if (HeaderConstants.STALE_WHILE_REVALIDATE.equalsIgnoreCase(elt.getName())) {
							return;
						}
					}
				}
				BasicHeaderElement staleTimeElement = new BasicHeaderElement(
						HeaderConstants.STALE_WHILE_REVALIDATE, String.valueOf(maxStale));
				BasicHeader staleHeader = new BasicHeader(HeaderConstants.CACHE_CONTROL,
						BasicHeaderValueFormatter.formatHeaderElement(staleTimeElement, false, null));
				response.addHeader(staleHeader);
			}
		});
	}

	protected long getMaxStaleTime() {
		return TimeUnit.HOURS.toSeconds(12);
	}

	protected CacheConfig createCacheConfig() {
		CacheConfig config = CacheConfig.custom()
				.setAsynchronousWorkerIdleLifetimeSecs(60)
				.setAsynchronousWorkersCore(0)
				.setAsynchronousWorkersMax(16)
				.setRevalidationQueueSize(32)
				.setHeuristicCachingEnabled(true)
				.setHeuristicDefaultLifetime(TimeUnit.HOURS.toSeconds(1))
				.setHeuristicCoefficient(0.3f)
				.setMaxUpdateRetries(3)
				.setMaxCacheEntries(2000)
				.setMaxObjectSize(1024 * 1024)
				.setNeverCacheHTTP10ResponsesWithQueryString(false)
				.setAllow303Caching(true)
				.setSharedCache(true)
				.build();
		return config;
	}
}

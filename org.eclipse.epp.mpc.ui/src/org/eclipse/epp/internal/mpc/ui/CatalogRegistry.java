/*******************************************************************************
 * Copyright (c) 2010, 2018 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * 	The Eclipse Foundation - initial API and implementation
 * 	Yatta Solutions - bug 432803: public API
 *******************************************************************************/
package org.eclipse.epp.internal.mpc.ui;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.epp.internal.mpc.core.util.URLUtil;
import org.eclipse.epp.mpc.core.model.ICatalogBranding;
import org.eclipse.epp.mpc.core.model.INews;
import org.eclipse.epp.mpc.core.service.ICatalogService;
import org.eclipse.epp.mpc.ui.CatalogDescriptor;

/**
 * @author David Green
 * @author Carsten Reckord
 */
public class CatalogRegistry {

	private static CatalogRegistry instance;

	public synchronized static CatalogRegistry getInstance() {
		if (instance == null) {
			instance = new CatalogRegistry();
		}
		return instance;
	}

	private final List<CatalogDescriptor> catalogDescriptors = new CopyOnWriteArrayList<>();

	private final Map<String, INews> catalogNews = new HashMap<>();

	public CatalogRegistry() {
		catalogDescriptors.addAll(new CatalogExtensionPointReader().getCatalogDescriptors());
	}

	public void register(CatalogDescriptor catalogDescriptor) {
		catalogDescriptors.add(new CatalogDescriptor(catalogDescriptor));
	}

	public void unregister(CatalogDescriptor catalogDescriptor) {
		catalogDescriptors.remove(catalogDescriptor);
		removeCatalogNews(catalogDescriptor);
	}

	public List<CatalogDescriptor> getCatalogDescriptors() {
		return Collections.unmodifiableList(catalogDescriptors);
	}

	/**
	 * @deprecated use {@link CatalogDescriptor#setCatalogBranding(ICatalogBranding)
	 *             descriptor.setCatalogBranding(branding)}
	 */
	@Deprecated
	public void addCatalogBranding(CatalogDescriptor descriptor, ICatalogBranding branding) {
		if (descriptor != null) {
			descriptor.setCatalogBranding(branding);
		}
	}

	/**
	 * @deprecated use {@link CatalogDescriptor#getCatalogBranding() descriptor.getCatalogBranding()}
	 */
	@Deprecated
	public ICatalogBranding getCatalogBranding(CatalogDescriptor descriptor) {
		return descriptor == null ? null : descriptor.getCatalogBranding();
	}

	// manage the predefined news configuration here, since that isn't supposed to become API
	public void addCatalogNews(CatalogDescriptor descriptor, INews news) {
		catalogNews.put(descriptor.getUrl().toExternalForm(), news);
	}

	private void removeCatalogNews(CatalogDescriptor descriptor) {
		catalogNews.remove(descriptor.getUrl().toExternalForm());
	}

	public INews getCatalogNews(CatalogDescriptor descriptor) {
		return catalogNews.get(descriptor.getUrl().toExternalForm());
	}

	public CatalogDescriptor findCatalogDescriptor(String url) {
		if (url == null || url.length() == 0) {
			return null;
		}
		CatalogDescriptor matchingDescriptor = doFindCatalogDescriptor(url);
		url = URLUtil.toggleHttps(url);
		CatalogDescriptor matchingToggledDescriptor = doFindCatalogDescriptor(url);
		if (matchingDescriptor == null) {
			matchingDescriptor = matchingToggledDescriptor;
		} else if (matchingToggledDescriptor != null) {
			String matchingUrl = matchingDescriptor.getUrl().toExternalForm();
			String matchingToggledUrl = matchingToggledDescriptor.getUrl().toExternalForm();
			int protocolDelta = matchingToggledUrl.startsWith("https") ? 1 : -1; //$NON-NLS-1$
			if (matchingToggledUrl.length() - protocolDelta > matchingUrl.length()) {
				matchingDescriptor = matchingToggledDescriptor;
			}
		}
		return matchingDescriptor;
	}

	private CatalogDescriptor doFindCatalogDescriptor(String url) {
		CatalogDescriptor matchingDescriptor = null;
		String matchingUrl = null;
		for (CatalogDescriptor catalogDescriptor : catalogDescriptors) {
			String descriptorUrl = catalogDescriptor.getUrl().toExternalForm();
			if (url.startsWith(descriptorUrl)
					&& (matchingUrl == null || matchingUrl.length() < descriptorUrl.length())) {
				String suffix = url.substring(descriptorUrl.length());
				if (suffix.length() > 0 && suffix.charAt(0) == '/') {
					suffix = suffix.substring(1);
				}
				if (suffix.startsWith(ICatalogService.DEDICATED_CATALOG_HOSTING_SEGMENT)) {
					continue;
				}
				matchingDescriptor = catalogDescriptor;
				matchingUrl = descriptorUrl;
			}
		}
		return matchingDescriptor;
	}
}

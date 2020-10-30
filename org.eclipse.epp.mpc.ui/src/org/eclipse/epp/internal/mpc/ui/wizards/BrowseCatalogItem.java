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
 * 	Yatta Solutions - bug 397004, bug 432803: public API, bug 461603: featured market
 *******************************************************************************/
package org.eclipse.epp.internal.mpc.ui.wizards;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.epp.internal.mpc.core.service.DefaultMarketplaceService;
import org.eclipse.epp.internal.mpc.ui.MarketplaceClientUi;
import org.eclipse.epp.internal.mpc.ui.catalog.MarketplaceCategory;
import org.eclipse.epp.internal.mpc.ui.wizards.MarketplaceViewer.ContentType;
import org.eclipse.epp.mpc.core.model.ICategory;
import org.eclipse.epp.mpc.core.model.IMarket;
import org.eclipse.epp.mpc.ui.CatalogDescriptor;
import org.eclipse.equinox.internal.p2.ui.discovery.wizards.DiscoveryResources;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.statushandlers.StatusManager;

/**
 * @author David Green
 * @author Carsten Reckord
 */
@SuppressWarnings("unused")
public class BrowseCatalogItem extends UserActionViewerItem<CatalogDescriptor> {

	private static final String TID = "tid:"; //$NON-NLS-1$

	private final MarketplaceCategory category;

	private final IMarketplaceWebBrowser browser;

	public BrowseCatalogItem(Composite parent, DiscoveryResources resources, IShellProvider shellProvider,
			IMarketplaceWebBrowser browser, MarketplaceCategory category, CatalogDescriptor element,
			MarketplaceViewer viewer) {
		super(parent, resources, shellProvider, element, viewer);
		this.browser = browser;
		this.category = category;
		createContent();
	}

	@Override
	protected String getLinkText() {
		if (getViewer().getQueryContentType() == ContentType.SEARCH
				|| getViewer().getQueryContentType() == ContentType.FEATURED_MARKET) {
			return NLS.bind(Messages.BrowseCatalogItem_browseMoreLink, category.getMatchCount());
		} else {
			return Messages.BrowseCatalogItem_browseMoreLinkNoCount;
		}
	}

	@Override
	protected String getLinkToolTipText() {
		return NLS.bind(Messages.BrowseCatalogItem_openUrlBrowser, getData().getUrl());
	}

	@Override
	protected void actionPerformed(Object data) {
		openMarketplace();
	}

	protected void openMarketplace() {
		openMarketplace(getData(), getViewer(), browser);
	}

	protected static void openMarketplace(CatalogDescriptor catalogDescriptor, MarketplaceViewer viewer,
			IMarketplaceWebBrowser browser) {

		try {
			String url = getMarketplaceUrl(catalogDescriptor, viewer);
			browser.openUrl(url);
		} catch (URISyntaxException e) {
			String message = String.format(Messages.BrowseCatalogItem_cannotOpenBrowser);
			IStatus status = new Status(IStatus.ERROR, MarketplaceClientUi.BUNDLE_ID, IStatus.ERROR, message, e);
			MarketplaceClientUi.handle(status, StatusManager.SHOW | StatusManager.BLOCK | StatusManager.LOG);
		}
	}

	private static String getMarketplaceUrl(CatalogDescriptor catalogDescriptor, MarketplaceViewer viewer)
			throws URISyntaxException {
		URL url = catalogDescriptor.getUrl();
		try {
			ContentType contentType = viewer.getQueryContentType();
			if (contentType == ContentType.SEARCH) {
				String queryText = viewer.getQueryText();
				ICategory queryCategory = viewer.getQueryCategory();
				IMarket queryMarket = viewer.getQueryMarket();
				String path = new DefaultMarketplaceService(url).computeRelativeSearchUrl(queryMarket, queryCategory,
						queryText, false);
				if (path != null) {
					url = new URL(url, path);
				}
			}
		} catch (IllegalArgumentException e) {
			// should never happen
			MarketplaceClientUi.error(e);
		} catch (MalformedURLException e) {
			// should never happen
			MarketplaceClientUi.error(e);
		}

		URI uri = url.toURI();
		return uri.toString();
	}

	@Override
	protected MarketplaceViewer getViewer() {
		return (MarketplaceViewer) super.getViewer();
	}
}

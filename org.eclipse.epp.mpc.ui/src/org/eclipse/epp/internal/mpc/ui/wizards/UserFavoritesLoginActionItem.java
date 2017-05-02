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
package org.eclipse.epp.internal.mpc.ui.wizards;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.epp.internal.mpc.ui.catalog.MarketplaceCatalog;
import org.eclipse.epp.internal.mpc.ui.catalog.UserActionCatalogItem;
import org.eclipse.equinox.internal.p2.ui.discovery.wizards.DiscoveryResources;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;

public class UserFavoritesLoginActionItem extends UserActionViewerItem<UserActionCatalogItem> {
	public UserFavoritesLoginActionItem(Composite parent, DiscoveryResources resources, IShellProvider shellProvider,
			UserActionCatalogItem element, MarketplaceViewer viewer) {
		super(parent, resources, shellProvider, element, viewer);
		createContent();
	}

	@Override
	protected String getLinkText() {
		String linkText = "<a>" + Messages.UserFavoritesLoginActionItem_logInActionLabel + "</a>"; //$NON-NLS-1$ //$NON-NLS-2$
		UserActionCatalogItem loginItem = getData();
		if (loginItem != null && loginItem.getData() != null && !"".equals(loginItem.getData())) { //$NON-NLS-1$
			String loginMessage = (String) loginItem.getData();
			loginMessage = loginMessage.trim();
			linkText = NLS.bind(Messages.UserFavoritesLoginActionItem_retryLoginLabel, loginMessage);
		}
		return linkText;
	}

	@Override
	protected void actionPerformed(Object data) {
		final MarketplaceCatalog catalog = getViewer().getCatalog();
		catalog.userFavorites(true, new NullProgressMonitor());
		getViewer().updateContents();
	}

	@Override
	protected MarketplaceViewer getViewer() {
		return (MarketplaceViewer) super.getViewer();
	}

}
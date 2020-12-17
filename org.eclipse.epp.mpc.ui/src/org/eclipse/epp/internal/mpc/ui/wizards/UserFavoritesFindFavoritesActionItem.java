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
 *     The Eclipse Foundation - initial API and implementation
 *******************************************************************************/
package org.eclipse.epp.internal.mpc.ui.wizards;

import org.eclipse.epp.internal.mpc.ui.MarketplaceClientUiResources;
import org.eclipse.epp.internal.mpc.ui.catalog.UserActionCatalogItem;
import org.eclipse.epp.mpc.ui.CatalogDescriptor;
import org.eclipse.equinox.internal.p2.discovery.model.Icon;
import org.eclipse.swt.widgets.Composite;

public class UserFavoritesFindFavoritesActionItem extends AbstractUserActionItem {

	private static final int IMPORT_BUTTON_ID = 1;

	private static final int BROWSE_BUTTON_ID = 0;

	private final MarketplaceWizard wizard;

	private final CatalogDescriptor descriptor;

	public UserFavoritesFindFavoritesActionItem(Composite parent, MarketplaceDiscoveryResources resources,
			UserActionCatalogItem connector, MarketplacePage page) {
		super(parent, resources, connector, page.getViewer());
		this.wizard = page.getWizard();
		this.descriptor = wizard.getConfiguration().getCatalogDescriptor();
	}

	@Override
	protected String getNameLabelText() {
		return Messages.UserFavoritesFindFavoritesActionItem_title;
	}

	@Override
	protected String getDescriptionText() {
		return Messages.UserFavoritesFindFavoritesActionItem_noFavoritesYetMessage;
	}

	@Override
	protected Icon getIcon() {
		String path = REGISTRY_SCHEME + MarketplaceClientUiResources.ACTION_ICON_FAVORITES;
		return createIcon(path);
	}

	@Override
	protected String getSublineText() {
		return Messages.UserFavoritesFindFavoritesActionItem_subline;
	}

	@Override
	protected void createButtons(Composite parent) {
		createButton(parent, Messages.UserFavoritesFindFavoritesActionItem_BrowseButtonLabel, Messages.UserFavoritesFindFavoritesActionItem_BrowseButtonTooltip,
				BROWSE_BUTTON_ID);
		createButton(parent, Messages.UserFavoritesAbstractImportActionItem_importFavoritesActionLabel,
				Messages.UserFavoritesAbstractImportActionItem_importFavoritesTooltip,
				IMPORT_BUTTON_ID);
	}

	@Override
	protected void buttonPressed(int id) {
		if (id == BROWSE_BUTTON_ID) {
			BrowseCatalogItem.openMarketplace(descriptor, (MarketplaceViewer) getViewer(), wizard);
		} else {
			wizard.importFavorites(null);
		}
	}
}

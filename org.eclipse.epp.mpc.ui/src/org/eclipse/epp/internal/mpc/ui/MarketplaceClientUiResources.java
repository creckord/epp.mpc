/*******************************************************************************
 * Copyright (c) 2018, 2020 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     The Eclipse Foundation - initial API and implementation
 *******************************************************************************/
package org.eclipse.epp.internal.mpc.ui;

import static org.eclipse.jface.resource.ResourceLocator.imageDescriptorFromBundle;

import org.eclipse.epp.internal.mpc.ui.catalog.ResourceProvider;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

@Component(name = "org.eclipse.epp.mpc.ui.resources", service = { MarketplaceClientUiResources.class })
public class MarketplaceClientUiResources {

	/**
	 * image registry key
	 */
	public static final String IU_ICON_UPDATE = "IU_ICON_UPDATE"; //$NON-NLS-1$

	/**
	 * image registry key
	 */
	public static final String IU_ICON_INSTALL = "IU_ICON_INSTALL"; //$NON-NLS-1$

	/**
	 * image registry key
	 */
	public static final String IU_ICON_UNINSTALL = "IU_ICON_UNINSTALL"; //$NON-NLS-1$

	/**
	 * image registry key
	 */
	public static final String IU_ICON_DISABLED = "IU_ICON_DISABLED"; //$NON-NLS-1$

	/**
	 * image registry key
	 */
	public static final String IU_ICON = "IU_ICON"; //$NON-NLS-1$

	/**
	 * image registry key
	 */
	public static final String IU_ICON_ERROR = "IU_ICON_ERROR"; //$NON-NLS-1$

	/**
	 * image registry key
	 */
	public static final String NEWS_ICON_UPDATE = "NEWS_ICON_UPDATE"; //$NON-NLS-1$

	/**
	 * image registry key
	 */
	public static final String NO_ICON_PROVIDED = "NO_ICON_PROVIDED"; //$NON-NLS-1$

	public static final String NO_ICON_PROVIDED_CATALOG = "NO_ICON_PROVIDED_CATALOG"; //$NON-NLS-1$

	public static final String DEFAULT_MARKETPLACE_ICON = "DEFAULT_MARKETPLACE_ICON"; //$NON-NLS-1$

	public static final String ACTION_ICON_FAVORITES = "ACTION_ICON_FAVORITES"; //$NON-NLS-1$

	public static final String ACTION_ICON_LOGIN = "ACTION_ICON_LOGIN"; //$NON-NLS-1$

	public static final String ACTION_ICON_WARNING = "ACTION_ICON_WARNING"; //$NON-NLS-1$

	public static final String ACTION_ICON_UPDATE = "ACTION_ICON_UPDATE"; //$NON-NLS-1$

	public static final String FAVORITES_LIST_ICON = "FAVORITES_LIST_ICON"; //$NON-NLS-1$

	public static final String ITEM_ICON_STAR = "ITEM_ICON_STAR"; //$NON-NLS-1$

	public static final String ITEM_ICON_STAR_SELECTED = "ITEM_ICON_STAR_SELECTED"; //$NON-NLS-1$

	public static final String ITEM_ICON_SHARE = "ITEM_ICON_SHARE"; //$NON-NLS-1$

	private static MarketplaceClientUiResources instance;

	private ResourceProvider resourceProvider;

	private AbstractUIPlugin delegate;

	@Activate
	protected void activate(ComponentContext context) throws Exception {
		delegate = new AbstractUIPlugin() {
			@Override
			protected void initializeImageRegistry(ImageRegistry reg) {
				super.initializeImageRegistry(reg);
				MarketplaceClientUiResources.this.initializeImageRegistry(reg);
			}
		};
		delegate.start(context.getBundleContext());
		resourceProvider = new ResourceProvider();
		synchronized (MarketplaceClientUiResources.class) {
			if (instance == null) {
				instance = this;
			}
		}
	}

	@Deactivate
	protected void deactivate(ComponentContext context) throws Exception {
		synchronized (MarketplaceClientUiResources.class) {
			if (instance == this) {
				instance = null;
			}
		}
		if (resourceProvider != null) {
			resourceProvider.dispose();
			resourceProvider = null;
		}
		if (delegate != null) {
			delegate.stop(context.getBundleContext());
		}
		delegate = null;
	}

	public IPreferenceStore getPreferenceStore() {
		return delegate == null ? null : delegate.getPreferenceStore();
	}

	public ImageRegistry getImageRegistry() {
		return delegate == null ? null : delegate.getImageRegistry();
	}

	public ResourceProvider getResourceProvider() {
		return resourceProvider;
	}

	public IDialogSettings getDialogSettings() {
		return delegate == null ? null : delegate.getDialogSettings();
	}

	protected void initializeImageRegistry(ImageRegistry imageRegistry) {

		imageRegistry.put(NO_ICON_PROVIDED,
				imageDescriptorFromBundle(MarketplaceClientUiResources.class, "icons/noiconprovided.png") //$NON-NLS-1$
				.get());
		imageRegistry.put(NO_ICON_PROVIDED_CATALOG,
				imageDescriptorFromBundle(MarketplaceClientUiResources.class, "icons/noiconprovided32.png").get()); //$NON-NLS-1$
		imageRegistry.put(DEFAULT_MARKETPLACE_ICON,
				imageDescriptorFromBundle(MarketplaceClientUiResources.class, "icons/marketplace_banner.png").get()); //$NON-NLS-1$
		imageRegistry.put(IU_ICON,
				imageDescriptorFromBundle(MarketplaceClientUiResources.class, "icons/iu_obj.png").get()); //$NON-NLS-1$
		imageRegistry.put(IU_ICON_UPDATE,
				imageDescriptorFromBundle(MarketplaceClientUiResources.class, "icons/iu_update_obj.png").get()); //$NON-NLS-1$
		imageRegistry.put(IU_ICON_INSTALL,
				imageDescriptorFromBundle(MarketplaceClientUiResources.class, "icons/iu_install_obj.png").get()); //$NON-NLS-1$
		imageRegistry.put(IU_ICON_UNINSTALL,
				imageDescriptorFromBundle(MarketplaceClientUiResources.class, "icons/iu_uninstall_obj.png").get()); //$NON-NLS-1$
		imageRegistry.put(IU_ICON_DISABLED,
				imageDescriptorFromBundle(MarketplaceClientUiResources.class, "icons/iu_disabled_obj.png").get()); //$NON-NLS-1$
		{
			ImageDescriptor errorOverlay = PlatformUI.getWorkbench()
					.getSharedImages()
					.getImageDescriptor(ISharedImages.IMG_DEC_FIELD_ERROR);
			Image iuImage = imageRegistry.get(IU_ICON);
			DecorationOverlayIcon iuErrorIcon = new DecorationOverlayIcon(iuImage, errorOverlay,
					IDecoration.BOTTOM_RIGHT);
			imageRegistry.put(IU_ICON_ERROR, iuErrorIcon);
		}

		imageRegistry.put(NEWS_ICON_UPDATE,
				imageDescriptorFromBundle(MarketplaceClientUiResources.class, "icons/news_update.png").get()); //$NON-NLS-1$
		imageRegistry.put(ITEM_ICON_STAR,
				imageDescriptorFromBundle(MarketplaceClientUiResources.class, "icons/star.png").get()); //$NON-NLS-1$
		imageRegistry.put(ITEM_ICON_STAR_SELECTED,
				imageDescriptorFromBundle(MarketplaceClientUiResources.class, "icons/star-selected.png").get()); //$NON-NLS-1$
		imageRegistry.put(ITEM_ICON_SHARE,
				imageDescriptorFromBundle(MarketplaceClientUiResources.class, "icons/share.png").get()); //$NON-NLS-1$
		imageRegistry.put(ACTION_ICON_FAVORITES,
				imageDescriptorFromBundle(MarketplaceClientUiResources.class, "icons/action-item-favorites.png").get()); //$NON-NLS-1$
		imageRegistry.put(ACTION_ICON_LOGIN,
				imageDescriptorFromBundle(MarketplaceClientUiResources.class, "icons/action-item-login.png").get()); //$NON-NLS-1$
		imageRegistry.put(ACTION_ICON_WARNING,
				imageDescriptorFromBundle(MarketplaceClientUiResources.class, "icons/action-item-warning.png").get()); //$NON-NLS-1$
		imageRegistry.put(ACTION_ICON_UPDATE,
				imageDescriptorFromBundle(MarketplaceClientUiResources.class, "icons/action-item-update.png").get()); //$NON-NLS-1$
		imageRegistry.put(FAVORITES_LIST_ICON,
				imageDescriptorFromBundle(MarketplaceClientUiResources.class, "icons/favorites-list.png").get()); //$NON-NLS-1$
	}

	public static synchronized MarketplaceClientUiResources getInstance() {
		if (instance == null) {
			BundleContext bundleContext = MarketplaceClientUi.getBundleContext();
			ServiceReference<MarketplaceClientUiResources> serviceReference = bundleContext == null ? null
					: bundleContext.getServiceReference(MarketplaceClientUiResources.class);
			MarketplaceClientUiResources registered = serviceReference == null ? null
					: bundleContext.getService(serviceReference);
			if (registered != null) {
				bundleContext.ungetService(serviceReference);//FIXME baaaad...
			}
			if (instance == null) {
				instance = registered;
			}
		}
		return instance;
	}

}

package org.eclipse.epp.internal.mpc.ui.wizards;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.epp.internal.mpc.ui.MarketplaceClientUiPlugin;
import org.eclipse.epp.mpc.ui.CatalogDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * @author Benjamin Muskalla
 */
public class CatalogSwitcher extends Composite implements ISelectionProvider {

	private final MarketplaceCatalogConfiguration configuration;

	private final ImageRegistry imageRegistry = new ImageRegistry();

	private final List<ISelectionChangedListener> listeners = new LinkedList<ISelectionChangedListener>();

	private CatalogDescriptor selection;

	private Composite marketplaceArea;

	public CatalogSwitcher(Composite parent, int style, MarketplaceCatalogConfiguration configuration) {
		super(parent, style);
		this.configuration = configuration;
		createContents(this);
	}

	private void createContents(final Composite parent) {
		final ScrolledComposite scrollArea = new ScrolledComposite(parent, SWT.V_SCROLL);
		scrollArea.setLayout(new FillLayout());

		marketplaceArea = new Composite(scrollArea, SWT.NONE);

		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		marketplaceArea.setLayout(layout);

		Color white = getDisplay().getSystemColor(SWT.COLOR_WHITE);
		setBackground(white);
		marketplaceArea.setBackground(white);
		scrollArea.setBackground(white);

		List<CatalogDescriptor> catalogDescriptors = configuration.getCatalogDescriptors();
		for (CatalogDescriptor catalogDescriptor : catalogDescriptors) {
			createMarketplace(marketplaceArea, catalogDescriptor);
		}

		scrollArea.setContent(marketplaceArea);
		scrollArea.setExpandVertical(true);
		scrollArea.setExpandHorizontal(true);
		scrollArea.setMinHeight(40);
		scrollArea.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				Rectangle r = parent.getClientArea();
				scrollArea.setMinSize(marketplaceArea.computeSize(r.width, SWT.DEFAULT));
			}
		});
	}

	private void createMarketplace(Composite composite, final CatalogDescriptor catalogDescriptor) {
		Composite container = new Composite(composite, SWT.NONE);
		Color white = getDisplay().getSystemColor(SWT.COLOR_WHITE);
		container.setBackground(white);
		container.setData(catalogDescriptor);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		container.setLayout(layout);

		Image image = getCatalogIcon(catalogDescriptor);
		final Label label = new Label(container, SWT.NONE);
		label.setImage(image);
		label.setBackground(white);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				selection = catalogDescriptor;
				refreshSelection();
				fireSelectionChanged();
			}
		});
		CatalogToolTip.attachCatalogToolTip(label, catalogDescriptor);
	}

	private void fireSelectionChanged() {
		for (ISelectionChangedListener listener : listeners) {
			SelectionChangedEvent event = new SelectionChangedEvent(this, new StructuredSelection(selection));
			listener.selectionChanged(event);
		}
	}

	private Image getCatalogIcon(final CatalogDescriptor catalogDescriptor) {
		String key = catalogDescriptor.getUrl().toExternalForm();
		Image image = imageRegistry.get(key);
		if (image == null) {
			ImageDescriptor catalogIcon = catalogDescriptor.getIcon();
			if (catalogIcon == null) {
				return getDefaultCatalogImage();
			}
			imageRegistry.put(key, catalogIcon);
			image = imageRegistry.get(key);
		}
		return image;
	}

	private Image getDefaultCatalogImage() {
		return MarketplaceClientUiPlugin.getInstance()
		.getImageRegistry()
				.get(MarketplaceClientUiPlugin.NO_ICON_PROVIDED_CATALOG);
	}

	@Override
	public void dispose() {
		imageRegistry.dispose();
		super.dispose();
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.add(listener);
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.remove(listener);
	}

	public ISelection getSelection() {
		return new StructuredSelection(selection);
	}

	public void setSelection(ISelection newSelection) {
		if (newSelection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) newSelection;
			this.selection = (CatalogDescriptor) structuredSelection.getFirstElement();
			refreshSelection();
		}
	}

	private void refreshSelection() {
		Control[] children = marketplaceArea.getChildren();
		for (Control control : children) {
			int color;
			if (this.selection == control.getData()) {
				color = SWT.COLOR_LIST_SELECTION;
			} else {
				color = SWT.COLOR_WHITE;
			}
			control.setBackground(getDisplay().getSystemColor(color));
			((Composite) control).getChildren()[0].setBackground(getDisplay().getSystemColor(color));
		}
	}

}
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

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.IPageChangingListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class AbstractMarketplaceWizardDialog extends WizardDialog {

	private static final class PageListener implements IPageChangingListener, IPageChangedListener {

		private Boolean forward;

		public void pageChanged(PageChangedEvent event) {
			if (forward != null) {
				boolean isForward = forward;
				reset();
				Object selectedPage = event.getSelectedPage();
				if (selectedPage instanceof IWizardPageAction) {
					((IWizardPageAction) selectedPage).enter(isForward);
				}
			}
		}

		public void handlePageChanging(PageChangingEvent event) {
			Object currentPage = event.getCurrentPage();
			if (event.doit && forward != null && currentPage instanceof IWizardPageAction) {
				event.doit = ((IWizardPageAction) currentPage).exit(forward);
				if (!event.doit) {
					forward = null;
				}
			}
		}

		public void setForward() {
			forward = true;
		}

		public void setBackward() {
			forward = false;
		}

		public void reset() {
			forward = null;
		}
	}

	private Button backButton;
	private Button nextButton;

	private Button cancelButton;

	private Button finishButton;

	private final PageListener pageListener;

	public AbstractMarketplaceWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
		pageListener = new PageListener();
		addPageChangingListener(pageListener);
		addPageChangedListener(pageListener);
	}

	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		Button button = super.createButton(parent, id, label, defaultButton);
		switch (id) {
		case IDialogConstants.NEXT_ID:
			nextButton = button;
			break;
		case IDialogConstants.BACK_ID:
			backButton = button;
			break;
		case IDialogConstants.CANCEL_ID:
			cancelButton = button;
			break;
		case IDialogConstants.FINISH_ID:
			finishButton = button;
			break;
		}
		return button;
	}

	@Override
	protected void backPressed() {
		IWizardPage fromPage = getCurrentPage();
		pageListener.setBackward();
		try {
			super.backPressed();
		} finally {
			pageListener.reset();
		}
		if (fromPage instanceof FeatureSelectionWizardPage
				&& ((FeatureSelectionWizardPage) fromPage).isInRemediationMode()) {
			((FeatureSelectionWizardPage) fromPage).flipToDefaultComposite();
		}
	}

	@Override
	protected void nextPressed() {
		pageListener.setForward();
		try {
			super.nextPressed();
		} finally {
			pageListener.reset();
		}
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		if (backButton != null || nextButton != null) {
			AccessibleAdapter adapter = new AccessibleAdapter() {
				@Override
				public void getName(AccessibleEvent e) {
					final Button button = (Button) ((Accessible) e.getSource()).getControl();
					final String text = button.getText();
					e.result = text.replace('<', ' ').replace('>', ' ');
				}
			};
			if (backButton != null) {
				backButton.getAccessible().addAccessibleListener(adapter);
			}
			if (nextButton != null) {
				nextButton.getAccessible().addAccessibleListener(adapter);
			}
		}
	}

	@Override
	public void updateButtons() {
		super.updateButtons();
		IWizardPage currentPage = getCurrentPage();
		if (currentPage != null) {
			Composite buttonBar = null;
			boolean buttonsChanged = false;
			if (nextButton != null) {
				String nextButtonLabel = getNextButtonLabel(currentPage);
				if (!nextButtonLabel.equals(nextButton.getText())) {
					nextButton.setText(nextButtonLabel);
					setButtonLayoutData(nextButton);
					buttonsChanged = true;
					buttonBar = nextButton.getParent();
				}
			}
			if (backButton != null) {
				String backButtonLabel = getBackButtonLabel(currentPage);
				if (!backButtonLabel.equals(backButton.getText())) {
					backButton.setText(backButtonLabel);
					setButtonLayoutData(backButton);
					buttonsChanged = true;
					buttonBar = backButton.getParent();
				}
			}
			if (cancelButton != null) {
				String cancelButtonLabel = getCancelButtonLabel(currentPage);
				if (!cancelButtonLabel.equals(cancelButton.getText())) {
					cancelButton.setText(cancelButtonLabel);
					setButtonLayoutData(cancelButton);
					buttonsChanged = true;
					buttonBar = cancelButton.getParent();
				}
			}
			if (finishButton != null) {
				String finishButtonLabel = getFinishButtonLabel(currentPage);
				if (!finishButtonLabel.equals(finishButton.getText())) {
					finishButton.setText(finishButtonLabel);
					setButtonLayoutData(finishButton);
					buttonsChanged = true;
					buttonBar = finishButton.getParent();
				}
			}
			if (buttonsChanged && buttonBar != null) {
				buttonBar.layout(true);
			}
		}
	}

	public String getNextButtonLabel(IWizardPage page) {
		if (page instanceof IWizardButtonLabelProvider) {
			IWizardButtonLabelProvider labelProvider = (IWizardButtonLabelProvider) page;
			String nextButtonLabel = labelProvider.getNextButtonLabel();
			if (nextButtonLabel != null) {
				return nextButtonLabel;
			}
		}
		return IDialogConstants.NEXT_LABEL;
	}

	public String getBackButtonLabel(IWizardPage page) {
		if (page instanceof IWizardButtonLabelProvider) {
			IWizardButtonLabelProvider labelProvider = (IWizardButtonLabelProvider) page;
			String backButtonLabel = labelProvider.getBackButtonLabel();
			if (backButtonLabel != null) {
				return backButtonLabel;
			}
		}
		return IDialogConstants.BACK_LABEL;
	}

	public String getFinishButtonLabel(IWizardPage page) {
		return IDialogConstants.FINISH_LABEL;
	}

	public String getCancelButtonLabel(IWizardPage page) {
		return IDialogConstants.CANCEL_LABEL;
	}

}
/*******************************************************************************
 * Copyright (c) 2010, 2019 The Eclipse Foundation and others.
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
package org.eclipse.epp.mpc.tests.ui.wizard;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.allOf;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellIsActive;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.epp.internal.mpc.core.model.Node;
import org.eclipse.epp.internal.mpc.ui.commands.MarketplaceWizardCommand;
import org.eclipse.epp.internal.mpc.ui.wizards.AbstractTagFilter;
import org.eclipse.epp.internal.mpc.ui.wizards.ComboTagFilter;
import org.eclipse.epp.internal.mpc.ui.wizards.FeatureSelectionWizardPage;
import org.eclipse.epp.internal.mpc.ui.wizards.MarketplaceWizard;
import org.eclipse.epp.internal.mpc.ui.wizards.MarketplaceWizard.WizardState;
import org.eclipse.epp.internal.mpc.ui.wizards.MarketplaceWizardDialog;
import org.eclipse.epp.mpc.core.model.ICategory;
import org.eclipse.epp.mpc.core.model.IMarket;
import org.eclipse.epp.mpc.core.model.INode;
import org.eclipse.epp.mpc.core.service.QueryHelper;
import org.eclipse.epp.mpc.tests.ui.wizard.matcher.NodeMatcher;
import org.eclipse.epp.mpc.tests.util.SWTBotComboAdapter;
import org.eclipse.equinox.internal.p2.discovery.model.Tag;
import org.eclipse.equinox.internal.p2.ui.discovery.wizards.CatalogFilter;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.junit.ScreenshotCaptureListener;
import org.eclipse.swtbot.swt.finder.matchers.WithRegex;
import org.eclipse.swtbot.swt.finder.results.ArrayResult;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferenceConstants;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.eclipse.swtbot.swt.finder.waits.WaitForObjectCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotBrowser;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCTabItem;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLink;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotStyledText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.eclipse.ui.IEditorReference;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.notification.Failure;
import org.junit.runners.model.Statement;

public abstract class AbstractMarketplaceWizardBotTest {

	protected static final INode[] TEST_NODES = new INode[] { //
			testNode("206", "http://marketplace.eclipse.org/content/mylyn", "Mylyn"), //
			testNode("311881", "http://marketplace.eclipse.org/content/eclipse-4-tools-css-spy",
					"Eclipse 4 Tools: CSS Spy"), //
			testNode("311774", "http://marketplace.eclipse.org/content/eclipse-4-tools-lightweight-css-editor",
					"Eclipse 4 Tools: Lightweight CSS Editor"), //
			testNode("311838", "http://marketplace.eclipse.org/content/eclipse-4-tools-application-model-editor", ""), //
			testNode("2780381", "http://marketplace.eclipse.org/content/trace-compass", "Trace Compass"), //
			testNode("2410217", "http://marketplace.eclipse.org/content/memory-analyzer-0", "Memory Analyzer"), //
			testNode("1336", "http://marketplace.eclipse.org/content/egit-git-team-provider",
					"EGit - Git Team Provider"), //
			testNode("2706327", "http://marketplace.eclipse.org/content/eclipse-docker-tooling",
					"Eclipse Docker Tooling"), //
			testNode("2706342", "http://marketplace.eclipse.org/content/eclipse-vagrant-tooling",
					"Eclipse Vagrant Tooling"), //
			testNode("2579663", "http://marketplace.eclipse.org/content/egerrit", "EGerrit") //
	};

	private static INode testNode(String id, String url, String name) {
		Node node = (Node) QueryHelper.nodeById(id);
		node.setUrl(url);
		node.setName(name);
		return node;
	}

	private static long PROGRESS_TIMEOUT = Long.getLong("org.eclipse.epp.mpc.tests.progress.timeout", 30000);

	private static final Logger logger = Logger.getLogger(AbstractMarketplaceWizardBotTest.class);

	private static boolean dumpThreadsOnTearDownError = Boolean.valueOf(System.getProperty(
			"org.eclipse.epp.mpc.tests.dump.threads", "true"));

	protected SWTBot bot;

	protected SWTBotShell wizardShell;

	public AbstractMarketplaceWizardBotTest() {
		super();
	}

	@Rule
	public TestRule screenshotOnFailureRule = (base, description) -> {
		String targetDir = System.getProperty(SWTBotPreferenceConstants.KEY_SCREENSHOTS_DIR);
		if (targetDir == null && new File("target").isDirectory()) {
			SWTBotPreferences.SCREENSHOTS_DIR = "target/screenshots";
		}
		return new Statement() {

			private final ScreenshotCaptureListener capturer = new ScreenshotCaptureListener();

			@Override
			public void evaluate() throws Throwable {
				try {
					base.evaluate();
				} catch (Throwable t) {
					capturer.testFailure(new Failure(description, t));
					throw t;
				} finally {
					tearDownBot();
				}
			}

		};
	};

	@Before
	public void setUp() {
		launchMarketplaceWizard();
		initWizardBot();
	}

	//tear-down is done in test rule above, since we need to do this after the rule has been applied
	//@After
	public void tearDownBot() {
		if (bot != null) {
			closeWizard();
		}
	}

	protected void launchMarketplaceWizard() {
		final MarketplaceWizardCommand marketplaceWizardCommand = new MarketplaceWizardCommand();
		WizardState wizardState = new WizardState();
		wizardState.setContent(new LinkedHashSet<>(Arrays.asList(TEST_NODES)));
		wizardState.setProceedWithInstallation(false);
		marketplaceWizardCommand.setWizardDialogState(wizardState);

		UIThreadRunnable.asyncExec(() -> {
			try {
				marketplaceWizardCommand.execute(new ExecutionEvent());
			} catch (ExecutionException e) {
				fail("ExecutionException: " + e.getMessage());
				//otherwise ignore, we'll notice in the test thread when we don't get the wizard dialog in time
			}
		});
	}

	protected void initWizardBot() {
		bot = new SWTBot();
		bot.waitUntil(shellIsActive("Eclipse Marketplace"), TimeUnit.SECONDS.toMillis(10));
		wizardShell = bot.shell("Eclipse Marketplace");
		bot = wizardShell.bot();
		assertNotNull(getWizardDialog());
		waitForWizardProgress();
	}

	protected void waitForWizardProgress() {
		waitForWizardProgress(PROGRESS_TIMEOUT);
	}

	protected void waitForWizardProgress(long timeout) {
		SWTBotButton cancelButton = bot.button("Cancel");
		bot.waitUntil(Conditions.widgetIsEnabled(cancelButton), timeout);
	}

	protected void closeWizard() {
		String problem = null;
		List<Exception> exceptions = new ArrayList<>();
		SWTBotShell mpcShell;
		try {
			//check if dialog is still open
			mpcShell = bot.shell("Eclipse Marketplace");
		} catch (WidgetNotFoundException | TimeoutException e) {
			//no MPC wizard found - maybe a bit strange, but so be it...
			return;
		}
		//check if any message dialogs are open
		boolean dumpedThreads = false;
		try {
			WaitForObjectCondition<Shell> subShellResult = Conditions.waitForShell(Matchers.any(Shell.class),
					mpcShell.widget);
			bot.waitUntil(subShellResult, 100, 60);
			List<Shell> subShells = subShellResult.getAllMatches();
			for (Shell shell : subShells) {
				if (shell == mpcShell.widget) {
					continue;
				}

				try {
					SWTBotShell botShell = new SWTBotShell(shell);
					//children are unexpected, so let's cry foul...
					if (problem == null) {
						problem = "MPC wizard has open child dialog:";
					}
					problem += "\n" + describeShell(botShell);
					logger.info(problem);

					problem += "\n" + captureShellScreenshot(botShell);

					//also dump threads, since this is often caused by the wizard not being cancellable due to a still running operation:
					//"Wizard can not be closed due to an active operation"
					if (!dumpedThreads) {
						dumpedThreads = true;
						dumpThreads();
					}

					//kill message dialog
					botShell.close();
				} catch (Exception ex) {
					exceptions.add(ex);
				}
			}
		} catch (Exception ex) {
			exceptions.add(ex);
		}
		//try killing it softly
		try {
			mpcShell.activate();
			waitForWizardProgress(SWTBotPreferences.TIMEOUT);
			mpcShell.close();//same as pressing "Cancel" actually
			ICondition shellCloses = Conditions.shellCloses(mpcShell);
			bot.waitUntil(shellCloses);
			return;
		} catch (Exception ex) {
			exceptions.add(ex);
		}

		//now kill it hard - this is a last resort, because it can cause spurious errors in MPC jobs
		//also dump threads, since this is often caused by the wizard not being cancellable due to a still running operation:
		//"Wizard can not be closed due to an active operation"
		problem += "\nFailed to close wizard regularly. Forcing close.";
		if (!dumpedThreads) {
			dumpedThreads = true;
			dumpThreads();
		}
		try {
			final Shell shell = mpcShell.widget;
			if (!shell.isDisposed()) {
				Display display = shell.getDisplay();
				display.syncExec(() -> {
					if (!shell.isDisposed()) {
						shell.dispose();
					}
				});
			}
		} catch (Exception ex) {
			exceptions.add(ex);
		}
		if (problem != null || !exceptions.isEmpty()) {
			//something happened
			try {
				fail(problem);
			} catch (AssertionError e) {
				for (Exception exception : exceptions) {
					e.addSuppressed(exception);
				}
				throw e;
			}
		}
	}

	private static void dumpThreads() {
		if (!dumpThreadsOnTearDownError) {
			return;
		}

		try {
			ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
			Method dumpMethod = ThreadMXBean.class.getMethod("dumpAllThreads", Boolean.TYPE, Boolean.TYPE);
			ThreadInfo[] threadInfos = (ThreadInfo[]) dumpMethod.invoke(threadMXBean, true, true);
			for (ThreadInfo threadInfo : threadInfos) {
				logger.debug(threadInfo);
			}
		} catch (NoSuchMethodException e) {
			dumpThreadsOnTearDownError = false;
			logger.warn("Method ThreadMXBean.dumpAllThreads(boolean, boolean) does not exist. Try running on Java 6 or later.");
		} catch (Throwable t) {
			logger.warn("Error dumping threads: " + t, t);
		}
	}

	private static String captureShellScreenshot(SWTBotShell botShell) {
		if (botShell.isVisible()) {
			try {
				//try to bring to front
				botShell.activate();
			} catch (Throwable ex) {
			}
			//make a screenshot
			String fileName = "dialog_" + System.currentTimeMillis() + "."
					+ SWTBotPreferences.SCREENSHOT_FORMAT.toLowerCase();
			SWTUtils.captureScreenshot(SWTBotPreferences.SCREENSHOTS_DIR + "/" + fileName);
			String message = "Captured screenshot of open shell '" + botShell.getText() + "' in " + fileName;
			logger.info(message);
			return message;
		}
		return "";
	}

	private static String describeShell(SWTBotShell botShell) {
		StringBuilder description = new StringBuilder("    Shell(\"").append(botShell.getText()).append("\")");

		try {
			SWTBot childBot = botShell.bot();
			@SuppressWarnings("unchecked")
			Matcher<Label> labelMatcher = allOf(widgetOfType(Label.class));
			List<? extends Label> labels = childBot.widgets(labelMatcher);
			for (Label label : labels) {
				if (label != null) {//TODO why can this be null?
					String labelText = new SWTBotLabel(label, labelMatcher).getText();
					if (labelText != null && labelText.trim().length() > 0) {
						description.append("\n    > ").append(labelText.trim());
					}
				}
			}
			@SuppressWarnings("unchecked")
			Matcher<Button> buttonMatcher = allOf(widgetOfType(Button.class));
			List<? extends Button> buttons = childBot.widgets(buttonMatcher);
			boolean firstButton = true;
			for (Button button : buttons) {
				if (button != null) {
					SWTBotButton buttonBot = new SWTBotButton(button, buttonMatcher);
					String buttonText = buttonBot.getText();
					if (buttonText != null && buttonText.trim().length() > 0) {
						if (firstButton) {
							firstButton = false;
							description.append("\n    >         ");
						} else {
							description.append("  ");
						}
						if (buttonBot.isEnabled()) {
							description.append('[').append(buttonText.trim()).append(']');
						} else {
							description.append("[(").append(buttonText.trim()).append(")]");
						}
					}
				}
			}
		} catch (Exception ex) {
			description.append("\n    > Error describing shell contents: ").append(ex);
		}
		return description.toString();
	}

	protected void checkNoItems() {
		checkNoItems(NodeMatcher.any());
	}

	protected void checkNoItems(NodeMatcher<? extends Widget> matcher) {
		List<? extends Widget> controls = bot.getFinder().findControls(matcher);
		assertThat(controls, empty());
	}

	protected SWTBot itemBot(NodeMatcher<? extends Widget> matcher) {
		List<? extends Widget> controls = bot.getFinder().findControls(matcher);
		assertThat(controls.size(), greaterThanOrEqualTo(1));
		Widget firstItem = controls.get(0);
		return new SWTBot(firstItem);
	}

	protected SWTBot itemBot(String id) {
		return itemBot(id == null ? NodeMatcher.any() : NodeMatcher.withId(id));
	}

	protected void checkSelectedTab(String tabLabel) {
		try {
			SWTBotCTabItem searchTab = bot.cTabItem(tabLabel);
			final CTabItem tab = searchTab.widget;
			CTabItem selection = UIThreadRunnable.syncExec((Result<CTabItem>) () -> tab.getParent().getSelection());
			assertNotNull(selection);
			assertSame(tab, selection);
		} catch (WidgetNotFoundException e) {
			SWTBotCTabItem searchTab = bot.cTabItem();
			final CTabItem tab = searchTab.widget;
			UIThreadRunnable.syncExec(() -> {
				CTabFolder folder = tab.getParent();
				CTabItem[] items = folder.getItems();
				for (CTabItem cTabItem : items) {
					System.out.println(cTabItem.getText());
				}
			});
			throw e;
		}
	}

	protected void filterMarket(String term) {
		SWTBotComboAdapter comboBox = marketCombo();
		select(comboBox, IMarket.class, term);
	}

	protected SWTBotComboAdapter marketCombo() {
		return SWTBotComboAdapter.comboBox(bot, 0);
	}

	protected void filterCategory(String term) {
		SWTBotComboAdapter comboBox = categoryCombo();
		select(comboBox, ICategory.class, term);
	}

	protected SWTBotComboAdapter categoryCombo() {
		return SWTBotComboAdapter.comboBox(bot, 1);
	}

	protected void search(String term) {
		SWTBotText searchField = searchField();
		searchField.setFocus();
		searchField.setText(term);
		bot.button("Go").click();
		waitForWizardProgress();
	}

	protected SWTBotText searchField() {
		return bot.text(0);
	}

	protected SWTBotBrowser marketplaceBrowser() {
		SWTWorkbenchBot wbBot = new SWTWorkbenchBot();
		Matcher<IEditorReference> marketplaceBrowserMatch = Matchers.allOf(WidgetMatcherFactory
				.<IEditorReference> withPartId("org.eclipse.ui.browser.editor"), WidgetMatcherFactory
				.<IEditorReference> withTitle(containsString("Marketplace")));
		SWTBotEditor browserEditor = wbBot.editor(marketplaceBrowserMatch);
		SWTBotBrowser browser = browserEditor.bot().browser();
		return browser;
	}

	protected List<StyleRange> findLinks(final SWTBotStyledText styledText) {
		StyleRange[] ranges = findStyleRanges(styledText);
		List<StyleRange> links = new ArrayList<>();
		for (StyleRange range : ranges) {
			if (range.underline == true && range.underlineStyle == SWT.UNDERLINE_LINK) {
				links.add(range);
			}
		}
		assertFalse(links.isEmpty());
		return links;
	}

	private static StyleRange[] findStyleRanges(final SWTBotStyledText styledText) {
		StyleRange[] ranges = UIThreadRunnable.syncExec((ArrayResult<StyleRange>) () -> styledText.widget
				.getStyleRanges());
		return ranges;
	}

	protected StyleRange findLink(final SWTBotStyledText styledText, String linkText) {
		List<StyleRange> links = findLinks(styledText);
		String text = styledText.getText();
		for (StyleRange link : links) {
			if (linkText.equals(getText(link, text))) {
				return link;
			}
		}
		fail("No link found with text '" + linkText + "'");
		return null;
	}

	protected StyleRange findLink(final SWTBotStyledText styledText) {
		List<StyleRange> links = findLinks(styledText);
		return links.get(0);
	}

	protected String getText(StyleRange range, String text) {
		return text.substring(range.start, range.start + range.length);
	}

	protected String getText(StyleRange range, SWTBotStyledText styledText) {
		String text = styledText.getText();
		return text.substring(range.start, range.start + range.length);
	}

	protected void select(SWTBotComboAdapter comboBox, Class<?> classifier, String choice) {
		AbstractTagFilter filter = findFilter(classifier);
		String choiceText = choice != null ? choice : ((ComboTagFilter) filter).getNoSelectionLabel();

		comboBox.setSelection(choiceText);
		waitForWizardProgress();
		assertEquals(choiceText, comboBox.getText());
		checkSelected(filter, choice);
	}

	private static void checkSelected(AbstractTagFilter filter, String selection) {
		Set<Tag> selected = filter.getSelected();
		if (selection == null) {
			assertTrue(selected.isEmpty());
			return;
		}
		for (Tag tag : selected) {
			if (tag.getLabel().equals(selection)) {
				return;
			}
			if (tag.getValue().equals(selection)) {
				return;
			}
		}
		fail(NLS.bind("Expected value {0} not selected in filter", selection));
	}

	private AbstractTagFilter findFilter(Class<?> classifier) {
		List<CatalogFilter> filters = getWizard().getConfiguration().getFilters();
		for (CatalogFilter filter : filters) {
			if (filter instanceof AbstractTagFilter) {
				AbstractTagFilter tagFilter = (AbstractTagFilter) filter;
				List<Tag> choices = tagFilter.getChoices();
				Object classification = choices.isEmpty() ? null : choices.get(0).getTagClassifier();
				if (classification == classifier) {
					return tagFilter;
				}
			}
		}
		fail("No filter found for " + classifier.getName());
		return null;//unreachable
	}

	protected void deselectPending(int count) {
		for (int i = 0; i < count; i++) {
			bot.button("Install Pending").click();
		}
	}

	protected SWTBotLink selectToInstall(int count) {
		if (count == 0) {
			return null;
		}
		bot.button("Install").click();
		waitForWizardProgress(3 * PROGRESS_TIMEOUT);
		assertSame(getWizard().getPage(FeatureSelectionWizardPage.class.getName()), getWizardDialog().getCurrentPage());
		bot.button("< Install More").click();
		waitForWizardProgress();
		SWTBotLink selectedSolutionsLink = selectedSolutionsLink(1);
		for (int i = 2; i <= count; i++) {
			bot.button("Install").click();
			selectedSolutionsLink = selectedSolutionsLink(i);
		}
		return selectedSolutionsLink;
	}

	protected SWTBotLink selectedSolutionsLink(int count) {
		String linkText;
		switch (count) {
		case 0:
			return null;
		case 1:
			linkText = "One solution selected";
			break;
		default:
			linkText = String.format("%s solutions selected", count);
		}
		String linkContent = String.format("<a href=\"showSelection\">%s</a>", linkText);

		Matcher<Link> matcher = allOf(widgetOfType(Link.class), WithRegex.withRegex("\\Q" + linkContent + "\\E"));
		return new SWTBotLink(bot.widget(matcher, 0), matcher);
	}

	protected void tryWaitForBrowser(SWTBotBrowser browser) {
		for (int i = 0; i < 6; i++) {
			try {
				browser.waitForPageLoaded();
			} catch (TimeoutException ex) {
				//ignore
			}
			String url = browser.getUrl();
			if (url != null && !"".equals(url) && !"about:blank".equals(url)) {
				return;
			} else {
				bot.sleep(1000);
			}
		}
	}

	protected MarketplaceWizardDialog getWizardDialog() {
		return (MarketplaceWizardDialog) UIThreadRunnable.syncExec((Result<Object>) () -> wizardShell.widget.getData());
	}

	protected MarketplaceWizard getWizard() {
		return getWizardDialog().getWizard();
	}

	protected SWTBot assume(SWTBot bot) {
		return new SWTBot(bot.getFinder()) {
			@Override
			public void waitUntilWidgetAppears(ICondition waitForWidget) {
				try {
					super.waitUntilWidgetAppears(waitForWidget);
				} catch (TimeoutException e) {
					throw new AssumptionViolatedException(e.getMessage(), e);
				} catch (WidgetNotFoundException e) {
					throw new AssumptionViolatedException(e.getMessage(), e);
				}
			}

			@Override
			public void waitUntil(ICondition condition, long timeout, long interval) throws TimeoutException {
				try {
					super.waitUntil(condition, timeout, interval);
				} catch (TimeoutException e) {
					throw new AssumptionViolatedException(e.getMessage(), e);
				}
			}

			@Override
			public void waitWhile(ICondition condition, long timeout, long interval) throws TimeoutException {
				try {
					super.waitWhile(condition, timeout, interval);
				} catch (TimeoutException e) {
					throw new AssumptionViolatedException(e.getMessage(), e);
				}
			}
		};
	}
}
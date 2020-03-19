package io.skymind.pathmind.webapp.ui.components;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class TabPanel extends HorizontalLayout
{
	private Tabs tabbedBar;
	private ArrayList<Tab> tabs;

	public TabPanel(String... tabNames) {
		this(Arrays.stream(tabNames).map(name -> new Tab(name)).toArray(Tab[]::new));
	}

	public TabPanel(Tab... tabsParam)
	{
		tabbedBar = new Tabs();
		tabbedBar.setWidthFull();
		tabbedBar.addThemeVariants(TabsVariant.LUMO_SMALL);

		tabs = new ArrayList<>();
		Arrays.stream(tabsParam).forEach(tab -> {
			tab.setVisible(true);
			tab.getStyle().set("color", "inherit");
			tabs.add(tab);
			tabbedBar.add(tab);
		});

		tabs.get(0).setSelected(true);

		setWidthFull();
		setMargin(false);
		add(tabbedBar);
	}

	/**
	 * Here we want to override it because we really mean the tabs and not the component itself.
	 */
	@Override
	public void setEnabled(boolean isEnabled) {
		tabs.stream().forEach(tab -> tab.setEnabled(isEnabled));
	}

	public void addThemeVariants(TabsVariant... variants){
		tabbedBar.addThemeVariants(variants);
	}

	public void addTabClickListener(Consumer<String> consumer) {
		tabbedBar.addSelectedChangeListener(change ->
			consumer.accept(change.getSelectedTab().getLabel())
		);
	}

	public void setTab(String tabName) {
		tabs.stream().forEach(tab -> tab.setSelected(tab.getLabel().equals(tabName)));
	}
}

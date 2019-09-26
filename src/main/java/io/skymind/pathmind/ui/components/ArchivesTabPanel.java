package io.skymind.pathmind.ui.components;

public class ArchivesTabPanel extends TabPanel
{
	private static final String ARCHIVES_TAB = "Archives";

	public ArchivesTabPanel(String tabName, Runnable runnable)
	{
		super(tabName, ARCHIVES_TAB);

		setAlignItems(Alignment.START);
		addTabClickListener(name -> {
			if(ARCHIVES_TAB.equals(name))
				runnable.run();
		});
	}
}

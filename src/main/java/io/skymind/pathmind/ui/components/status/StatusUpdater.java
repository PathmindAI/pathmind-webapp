package io.skymind.pathmind.ui.components.status;

public interface StatusUpdater
{
	public void updateStatus(double percentage);
	public void updateError(String error);
	public void done();
}

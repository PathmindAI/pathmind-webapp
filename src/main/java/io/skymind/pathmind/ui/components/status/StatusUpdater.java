package io.skymind.pathmind.ui.components.status;

import java.util.List;

public interface StatusUpdater
{
	public void updateStatus(double percentage);
	public void updateError(String error);
	public void done();
}

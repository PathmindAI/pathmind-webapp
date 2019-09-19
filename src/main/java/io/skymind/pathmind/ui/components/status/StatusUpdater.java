package io.skymind.pathmind.ui.components.status;

import io.skymind.pathmind.services.project.FileCheckResult;

public interface StatusUpdater
{
	public void updateStatus(double percentage);
	public void updateError(String error);
	public void done();

    public void fileCheckComplete(FileCheckResult anylogicFileCheckResult);

}

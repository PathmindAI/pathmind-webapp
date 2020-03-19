package io.skymind.pathmind.shared.services.training;

public enum ExecutionProviderClass {
	// These numeric ids are stored to the database so if changed, database migrations are needed
	Rescale(1),
	AWS(2);

	private final int id;

	ExecutionProviderClass(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}

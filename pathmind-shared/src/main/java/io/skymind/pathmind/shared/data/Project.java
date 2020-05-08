package io.skymind.pathmind.shared.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Project extends ArchivableData
{
	private LocalDateTime dateCreated;
	private LocalDateTime lastActivityDate;
	private String userNotes;

	private long pathmindUserId;

	// Helper GUI attributes not stored in the database
	private transient List<Model> models;

}


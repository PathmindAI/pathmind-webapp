package io.skymind.pathmind.shared.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Project extends ArchivableData
{
    private static final long serialVersionUID = -8482276917940795086L;
	private LocalDateTime dateCreated;
	private LocalDateTime lastActivityDate;
	private String userNotes;

	private long pathmindUserId;

	// Helper GUI attributes not stored in the database
	private transient List<Model> models;

}


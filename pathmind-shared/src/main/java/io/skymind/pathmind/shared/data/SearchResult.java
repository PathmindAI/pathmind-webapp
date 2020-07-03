package io.skymind.pathmind.shared.data;

import java.time.LocalDateTime;
import java.util.Objects;

import io.skymind.pathmind.shared.constants.SearchResultItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * DTO for search result object, which can be one of these: Project, Model, Experiment 
 */
@AllArgsConstructor
@Builder
public class SearchResult {
	
    private SearchResultItemType itemType;
    private long itemId;
    private String name;
    private Boolean isArchived;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String notes;

	@Override
 	public boolean equals(Object o) {
 		if(this == o) return true;
 		if(o == null || getClass() != o.getClass()) return false;
 		SearchResult item = (SearchResult) o;
 		return Objects.equals(item.getItemType(), getItemType()) 
 				&& Objects.equals(item.getItemId(), getItemId());
 	}

 	@Override
 	public int hashCode() {
 		return Objects.hash(getItemType(), getItemId());
 	}

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public SearchResultItemType getItemType() {
        return itemType;
    }

    public void setItemType(SearchResultItemType itemType) {
        this.itemType = itemType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

}

package io.skymind.pathmind.shared.data;

import java.time.LocalDateTime;
import java.util.Objects;

import io.skymind.pathmind.shared.constants.RunStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * DTO for dashboard purposes
 */
@AllArgsConstructor
@Builder
public class SearchResult {
	
    private long itemId;
    private String itemType;
    private String name;
    private LocalDateTime latestUpdateTime;
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

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getLatestUpdateTime() {
        return latestUpdateTime;
    }

    public void setLatestUpdateTime(LocalDateTime latestUpdateTime) {
        this.latestUpdateTime = latestUpdateTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}

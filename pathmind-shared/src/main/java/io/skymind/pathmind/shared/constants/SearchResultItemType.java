package io.skymind.pathmind.shared.constants;

import java.util.Arrays;

public enum SearchResultItemType {

    PROJECT("project"), MODEL("model"), EXPERIMENT("experiment");
    
    private String name;
    
    private SearchResultItemType(String name){
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public static SearchResultItemType getEnumFromName(String name) {
        return Arrays.stream(values())
                .filter(itemType -> itemType.getName().equals(name))
                .findAny()
                .get();
    }
}

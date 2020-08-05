package io.skymind.pathmind.db.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.skymind.pathmind.shared.constants.SearchResultItemType;

import static io.skymind.pathmind.db.dao.SearchDescription.OrClause;
import static io.skymind.pathmind.db.dao.SearchDescription.Field.NAME;
import static io.skymind.pathmind.db.dao.SearchDescription.Field.USERNOTES;

public class SearchParser {
    public SearchDescription parseKeyword(String originalKeyword) {
        String[] splitTypeAndKeyword = splitTypeAndKeyword(originalKeyword);
        String searchType = splitTypeAndKeyword[0].toLowerCase();
        String searchKeyword = splitTypeAndKeyword[1].toLowerCase();

        if (isProjectSearch(searchType)) {
            return new SearchDescription()
                    .withProjectClauses(parseProjectSearch(searchKeyword));

        } else if (isModelSearch(searchType)) {
            return new SearchDescription()
                    .withModelClauses(parseModelSearch(searchKeyword));
        } else if (isExperimentSearch(searchType)) {
            return new SearchDescription()
                    .withExperimentClauses(parseExperimentSearch(searchKeyword));

        } else {
            return new SearchDescription()
                    .withProjectClauses(parseProjectSearch(searchKeyword))
                    .withModelClauses(parseModelSearch(searchKeyword))
                    .withExperimentClauses(parseExperimentSearch(searchKeyword))
                    ;
        }
    }

    private String[] splitTypeAndKeyword(String originalKeyword) {
        String[] candidate = originalKeyword.split(":", 2);
        if (candidate.length == 2) {
            return candidate;
        }
        else {
            return new String[]{"all", candidate[0]};
        }
    }

    private Collection<OrClause> parseProjectSearch(String searchKeyword) {
        return Arrays.asList(
                new OrClause(NAME, searchKeyword),
                new OrClause(USERNOTES, searchKeyword)
        );
    }

    private Collection<OrClause> parseModelSearch(String searchKeyword) {
        return parseSearchForTypeWhoseNameIsANumber(SearchResultItemType.MODEL, searchKeyword);
    }

    private Collection<OrClause> parseExperimentSearch(String searchKeyword) {
        return parseSearchForTypeWhoseNameIsANumber(SearchResultItemType.EXPERIMENT, searchKeyword);
    }

    private Collection<OrClause> parseSearchForTypeWhoseNameIsANumber(SearchResultItemType itemType, String searchKeyword) {
        List<OrClause> orClauses = new ArrayList<>(Arrays.asList(
                new OrClause(USERNOTES, searchKeyword)
        ));
        if (matchTypeInKeyword(itemType, searchKeyword)) {
            orClauses.add(new OrClause(NAME, removeTypeFromKeyword(itemType, searchKeyword)));
        }
        else {
            orClauses.add(new OrClause(NAME, searchKeyword));
        }
        return orClauses;
    }


    private String removeTypeFromKeyword(SearchResultItemType itemType, String originalKeyword) {
        String typeName = itemType.getName().toLowerCase();
        return originalKeyword.replaceFirst(typeName + " #", "").replaceFirst(typeName + " ", "");
    }


    private Boolean matchTypeInKeyword(SearchResultItemType itemType, String keyword) {
        String typeName = itemType.getName().toLowerCase();
        return keyword.matches("(?i)" + typeName + "\\s#?\\d+");
    }

    private boolean isProjectSearch(String searchType) {
        return isSearchType(SearchResultItemType.PROJECT, searchType);
    }

    private boolean isModelSearch(String searchType) {
        return isSearchType(SearchResultItemType.MODEL, searchType);
    }

    private boolean isExperimentSearch(String searchType) {
        return isSearchType(SearchResultItemType.EXPERIMENT, searchType);
    }

    private boolean isSearchType(SearchResultItemType type, String searchTypeStr) {
        return type.getName().toLowerCase().equals(searchTypeStr);
    }
}

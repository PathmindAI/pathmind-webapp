package io.skymind.pathmind.db.dao;

import org.junit.Test;
import org.junit.Assert;

import static io.skymind.pathmind.db.dao.SearchDescription.OrClause;
import static io.skymind.pathmind.db.dao.SearchDescription.Field.NAME;
import static io.skymind.pathmind.db.dao.SearchDescription.Field.USERNOTES;

public class SearchParserTest {
    private void assertParseResultWillBe(String keyword, SearchDescription expected) {
        SearchParser searchParser = new SearchParser();
        SearchDescription searchDescription = searchParser.parseKeyword(keyword);
        Assert.assertEquals(expected, searchDescription);
    }

    @Test
    public void testSimpleProjectSearch() {
        assertParseResultWillBe("project:some search", new SearchDescription()
                .withProjectClauses(
                        new OrClause(NAME, "some search"),
                        new OrClause(USERNOTES, "some search")
                ));
    }

    @Test
    public void testProjectSearchHasNoMatch() {
        assertParseResultWillBe("project:project nice", new SearchDescription()
                .withProjectClauses(
                        new OrClause(NAME, "project nice"),
                        new OrClause(USERNOTES, "project nice")
                ));
    }

    @Test
    public void testSimpleModelSearch() {
        assertParseResultWillBe("model:some search", new SearchDescription()
                .withModelClauses(
                        new OrClause(NAME, "some search"),
                        new OrClause(USERNOTES, "some search")
                ));
    }

    @Test
    public void testModelSearchWithTypeMatch() {
        assertParseResultWillBe("model:model 1", new SearchDescription()
                .withModelClauses(
                        new OrClause(NAME, "1"),
                        new OrClause(USERNOTES, "model 1")
                ));
    }

    @Test
    public void testModelSearchWithTypeAndHashMatch() {
        assertParseResultWillBe("model:model #1", new SearchDescription()
                .withModelClauses(
                        new OrClause(NAME, "1"),
                        new OrClause(USERNOTES, "model #1")
                ));
    }

    @Test
    public void testModelSearchWithHashMatch() {
        assertParseResultWillBe("model:#1", new SearchDescription()
                .withModelClauses(
                        new OrClause(NAME, "1"),
                        new OrClause(USERNOTES, "#1")
                ));
    }

    @Test
    public void testSimpleExperimentSearch() {
        assertParseResultWillBe("experiment:some search", new SearchDescription()
                .withExperimentClauses(
                        new OrClause(NAME, "some search"),
                        new OrClause(USERNOTES, "some search")
                ));
    }

    @Test
    public void testExperimentSearchWithTypeMatch() {
        assertParseResultWillBe("experiment:experiment 1", new SearchDescription()
                .withExperimentClauses(
                        new OrClause(NAME, "1"),
                        new OrClause(USERNOTES, "experiment 1")
                ));
    }

    @Test
    public void testExperimentSearchWithTypeAndHashMatch() {
        assertParseResultWillBe("experiment:experiment #1", new SearchDescription()
                .withExperimentClauses(
                        new OrClause(NAME, "1"),
                        new OrClause(USERNOTES, "experiment #1")
                ));
    }

    @Test
    public void testExperimentSearchWithHashMatch() {
        assertParseResultWillBe("experiment:#1", new SearchDescription()
                .withExperimentClauses(
                        new OrClause(NAME, "1"),
                        new OrClause(USERNOTES, "#1")
                ));
    }

    @Test
    public void testSimpleAllSearch() {
        assertParseResultWillBe("all:some search", new SearchDescription()
                .withProjectClauses(
                        new OrClause(NAME, "some search"),
                        new OrClause(USERNOTES, "some search")
                )
                .withModelClauses(
                        new OrClause(NAME, "some search"),
                        new OrClause(USERNOTES, "some search")
                )
                .withExperimentClauses(
                        new OrClause(NAME, "some search"),
                        new OrClause(USERNOTES, "some search")
                ));
    }

    @Test
    public void testAllSearchWithExperimentMatch() {
        assertParseResultWillBe("all:experiment 1", new SearchDescription()
                .withProjectClauses(
                        new OrClause(NAME, "experiment 1"),
                        new OrClause(USERNOTES, "experiment 1")
                )
                .withModelClauses(
                        new OrClause(NAME, "experiment 1"), // it actually doesn't make sense, but it won't hurt
                        new OrClause(USERNOTES, "experiment 1")
                )
                .withExperimentClauses(
                        new OrClause(NAME, "1"),
                        new OrClause(USERNOTES, "experiment 1")
                ));
    }

    @Test
    public void testAllSearchWithModelMatch() {
        assertParseResultWillBe("all:model 1", new SearchDescription()
                .withProjectClauses(
                        new OrClause(NAME, "model 1"),
                        new OrClause(USERNOTES, "model 1")
                )
                .withModelClauses(
                        new OrClause(NAME, "1"),
                        new OrClause(USERNOTES, "model 1")
                )
                .withExperimentClauses(
                        new OrClause(NAME, "model 1"), // it actually doesn't make sense, but it won't hurt
                        new OrClause(USERNOTES, "model 1")
                ));
    }

    private void assertParseResultEquivalence(String keyword, String equivalentToKeyword) {
        SearchParser searchParser = new SearchParser();
        SearchDescription actual = searchParser.parseKeyword(keyword);
        SearchDescription expected = searchParser.parseKeyword(equivalentToKeyword);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testSearchWithoutAType() {
        // this shouldn't happen in practice, unless someone direct change the URL, but let's prevent anyway.
        assertParseResultEquivalence("some search", "all:some search");
    }

    @Test
    public void testSearchWithATypeThatDoesntExist() {
        // this shouldn't happen in practice, unless someone direct change the URL, but let's prevent anyway.
        assertParseResultEquivalence("i dont exist:new search", "all:new search");
    }

}

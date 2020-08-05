package io.skymind.pathmind.db.dao;

import org.junit.Test;
import org.junit.Assert;

import static io.skymind.pathmind.db.dao.SearchDescription.OrClause;
import static io.skymind.pathmind.db.dao.SearchDescription.Field.NAME;
import static io.skymind.pathmind.db.dao.SearchDescription.Field.USERNOTES;

public class SearchParserTest {
    @Test
    public void testSimpleProjectSearch() {
        SearchParser searchParser = new SearchParser();
        SearchDescription searchDescription = searchParser.parseKeyword("project:some search");
        SearchDescription expected = new SearchDescription()
                .withProjectClauses(
                        new OrClause(NAME, "some search"),
                        new OrClause(USERNOTES, "some search")
                );
        Assert.assertEquals(expected, searchDescription);
    }

    @Test
    public void testProjectSearchHasNoMatch() {
        SearchParser searchParser = new SearchParser();
        SearchDescription searchDescription = searchParser.parseKeyword("project:project nice");
        SearchDescription expected = new SearchDescription()
                .withProjectClauses(
                        new OrClause(NAME, "project nice"),
                        new OrClause(USERNOTES, "project nice")
                );
        Assert.assertEquals(expected, searchDescription);

    }

    @Test
    public void testSimpleModelSearch() {
        SearchParser searchParser = new SearchParser();
        SearchDescription searchDescription = searchParser.parseKeyword("model:some search");
        SearchDescription expected = new SearchDescription()
                .withModelClauses(
                        new OrClause(NAME, "some search"),
                        new OrClause(USERNOTES, "some search")
                );
        Assert.assertEquals(expected, searchDescription);
    }

    @Test
    public void testModelSearchWithMatch() {
        SearchParser searchParser = new SearchParser();
        SearchDescription searchDescription = searchParser.parseKeyword("model:model 1");
        SearchDescription expected = new SearchDescription()
                .withModelClauses(
                        new OrClause(NAME, "1"),
                        new OrClause(USERNOTES, "model 1")
                );
        Assert.assertEquals(expected, searchDescription);
    }


    @Test
    public void testModelSearchWithMatchVariant() {
        SearchParser searchParser = new SearchParser();
        SearchDescription searchDescription = searchParser.parseKeyword("model:model #1");
        SearchDescription expected = new SearchDescription()
                .withModelClauses(
                        new OrClause(NAME, "1"),
                        new OrClause(USERNOTES, "model #1")
                );
        Assert.assertEquals(expected, searchDescription);
    }

    @Test
    public void testSimpleExperimentSearch() {
        SearchParser searchParser = new SearchParser();
        SearchDescription searchDescription = searchParser.parseKeyword("experiment:some search");
        SearchDescription expected = new SearchDescription()
                .withExperimentClauses(
                        new OrClause(NAME, "some search"),
                        new OrClause(USERNOTES, "some search")
                );
        Assert.assertEquals(expected, searchDescription);
    }

    @Test
    public void testExperimentSearchWithMatch() {
        SearchParser searchParser = new SearchParser();
        SearchDescription searchDescription = searchParser.parseKeyword("experiment:experiment 1");
        SearchDescription expected = new SearchDescription()
                .withExperimentClauses(
                        new OrClause(NAME, "1"),
                        new OrClause(USERNOTES, "experiment 1")
                );
        Assert.assertEquals(expected, searchDescription);
    }


    @Test
    public void testExperimentSearchWithMatchVariant() {
        SearchParser searchParser = new SearchParser();
        SearchDescription searchDescription = searchParser.parseKeyword("experiment:experiment #1");
        SearchDescription expected = new SearchDescription()
                .withExperimentClauses(
                        new OrClause(NAME, "1"),
                        new OrClause(USERNOTES, "experiment #1")
                );
        Assert.assertEquals(expected, searchDescription);
    }

    @Test
    public void testSimpleAllSearch() {
        SearchParser searchParser = new SearchParser();
        SearchDescription searchDescription = searchParser.parseKeyword("all:some search");
        SearchDescription expected = new SearchDescription()
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
                );
        Assert.assertEquals(expected, searchDescription);
    }

    @Test
    public void testAllSearchWithExperimentMatch() {
        SearchParser searchParser = new SearchParser();
        SearchDescription searchDescription = searchParser.parseKeyword("all:experiment 1");
        SearchDescription expected = new SearchDescription()
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
                );
        Assert.assertEquals(expected, searchDescription);
    }

    @Test
    public void testAllSearchWithModelMatch() {
        SearchParser searchParser = new SearchParser();
        SearchDescription searchDescription = searchParser.parseKeyword("all:model 1");
        SearchDescription expected = new SearchDescription()
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
                );
        Assert.assertEquals(expected, searchDescription);
    }

    @Test
    public void testSearchWithoutAType() {
        // this shouldn't happen in practice, unless someone direct change the URL, but let's prevent anyway.
        SearchParser searchParser = new SearchParser();
        SearchDescription searchDescription = searchParser.parseKeyword("some search");
        SearchDescription expected = searchParser.parseKeyword("all:some search");
        Assert.assertEquals(expected, searchDescription);
    }

    @Test
    public void testSearchWithATypeThatDoesntExist() {
        // this shouldn't happen in practice, unless someone direct change the URL, but let's prevent anyway.
        SearchParser searchParser = new SearchParser();
        SearchDescription searchDescription = searchParser.parseKeyword("i dont exist:some search");
        SearchDescription expected = searchParser.parseKeyword("all:some search");
        Assert.assertEquals(expected, searchDescription);
    }

}

package io.skymind.pathmind.webapp.ui.layouts.components;

import io.skymind.pathmind.webapp.ui.components.FavoriteStar;
import io.skymind.pathmind.webapp.ui.karibu.KaribuUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FavoriteStarTest {

    private FavoriteStar favoriteStar;

    @Before
    public void setUp() {
        KaribuUtils.setup();
        favoriteStar = new FavoriteStar(false, newValue -> {});
    }

    @After
    public void tearDown() {
        KaribuUtils.tearDown();
    }

    @Test
    public void favoriteStarTest() {
        assertEquals(false, favoriteStar.getValue());
        favoriteStar.setValue(true);
        assertEquals(true, favoriteStar.getValue());
    }

}
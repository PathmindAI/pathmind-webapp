package io.skymind.pathmind.webapp.ui.components;

import java.util.function.Consumer;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("favorite-star")
@JsModule("/src/components/favorite-star.js")
public class FavoriteStar extends PolymerTemplate<TemplateModel> implements HasStyle {
    private boolean isFavorite = false;
    private Consumer<Boolean> onToggleFavorite;
    
    public FavoriteStar(boolean isFavorite, Consumer<Boolean> toggleAction) {
        super();
        this.onToggleFavorite = toggleAction;
        setValue(isFavorite);
    }

    public void setValue(boolean isFavorite) {
        this.isFavorite = isFavorite;
        getElement().setProperty("isFavorite", isFavorite);
    }

    public boolean getValue() {
        return isFavorite;
    }

    @EventHandler
    private void toggleFavorite() {
        boolean newValue = !isFavorite;
        onToggleFavorite.accept(newValue);
        setValue(newValue);
    }
}
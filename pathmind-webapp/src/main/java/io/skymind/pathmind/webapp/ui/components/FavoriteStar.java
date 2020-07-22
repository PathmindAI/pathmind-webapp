package io.skymind.pathmind.webapp.ui.components;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("favorite-star")
@JsModule("/src/components/favorite-star.js")
public class FavoriteStar extends PolymerTemplate<TemplateModel> implements HasStyle {
    private Boolean isFavorite = false;
    private Command onToggleFavorite;
    
    public FavoriteStar(Boolean isFavorite, Command toggleAction) {
        super();
        this.onToggleFavorite = toggleAction;
        setValue(isFavorite);
    }

    public void setValue(Boolean isFavorite) {
        this.isFavorite = isFavorite;
        getElement().setProperty("isFavorite", isFavorite);
    }

    public Boolean getValue() {
        return isFavorite;
    }

    @EventHandler
    private void toggleFavorite() {
        setValue(!isFavorite);
        onToggleFavorite.execute();
    }
}
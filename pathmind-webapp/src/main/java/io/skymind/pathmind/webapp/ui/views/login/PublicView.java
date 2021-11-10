package io.skymind.pathmind.webapp.ui.views.login;

import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.HasDynamicTitle;

import io.skymind.pathmind.webapp.utils.PathmindUtils;

@Push
public interface PublicView extends HasDynamicTitle {
    default String getPageTitle() {
        return PathmindUtils.getPageTitle();
    }
}

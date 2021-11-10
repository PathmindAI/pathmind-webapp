package io.skymind.pathmind.webapp.security;

import com.vaadin.flow.server.BootstrapListener;
import com.vaadin.flow.server.BootstrapPageResponse;

import org.jsoup.nodes.Element;

// https://vaadin.com/docs/v14/flow/advanced/tutorial-bootstrap#bootstraplistener
public class CustomBootstrapListener implements BootstrapListener {

    public void modifyBootstrapPage(BootstrapPageResponse response) {
        final Element head = response.getDocument().head();
        head.append("<link rel=\"shortcut icon\" href=\"frontend/images/favicon.png\" sizes=\"32x32\">");
        head.append("<script src=\"https://www.googleoptimize.com/optimize.js?id=GTM-T2DSBKT\"></script>");
    }

}

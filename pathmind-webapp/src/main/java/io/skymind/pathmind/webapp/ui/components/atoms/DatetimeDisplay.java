package io.skymind.pathmind.webapp.ui.components.atoms;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.littemplate.LitTemplate;

@Tag("datetime-display")
@NpmPackage(value = "timeago.js", version = "4.0.2")
@JsModule("./src/components/atoms/datetime-display.ts")
public class DatetimeDisplay extends LitTemplate {
    public DatetimeDisplay(LocalDateTime datetime) {
        super();
        getElement().setProperty("datetime", datetime.toString());
        getElement().setProperty("serverTimeZoneOffsetFromUTC", calculateTimeZoneOffset());
    }

    private String calculateTimeZoneOffset() {
        LocalDateTime now = LocalDateTime.now();
        ZoneId serverTimeZone = ZoneId.systemDefault();
        return serverTimeZone.getRules().getOffset(now).toString();
    }
}
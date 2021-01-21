package io.skymind.pathmind.webapp.ui.components.atoms;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("datetime-display")
@NpmPackage(value = "timeago.js", version = "4.0.2")
@JsModule("/src/components/atoms/datetime-display.js")
public class DatetimeDisplay extends PolymerTemplate<DatetimeDisplay.Model> implements HasStyle {

    public DatetimeDisplay(LocalDateTime datetime) {
        super();
        getModel().setDatetime(datetime.toString());
        getModel().setServerTimeZoneOffsetFromUTC(calculateTimeZoneOffset());
    }

    private String calculateTimeZoneOffset() {
        LocalDateTime now = LocalDateTime.now();
        ZoneId serverTimeZone = ZoneId.systemDefault();
        return serverTimeZone.getRules().getOffset(now).toString();
    }

    public interface Model extends TemplateModel {
        void setDatetime(String datetime);

        void setServerTimeZoneOffsetFromUTC(String serverTimeZoneOffsetFromUTC);
    }
}
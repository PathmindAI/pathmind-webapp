package io.skymind.pathmind.webapp.ui.components.atoms;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

@Tag("datetime-display")
@NpmPackage(value = "timeago.js", version = "4.0.2")
@JsModule("/src/components/atoms/datetime-display.js")
public class DatetimeDisplay extends PolymerTemplate<DatetimeDisplay.Model> implements HasStyle {
    public DatetimeDisplay(LocalDateTime datetime) {
        super();
        getModel().setDatetime(datetime.toString());
        getModel().setDate(formatDateTimeInUserTimezone(datetime, DateAndTimeUtils.STANDARD_DATE_ONLY_FOMATTER));
        getModel().setTooltip(formatDateTimeInUserTimezone(datetime, DateAndTimeUtils.STANDARD_DATE_AND_TIME_FOMATTER));
    }

    private String formatDateTimeInUserTimezone(LocalDateTime dateTime, DateTimeFormatter formatter) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault());
        String userTimeZoneId = VaadinDateAndTimeUtils.getUserTimeZoneId();
        if (userTimeZoneId != null) {
            zonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of(userTimeZoneId));
        }
        return formatter.format(zonedDateTime);
    }

    public interface Model extends TemplateModel {
        void setDate(String date);

        void setDatetime(String datetime);

        void setTooltip(String tooltip);
    }
}
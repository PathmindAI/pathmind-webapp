package io.skymind.pathmind.webapp.ui.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.server.InitialPageSettings;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.utils.CookieUtils;

public class VaadinUtils {
    private VaadinUtils() {
    }

    /**
     * Until Vaadin has the ability to get the view names https://github.com/vaadin/flow/issues/1897 this is a workaround.
     */
    public static String getViewName() {
        return UI.getCurrent().getInternals().getActiveViewLocation().getFirstSegment();
    }

    public static void setupFavIcon(InitialPageSettings settings) {
        settings.addFavIcon("icon", "frontend/images/favicon.png", "32x32");
    }

    public static Optional<Element> getElementById(UI ui, String id) {
        if (id == null) {
            return Optional.empty();
        }
        return ui.getElement().getChildren()
                .filter((element) -> id.equals(element.getAttribute("id"))).findFirst();
    }

    public static void signout(UI ui, boolean keepCurrentUrl) {
        CookieUtils.deleteAWSCanCookie();
        if (keepCurrentUrl) {
            ui.getSession().getSession().invalidate();
            ui.getPage().reload();
        } else {
            ui.getPage().setLocation(Routes.LOGOUT_URL);
        }
    }

    /**
     * This is a complex solution because the internal representation is both an unmodifiable map and list. Therefore instead of
     * updating the values we need to recreate it from the internal data.
     */
    public static QueryParameters updateQueryParameters(QueryParameters queryParameters, String name, String value) {

        Map<String, List<String>> parameters = queryParameters.getParameters().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> new ArrayList<>(entry.getValue())));

        // If this is a new parameter then we need to add it.
        if (!parameters.containsKey(name)) {
            parameters.put(name, new ArrayList<>());
        }

        // IMPORTANT -> In our case we only ever have one value per parameter so we can just replace it as needed.
        parameters.get(name).clear();
        parameters.get(name).add(value);

        // Make all unmodifiable to be safe as per Vaadin's code.
        parameters.keySet().stream().forEach(key -> Collections.unmodifiableList(parameters.get(key)));
        Collections.unmodifiableMap(parameters);

        return new QueryParameters(parameters);
    }

    /**
     * IMPORTANT -> This is a naive implementation which assumes that all the parameters passed into this method are already all encoded. That is to say they
     * don't use any special characters and so on. This is because at this stage the names and values are entirely defined within
     * the application and contain no user data. If the parameter is already there then this method will overwrite the existing value with
     * the new one that is passed in. So for example if you try to set chart=1 and there is already a parameter of chart=2 then that parameter
     * will be overwritten to the new value. Should we need to adjust this then we'll need to refactor this method.
     */
    public static void addParameterToCurrentURL(Supplier<Optional<UI>> getUISupplier, String parameterName, String parameterValue) {
        getUISupplier.get().ifPresent(ui -> {
            ui.getPage().executeJs("return window.location.href").then(String.class, location -> {
                List<String> parameters = getExistingParameterList(location);
                // Remove the parameter if it already exists
                parameters.stream()
                        .filter(parameter -> parameter.startsWith(parameterName))
                        .findAny()
                        .ifPresent(duplicate -> parameters.remove(duplicate));
                // Add the parameter now that we know it's not possible to be in the list.
                parameters.add(parameterName + "=" + parameterValue);
                getUISupplier.get().get().getPage().getHistory().replaceState(null,
                        getLocationWithoutParameters(location) + "?" + String.join("&", parameters));
            });
        });
    }

    private static String getLocationWithoutParameters(String location) {
        if (location.indexOf("?") < 0) {
            return location;
        }
        return location.substring(0, location.indexOf("?"));
    }

    private static List<String> getExistingParameterList(String location) {
        if (location.indexOf("?") < 0) {
            return new ArrayList<>();
        }
        String parameterString = location.substring(location.indexOf("?") + 1);
        // The convoluted code below is so that we can add the new parameter to the list.
        return new ArrayList<>(Arrays.asList(parameterString.split("\\s*&\\s*")));
    }
}

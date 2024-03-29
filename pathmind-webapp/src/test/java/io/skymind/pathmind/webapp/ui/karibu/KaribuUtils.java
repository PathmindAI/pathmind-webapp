package io.skymind.pathmind.webapp.ui.karibu;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.mock.MockedUI;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.KaribuExtendedClientDetails;

import org.mockito.Mockito;

import kotlin.jvm.functions.Function0;

import static org.junit.Assert.assertSame;

public class KaribuUtils {

    private static class ParentComponent extends Div {

        private final UI ui;

        public ParentComponent(UI ui, Component component) {
            this.ui = ui;
            add(component);
        }

        @Override
        public Optional<UI> getUI() {
            return Optional.of(ui);
        }
    }

    public static void setup() {
        MockVaadin.setup(new Routes(), () -> new MockedUI());
    }

    public static UI setup(Component component) {
        MockedUI ui = Mockito.spy(new MockedUI());
        MockVaadin.setup(new Routes(), () -> ui);
        new ParentComponent(ui, component);
        return ui;
    }

    public static void setupRoutes(Class<? extends Component>... routes) {
        HashSet<Class<? extends Component>> routesSet = new HashSet<>(Arrays.asList(routes));
        MockVaadin.setup(new Routes(routesSet, Collections.emptySet(), true), () -> new MockedUI());
    }

    public static void tearDown() {
        MockVaadin.tearDown();
    }

    public static Class<? extends HasElement> getActiveViewClass() {
        return UI.getCurrent().getInternals().getActiveRouterTargetsChain().get(0).getClass();
    }

    public static void assertActiveViewClass(Class<? extends Component> expectedActiveViewClass) {
        assertSame(expectedActiveViewClass, KaribuUtils.getActiveViewClass());
    }

    public static void mockExtendedClientDetails() {
        UI.getCurrent().getInternals().setExtendedClientDetails(new KaribuExtendedClientDetails());
    }
}
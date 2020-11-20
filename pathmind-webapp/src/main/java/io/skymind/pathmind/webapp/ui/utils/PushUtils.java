package io.skymind.pathmind.webapp.ui.utils;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.server.Command;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PushUtils {
    // TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/41 -> Trying different ways to resolve push
    public static void push(Component component, Command command) {
        push(component.getUI(), command);
    }

    // TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/41 -> Trying different ways to resolve push
    public static void push(Optional<UI> optionalUI, Command command) {
        optionalUI.ifPresentOrElse(
                ui -> push(ui, command),
                () -> log.error("-------> PUSH FAILED"));
    }

    public static void push(Optional<UI> optionalUI, SerializableConsumer<UI> consumer) {
        optionalUI.ifPresentOrElse(
                ui -> push(ui, consumer),
                () -> log.error("-------> PUSH FAILED"));
    }

    /**
     * Helper method for eventbus subscribers.
     */
    public static void push(Supplier<Optional<UI>> supplier, SerializableConsumer<UI> consumer) {
        push(supplier.get(), consumer);
    }

    /**
     * Helper method for eventbus subscribers.
     */
    public static void push(Supplier<Optional<UI>> supplier, Command command) {
        push(supplier.get(), command);
    }

    // TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/41 -> Trying different ways to resolve push
    private static void push(UI ui, Command command) {
        if (ui == null) {
            log.error("-------> PUSH FAILED");
            return;
        }
        ui.access(command);
    }

    private static void push(UI ui, SerializableConsumer<UI> consumer) {
        if (ui == null) {
            log.error("-------> PUSH FAILED");
            return;
        }
        ui.access(() -> consumer.accept(ui));
    }
}
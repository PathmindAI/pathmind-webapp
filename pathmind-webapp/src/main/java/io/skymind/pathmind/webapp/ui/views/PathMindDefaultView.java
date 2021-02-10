package io.skymind.pathmind.webapp.ui.views;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.shared.communication.PushMode;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.molecules.CookieConsentBox;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.utils.PathmindUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Do NOT implement any default methods for this interface because a large part of it's goal is to remind
 * the developer to implement these methods in all the views to keep the layout and coding consistent.
 */
@Slf4j
public abstract class PathMindDefaultView extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle {
    @Autowired
    private SegmentIntegrator segmentIntegrator;

    @Autowired
    private AWSApiClient awsApiClient;

    private int previousWindowWidth = 0;

    private UI ui;

    public PathMindDefaultView() {
        setWidth("100%");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        GuiUtils.removeMarginsPaddingAndSpacing(this);
        setClassName("default-view");
    }

    public void beforeEnter(BeforeEnterEvent event) {

        // IMPORTANT -> This is needed because the UI needed for component rendering is not always available on time.
        ui = event.getUI();

        // IMPORTANT -> Needed so that Push works consistently on every page/view.
        event.getUI().getPushConfiguration().setPushMode(PushMode.AUTOMATIC);

        // TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/217 Implement a security framework on the views.
        // Before we do anything we need to confirm the user has permission to access the data.
        // TODO -> This solution is a band-aid solution and although it does implement enough security for now
        // we absolutely have to revisit it (as well as the exception). See the method itself for more details.
        // TODO -> This throws InvalidDataException which is incorrect but it is the best we can do with the current solution
        // until we decide how we want to implement user data management.
        if (!isAccessAllowedForUser()) {
            throw new InvalidDataException("Item does not exist");
        }

        initLoadData();

        // This is used to determine if the view is correct URL such as newExperimentView and ExperimentView. If we're at the wrong
        // URL then we want to event.forwardTo() the correct URL.
        if(!isValidView(event)) {
            return;
        }

        // If there is an exception in generating the screens we don't want to display any system related information to the user for security reasons.
        // Create screenComponents prior to having them added to the screen (mainly used for parent view classes)
        createComponents();
        addComponents();
        // Update the screen based on the parameters if need be.
        initComponents();
        // Segment plugin added
        add(segmentIntegrator);
        // This is needed for pages that are reloaded through ui.navigate such as the UploadModelView.
        EventBus.unsubscribe(this);
        addEventBusSubscribers();
    }

    protected void addEventBusSubscribers() {
    }

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(this);
    }

    protected boolean isValidView(BeforeEnterEvent event) {
        return true;
    }

    /**
     * This is mainly used for when a parent view such as DefaultExperimentView needs to create components that the sub (children)
     * views will need to have instantiated before the addScreens() method is called.
     **/
    protected void createComponents() {
    }

    public void recalculateGridColumnWidth(Page page, Grid grid) {
        page.addBrowserWindowResizeListener(resizeEvent -> {
            int windowWidth = resizeEvent.getWidth();
            if ((windowWidth > 1024 && previousWindowWidth <= 1024) ||
                    (windowWidth > 1280 && previousWindowWidth <= 1280)) {
                grid.recalculateColumnWidths();
            }
            previousWindowWidth = windowWidth;
        });
    }

    protected void initLoadData() throws InvalidDataException {
        // Do nothing by default.
    }

    private void addComponents() {
        removeAll();

        CookieConsentBox cookieConsentBox = new CookieConsentBox();
        add(cookieConsentBox);

        if (awsApiClient.isUsingMockBackend()) {
            add(getWarningMessage());
        }
        final Component titlePanel = getTitlePanel();
        if (titlePanel != null) {
            add(titlePanel);
        }
        final Component mainContent = getMainContent();
        if (mainContent != null) {
            add(mainContent);
        }
    }

    private Component getWarningMessage() {
        return LabelFactory.createLabel("Using Mock Backend", "mock-backend-header");
    }

    // TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/217 Implement a security framework on the views.
    // NOTE -> This is a janky solution for https://github.com/SkymindIO/pathmind-webapp/issues/217 until we decide exactly
    // what we want to implement.
    protected boolean isAccessAllowedForUser() {
        return true;
    }

    protected abstract Component getTitlePanel();

    protected abstract Component getMainContent();

    protected void initComponents() throws InvalidDataException {
    }

    @Override
    public String getPageTitle() {
        return PathmindUtils.getPageTitle();
    }

    public Supplier<Optional<UI>> getUISupplier() {
        // Kind of a hacky solution compared to just getUI() but it's because we need the UI to be populated in the
        // beforeEnter() rather than just onAttach because the components with the timezone require it. We also can't
        // wait to render in the onAttach() method because some component navigate to themselves which means the
        // onAttach() is not called again.
        return () -> Optional.of(ui);
    }
}

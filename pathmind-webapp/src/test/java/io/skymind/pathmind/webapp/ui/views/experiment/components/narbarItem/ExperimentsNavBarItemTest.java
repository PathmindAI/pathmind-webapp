package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem;

import io.skymind.pathmind.db.dao.ExperimentDAOMock;
import io.skymind.pathmind.shared.constants.UserRole;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.security.PathmindUserDetails;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentFavoriteBusEvent;
import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.ui.mocks.ExperimentMock;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main.NavBarItemExperimentFavoriteSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;
import io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment.ExperimentViewRunUpdateSubscriber;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@Profile("test")
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = { ExperimentDAOMock.class })
public class ExperimentsNavBarItemTest {

    @Autowired
    private ExperimentDAOMock experimentDAOMock;

    // I can easily do navbarItem.setExperiment() and then assertEquals(experiment.isFavorite(), navbarItem.getChildren()...blah...blah...getComponent.getText()) or something like that but that's really
    // not a useful test in my view. In some cases yes where we have more complex components. I could also test clickButton actions which for actions that don't trigger events that's easy
    // but I'm trying to push the limits of karibu testing and trying to simulate the eventbus. Trying to test things like is the event correctly filtered, and does it render the
    // components as expected (updating the experiment from the event and then rendering the components). That also allows us to test things like popup notifications on events (archived), etc.
    @Test
    public void favoriteEventbusUpdateStatus() {

        // Temporary hack to fake logged in user since I don't want to start the whole application but the view requires it to load an experiment.
        PathmindUserDetails user = new PathmindUserDetails("u", "p", UserRole.getEnumFromId(UserRole.Paid.getId()).getGrantedAuthorities(), -1, "f", "l");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, ""));

        // Mock experiment.
        Experiment mockExperiment = new ExperimentMock();
        mockExperiment.setFavorite(false);

        // I couldn't Autowire the view. I started going down a rabbit hole of having to mock a ton of objects so instead I'm just trying to manually set it up for now.
        ExperimentView experimentView = new ExperimentView(10, 10, 10);
        experimentView.setExperiment(mockExperiment);

        // Create a dummy event.
        RunUpdateBusEvent event = new RunUpdateBusEvent(new Run());

        // Check that the event filters and then fire it. I can't fire the event through the eventbus because it's multi-threaded and I can't guarantee the event has been processed
        // by the time the next line has fired. So I need to manually call the subscriber as well as handleBusEvent.
        ExperimentViewRunUpdateSubscriber experimentViewRunUpdateSubscriber = new ExperimentViewRunUpdateSubscriber(experimentView, experimentDAOMock);
        assertEquals(experimentViewRunUpdateSubscriber.filterBusEvent(event), true);
        experimentViewRunUpdateSubscriber.handleBusEvent(event);

        // Do some basic checks on the experiment after the event has been fired.
        assertEquals(experimentView.getExperiment().getRuns().size(),1);
        assertEquals((experimentView.getExperimentsNavbar().getChildren()...blah...blah...getComponent().getText(), someValue));
    }

}

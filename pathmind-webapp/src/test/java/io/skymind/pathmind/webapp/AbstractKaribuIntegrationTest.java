package io.skymind.pathmind.webapp;

import java.util.HashSet;
import java.util.Set;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.MockedUI;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.server.RouteRegistry;
import com.vaadin.flow.server.ServiceException;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServletService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.startup.ApplicationRouteRegistry;
import com.vaadin.flow.server.startup.RouteRegistryInitializer;
import com.vaadin.flow.spring.SpringServlet;
import com.vaadin.flow.spring.SpringVaadinServletService;
import io.skymind.pathmind.KaribuUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AbstractKaribuIntegrationTest.KaribuIntegrationTestApp.class)
@WebAppConfiguration
@DirtiesContext
public abstract class AbstractKaribuIntegrationTest {

    public static final String PACKAGE_NAME = "io.skymind.pathmind";

    @ComponentScan(PACKAGE_NAME)
    public static class KaribuIntegrationTestApp {

    }

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private ServletContext sctx;

    @Before
    public void setupVaadin() throws ServletException {
        Routes foo = new Routes().autoDiscoverViews(PACKAGE_NAME);
        Set<Class<?>> routes = new HashSet<>(foo.getRoutes());
        routes.addAll(foo.getErrorRoutes());

        RouteRegistryInitializer routeRegistryInitializer = new RouteRegistryInitializer();
        routeRegistryInitializer.onStartup(routes, sctx);
        RouteRegistry routeRegistry = ApplicationRouteRegistry.getInstance(sctx);

        final SpringServlet servlet = new SpringServlet(ctx, true) {
            @Override
            protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException {
                final VaadinServletService service = new SpringVaadinServletService(this, deploymentConfiguration, ctx) {
                    @Override
                    protected boolean isAtmosphereAvailable() {
                        return false;
                    }

                    @Override
                    protected RouteRegistry getRouteRegistry() {
                        return routeRegistry;
                    }

                    @Override
                    public String getMainDivId(VaadinSession session, VaadinRequest request) {
                        return "ROOT-1";
                    }
                };
                service.init();
                return service;
            }
        };
        MockVaadin.setup(MockedUI::new, servlet);

        // VaadinDateAndTimeUtils.withUserTimeZoneId we use in many places to resolve correct timezone to
        // show date and time needs ExtendedClientDetails to be be resolved.
        KaribuUtils.mockExtendedClientDetails();
    }


}

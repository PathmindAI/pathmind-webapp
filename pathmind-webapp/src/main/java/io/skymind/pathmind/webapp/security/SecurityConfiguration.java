package io.skymind.pathmind.webapp.security;

import java.util.HashMap;
import java.util.Map;

import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.services.billing.StripeService;
import io.skymind.pathmind.shared.constants.UserRole;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.firewall.RequestRejectedException;

@Slf4j
@EnableWebSecurity
public class SecurityConfiguration {

    /**
     * This is a security configuration for REST APIs exposed from this web app.
     * HTTP Basic auth is used to secure those APIs.
     */
    @Configuration
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        private final PasswordEncoder passwordEncoder;
        private final String apiUsername;
        private final String apiPassword;

        @Autowired
        public ApiWebSecurityConfigurationAdapter(PasswordEncoder passwordEncoder,
                                                  @Value("${api.username}") String apiUsername,
                                                  @Value("${api.password}") String apiPassword) {
            this.apiUsername = apiUsername;
            this.apiPassword = apiPassword;
            this.passwordEncoder = passwordEncoder;
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            String password = passwordEncoder.encode(apiPassword);
            auth.inMemoryAuthentication().passwordEncoder(passwordEncoder)
                    .withUser(apiUsername).password(password).roles("API_USER");
        }

        protected void configure(HttpSecurity http) throws Exception {
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            http.csrf().disable()
                    .antMatcher("/api/**")
                    .authorizeRequests()
                    .antMatchers("/api/**").authenticated()
                    .and()
                    .httpBasic();
        }
    }


    /**
     * Configures spring security, doing the following:
     * <li>Bypass security checks for static resources,</li>
     * <li>Restrict access to the application, allowing only logged in users,</li>
     * <li>Set up the login form,</li>
     * <li>Configures the {@link UserDetailsServiceImpl}.</li>
     */
    @Configuration
    static class VaadinFrontendConfigurationAdapter extends WebSecurityConfigurerAdapter {

        private final UserDetailsService userDetailsService;
        private final PasswordEncoder passwordEncoder;
        private final StripeService stripeService;

        @Autowired
        public VaadinFrontendConfigurationAdapter(
                StripeService stripeService,
                UserDetailsService userDetailsService,
                PasswordEncoder passwordEncoder) {
            this.userDetailsService = userDetailsService;
            this.passwordEncoder = passwordEncoder;
            this.stripeService = stripeService;
        }

        @Bean
        @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        public CurrentUser currentUser(UserDAO userDAO) {
            final String username = SecurityUtils.getUsername();
            PathmindUser user =
                    username != null ? userDAO.findByEmailIgnoreCase(username) : null;

            if (user != null) {
                final UserRole initRole  = user.getAccountType();
                if (!UserRole.isInternalOrEnterpriseOrPartnerUser(initRole)) {
                    try {
                        StripeService.Result<Boolean, StripeService.StripeError> resultHasActiveSubscription =
                                this.stripeService.userHasActiveProfessionalSubscription(user.getEmail());

                        StripeService.StripeError error = resultHasActiveSubscription.getError();

                        if (error != StripeService.StripeError.NoUserFound) {
                            if (resultHasActiveSubscription.getResult()) {
                                user.setAccountType(UserRole.Professional.getId());
                            } else {
                                user.setAccountType(UserRole.Basic.getId());
                            }
                            if(initRole != user.getAccountType()) {
                                log.info("Change userRole for user {} {} -> {}", user.getEmail(), initRole, user.getAccountType());
                                userDAO.update(user);
                            }
                        }

                    } catch (Exception e) {
                        log.error("Failed to verify user's subscription {}", user.getEmail(), e);
                    }
                }
            }

            return () -> user;
        }

        @Bean
        public static ErrorPageRegistrar securityErrorPageRegistrar() {
            return registry -> registry.addErrorPages(new ErrorPage(RequestRejectedException.class, "/" + Routes.LOGOUT));
        }

        /**
         * Registers our UserDetailsService and the password encoder to be used on login attempts.
         */
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            super.configure(auth);
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        }

        /**
         * Require login to access internal pages and configure login form.
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // Not using Spring CSRF here to be able to use plain HTML for the login page
            http.csrf().disable()

                    // Register our CustomRequestCache, that saves unauthorized access attempts, so
                    // the user is redirected after login.
                    .requestCache().requestCache(new CustomRequestCache())

                    // Restrict access to our application.
                    .and().authorizeRequests()

                    // Allow access to sign-up view (disabled for public beta https://github.com/SkymindIO/pathmind-webapp/issues/356)
                    .antMatchers("/" + Routes.LOGIN + Routes.WITH_PARAMETER).permitAll()
                    .antMatchers("/" + Routes.SIGN_UP).permitAll()
                    .antMatchers("/" + Routes.RESET_PASSWORD + Routes.WITH_PARAMETER).permitAll()
                    .antMatchers("/" + Routes.EMAIL_VERIFICATION + Routes.WITH_PARAMETER).permitAll()
                    .antMatchers("/" + Routes.VERIFICATION_EMAIL_SENT).permitAll()
                    .antMatchers("/actuator/health").permitAll()

                    // Allow all flow internal requests.
                    .requestMatchers(VaadinSecurityUtils::isFrameworkInternalRequest).permitAll()

                    // Allow all requests by logged in users.
                    .anyRequest().authenticated()

                    // Configure the login page.
                    .and().formLogin().loginPage("/" + Routes.LOGIN).permitAll().loginProcessingUrl("/" + Routes.LOGIN_PROCESSING)
                    .failureHandler(getFailureHandler())

                    // Register the success handler that redirects users to the page they last tried
                    // to access
                    .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())

                    // Configure logout
                    .and().logout().logoutUrl("/" + Routes.LOGOUT).logoutSuccessUrl("/" + Routes.LOGOUT_SUCCESS);
        }

        private AuthenticationFailureHandler getFailureHandler() {
            Map<String, String> failureUrlMap = new HashMap();
            failureUrlMap.put(BadCredentialsException.class.getName(), "/" + Routes.LOGIN + "/" + Routes.BAD_CREDENTIALS);
            failureUrlMap.put(InternalAuthenticationServiceException.class.getName(),
                    "/" + Routes.LOGIN + "/" + Routes.EMAIL_VERIFICATION_FAILED);

            PathmindAuthenticationFailureHandler handler = new PathmindAuthenticationFailureHandler();
            handler.setExceptionMappings(failureUrlMap);
            return handler;
        }

        /**
         * Allows access to static resources, bypassing Spring security.
         */
        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers(
                    // Vaadin Flow static resources
                    "/VAADIN/**",

                    // the standard favicon URI
                    "/favicon.ico",

                    // the robots exclusion standard
                    "/robots.txt",

                    // icons and images
                    "/icons/**",
                    "/images/**",

                    // (development mode) static resources
                    "/frontend/**",

                    // (development mode) webjars
                    "/webjars/**",

                    // (development mode) H2 debugging console
                    "/h2-console/**",

                    // (production mode) static resources
                    "/frontend-es5/**", "/frontend-es6/**");
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

    }
}

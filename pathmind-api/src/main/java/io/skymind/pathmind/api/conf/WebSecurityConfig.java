package io.skymind.pathmind.api.conf;

import io.skymind.pathmind.api.conf.security.AuthenticationFailureHandlerEntryPoint;
import io.skymind.pathmind.api.conf.security.PathmindApiAuthenticationProcessingFilter;
import io.skymind.pathmind.api.conf.security.PathmindApiAuthenticationProvider;
import io.skymind.pathmind.api.domain.user.SingupController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan("io.skymind.pathmind.api")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PathmindApiAuthenticationProvider pathmindApiAuthenticationProvider;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(pathmindApiAuthenticationProvider);
    }

    @Autowired
    private AuthenticationFailureHandlerEntryPoint authenticationFailureHandlerEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationFailureHandlerEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, SingupController.SIGNUP_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic().disable()
                .addFilterBefore(
                        new PathmindApiAuthenticationProcessingFilter(
                                authenticationManager(), authenticationFailureHandlerEntryPoint
                        ),
                        BasicAuthenticationFilter.class
                )
                .cors()
        ;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}

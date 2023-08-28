package com.jorge.securitytest.security;

import com.jorge.securitytest.security.customhandlers.CustomAuthenticationSucessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("jorge")
                .password(passwordEncoder().encode("jorge123"))
                .roles("ADMIN")
                .build()
        );
        manager.createUser(User.withUsername("maria")
                .password(passwordEncoder().encode("maria123"))
                .roles("USER")
                .build()
        );

        return manager;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .authorizeHttpRequests( auth -> {
                    auth.requestMatchers("/basic/p1").hasRole("ADMIN");
                    auth.requestMatchers("/basic/p2").hasAnyRole("USER", "ADMIN");
                    auth.requestMatchers("/basic").permitAll();
                    auth.requestMatchers("/session/info").authenticated();
                    auth.requestMatchers("/session/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(formlogin -> {
                    formlogin.loginPage("/customloginpage").permitAll();
                    formlogin.loginProcessingUrl("/customlogin");
                    //formlogin.defaultSuccessUrl("/basic/home");                         // --> Basic redirect   [SUCCESS]
                    formlogin.successHandler(customSuccessHandler());                     // --> Custom           [SUCCESS]
                    formlogin.failureUrl("/loginfailure").permitAll(); // --> Basic redirect   [FAILURE]
                    //formlogin.failureHandler(customFailureHandler());                   // --> Custom           [FAILURE]

                })
                .logout(logout -> {
                    logout.logoutUrl("/exit").permitAll();
                    logout.logoutSuccessUrl("/basic/goodbye");
                })
                .exceptionHandling( exception -> {
                    exception.accessDeniedPage("/basic/noaccess");          // --> Basic redirect   [EXCEPTION]
                    //exception.accessDeniedHandler()                                     // --> Custom           [EXCEPTION]
                })
                .sessionManagement(session -> {
                    session.sessionConcurrency(sessionConcurrency -> {
                        sessionConcurrency.maximumSessions(1);
                        sessionConcurrency.expiredUrl("/session/expired");
                        sessionConcurrency.sessionRegistry(sessionRegistry()); // * necessary?
                    });
                })
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    public AuthenticationSuccessHandler customSuccessHandler(){
        return new CustomAuthenticationSucessHandler();
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    // Setting custom handlers help you execute additional actions and are not limited to requests - responses
    // Those actions may include persist data
    // That goes for all custom-type handlers
    // You can store session-info type too
}

package org.zerock.b01.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.zerock.b01.security.CustomUserDetailsService;
import org.zerock.b01.security.handler.Custom403Handler;
import org.zerock.b01.security.handler.CustomSocialLoginSuccessHandler;

import javax.sql.DataSource;

@Configuration
@Log4j2
@EnableMethodSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

    private final DataSource dataSource;

    private final UserDetailsService userDetailsService;

//    @Bean
//    public UserDetailsService userDetailsService(){
//        return new CustomUserDetailsService(passwordEncoder());
//    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception {

        log.info("----------------Security Config----------------------");

        //deprecated
        // http.formLogin();
        // http.formLogin(Customizer.withDefaults());
        http.formLogin(form -> {

            form.loginPage("/member/login");


        });

        http.csrf(httpSecurityCsrfConfigurer ->  httpSecurityCsrfConfigurer.disable() );
        http.logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.logoutUrl("/logout"));

        http.rememberMe(httpSecurityRememberMeConfigurer -> {

            httpSecurityRememberMeConfigurer.key("12345678")
                    .tokenRepository(persistentTokenRepository())
                    .userDetailsService(userDetailsService)
                    .tokenValiditySeconds(60*60*24*30);

        });

        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {

            httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler());
        });

        http.oauth2Login( httpSecurityOAuth2LoginConfigurer -> {
            httpSecurityOAuth2LoginConfigurer.loginPage("/member/login");
            httpSecurityOAuth2LoginConfigurer.successHandler(authenticationSuccessHandler());
        });


        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    }


    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        log.info("------------web configure-------------------");

        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());

    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }
}

package com.cursor.onlineshop.security;

import com.cursor.onlineshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.Set;

@Configuration
@EnableJpaRepositories(basePackages = "com.cursor.onlineshop", considerNestedRepositories = true)
@EnableWebSecurity
@RequiredArgsConstructor
public class AppConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
    // keep empty line after class declaration
    private final UserService userService;
    private final JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/health").permitAll()
                .antMatchers("/auth/register", "/auth/regadmin", "/auth/login").anonymous()
                .anyRequest().authenticated()
                .and()
                .formLogin() // useless here as we don't have login form, because it's restful service
                .and()
                .exceptionHandling().disable() // please add exception handling with @ControllerAdvice like here
                // https://github.com/TarasLavrenyuk/MovieReview/blob/master/src/main/java/com/cursor/moviereview/exceptions/handlers/MovieResponseEntityExceptionHandler.java
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SPRING_WEB)
                .produces(Set.of(MediaType.APPLICATION_JSON_VALUE))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cursor.onlineshop"))
                .build().apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Cursor team project #3 \"Online shop\"", "REST API description", "1.0",
                null, null, null, null,
                Collections.emptyList());
    }
}

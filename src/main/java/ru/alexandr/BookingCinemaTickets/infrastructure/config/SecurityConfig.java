package ru.alexandr.BookingCinemaTickets.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.alexandr.BookingCinemaTickets.infrastructure.config.property.JavaMelodyProperties;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.RoleEnum;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.filter.JwtAuthenticationFilter;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.parser.AccessTokenParser;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserDetailsService userDetailsService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration,
                          UserDetailsService userDetailsService,
                          CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.userDetailsService = userDetailsService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    /**
     * Для REST API - Stateless + JWT
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http,
                                                      JwtAuthenticationFilter jwtAuthenticationFilter)
            throws Exception {
        return http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/registration").permitAll()
                        .anyRequest().authenticated()
                ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Для Actuator endpoints
     */
    @Bean
    @Order(2)
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/actuator/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .build();
    }

    /**
     * Для JavaMelody endpoints
     */
    @Bean
    @Order(3)
    public SecurityFilterChain javaMelodySecurityFilterChain(HttpSecurity http,
                                                             JavaMelodyProperties javaMelodyProperties) throws Exception {
        String javaMelodyUrl = javaMelodyProperties.getMonitoringPath() + "/**";
        return http
                .securityMatcher(javaMelodyUrl)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().hasRole(RoleEnum.ADMIN.name())
                )
                .build();
    }

    /**
     * Для HTML - Stateful + formLogin
     */
    @Bean
    @Order(4)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/registration").anonymous()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasRole(RoleEnum.ADMIN.name())
                        .anyRequest().authenticated()
                ).formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureUrl("/login")
                        .permitAll()
                ).logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                ).userDetailsService(userDetailsService)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AccessTokenParser accessTokenParser,
                                                           UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(accessTokenParser, userDetailsService);
    }
}

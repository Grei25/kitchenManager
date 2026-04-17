package ru.top.kitchenmanager.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import ru.top.kitchenmanager.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(loginSuccessHandler())
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll())
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied"))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(mvcMatcherBuilder.pattern("/css/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/js/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/images/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/static/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/image/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/favicon.ico")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/client/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/login")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/access-denied")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/admin/dishes/**")).hasRole("SUPER_ADMIN")
                        .requestMatchers(mvcMatcherBuilder.pattern("/admin/**")).hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .requestMatchers(mvcMatcherBuilder.pattern("/cook/**")).hasAnyRole("COOK", "SUPER_ADMIN")
                        .requestMatchers(mvcMatcherBuilder.pattern("/courier/**")).hasAnyRole("COURIER", "SUPER_ADMIN")
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:8080"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return username -> {
            // Только встроенные пользователи (из памяти)
            if (username.equals("superadmin")) {
                return User.builder()
                        .username("superadmin")
                        .password(passwordEncoder.encode("super123"))
                        .roles("SUPER_ADMIN")
                        .build();
            }
            if (username.equals("admin")) {
                return User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .roles("ADMIN")
                        .build();
            }
            if (username.equals("cook")) {
                return User.builder()
                        .username("cook")
                        .password(passwordEncoder.encode("cook123"))
                        .roles("COOK")
                        .build();
            }
            if (username.equals("courier")) {
                return User.builder()
                        .username("courier")
                        .password(passwordEncoder.encode("courier123"))
                        .roles("COURIER")
                        .build();
            }
            
            // Попробуем найти в БД
            var dbUser = userRepository.findByUsername(username);
            if (dbUser.isPresent()) {
                ru.top.kitchenmanager.model.User user = dbUser.get();
                boolean active = user.getActive() != null ? user.getActive() : true;
                
                return User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .accountLocked(!active)
                        .disabled(!active)
                        .build();
            }
            
            throw new UsernameNotFoundException("User not found: " + username);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

            String redirectUrl = "/access-denied";

            if (roles.contains("ROLE_SUPER_ADMIN") || roles.contains("ROLE_ADMIN")) {
                redirectUrl = "/admin/dashboard";
            } else if (roles.contains("ROLE_COOK")) {
                redirectUrl = "/cook/orders";
            } else if (roles.contains("ROLE_COURIER")) {
                redirectUrl = "/courier/orders";
            }

            response.sendRedirect(redirectUrl);
        };
    }
}
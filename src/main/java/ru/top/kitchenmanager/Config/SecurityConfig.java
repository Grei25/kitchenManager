package ru.top.kitchenmanager.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http
                .authorizeHttpRequests(authz -> authz
                        // H2 консоль доступна всем
                        .requestMatchers(mvcMatcherBuilder.pattern("/h2-console/**")).permitAll()
                        // Статические ресурсы
                        .requestMatchers(mvcMatcherBuilder.pattern("/css/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/js/**")).permitAll()
                        // Клиентская часть
                        .requestMatchers(mvcMatcherBuilder.pattern("/")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/client/**")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/login")).permitAll()
                        // Админка
                        .requestMatchers(mvcMatcherBuilder.pattern("/admin/**")).hasRole("ADMIN")
                        // Повар
                        .requestMatchers(mvcMatcherBuilder.pattern("/cook/**")).hasRole("COOK")
                        // Курьер
                        .requestMatchers(mvcMatcherBuilder.pattern("/courier/**")).hasRole("COURIER")
                        // Все остальное требует аутентификации
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/", true)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                )
                // Отключаем CSRF для H2 консоли
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(mvcMatcherBuilder.pattern("/h2-console/**"))
                )
                // Разрешаем фреймы для H2 консоли
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails cook = User.builder()
                .username("cook")
                .password(passwordEncoder().encode("cook123"))
                .roles("COOK")
                .build();

        UserDetails courier = User.builder()
                .username("courier")
                .password(passwordEncoder().encode("courier123"))
                .roles("COURIER")
                .build();

        return new InMemoryUserDetailsManager(admin, cook, courier);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
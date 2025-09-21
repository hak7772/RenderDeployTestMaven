package org.example.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .antMatchers("/register", "/css/**", "/js/**").permitAll() // Разрешить доступ к странице регистрации и статическим ресурсам
                        .antMatchers("/admin/**").hasRole("ADMIN") // Пример: Страницы для администраторов
                        .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                )
                .formLogin((form) -> form
                        .loginPage("/login") // Указываем URL страницы входа
                        .defaultSuccessUrl("/") // URL после успешного входа
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll()); // Разрешаем всем пользователям выходить

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Используем BCrypt для хеширования паролей
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
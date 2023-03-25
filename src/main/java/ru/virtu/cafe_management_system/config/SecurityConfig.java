package ru.virtu.cafe_management_system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.virtu.cafe_management_system.security.AuthProviderImpl;
import ru.virtu.cafe_management_system.services.PersonDetailsService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthProviderImpl authProvider;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(AuthProviderImpl authProvider, PersonDetailsService personDetailsService) {
        this.authProvider = authProvider;
        this.personDetailsService = personDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // configure authorization
        http.authorizeRequests()
                .antMatchers("/auth/login", "/auth/registration", "/error",
                        "/cafes", "/cafes/{id}/edit", "/cafes/{id}",
                        "/employees", "/employees/{id}/edit", "/employees/{id}",
                        "/shifts", "/shifts/{id}/edit", "/shifts/{id}",
                        "/dishes", "/dishes/{id}/edit", "/dishes/{id}",
                        "/orders", "/orders/{id}/edit", "/orders/{id}", "/orders/{id}/addDish", "/orders/deleteDish/{id}",
                        "/bookings", "/bookings/{id}/edit", "/bookings/{id}"
                        ).permitAll()
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/cafes", true)
                .failureUrl("/auth/login?error")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login");
    }

    // Sets up authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
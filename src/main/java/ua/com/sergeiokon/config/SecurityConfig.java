package ua.com.sergeiokon.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ua.com.sergeiokon.repository.UserRepository;
import ua.com.sergeiokon.repository.entity.Role;
import ua.com.sergeiokon.security.UserDetailsServiceImpl;
import ua.com.sergeiokon.security.jwt.JWTFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private UserRepository userRepository;
    @Autowired private JWTFilter filter;
    @Autowired private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .cors()
                .and()
                .authorizeHttpRequests()
//                .antMatchers("/api/auth/**").permitAll()
//                .antMatchers("/api/user/**").hasRole("USER")

                .antMatchers("/v1/auth/**").permitAll()

                .antMatchers(HttpMethod.GET, "/v1/events**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/v1/events/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())

                .antMatchers(HttpMethod.GET, "/v1/files**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/v1/files/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())

                .antMatchers("/v1/s3**").hasAnyAuthority(Role.MODERATOR.name(), Role.ADMIN.name())
                .antMatchers("/v1/s3/**").hasAnyAuthority(Role.MODERATOR.name(), Role.ADMIN.name())

                .antMatchers("/**").hasAuthority(Role.ADMIN.name())

                .and()
                .userDetailsService(userDetailsService)
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                )
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
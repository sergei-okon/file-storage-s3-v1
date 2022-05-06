//package ua.com.sergeiokon.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import ua.com.sergeiokon.repository.entity.Role;
//
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfigOLD extends WebSecurityConfigurerAdapter {
//
//    private final UserDetailsService userDetailsService;
//
//    public SecurityConfigOLD(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Bean
//    protected PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(12);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/v1/").permitAll()
//
//                .antMatchers(HttpMethod.GET, "/v1/events**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
//                .antMatchers(HttpMethod.GET, "/v1/events/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
//
//                .antMatchers(HttpMethod.GET, "/v1/files**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
//                .antMatchers(HttpMethod.GET, "/v1/files/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
//
//                .antMatchers("/v1/s3**").hasAnyAuthority(Role.MODERATOR.name(), Role.ADMIN.name())
//                .antMatchers("/v1/s3/**").hasAnyAuthority(Role.MODERATOR.name(), Role.ADMIN.name())
//
//                .antMatchers("/**").hasAuthority(Role.ADMIN.name())
//
//                .anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
//    }
//}

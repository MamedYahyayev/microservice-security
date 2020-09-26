package az.maqa.microservices.security;


import az.maqa.microservices.config.JwtConfig;
import az.maqa.microservices.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .addFilter(getAuthenticationFilter())
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
                .antMatchers(HttpMethod.POST, "/create").permitAll()
                .anyRequest()
                .authenticated();
    }

    private JwtUsernameAndPasswordAuthenticationFilter getAuthenticationFilter() throws Exception {
        JwtUsernameAndPasswordAuthenticationFilter filter = new JwtUsernameAndPasswordAuthenticationFilter(userService, jwtConfig, authenticationManager());
        filter.setFilterProcessesUrl(jwtConfig.getUri());
        return filter;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

}

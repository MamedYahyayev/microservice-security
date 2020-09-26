package az.maqa.microservices.security;

import az.maqa.microservices.config.JwtConfig;
import az.maqa.microservices.dto.UserDTO;
import az.maqa.microservices.request.RequestLogin;
import az.maqa.microservices.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private JwtConfig jwtConfig;
    private UserService userService;

    public JwtUsernameAndPasswordAuthenticationFilter(UserService userService, JwtConfig jwtConfig, AuthenticationManager authenticationManager) {
        this.jwtConfig = jwtConfig;
        this.userService = userService;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            RequestLogin credentials = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(credentials.getEmail(),
                    credentials.getPassword(), new ArrayList<>());

            return getAuthenticationManager().authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String userName = ((User) auth.getPrincipal()).getUsername();
        UserDTO userDetails = userService.getUserDetailsByEmail(userName);

        String token = Jwts.builder()
                .setSubject(userDetails.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .compact();

        res.addHeader("token", jwtConfig.getPrefix() + token);
        res.addHeader("userId", userDetails.getUserId());

    }
}

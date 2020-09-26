package az.maqa.microservices.security;

import az.maqa.microservices.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtConfig jwtConfig;

    public JwtTokenAuthenticationFilter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String header = req.getHeader(jwtConfig.getHeader());

        // 2. validate the header and check the prefix
        if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
            chain.doFilter(req, res);        // If not valid, go to the next filter.
            return;
        }

        String token = header.replace(jwtConfig.getPrefix(), "");

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSecret().getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();

            if (username != null) {

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, new ArrayList<>());

                // 6. Authenticate the user
                // Now, user is authenticated
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(req, res);
    }


}

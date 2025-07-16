package Adventure_generator.Config;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import Adventure_generator.Util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/*
 * This class check every incoming request for a JWT token, to see if it's valid or not.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException{
            // get Auth header
            String httpHeader = request.getHeader("Authorization");

            if (httpHeader != null && httpHeader.startsWith("Bearer ")){
                String token = httpHeader.substring(7); // remove "Bearer "

                try{
                    String username = jwtUtil.getUsernameFromToken(token);
                    if(jwtUtil.validateToken(token, username)){
                        // Represents an authenticated user 
                        UsernamePasswordAuthenticationToken authUser = 
                        new UsernamePasswordAuthenticationToken
                        (username,
                        null, // credentials 
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // authorities 
                        );
                        SecurityContextHolder.getContext().setAuthentication(authUser); // Allow access to authenticated user
                    }
                }catch(Exception e){
                    logger.warn("JWT validation failed: ", e.getMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Invalid or expired token\"}");
                    return;
                }
            }
            filterChain.doFilter(request,response);

    }
    
}

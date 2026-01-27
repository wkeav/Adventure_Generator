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
            
            logger.debug("JwtAuthenticationFilter - Request URI: {}", request.getRequestURI());
            logger.debug("JwtAuthenticationFilter - Authorization header: {}", httpHeader);

            if (httpHeader != null && httpHeader.startsWith("Bearer ")){
                String token = httpHeader.substring(7); // Remove "Bearer "

                try{
                    String username = jwtUtil.getUsernameFromToken(token);
                    logger.debug("JwtAuthenticationFilter - Extracted username: {}", username);
                    if(jwtUtil.validateToken(token, username)){
                        logger.debug("JwtAuthenticationFilter - Token validated successfully for user: {}", username);
                        // Represents an authenticated user 
                        UsernamePasswordAuthenticationToken authUser = 
                        new UsernamePasswordAuthenticationToken
                        (username,
                        null, // Credentials 
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // Authorities 
                        );
                        SecurityContextHolder.getContext().setAuthentication(authUser); // Allow access to authenticated user
                    } else {
                        logger.warn("JwtAuthenticationFilter - Token validation failed for username: {}", username);
                    }
                }catch(Exception e){
                    logger.error("JWT validation failed: {}", e.getMessage(), e);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Invalid or expired token\"}");
                    return;
                }
            }
            filterChain.doFilter(request,response);

    }
    
}

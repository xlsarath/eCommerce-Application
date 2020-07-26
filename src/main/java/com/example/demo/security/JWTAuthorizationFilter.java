package com.example.demo.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.example.demo.security.SecurityConstants.HEADER_STRING;
import static com.example.demo.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
            if (JwtUtils.isBasicAuth(request)){
                chain.doFilter(request, response);
            } else {
                UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                chain.doFilter(request, response);
            }
        }

        private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
            String header = request.getHeader(HEADER_STRING);
            boolean isValidToken = JwtUtils.isvalidHeader(header); //checks that the token is available and has a valid signature
            if (isValidToken){
                String token = header.replace(TOKEN_PREFIX, "");
                if (JwtUtils.isvalidToken(token)){
                    String user = JwtUtils.getSubject(token).get();
                    return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                }
            }
            return null;
        }

    }
}

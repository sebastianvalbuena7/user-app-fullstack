package com.backend.usersapp.auth.filters;

import com.backend.usersapp.auth.TokenConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.*;

import static com.backend.usersapp.auth.TokenConfig.SECRET_KEY;

public class JwtValidationFilter extends BasicAuthenticationFilter {
    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
                throws IOException, ServletException {
        String header = request.getHeader(TokenConfig.HEADER_AUTHORIZATION);

        if (header == null || !header.startsWith(TokenConfig.PREFIX_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(TokenConfig.PREFIX_TOKEN, "");

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                        .getBody();
            String username = claims.getSubject();
            List<GrantedAuthority>  authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (JwtException e){
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "El token JWT no es válido");

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(403);
            response.setContentType("application/json");
        }
    }
}
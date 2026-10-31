package com.jg.sistema.auth.infrastructure.config;

import com.jg.sistema.auth.infrastructure.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final com.jg.sistema.user.application.port.out.UserRepositoryPort users;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if(jwtService.validateToken(token) && SecurityContextHolder.getContext().getAuthentication() == null){
                String userEmail = jwtService.extractSubject(token);
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                    Number version = jwtService.extractAllClaims(token).get("sessionVersion", Number.class);
                    if (userDetails.isEnabled() && userDetails.isAccountNonLocked() && version != null
                            && version.longValue() == users.sessionVersion(userEmail)) {
                        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (org.springframework.security.core.AuthenticationException | com.jg.sistema.user.domain.exception.UserNotFoundException ex) {
                    SecurityContextHolder.clearContext();
                }

            }
        }

        filterChain.doFilter(request, response);
    }
}

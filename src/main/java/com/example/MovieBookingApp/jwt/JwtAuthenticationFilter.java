package com.example.MovieBookingApp.jwt;

import com.example.MovieBookingApp.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        //Extract the JWT Token from the header
        jwtToken = authHeader.substring(7);
        username = jwtService.extractUsername(jwtToken);

        //Check if we have usernmae or no authentication exist yet
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            // validate the token
            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                // create authentication with user roles
                List<SimpleGrantedAuthority> authorities = userDetails.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                //set authentication  details
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //Update the security context with authentication
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
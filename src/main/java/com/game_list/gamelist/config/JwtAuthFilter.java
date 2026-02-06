package com.game_list.gamelist.config;

import com.game_list.gamelist.service.JwtService;
import com.game_list.gamelist.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    
    @PostConstruct
    public void init(){
        System.out.println(">>JWT FILTER CRIADO<<");
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain) throws ServletException, IOException {

    System.out.println(">>> JWT FILTER: " + request.getRequestURI());

    String uri = request.getRequestURI();

    if (uri.startsWith("/auth") || uri.startsWith("/swagger") || uri.startsWith("/v3")){
        filterChain.doFilter(request, response);
        return;
    }

    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        log.debug("Sem Authorization Header ou inválido em {}", uri);
        filterChain.doFilter(request, response);
        return;
    }

    String token = authHeader.substring(7);
    String username = jwtService.validateToken(token);

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

    try{
        UserDetails userDetails = userService.loadUserByUsername(username);
        
        System.out.println(">> AUTH USER: " + userDetails.getUsername());
        System.out.println(">> AUTHORITIES: " + userDetails.getAuthorities());

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    } catch (DisabledException ex){
        log.warn("Usuário desativado tentou acessar: {}", username);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return;
    }

    filterChain.doFilter(request, response);
}
    }
}

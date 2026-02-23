package com.example.wms.tenant;

import com.example.wms.security.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Required for Roles
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TenantFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public TenantFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtils.validateToken(token)) {
                String username = jwtUtils.getUsernameFromToken(token);
                String groupId = jwtUtils.getGroupIdFromToken(token);
                String companyId = jwtUtils.getCompanyIdFromToken(token);

                // Requirement 5 & 77: Extract roles from JWT and convert to Spring Authorities
                List<String> roles = jwtUtils.getRolesFromToken(token);
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // Requirement 13, 15: Set ThreadLocal Context
                TenantContext.setGroupId(groupId);
                TenantContext.setCompanyId(companyId);

                // Requirement 38: Add MDC logging for tenant tracing
                MDC.put("tenantId", companyId);

                // Pass the extracted authorities (roles) to the authentication token
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Requirement 15, 37: Ensure tenant context is cleared after each request
            TenantContext.clear();
            MDC.remove("tenantId");
        }
    }
}
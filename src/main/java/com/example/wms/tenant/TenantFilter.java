package com.example.wms.tenant;

import com.example.wms.security.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class TenantFilter implements Filter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        String authHeader = req.getHeader("Authorization");
        String groupId = req.getHeader("X-Group-Id");
        String companyId = req.getHeader("X-Company-Id");

        // Requirement 91: Full Integration (JWT + Filter)
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                Claims claims = jwtUtils.extractAllClaims(token);

                // Extracting tenant IDs from JWT claims
                groupId = claims.get("groupId", String.class);
                companyId = claims.get("companyId", String.class);
            } catch (Exception e) {
                // Log error or handle invalid token if necessary
            }
        }

        try {
            // Setting context for the current thread (Requirement 13, 15)
            TenantContext.setGroupId(groupId);
            TenantContext.setCompanyId(companyId);
            chain.doFilter(request, response);
        } finally {
            // Mandatory: Clear context to prevent ThreadLocal leakage (Requirement 15, 142)
            TenantContext.clear();
        }
    }
}
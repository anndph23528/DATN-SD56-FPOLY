package com.example.datnsd56.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof UserInfoUserDetails) {
            UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();

            // Store user ID in the session
            HttpSession session = request.getSession();
            session.setAttribute("userId", userDetails.getUserId());
        }

        // Continue with your existing logic for role-based redirection
        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("admin"))) {
            // Nếu là admin, chuyển hướng đến /admin
            response.sendRedirect("/admin/thong-ke/hien-thi");
        } else {
            // Ngược lại, chuyển hướng đến /product
            response.sendRedirect("/product/hien-thi");
        }
    }
}

package com.goldbyte.arena.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Keeps students and admins inside their own areas. Students and admins use separate session attributes. */
@WebFilter(urlPatterns = {
    "/dashboard", "/dashboard.jsp", "/missions", "/missions.jsp",
    "/submit-answer", "/mission-result.jsp",
    "/admin/*", "/admin-dashboard.jsp", "/add-question.jsp", "/view-questions.jsp"
})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String path = request.getServletPath();
        String ctx = request.getContextPath();

        boolean adminArea = path.startsWith("/admin") || path.equals("/add-question.jsp")
                || path.equals("/view-questions.jsp");

        if (adminArea) {
            if (session == null || session.getAttribute("adminId") == null) {
                response.sendRedirect(ctx + "/admin-login.jsp?msg=Admin+login+required");
                return;
            }
        } else {
            if (session == null || session.getAttribute("userId") == null) {
                response.sendRedirect(ctx + "/login.jsp?msg=Please+log+in+to+enter+the+arena");
                return;
            }
        }
        chain.doFilter(req, res);
    }
}

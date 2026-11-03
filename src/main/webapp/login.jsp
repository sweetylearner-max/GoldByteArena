<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.goldbyte.arena.config.Html,jakarta.servlet.http.Cookie" %>
<% String pageTitle = "Log in"; %>
<%@ include file="includes/navbar.jsp" %>
<%
    String error = (String) request.getAttribute("error");
    String msg = request.getParameter("msg");
    // Cookie set by LoginServlet remembers the last username
    String remembered = "";
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie ck : cookies) { if ("gba_user".equals(ck.getName())) remembered = ck.getValue(); }
    }
    String shown = request.getParameter("identifier") != null ? request.getParameter("identifier") : remembered;
%>
<div class="panel narrow">
  <h2>Enter the Arena</h2>
  <% if (msg != null) { %><div class="alert info" style="margin-top:1rem"><%= Html.esc(msg) %></div><% } %>
  <% if (error != null) { %><div class="alert err" style="margin-top:1rem"><%= Html.esc(error) %></div><% } %>
  <form method="post" action="<%= ctx %>/login">
    <label for="identifier">Username or email</label>
    <input type="text" id="identifier" name="identifier" required value="<%= Html.esc(shown) %>">
    <label for="password">Password</label>
    <input type="password" id="password" name="password" required>
    <div class="form-actions">
      <button class="btn" type="submit">Log in</button>
      <a href="<%= ctx %>/register.jsp">Create an account</a>
    </div>
  </form>
</div>
<%@ include file="includes/footer.jsp" %>

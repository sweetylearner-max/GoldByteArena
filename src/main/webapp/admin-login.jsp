<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.goldbyte.arena.config.Html" %>
<% String pageTitle = "Admin login"; %>
<%@ include file="includes/navbar.jsp" %>
<%
    String error = (String) request.getAttribute("error");
    String msg = request.getParameter("msg");
%>
<div class="panel narrow">
  <h2>Admin sign-in</h2>
  <p class="muted">Only administrators can create and manage missions.</p>
  <% if (msg != null) { %><div class="alert info" style="margin-top:1rem"><%= Html.esc(msg) %></div><% } %>
  <% if (error != null) { %><div class="alert err" style="margin-top:1rem"><%= Html.esc(error) %></div><% } %>
  <form method="post" action="<%= ctx %>/admin-login">
    <label for="username">Admin username</label>
    <input type="text" id="username" name="username" required value="<%= Html.esc(request.getParameter("username")) %>">
    <label for="password">Password</label>
    <input type="password" id="password" name="password" required>
    <div class="form-actions"><button class="btn violet" type="submit">Sign in</button></div>
  </form>
</div>
<%@ include file="includes/footer.jsp" %>

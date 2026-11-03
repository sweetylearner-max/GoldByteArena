<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.goldbyte.arena.config.Html" %>
<% String pageTitle = "Register"; %>
<%@ include file="includes/navbar.jsp" %>
<% String error = (String) request.getAttribute("error"); %>
<div class="panel narrow">
  <h2>Join the Arena</h2>
  <p class="muted">New players start at Level 1 with 0 XP.</p>
  <% if (error != null) { %><div class="alert err" style="margin-top:1rem"><%= Html.esc(error) %></div><% } %>
  <form method="post" action="<%= ctx %>/register" novalidate>
    <label for="username">Username</label>
    <input type="text" id="username" name="username" maxlength="30" required value="<%= Html.esc(request.getParameter("username")) %>">
    <label for="email">Email address</label>
    <input type="email" id="email" name="email" maxlength="100" required value="<%= Html.esc(request.getParameter("email")) %>">
    <label for="password">Password (6 characters or more)</label>
    <input type="password" id="password" name="password" required>
    <label for="confirm">Confirm password</label>
    <input type="password" id="confirm" name="confirm" required>
    <div class="form-actions">
      <button class="btn" type="submit">Create account</button>
      <a href="<%= ctx %>/login.jsp">I already have an account</a>
    </div>
  </form>
</div>
<%@ include file="includes/footer.jsp" %>

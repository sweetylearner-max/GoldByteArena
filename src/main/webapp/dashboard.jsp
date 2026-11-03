<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.goldbyte.arena.config.Html,com.goldbyte.arena.models.User" %>
<%
    User p = (User) request.getAttribute("player");
    String error = (String) request.getAttribute("error");
    if (p == null && error == null) { response.sendRedirect(request.getContextPath() + "/dashboard"); return; }
%>
<% String pageTitle = "Dashboard"; %>
<%@ include file="includes/navbar.jsp" %>
<% if (error != null) { %><div class="alert err"><%= Html.esc(error) %></div><% } %>
<% if (p != null) {
     int solved = ((Integer) request.getAttribute("solved")).intValue();
     int rank = ((Integer) request.getAttribute("rank")).intValue();
%>
<h2>Welcome back, <%= Html.esc(p.getUsername()) %></h2>
<p class="muted">Current title: <strong class="gold"><%= Html.esc(p.getTitle()) %></strong></p>

<div class="panel" style="margin:1.5rem 0">
  <div style="display:flex;justify-content:space-between;flex-wrap:wrap;gap:.5rem">
    <h3>Level <%= p.getLevel() %></h3>
    <span class="muted"><%= p.getXpToNext() %> XP until Level <%= p.getLevel() + 1 %></span>
  </div>
  <div class="xpbar" role="progressbar" aria-valuenow="<%= p.getXpIntoLevel() %>" aria-valuemin="0" aria-valuemax="100">
    <div style="width:<%= p.getXpIntoLevel() %>%"></div>
  </div>
  <span class="muted"><%= p.getXpIntoLevel() %> / 100 XP in this level</span>
</div>

<div class="grid cols-4">
  <div class="panel stat"><b><%= p.getXp() %></b><span>Total XP</span></div>
  <div class="panel stat"><b><%= solved %></b><span>Missions solved</span></div>
  <div class="panel stat"><b>#<%= rank %></b><span>Leaderboard rank</span></div>
  <div class="panel stat"><b><%= p.getStreak() %></b><span>Day streak</span></div>
</div>

<div class="panel" style="margin-top:1.25rem">
  <h3>Achievements</h3>
  <div class="badges">
    <span class="badge <%= solved >= 1 ? "on" : "off" %>">First mission solved</span>
    <span class="badge <%= p.getStreak() >= 3 ? "on" : "off" %>">3-day streak</span>
    <span class="badge <%= solved >= 10 ? "on" : "off" %>">10 missions solved</span>
    <span class="badge <%= p.getLevel() >= 4 ? "on" : "off" %>">Reached Gold Operator</span>
  </div>
</div>

<div class="form-actions">
  <a class="btn" href="<%= ctx %>/missions">Take a mission</a>
  <a class="btn ghost" href="<%= ctx %>/leaderboard">View leaderboard</a>
</div>
<% } %>
<%@ include file="includes/footer.jsp" %>

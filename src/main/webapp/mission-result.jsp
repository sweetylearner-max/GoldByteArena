<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.goldbyte.arena.config.Html,com.goldbyte.arena.models.User,com.goldbyte.arena.dao.SubmissionDAO" %>
<%
    SubmissionDAO.SubmitResult r = (SubmissionDAO.SubmitResult) request.getAttribute("result");
    if (r == null) { response.sendRedirect(request.getContextPath() + "/missions"); return; }
%>
<% String pageTitle = "Mission result"; %>
<%@ include file="includes/navbar.jsp" %>
<div class="panel result <%= r.correct ? "win" : "lose" %>">
  <p class="muted"><%= Html.esc(r.missionTitle) %></p>
  <% if (r.correct) { %>
    <h1 class="verdict">Correct</h1>
    <p style="font-size:1.2rem">You earned <strong class="gold">+<%= r.awarded %> XP</strong>. Total XP: <strong><%= r.newXp %></strong></p>
    <% if (r.isLevelUp()) { %>
      <div class="levelup">Level up! You are now Level <%= r.newLevel %>, <%= Html.esc(User.titleFor(r.newLevel)) %>.</div>
    <% } %>
    <p class="muted">Daily streak: <%= r.streak %> <%= r.streak == 1 ? "day" : "days" %></p>
  <% } else { %>
    <h1 class="verdict">Not this time</h1>
    <p style="font-size:1.1rem">You chose <strong><%= Html.esc(r.selected) %></strong>. The correct answer was <strong class="gold"><%= Html.esc(r.correctOption) %></strong>. No XP awarded.</p>
    <p class="muted">Every mission allows one attempt, so on to the next one.</p>
  <% } %>
  <div class="form-actions" style="justify-content:center">
    <a class="btn" href="<%= ctx %>/missions">Back to missions</a>
    <a class="btn ghost" href="<%= ctx %>/dashboard">Dashboard</a>
    <a class="btn violet" href="<%= ctx %>/leaderboard">Leaderboard</a>
  </div>
</div>
<script>
  window.addEventListener('load', function () {
    <% if (r.correct) { %>
      Sfx.win(); launchConfetti(<%= r.isLevelUp() ? 320 : 120 %>);
    <% } else { %>
      Sfx.lose();
    <% } %>
  });
</script>
<%@ include file="includes/footer.jsp" %>

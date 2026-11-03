<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.goldbyte.arena.config.Html,com.goldbyte.arena.models.LeaderboardEntry,com.goldbyte.arena.models.User,java.util.List" %>
<%
    List<LeaderboardEntry> entries = (List<LeaderboardEntry>) request.getAttribute("entries");
    if (entries == null) { response.sendRedirect(request.getContextPath() + "/leaderboard"); return; }
%>
<% String pageTitle = "Leaderboard"; %>
<%@ include file="includes/navbar.jsp" %>
<%
    String error = (String) request.getAttribute("error");
    String me = (String) session.getAttribute("username");
    int[] order = {1, 0, 2};              // display order: silver, gold, bronze
    String[] cls = {"p1", "p2", "p3"};
    String[] place = {"1", "2", "3"};
%>
<div style="display:flex;justify-content:space-between;flex-wrap:wrap;gap:1rem;align-items:center">
  <div><h2>Hall of Champions</h2><p class="muted">Ranked by total XP, then missions solved.</p></div>
  <a class="btn ghost small" href="<%= ctx %>/leaderboard">Refresh rankings</a>
</div>
<% if (error != null) { %><div class="alert err" style="margin-top:1rem"><%= Html.esc(error) %></div><% } %>

<% if (entries.isEmpty() && error == null) { %>
  <div class="panel muted" style="margin-top:1.5rem">No players yet. Register and solve a mission to claim the first spot.</div>
<% } else if (!entries.isEmpty()) { %>
  <div class="podium">
  <% for (int idx : order) {
       if (idx >= entries.size()) continue;
       LeaderboardEntry e = entries.get(idx); %>
    <div class="slot <%= cls[idx] %>">
      <div class="who">
        <% if (idx == 0) { %><span class="crown" aria-hidden="true">&#9819;</span><% } %>
        <strong><%= Html.esc(e.getUsername()) %></strong>
        <span class="muted"><%= e.getXp() %> XP &middot; Level <%= e.getLevel() %></span>
      </div>
      <div class="block"><%= place[idx] %></div>
    </div>
  <% } %>
  </div>

  <div class="panel table-wrap">
    <table>
      <thead><tr><th>Rank</th><th>Player</th><th>Level</th><th class="num">Missions solved</th><th class="num">Total XP</th></tr></thead>
      <tbody>
      <% for (LeaderboardEntry e : entries) { %>
        <tr class="<%= e.getUsername().equals(me) ? "me" : "" %>">
          <td><%= e.getRank() <= 3 ? "<span class=\"medal gold\">&#9733;</span> " : "" %><%= e.getRank() %></td>
          <td><%= Html.esc(e.getUsername()) %><%= e.getUsername().equals(me) ? " (you)" : "" %></td>
          <td><%= e.getLevel() %> <span class="muted">&middot; <%= Html.esc(User.titleFor(e.getLevel())) %></span></td>
          <td class="num"><%= e.getSolved() %></td>
          <td class="num gold"><%= e.getXp() %></td>
        </tr>
      <% } %>
      </tbody>
    </table>
  </div>
<% } %>
<%@ include file="includes/footer.jsp" %>

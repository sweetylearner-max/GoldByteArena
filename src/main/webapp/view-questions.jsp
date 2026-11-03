<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.goldbyte.arena.config.Html,com.goldbyte.arena.models.Mission,java.util.List" %>
<%
    List<Mission> missions = (List<Mission>) request.getAttribute("missions");
    if (missions == null) { response.sendRedirect(request.getContextPath() + "/admin/view-questions"); return; }
%>
<% String pageTitle = "All questions"; %>
<%@ include file="includes/navbar.jsp" %>
<%
    String error = (String) request.getAttribute("error");
%>
<h2>Question catalog</h2>
<p class="muted"><%= missions.size() %> missions are live.</p>
<% if ("1".equals(request.getParameter("added"))) { %><div class="alert ok" style="margin-top:1rem">Mission saved. It is now visible to students.</div><% } %>
<% if (error != null) { %><div class="alert err" style="margin-top:1rem"><%= Html.esc(error) %></div><% } %>
<div class="panel table-wrap" style="margin-top:1rem">
  <table>
    <thead><tr><th>Title</th><th>Category</th><th>Difficulty</th><th class="num">Base XP</th><th class="num">XP earned</th></tr></thead>
    <tbody>
    <% for (Mission m : missions) { %>
      <tr>
        <td><%= Html.esc(m.getTitle()) %></td>
        <td><%= Html.esc(m.getCategory()) %></td>
        <td><span class="tag <%= Html.esc(m.getDifficulty()) %>"><%= Html.esc(m.getDifficulty()) %></span></td>
        <td class="num"><%= m.getXpReward() %></td>
        <td class="num gold"><%= m.getEffectiveXp() %></td>
      </tr>
    <% } %>
    <% if (missions.isEmpty()) { %><tr><td colspan="5" class="muted">No questions yet. Add the first one.</td></tr><% } %>
    </tbody>
  </table>
</div>
<div class="form-actions">
  <a class="btn" href="<%= ctx %>/add-question.jsp">Add question</a>
  <a class="btn ghost" href="<%= ctx %>/admin-dashboard.jsp">Back to console</a>
</div>
<%@ include file="includes/footer.jsp" %>

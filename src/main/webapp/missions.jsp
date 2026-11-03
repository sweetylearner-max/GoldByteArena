<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.goldbyte.arena.config.Html,com.goldbyte.arena.models.Mission,java.util.List" %>
<%
    List<Mission> missions = (List<Mission>) request.getAttribute("missions");
    if (missions == null) { response.sendRedirect(request.getContextPath() + "/missions"); return; }
%>
<% String pageTitle = "Missions"; %>
<%@ include file="includes/navbar.jsp" %>
<%
    String error = (String) request.getAttribute("error");
    String selCat = (String) request.getAttribute("selCategory");
    String selDif = (String) request.getAttribute("selDifficulty");
    String[] cats = {"Java", "SQL", "DSA"};
    String[] difs = {"Easy", "Medium", "Hard"};
    String[] letters = {"A", "B", "C", "D"};
%>
<h2>Missions</h2>
<p class="muted">One attempt per mission. Easy pays base XP, Medium 1.5 times, Hard double.</p>
<% if (error != null) { %><div class="alert err" style="margin-top:1rem"><%= Html.esc(error) %></div><% } %>

<div class="filters">
  <a class="<%= selCat.isEmpty() ? "active" : "" %>" href="<%= ctx %>/missions?difficulty=<%= selDif %>">All categories</a>
  <% for (String c : cats) { %>
    <a class="<%= c.equals(selCat) ? "active" : "" %>" href="<%= ctx %>/missions?category=<%= c %>&difficulty=<%= selDif %>"><%= c %></a>
  <% } %>
</div>
<div class="filters" style="margin-top:-.8rem">
  <a class="<%= selDif.isEmpty() ? "active" : "" %>" href="<%= ctx %>/missions?category=<%= selCat %>">Any difficulty</a>
  <% for (String d : difs) { %>
    <a class="<%= d.equals(selDif) ? "active" : "" %>" href="<%= ctx %>/missions?category=<%= selCat %>&difficulty=<%= d %>"><%= d %></a>
  <% } %>
</div>

<% if (missions.isEmpty() && error == null) { %>
  <div class="panel muted">No missions match these filters. Try another category or difficulty.</div>
<% } %>

<%
    String lastCat = "";
    for (Mission m : missions) {
        if (!m.getCategory().equals(lastCat)) {
            lastCat = m.getCategory();
%>
  <h3 class="cat-head"><%= Html.esc(lastCat) %></h3>
<%      }
        String[] texts = {m.getOptionA(), m.getOptionB(), m.getOptionC(), m.getOptionD()};
%>
  <article class="panel mission <%= m.getStatus() != 0 ? "done" : "" %>">
    <header>
      <h3><%= Html.esc(m.getTitle()) %></h3>
      <div style="display:flex;gap:.5rem;flex-wrap:wrap">
        <span class="tag <%= Html.esc(m.getDifficulty()) %>"><%= Html.esc(m.getDifficulty()) %></span>
        <span class="tag xp">+<%= m.getEffectiveXp() %> XP</span>
      </div>
    </header>
    <p style="margin:.7rem 0"><%= Html.esc(m.getDescription()) %></p>

    <% if (m.getStatus() == 0) { %>
    <form method="post" action="<%= ctx %>/submit-answer" onsubmit="Sfx.click()">
      <input type="hidden" name="missionId" value="<%= m.getId() %>">
      <% for (int i = 0; i < 4; i++) { %>
        <label class="opt"><input type="radio" name="answer" value="<%= letters[i] %>" required><b><%= letters[i] %></b><%= Html.esc(texts[i]) %></label>
      <% } %>
      <div class="form-actions"><button class="btn small" type="submit">Lock in answer</button></div>
    </form>
    <% } else { %>
      <% for (int i = 0; i < 4; i++) { %><div class="opt" style="cursor:default"><b><%= letters[i] %></b><%= Html.esc(texts[i]) %></div><% } %>
      <p style="margin-top:.8rem"><span class="tag <%= m.getStatus() == 1 ? "Easy" : "Hard" %>"><%= m.getStatus() == 1 ? "Solved" : "Attempted, no XP" %></span></p>
    <% } %>
  </article>
<% } %>
<%@ include file="includes/footer.jsp" %>

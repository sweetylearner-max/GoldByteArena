<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.goldbyte.arena.config.Html" %>
<% String pageTitle = "Add question"; %>
<%@ include file="includes/navbar.jsp" %>
<%
    String error = (String) request.getAttribute("error");
    String cat = request.getParameter("category") == null ? "" : request.getParameter("category");
    String dif = request.getParameter("difficulty") == null ? "" : request.getParameter("difficulty");
    String cor = request.getParameter("correct") == null ? "" : request.getParameter("correct");
%>
<div class="panel" style="max-width:760px;margin:0 auto">
  <h2>Add a mission</h2>
  <% if (error != null) { %><div class="alert err"><%= Html.esc(error) %></div><% } %>
  <form method="post" action="<%= ctx %>/admin/add-question">
    <label for="title">Title</label>
    <input type="text" id="title" name="title" maxlength="150" required value="<%= Html.esc(request.getParameter("title")) %>">
    <label for="description">Question</label>
    <textarea id="description" name="description" required><%= Html.esc(request.getParameter("description")) %></textarea>
    <div class="row2">
      <div><label for="category">Category</label>
        <select id="category" name="category" required>
          <option value="">Choose</option>
          <option value="Java" <%= "Java".equals(cat) ? "selected" : "" %>>Java</option>
          <option value="SQL" <%= "SQL".equals(cat) ? "selected" : "" %>>SQL</option>
          <option value="DSA" <%= "DSA".equals(cat) ? "selected" : "" %>>DSA</option>
        </select></div>
      <div><label for="difficulty">Difficulty</label>
        <select id="difficulty" name="difficulty" required>
          <option value="">Choose</option>
          <option value="Easy" <%= "Easy".equals(dif) ? "selected" : "" %>>Easy</option>
          <option value="Medium" <%= "Medium".equals(dif) ? "selected" : "" %>>Medium</option>
          <option value="Hard" <%= "Hard".equals(dif) ? "selected" : "" %>>Hard</option>
        </select></div>
    </div>
    <div class="row2">
      <div><label for="optionA">Choice A</label><input type="text" id="optionA" name="optionA" maxlength="255" required value="<%= Html.esc(request.getParameter("optionA")) %>"></div>
      <div><label for="optionB">Choice B</label><input type="text" id="optionB" name="optionB" maxlength="255" required value="<%= Html.esc(request.getParameter("optionB")) %>"></div>
      <div><label for="optionC">Choice C</label><input type="text" id="optionC" name="optionC" maxlength="255" required value="<%= Html.esc(request.getParameter("optionC")) %>"></div>
      <div><label for="optionD">Choice D</label><input type="text" id="optionD" name="optionD" maxlength="255" required value="<%= Html.esc(request.getParameter("optionD")) %>"></div>
    </div>
    <div class="row2">
      <div><label for="correct">Correct answer</label>
        <select id="correct" name="correct" required>
          <option value="">Choose</option>
          <option value="A" <%= "A".equals(cor) ? "selected" : "" %>>A</option>
          <option value="B" <%= "B".equals(cor) ? "selected" : "" %>>B</option>
          <option value="C" <%= "C".equals(cor) ? "selected" : "" %>>C</option>
          <option value="D" <%= "D".equals(cor) ? "selected" : "" %>>D</option>
        </select></div>
      <div><label for="xp">Base XP reward (5 to 100)</label>
        <input type="number" id="xp" name="xp" min="5" max="100" required value="<%= Html.esc(request.getParameter("xp") == null ? "20" : request.getParameter("xp")) %>"></div>
    </div>
    <p class="muted" style="margin-top:.8rem">Students earn the base XP on Easy, 1.5 times on Medium and double on Hard.</p>
    <div class="form-actions">
      <button class="btn" type="submit">Save mission</button>
      <a class="btn ghost" href="<%= ctx %>/admin-dashboard.jsp">Back to console</a>
    </div>
  </form>
</div>
<%@ include file="includes/footer.jsp" %>

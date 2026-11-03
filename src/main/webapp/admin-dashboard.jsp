<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.goldbyte.arena.config.Html" %>
<% String pageTitle = "Admin console"; %>
<%@ include file="includes/navbar.jsp" %>
<%
    String panelName = (String) session.getAttribute("panelName");            // from ServletConfig init-param
    String arenaName = (String) session.getAttribute("arenaName");            // from ServletContext context-param
    java.util.concurrent.atomic.AtomicInteger logins =
        (java.util.concurrent.atomic.AtomicInteger) application.getAttribute("totalLogins");
%>
<h2><%= Html.esc(panelName == null ? "Admin console" : panelName) %></h2>
<p class="muted">Signed in to <%= Html.esc(arenaName == null ? "GoldByte Arena" : arenaName) %> as <%= Html.esc((String) session.getAttribute("adminName")) %>.
  Student logins since the server started: <strong class="gold"><%= logins == null ? 0 : logins.get() %></strong></p>
<div class="grid cols-2" style="margin-top:1.5rem">
  <div class="panel"><h3>Add a question</h3><p class="muted" style="margin:.5rem 0 1rem">Write a Java, SQL or DSA mission with four choices and an XP reward.</p>
    <a class="btn" href="<%= ctx %>/add-question.jsp">Add question</a></div>
  <div class="panel"><h3>View all questions</h3><p class="muted" style="margin:.5rem 0 1rem">Review every mission currently live for students.</p>
    <a class="btn violet" href="<%= ctx %>/admin/view-questions">View questions</a></div>
</div>
<div class="form-actions"><a class="btn ghost" href="<%= ctx %>/admin-logout">Log out</a></div>
<%@ include file="includes/footer.jsp" %>

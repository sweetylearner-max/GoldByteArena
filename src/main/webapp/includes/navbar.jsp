<%-- Static include: the including page must declare String pageTitle and the page directive (contentType, imports) --%>
<%
    String ctx = request.getContextPath();
    boolean navStudent = session.getAttribute("userId") != null;
    boolean navAdmin = session.getAttribute("adminId") != null;
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title><%= Html.esc(pageTitle) %> | GoldByte Arena</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@600;700;900&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body>
<header class="nav">
  <div class="nav-inner">
    <a class="brand" href="<%= ctx %>/index.html"><span class="brand-mark">G</span>GoldByte Arena</a>
    <a class="link" href="<%= ctx %>/index.html">Home</a>
    <a class="link" href="<%= ctx %>/missions">Missions</a>
    <a class="link" href="<%= ctx %>/leaderboard">Leaderboard</a>
    <% if (navStudent) { %>
      <a class="link" href="<%= ctx %>/dashboard">Dashboard</a>
      <span class="chip"><%= Html.esc((String) session.getAttribute("username")) %></span>
      <a class="link" href="<%= ctx %>/logout">Log out</a>
    <% } else if (navAdmin) { %>
      <a class="link" href="<%= ctx %>/admin-dashboard.jsp">Admin console</a>
      <span class="chip">Admin: <%= Html.esc((String) session.getAttribute("adminName")) %></span>
      <a class="link" href="<%= ctx %>/admin-logout">Log out</a>
    <% } else { %>
      <a class="link" href="<%= ctx %>/register.jsp">Register</a>
      <a class="link" href="<%= ctx %>/login.jsp">Log in</a>
      <a class="link" href="<%= ctx %>/admin-login.jsp">Admin</a>
    <% } %>
    <button class="sound" id="soundBtn" type="button" onclick="Sfx.toggle()">Sound on</button>
  </div>
</header>
<main>

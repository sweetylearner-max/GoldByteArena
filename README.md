# GoldByte Arena

A gamified programming practice platform. Students solve Java, SQL and DSA multiple-choice missions, earn XP, build daily streaks and climb a leaderboard. Built with JSP, Servlets, JDBC, HTML and CSS (no JSTL, no Expression Language) on Tomcat 10.1 and MySQL 8.

Theme: obsidian black, gold and amethyst purple.

## Demo accounts

| Role    | Username    | Password |
|---------|-------------|----------|
| Admin   | admin       | admin123 |
| Student | AuricKnight | pass123  |
| Student | ViolaByte   | pass123  |
| Student | NovaCoder   | pass123  |

## Run with Docker (nothing else to install)

1. Install Docker Desktop.
2. In this folder run: `docker compose up --build`
3. Open http://localhost:8080

To reset the data: `docker compose down -v` and start again.

## Run without Docker

1. Install JDK 17, Maven, Tomcat 10.1 and MySQL 8.
2. Run `database/goldbyte_db.sql` in MySQL.
3. Set environment variables before starting Tomcat: `DB_URL`, `DB_USER`, `DB_PASS`
   (defaults: local MySQL, user `root`, empty password).
4. `mvn package`, then copy `target/ROOT.war` into Tomcat's `webapps` folder and start Tomcat.
   In Eclipse: File > Import > Existing Maven Projects, then Run As > Run on Server (Tomcat 10.1).

## Deploy online (Render + free cloud MySQL)

1. Create a free MySQL database (TiDB Cloud, Aiven or Railway). Run `database/goldbyte_db.sql` on it
   (if the host does not let you create a database, delete the first `CREATE DATABASE` and `USE` lines and select your database first).
2. Push this folder to GitHub.
3. On Render: New > Web Service > connect the repo > Runtime: Docker.
4. Add environment variables: `PORT=8080`, `DB_URL` (for example `jdbc:mysql://HOST:PORT/DBNAME?sslMode=REQUIRED&serverTimezone=UTC`), `DB_USER`, `DB_PASS`.
5. Deploy. Never commit real passwords; they only live in Render's environment settings.

## Game rules

- Level = 1 + floor(XP / 100)
- XP earned = base XP x difficulty multiplier (Easy 1, Medium 1.5, Hard 2)
- One attempt per mission, enforced in the servlet and by a UNIQUE key in the database
- Answer saving and XP, level and streak updates run in one JDBC transaction
- Titles: Bronze Initiate, Silver Scripter, Gold Operator, Amethyst Architect, Golden Overlord

## Module map

| Module | Files |
|---|---|
| 1 Home and UI | index.html, css/style.css |
| 2 Registration | register.jsp, RegisterServlet |
| 3 Login and logout | login.jsp, LoginServlet, LogoutServlet, AuthFilter, cookie `gba_user` |
| 4 Admin login | admin-login.jsp, AdminLoginServlet (ServletConfig init-param), admin-dashboard.jsp (ServletContext), AdminLogoutServlet |
| 5 Database and JDBC | database/goldbyte_db.sql, DBConnection, DAO classes (PreparedStatement) |
| 6 Create questions | add-question.jsp, AddQuestionServlet |
| 7 Manage questions | ViewQuestionsServlet, view-questions.jsp |
| 8 Dashboard | DashboardServlet, dashboard.jsp |
| 9 Missions | MissionServlet, missions.jsp (correct answer never sent to students) |
| 10 Evaluation and XP | SubmitAnswerServlet, SubmissionDAO.submit, mission-result.jsp |
| 11 Leaderboard | LeaderboardServlet, leaderboard.jsp |
| 12 Integration | web.xml, sessions, RequestDispatcher forwards, redirects, on-page error messages |

## Test checklist

Register, log in, open Missions, answer one correctly (XP and level update, confetti), answer one wrongly (no XP),
try to answer the same mission again (blocked), check Dashboard and Leaderboard, log out, log in as admin,
add a question, confirm it appears in View questions and in Missions, log out.

<div align="center">

# GoldByte Arena

A gamified programming practice platform built with JSP, Servlets and JDBC.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk&logoColor=white)
![Tomcat](https://img.shields.io/badge/Tomcat-10.1-yellow?style=flat-square&logo=apachetomcat&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8-blue?style=flat-square&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?style=flat-square&logo=docker&logoColor=white)

**Live demo:** https://goldbytearena.onrender.com/

</div>

> The demo runs on a free hosting plan. If it has been idle, the first load can take 30 to 60 seconds while the server wakes up.

---

## Academic Disclaimer

This project was developed for academic purposes only, as a student assignment (*CodeQuest: Programming Learning Game*) given by our faculty. It demonstrates Java web development concepts and is not a commercial product. The demo accounts contain sample data only.

---

## Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Demo Accounts](#-demo-accounts)
- [Game Rules](#-game-rules)
- [Project Structure](#-project-structure)
- [Database Design](#-database-design)
- [Getting Started](#-getting-started)
- [Deployment](#-deployment)
- [Module Map](#-module-map)
- [Test Checklist](#-test-checklist)
- [Author](#-author)

---

## ✨ Features

**Students**
- Register and log in with a username or email address
- Solve Java, SQL and DSA missions at Easy, Medium and Hard difficulty
- Earn XP, level up and keep a daily streak
- Track progress on a personal dashboard with a live XP bar
- Compete on a global leaderboard with podium highlights for the top three
- Confetti celebrations and sound effects, with an on/off toggle

**Administrators**
- Separate admin login and session
- Create questions with category, difficulty, four options, correct answer and XP reward
- View and manage every question stored in the database

**Technical highlights**
- One attempt per mission, enforced in the servlet and by a `UNIQUE` key in the database
- Answer saving and XP, level and streak updates run in a single JDBC transaction
- The correct answer is never sent to the student's browser
- Passwords stored as SHA-256 hashes
- Role-based access control through a servlet filter
- Validation and database errors shown on the existing pages, with no separate error pages
- Pure scriptlets: no JSTL and no Expression Language

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Frontend | HTML, CSS, JSP scriptlets |
| Backend | Java 17, Servlets (Jakarta EE), JDBC with `PreparedStatement` |
| Database | MySQL 8 (TiDB Cloud in the live demo) |
| Server | Apache Tomcat 10.1 |
| Build | Maven (WAR packaging) |
| Containers | Docker and Docker Compose |
| Hosting | Render (application) and TiDB Cloud (database) |

Theme: obsidian black, gold and amethyst purple.

---

## 🔑 Demo Accounts

| Role | Username | Password |
|---|---|---|
| Admin | `admin` | `admin123` |
| Student | `AuricKnight` | `pass123` |
| Student | `ViolaByte` | `pass123` |
| Student | `NovaCoder` | `pass123` |

The admin login page is available at `/admin-login.jsp`.

---

## 🎮 Game Rules

- **Level** = 1 + floor(XP / 100)
- **XP earned** = base XP x difficulty multiplier (Easy 1, Medium 1.5, Hard 2)
- **One attempt** per mission
- **Streak:** solve at least one mission on consecutive days

| Title | Levels |
|---|---|
| Bronze Initiate | 1 |
| Silver Scripter | 2 to 3 |
| Gold Operator | 4 to 5 |
| Amethyst Architect | 6 to 7 |
| Golden Overlord | 8 and above |

---

## 📂 Project Structure

```
GoldByteArena/
├── database/
│   └── goldbyte_db.sql          # Schema and sample data
├── src/main/
│   ├── java/com/goldbyte/arena/
│   │   ├── config/              # DBConnection, Html helper
│   │   ├── dao/                 # UserDAO, AdminDAO, MissionDAO, SubmissionDAO
│   │   ├── filters/             # AuthFilter
│   │   ├── models/              # User, Mission, LeaderboardEntry
│   │   └── servlets/            # Register, Login, Logout, Admin*, Mission, Submit, ...
│   └── webapp/
│       ├── css/  js/  includes/
│       ├── WEB-INF/web.xml
│       └── *.jsp, index.html
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

---

## 🗄️ Database Design

| Table | Purpose |
|---|---|
| `users` | Student accounts, XP, level, streak |
| `admins` | Administrator credentials |
| `missions` | Questions, options, difficulty, category, XP reward |
| `mission_submissions` | Student answers and results, with `UNIQUE (user_id, mission_id)` and foreign keys to `users` and `missions` |

---

## 🚀 Getting Started

### Option 1: Docker (recommended)

Nothing else needs to be installed except Docker Desktop.

```bash
docker compose up --build
```

Then open http://localhost:8080

To stop the application:

```bash
docker compose down
```

To reset all data, run `docker compose down -v` and start again.

### Option 2: Without Docker

**Requirements:** JDK 17, Maven, Tomcat 10.1, MySQL 8

1. Run `database/goldbyte_db.sql` in MySQL.
2. Set these environment variables before starting Tomcat (defaults: local MySQL, user `root`, empty password):
   - `DB_URL`
   - `DB_USER`
   - `DB_PASS`
3. Build the WAR file:
   ```bash
   mvn package
   ```
4. Copy `target/ROOT.war` into Tomcat's `webapps` folder and start Tomcat.

**Eclipse:** File > Import > Existing Maven Projects, then Run As > Run on Server (Tomcat 10.1).

---

## ☁️ Deployment

The live demo uses Render for the application (Docker runtime) and TiDB Cloud for a free MySQL-compatible database.

1. Create a free MySQL-compatible database (TiDB Cloud, Aiven or Railway) and run `database/goldbyte_db.sql` on it.
   If the host does not allow creating a database, remove the first `CREATE DATABASE` and `USE` lines and select your database first.
2. Push the project to GitHub.
3. On Render, choose **New > Web Service**, connect the repository, and select **Docker** as the runtime.
4. Add these environment variables:

   | Variable | Example |
   |---|---|
   | `PORT` | `8080` |
   | `DB_URL` | `jdbc:mysql://HOST:PORT/DBNAME?sslMode=REQUIRED&serverTimezone=UTC` |
   | `DB_USER` | your database user |
   | `DB_PASS` | your database password |

5. Deploy.

> Never commit real credentials to the repository. Keep them in the hosting provider's environment settings.

---

## 🧩 Module Map

| # | Module | Files |
|---|---|---|
| 1 | Home and UI | `index.html`, `css/style.css` |
| 2 | Registration | `register.jsp`, `RegisterServlet` |
| 3 | Login and logout | `login.jsp`, `LoginServlet`, `LogoutServlet`, `AuthFilter`, cookie `gba_user` |
| 4 | Admin login and dashboard | `admin-login.jsp`, `AdminLoginServlet` (ServletConfig init-param), `admin-dashboard.jsp` (ServletContext), `AdminLogoutServlet` |
| 5 | Database and JDBC | `goldbyte_db.sql`, `DBConnection`, DAO classes (`PreparedStatement`) |
| 6 | Create questions | `add-question.jsp`, `AddQuestionServlet` |
| 7 | Manage questions | `ViewQuestionsServlet`, `view-questions.jsp` |
| 8 | Student dashboard | `DashboardServlet`, `dashboard.jsp` |
| 9 | Missions | `MissionServlet`, `missions.jsp` |
| 10 | Evaluation and XP | `SubmitAnswerServlet`, `SubmissionDAO.submit`, `mission-result.jsp` |
| 11 | Leaderboard | `LeaderboardServlet`, `leaderboard.jsp` |
| 12 | Integration | `web.xml`, sessions, `RequestDispatcher` forwards, redirects, on-page error messages |

---

## ✅ Test Checklist

1. Register a new student and log in.
2. Open **Missions** and answer one correctly (XP and level update, confetti shown).
3. Answer another one wrongly (no XP awarded).
4. Try to answer the same mission again (blocked).
5. Check the **Dashboard** and **Leaderboard**.
6. Log out, then log in as admin.
7. Add a question and confirm it appears in **View questions** and in **Missions**.
8. Log out.

---

## 👤 Author

<div align="center">

**Akanksha Bursu**

Developed as an academic project assignment.

</div>

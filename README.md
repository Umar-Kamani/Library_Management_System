# Library Management System

A desktop library management application for ALCHE, built in Java with a
Swing interface and a MySQL-backed data layer, developed as the Programming
II final project.

## Prerequisites

- **Java Development Kit (JDK)** matching the version in `pom.xml`
  (`maven.compiler.source`/`target`). Check this before building — if your
  installed JDK is older, either install a matching JDK or lower this value
  and rebuild.
- **Apache Maven** (3.8 or later)
- **MySQL Server** (8.x), installed and running locally on the default port
  `3306`

## One-time database setup

The application creates its own database, tables, and schema automatically
on first launch — but it needs one manual step first, because it can't
create its own login account before it has an account to log in with.

Open a MySQL client (MySQL Workbench, or `mysql -u root -p` from a
terminal) using your MySQL **root** account, and run:

```sql
CREATE USER IF NOT EXISTS 'library_manager'@'localhost' IDENTIFIED BY 'password1234';
GRANT ALL PRIVILEGES ON *.* TO 'library_manager'@'localhost';
FLUSH PRIVILEGES;
```

This only needs to be done once per machine. After this, every time the
application starts, it automatically creates the `alche_library` database
and all required tables if they don't already exist — you never need to
create tables by hand.

## Building the project

From the project root:

```bash
mvn clean package
```

This compiles the source, runs the test suite, and produces a runnable JAR
under `target/`.

## Running the application

After packaging (see below), run the produced JAR directly:

```bash
java -jar target/Library_Management_System-1.0-SNAPSHOT.jar
```

## Running the tests

```bash
mvn test
```

Test results are written to `target/surefire-reports/`.

## Packaging for a machine with no Java installed

This produces a self-contained application that bundles its own Java
runtime, so it runs on a machine without Java installed.

1. Build the shaded JAR:
```bash
   mvn clean package
```
2. Run `jpackage` (included with the JDK, no separate install needed):
```bash
   jpackage --type app-image ^
     --input target ^
     --dest dist ^
     --name LibraryManagementSystem ^
     --main-jar Library_Management_System-1.0-SNAPSHOT.jar ^
     --main-class mu.alche.library.App.Launcher
```
   (On macOS/Linux, replace the `^` line continuations with `\`.)
3. The result is a folder under `dist/LibraryManagementSystem/` containing
   a native launcher — this folder can be copied to and run on any machine
   of the same OS, with no Java installation required.

**Verified on:** Windows 11 Pro, version 25H2 (OS Build 26200.8875). Packaged app-image launches correctly; not yet tested on a machine without a separate Java installation.

## Project structure
```
src/main/java/mu/alche/library/
├── App/ # Application entry point and main window
├── Database/ # DBUtils (connection + schema) and the DAO layer
│ └── DAO/
│ └── Impl/ # Concrete DAO implementations
├── Models/ # Entity classes (Book, Genre, Location, Borrowing, User, ...)
├── Service/ # Business logic layer, sitting between the UI and the DAOs
└── UI/ # Swing panels for each entity

src/test/java/ # Mirrors the structure above; JUnit 5 test suite
```
## Team

Umar
My main contribution to the project was the implementation of the Database layer which includes the connection, schema creation, DAO and DAO Implementation where I developed the Book, Location and User DAO’s and their corresponding implementations. I also implemented the Book, Faculty, Location, Student and User models along with bookService and LocationService. Moreover, I also worked with my teammates to develop the Swing UI interface.

Sherif
I helped write the initial project proposal before we started coding, and in the early stages of the project, I built the first version of the book and location logic as BookManager and LocationManager, along with the Location model itself, before the team moved the codebase over to the DAO and Service pattern we use now. Once we made that move, my main contribution became GenreService and UserService, which sit between the Swing panels and the DAO layer.I also fixed some errors in the LocationService and mainWindow JFrame file. I did a few unit tests on my laptop with fake inputs to ensure the program was running smoothly. Lastly, I worked with my team members on working with this final project brief.

Nelly
I implemented the Borrowing and Genre entities end-to-end: the Genre/Borrowing models, their DAO interfaces and implementations, and BorrowingService, which enforces the borrowing-limit and book-availability rules and calculates due dates based on user role. I also wrote the JUnit test suite for the service layer using hand-written fake DAOs rather than a live database connection, wired the Genres and Borrowings screens to real data, and wrote the project README.
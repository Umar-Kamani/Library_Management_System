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

After building:

```bash
mvn exec:java -Dexec.mainClass="mu.alche.library.App.Launcher"
```

Or, after packaging (see below), run the produced JAR directly:

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

**Verified on:** _[fill in: e.g. "Windows 11, built and tested on a machine
with no separate Java install"]_

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

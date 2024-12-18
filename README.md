# Poimen - Simplified Church Management

Poimen is an application built with **JHipster** designed to meet the administrative needs of churches, providing a centralized platform to manage members, services, ministries, financial transactions, and other essential areas for church organization and efficiency.

## **Key Features**

- **Member Management**: Handle detailed information about church members.
- **Group Organization**: Manage departments, internal societies, deacon boards, and councils.
- **Financial Management**: Record transactions and link invoices to ensure transparency.
- **Service Planning**: Organize worship services, including hymns, liturgists, and preachers.
- **Task Tracking**: Oversee pastoral duties and general church tasks.
- **Counseling Sessions**: Log and manage counseling sessions with notes and member information.

---

## Project Structure

This application was generated using **JHipster 8.7.3**, you can find documentation and help at [JHipster Documentation](https://www.jhipster.tech/documentation-archive/v8.7.3/).

Node is required for generation and recommended for development. `package.json` is always generated for a better development experience with Prettier, commit hooks, scripts, and so on.

In the project root, JHipster generates configuration files for tools like Git, Prettier, ESLint, Husky, and others that are well-known and documented online.

`/src/*` structure follows the default Java structure.

- `.yo-rc.json` - Yeoman configuration file
  JHipster configuration is stored in this file at `generator-jhipster` key. You may find `generator-jhipster-*` for specific blueprints configuration.
- `.yo-resolve` (optional) - Yeoman conflict resolver
  Allows using a specific action when conflicts are found, skipping prompts for files that match a pattern. Each line should match `[pattern] [action]` with pattern being a [Minimatch](https://github.com/isaacs/minimatch#minimatch) pattern and action being one of skip (default if omitted) or force. Lines starting with `#` are considered comments and are ignored.
- `.jhipster/*.json` - JHipster entity configuration files
- `npmw` - Wrapper to use locally installed npm.
  JHipster installs Node and npm locally using the build tool by default. This wrapper ensures npm is installed locally and uses it, avoiding version differences.
- `/src/main/docker` - Docker configurations for the application and services it depends on.

---

## Development

The build system will automatically install the recommended version of Node and npm.

We provide a wrapper to launch npm. You will only need to run this command when dependencies change in [package.json](package.json).

```bash
./npmw install
```

We use npm scripts and [Angular CLI](https://cli.angular.io/) with [Webpack](https://webpack.github.io/) as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser auto-refreshes when files change on your hard drive.

```bash
./mvnw
./npmw start
```

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by specifying a newer version in [package.json](package.json). You can also run `./npmw update` and `./npmw install` to manage dependencies.

Add the `help` flag on any command to see how you can use it. For example:

```bash
./npmw help update
```

The `./npmw run` command will list all the scripts available to run for this project.

---

## **Technologies Used**

- **Backend**: Spring Boot (Java) with OAuth2 or JWT authentication.
- **Frontend**: Angular with built-in internationalization (i18n) support.
- **Database**: PostgreSQL for both production and development environments.
- **Docker**: Containers for runtime and testing environments.
- **Testing**: Gatling and Cucumber integrated into the project.

---

## Building for Production

### Packaging as jar

To build the final jar and optimize the Poimen application for production, run:

```bash
./mvnw -Pprod clean verify
```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` to reference these new files. To ensure everything worked, run:

```bash
java -jar target/*.jar
```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production](https://www.jhipster.tech/documentation-archive/v8.7.3/production/) for more details.

### Packaging as war

To package your application as a war to deploy it to an application server, run:

```bash
./mvnw -Pprod,war clean verify
```

---

## **Purpose**

Poimen aims to be an efficient, practical, and customizable tool for churches of various sizes, helping pastors and leaders manage their responsibilities with greater ease and focus on ministry.

---

## How to Contribute

Contributions are welcome! Check the `CONTRIBUTING.md` file for details on how to submit bug reports, feature requests, or pull requests.

---

> **“Poimen” comes from the Greek ποιμήν, meaning shepherd. Just as the shepherd cares for their flock, this system was created to help churches manage their responsibilities with excellence.”**

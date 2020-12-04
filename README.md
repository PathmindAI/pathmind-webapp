# Pathmind Web Application

This repository contains the Pathmind Web application.

The other parts of Pathmind (NativeRL, Pathmind Helper) can be currently still be found here:

- [NativeRL](https://github.com/SkymindIO/nativerl/tree/prod/nativerl)
- [Pathmind Helper](https://github.com/SkymindIO/nativerl/tree/prod/PathmindPolicyHelper)

## Tech stack

- **UI Framework:** Vaadin 14
- **Dependency Injection / Application Container:** Spring Boot 2
- **Database:** PostgreSQL 10
- **Build:** Maven
- **Deployment:** AWS
- **Database Migrations:** Liquibase

[Detailed documentation can be found on the wiki](https://github.com/SkymindIO/pathmind-webapp/wiki)

## Setup

The quickest way to get up and running is with the Vagrant file. You'll need to set up your env variables first.

```
vagrant up
vagrant ssh
./start-pathmind.sh
```

Your local env will be running at 127.0.0.1:8080

[Details for setting up your env variables](https://github.com/SkymindIO/pathmind-webapp/wiki/Setting-up-your-dev-environment). 

Here are instructions for how you can try out the app and the files you can upload to train:
https://help.pathmind.com/en/articles/4004788-getting-started

### Running Automated Tests
[Check out the readme here](pathmind-bdd-tests/README.md)

#### Details

When working on this project, you will have to set up a PostgreSQL database server locally. And set the credentials for
it as an Environment Variable in IntelliJ (or your IDE of choice).

The required environment variable looks something like this:

```
DB_URL=jdbc:postgresql://localhost/pathmind?user=skynet&password=skynetskynet123
```

For running the application you set it up in your `Run/Debug Configuration` when starting the application from inside
Intellij. For maven commands (also to be run from the IntelliJ) you have to set it also by going to
`File -> Settings -> Build, Execution, Deployment -> Build Tools -> Maven -> Runner` and set it there in the
`Environment variables` field.

## JOOQ

We are using [Jooq](https://www.jooq.org/doc/3.11/manual/) for type-safe database access. So when you change anything
about the structure of the database, you will have to also regenerate those classes. You can do this by running
`jooq-codegen:generate` from the `pathmind-database` submodule in the maven tab in IntelliJ.

[More details are here.](https://github.com/SkymindIO/pathmind-webapp/wiki/Liquibase-and-JOOQ#jooq)

## Migrations

We are using [Liquibase](https://www.liquibase.org/documentation/xml_format.html) for migrations. Database migrations
can be run in one of two ways.

1. When starting the application
2. By running `liquibase:update` from the maven tab in IntelliJ

When adding a migration, remember to add a `logicalFilePath="RELATIVE_PATH_TO_FILE"` as a parameter to each changelog
files root element. This will ensure that the changelog file has a unique path to it. **We require a unique changelog
path**, because otherwise migrations ran by the maven liquibase plugin and migrations ran by application start might
get slightly different paths.

The different paths issue shows itself by migrations being run again, even though you have executed them already. This
is because a migration is identified by the triple `(id, author, filepath)`.

When adding migrations, use the pattern `db.YYYYMMDD.xml`, so a migration added on the September 13th 2019 would have
the name `db.20190913.xml`. As the author use your work email address and the id should be the github issue id and if you need more than one migration on that day, add a `-N` suffix. The id for the second
migration on for github issue #2501 would therefore be `2501-2`. (**Updated Dec 2020**)

This pattern makes it easy for multiple people to work together.

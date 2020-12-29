# Development Guide

## Table of Contents

- [Development Guide](#development-guide)
  - [Prerequisites](#prerequisites)
  - [Code Organization](#code-organization)
  - [Starting a Dev Environment](#starting-a-dev-environment)
  - [Running Migrations](#running-migrations)
  - [Updating JOOQ Entities](#updating-jooq-entities)
  - [Formatting source](#formatting-source)

## Prerequisites

1. Java 11 or higher ([AdoptOpenJDK](https://adoptopenjdk.net/), [Zulu Community](https://www.azul.com/products/zulu-community/) or [OpenJDK](https://openjdk.java.net/))
2. [Docker](https://docs.docker.com/install/)
3. [Docker Compose](https://docs.docker.com/compose/install/)
4. [direnv](https://github.com/direnv/direnv/blob/master/docs/installation.md)
5. Authenticated against the `docker.pkg.github.com/freight-trust/as2ng` [container registry](https://docs.github.com/en/free-pro-team@latest/packages/using-github-packages-with-your-projects-ecosystem/configuring-docker-for-use-with-github-packages)

## Code Organization

The codebase is organized as follows:

```text
├── bin                 # various utility scripts
│   ├── cli             # calls the cli module for running admin tasks on a local environment
│   ├── flyway          # run database migrations
│   ├── initialize      # initialize a local development environment
│   ├── mc              # utility script for local minio
│   └── vault           # utility script for local vault
├── docker
│   ├── as2-lib         # containerized version of as2-lib server for use in integration testing
│   ├── cli             # containerized version of the as2ng cli
│   ├── server          # containerized version of the main as2 exchange server
│   └── vault           # customised local vault build for development
├── docker-compose.yml  # docker compose based services for local development
├── docker-utils        # git submodule with utility scripts for working with docker
├── modules
│   ├── as2-lib         # simple project for pulling together and building the as2-lib dependencies
│   ├── cli             # standalone cli for performing config/admin tasks relating to the server
│   ├── common          # shared common code and configuration
│   ├── crypto          # cryptography related code
│   ├── persistence     # persistence related code such as postgres and s3
│   └── server          # main as2 exchange server code based on vert.x
```

## Starting a Dev Environment

Run the following commands from the root directory of the repository:

```sh
$ git submodule update --init --recursive       # only required on initial checkout
$ direnv allow                                  # as and when prompted to do so

$ docker-compose up -d                          # starts the various services such as vault, minio, postgres etc
$ docker-compose tail -f                        # watch the logs for successful services startup
$ bin/initialize                                # this will generate some initial state
```

The `initialize` script will do the following:

- Enable the [Public Key Infrastructure (PKI)](https://www.vaultproject.io/docs/secrets/pki) secrets engine within Vault and generate an initial certificate authority
- Apply the database migrations located in `modules/persistence/src/main/resources/db/migration` to the Postgres database
- Create a S3 bucket within the Minio service as configured by the `AS2NG_S3_BUCKET` environment variable
- Generate some encryption key pairs
- Generate some example trading partners
- Generate some example trading channels
- Export some keystores based on the configured trading channels and partners for use within local testing as2-lib based servers

After the `initialize` script has successfully completed run the following:

```sh
$ ./gradlew :modules:as2-lib:runOpenAS2A        # as2-lib based test server configured with one of the exported keystores and trading channels
$ ./gradlew :modules:as2-lib:runOpenAS2B        # as2-lib based test server configured with one of the exported keystores and trading channels
$ ./gradlew :modules:server:runAS2Server        # starts the vert.x based as2 exchange server
```

You can now send a test message from `OpenAS2A` to `OpenAS2B` by running the following:

```sh
$ echo "Hello world" > modules/as2-lib/data/text/OpenAS2A/toOpenAS2B/test.txt
```

## Running Migrations

After making changes to the database structure you can run the migrations against the local Postgres database as follows:

```sh
$ ./gradlew :persistence:flywayMigrate
```

To drop all data and structure in the DB you can use:

```sh
$ ./gradlew :persistence:flywayClean
```

Or you can use the utility script:

```sh
$ bin/flyway migrate
$ bin/flyway clean
```

For more information about the migration framework please consult the [official docs](https://flywaydb.org/).

## Updating JOOQ Entities

If you have added db migrations and applied them, you will need to regenerate the `jooq` entities for interacting with the db. First ensure that:

1. Postgres is running.
2. Migrations have been applied correctly.
3. Manually delete the `modules/persistence/src/main/java/com/freighttrust/jooq` folder.

Then in the terminal run:

```sh
$ ./gradlew :persistence:jooq-codegen-primary
```

For more information about JOOQ please consult the [official docs](https://www.jooq.org/).

## Formatting source

To format the code you can run:

```sh
$ ./gradlew ktlintFormat
```

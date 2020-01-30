![alt text](https://i.imgur.com/22D6PAc.png "Shinsoo")

## About

Shinsoo is a backend implementation for [Aria](https://github.com/AlanMorel/aria)
utilizing modern Java tooling.

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/

All issues and features that require work will be placed in the issues section of this
repository.  Please submit pull requests with feature changes if desired.

## Configuration
Note that after configuration, the application may need to be built following instructions
below.

## General Configuration
All general configuration can be found in **src/main/resources/application.properties**
and should be changed prior to deployment.  For information on settings to deploy with
HTTPS, please consult the official Quarkus documentation.

### Database Configuration

The database must be configured in three places:
- src/main/resources/application.properties
- src/main/resources/[xxx].dsl.json
- build.gradle.kts

#### application.properties

All sections in the Database Config section must be filled in.  This includes
- database.driver
- database.url
- database.user
- database.pass

If using a custom DSL JSON dictionary, please also configure:
- dsl.json.source

#### [xxx].dsl.json

The DSL JSON dictionary represents mappings of entities used in the application
to the names utilized in the database.  This includes table names as well as
column names.  Please refer to example.dsl.json for the complete list of these
mappings.

#### build.gradle.kts

If a different driver it utilized besides the default (MySQL), then it must be
supplied as a runtime dependency in the build script.

### Cache Configuration

It is not recommended to touch the cache configuration; however, if it is required,
the cache settings may be modified in the Infinispan settings located at:

src/main/resources/infinispan-embedded.xml

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./gradlew quarkusDev
```

## Packaging and running the application

The application is packageable using `./gradlew quarkusBuild`.
It produces the executable `shinsoo-1.0.0.1-runner.jar` file in `build` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/lib` directory.

The application is now runnable using `java -jar build/shinsoo-1.0.0.1-runner.jar`.

If you want to build an _über-jar_, just add the `--uber-jar` option to the command line:
```
./gradlew quarkusBuild --uber-jar
```

## Creating a native executable

You can create a native executable using: `./gradlew buildNative`.

Or you can use Docker to build the native executable using: `./gradlew buildNative --docker-build=true`.

You can then execute your binary: `./build/shinsoo-1.0.0.1-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling#building-a-native-executable .
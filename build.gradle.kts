group = "dev.zygon"
version = "1.0.0.1"

plugins {
    java
    id("io.freefair.lombok") version "4.1.6"
    id("io.quarkus") version ("1.1.0.Final")
}

repositories {
     mavenLocal()
     mavenCentral()
}

dependencies {
    implementation(enforcedPlatform("io.quarkus:quarkus-bom:1.1.0.Final"))
    implementation("io.quarkus:quarkus-resteasy")
    implementation("io.quarkus:quarkus-resteasy-jackson")
    implementation("org.jboss.resteasy:resteasy-multipart-provider")

    implementation("org.mindrot:jbcrypt:0.4")
    implementation("com.zaxxer:HikariCP:3.4.1")
    implementation("org.jooq:jooq:3.12.3")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

tasks {
    compileJava {
        options.compilerArgs.add("-parameters")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}


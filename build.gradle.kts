/*
    Shinsoo: Java-Quarkus Back End for Aria
    Copyright (C) 2020  Brenterino <brent@zygon.dev>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

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
    implementation("io.quarkus:quarkus-undertow")
    implementation("io.quarkus:quarkus-narayana-jta")
    implementation("io.quarkus:quarkus-rest-client")
    implementation("io.quarkus:quarkus-infinispan-embedded")
    implementation("io.quarkus:quarkus-resteasy")
    implementation("io.quarkus:quarkus-resteasy-jackson")
    implementation("org.jboss.resteasy:resteasy-multipart-provider")
    implementation("io.quarkus:quarkus-jdbc-mysql")

    implementation("com.zaxxer:HikariCP:3.4.1")
    implementation("org.jooq:jooq:3.12.3")
    implementation("org.mindrot:jbcrypt:0.4")

    runtimeOnly("mysql:mysql-connector-java:8.0.18")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("org.mockito:mockito-junit-jupiter:3.1.0")
}

configurations.all {
    exclude("jakarta.xml.bind", "jakarta.xml.bind-api")
    exclude("javax.activation", "javax.activation-api")
    exclude("javax.xml.bind", "jaxb-api")
    exclude("org.jboss.spec.javax.interceptor", "jboss-interceptors-api_1.2_spec")
}

tasks {
    generateLombokConfig {
        enabled = false
    }

    compileJava {
        options.compilerArgs.add("-parameters")
    }

    test {
        useJUnitPlatform()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

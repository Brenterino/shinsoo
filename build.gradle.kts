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
    compileJava {
        options.compilerArgs.add("-parameters")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

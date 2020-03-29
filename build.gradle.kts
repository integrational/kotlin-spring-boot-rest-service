import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.3.71"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion // makes classes with Spring annotations open
    kotlin("plugin.jpa") version kotlinVersion // adds no-arg constructor to classes with JPA annotations

    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
}

val javaVersion = JavaVersion.VERSION_11

val bootGrp = "org.springframework.boot"
val kotlinGrp = "org.jetbrains.kotlin"

group = "org.integrational"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = javaVersion
java.targetCompatibility = javaVersion

repositories {
    mavenCentral() // for Spring Boot BOM
}

dependencies {
    implementation(kotlinGrp, "kotlin-reflect")
    implementation(kotlinGrp, "kotlin-stdlib-jdk8")

    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin")
    implementation("javax.inject", "javax.inject", "1") // JSR-330 @Inject

    implementation(bootGrp, "spring-boot-starter-web") {
        exclude(bootGrp, "spring-boot-starter-tomcat")
    }
    implementation(bootGrp, "spring-boot-starter-jetty") // Jetty instead of Tomcat
    implementation(bootGrp, "spring-boot-starter-actuator")

    testImplementation(bootGrp, "spring-boot-starter-test") {
        exclude("org.junit.vintage", "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = javaVersion.majorVersion
    }
}

springBoot {
    buildInfo() // generate META-INF/build-info.properties for Spring Boot Actuator
}
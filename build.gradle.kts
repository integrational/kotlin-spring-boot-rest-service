import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.3.72"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion // makes classes with Spring annotations open
    kotlin("plugin.jpa") version kotlinVersion // adds no-arg constructor to classes with JPA annotations

    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
}

val cloudVersion = "Hoxton.SR3"
val javaVersion = JavaVersion.VERSION_11

// dependency groups
val kotlinGrp = "org.jetbrains.kotlin"
val bootGrp = "org.springframework.boot"
val cloudGrp = "org.springframework.cloud"
val secGrp = "org.springframework.security"

// dependency names
val bootStarter = "spring-boot-starter"
val cloudStarter = "spring-cloud-starter"

group = "org.integrational"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = javaVersion
java.targetCompatibility = javaVersion

repositories {
    mavenCentral() // for Spring Boot BOM
}

dependencyManagement {
    imports {
        mavenBom("$cloudGrp:spring-cloud-dependencies:$cloudVersion")
    }
}

dependencies {
    implementation(kotlinGrp, "kotlin-reflect")
    implementation(kotlinGrp, "kotlin-stdlib-jdk8")

    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin")
    implementation("javax.inject", "javax.inject", "1") // JSR-330: @Inject, @Singleton, @Named, ...

    implementation(bootGrp, "$bootStarter-web") {
        exclude(bootGrp, "$bootStarter-tomcat")
    }
    runtimeOnly(bootGrp, "$bootStarter-jetty") // Jetty instead of Tomcat
    implementation(bootGrp, "$bootStarter-actuator")

    // Spring Cloud and related
    implementation(cloudGrp, "$cloudStarter-config")
    runtimeOnly(secGrp, "spring-security-rsa") // encrypted props

    testImplementation(bootGrp, "$bootStarter-test") {
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
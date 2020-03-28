import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.3.71"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion

    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
}

val javaVersion = JavaVersion.VERSION_11
val bootGrp = "org.springframework.boot"

group = "org.integrational"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = javaVersion
java.targetCompatibility = javaVersion

repositories {
    mavenCentral() // for Spring Boot BOM
}

dependencies {
    implementation(group = bootGrp, name = "spring-boot-starter-web") {
        exclude(group = bootGrp, module = "spring-boot-starter-tomcat")
    }
    implementation(group = bootGrp, name = "spring-boot-starter-jetty")
    implementation(group = bootGrp, name = "spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation(group = bootGrp, name = "spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
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
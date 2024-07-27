import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.graalvm.buildtools.native") version "0.10.2"
    //id("com.google.cloud.tools.jib") version "3.4.1"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"

}

group = "com.web.resume.ai"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
tasks.named<BootBuildImage>("bootBuildImage") {
    builder.set("paketobuildpacks/builder-jammy-base:latest")
    //runImage.set("run-service-img/${project.name}")
    imageName.set("us-west1-docker.pkg.dev/${project.properties["gcpProjectName"]}/run-service-img/${project.name}:${project.version}")
    buildWorkspace {
        bind {
            source.set("/tmp/cache-${rootProject.name}.work")
        }
    }
    buildCache {
        bind {
            source.set("/tmp/cache-${rootProject.name}.build")
        }
    }
    launchCache {
        bind {
            source.set("/tmp/cache-${rootProject.name}.launch")
        }
    }
}

/*jib {
    to {
        image = "us-west1-docker.pkg.dev/${project.properties["gcpProjectName"]}/run-service-img/${name}:${version}"
        credHelper {
            helper = "gcr"
        }
    }

    container {
        jvmFlags = listOf("-Xmx256m")
        workingDirectory = "/app"
        environment = mapOf(
            "SPRING_APPLICATION_JSON" to "{ \"server\": { \"port\": 8080 } }"
        )
        ports = listOf("8080")
        mainClass = "com.demo.resume.ai.ssrservice.SsrServiceApplication"
    }
}*/

/*springBoot{
    mainClass.set("com.demo.resume.ai.ssrservice.SsrServiceApplicationKt")
}*/

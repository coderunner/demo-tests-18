plugins {
	java
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
    id("com.google.cloud.tools.jib") version "3.4.4"
    jacoco
}

group = "org.inf5190"
version = "0.0.1-SNAPSHOT"

jib {
    to {
        image = "northamerica-northeast1-docker.pkg.dev/fir-deploiement/inf5190/book-app"
        auth {
            username = "_json_key"
            password = file("firebase-production-key.json").readText(Charsets.UTF_8)
        }
    }
} 

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.google.firebase:firebase-admin:9.7.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("PASSED", "FAILED", "SKIPPED")
    }
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

springBoot {
    mainClass.set("org.inf5190.library.LibraryApplication")
}


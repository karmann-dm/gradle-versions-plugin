import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.14.0"
    id("maven-publish")

    id("jacoco")
    id("com.github.nbaztec.coveralls-jacoco") version "1.2.13"
}

java.sourceCompatibility = JavaVersion.VERSION_11

group = "com.karmanno.plugins"
version = "2.0.0"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.4.2.201908231537-r")

    // Test dependencies
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.1.10")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.4.2")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        csv.required.set(false)
    }
}

gradlePlugin {
    plugins {
        create("semverPlugin") {
            id = "com.karmanno.plugins.semver"
            displayName = "Gradle Versions Plugin"
            description = "Plugin for automatic versions management inspired by SemVer concepts"
            implementationClass = "com.karmanno.plugins.VersionsPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/karmann-dm/gradle-versions-plugin"
    vcsUrl = "https://github.com/karmann-dm/gradle-versions-plugin"
    description = "Plugin for auto increase version"
    tags = listOf("version", "semver", "automatic-versioning")
}

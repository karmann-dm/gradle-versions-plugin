import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
    id("java-gradle-plugin")
}

java.sourceCompatibility = JavaVersion.VERSION_11

group = "com.karmanno.plugins"
version = "2.0.0"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.4.2.201908231537-r")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.1.10")
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


gradlePlugin {
    plugins {
        create("semverPlugin") {
            id = "com.karmanno.plugins.semver"
            implementationClass = "com.karmanno.plugins.VersionsPlugin"
        }
    }
}

//gradlePlugin {
//    plugins {
//        semverPlugin {
//            id = 'com.karmanno.plugins.semver'
//            implementationClass = 'com.karmanno.plugins.VersionsPlugin'
//        }
//    }
//}
//
//
//dependencies {
//    compile group: 'org.eclipse.jgit', name: 'org.eclipse.jgit', version: '5.4.2.201908231537-r'
//    compileOnly 'org.projectlombok:lombok:1.18.10'
//    annotationProcessor 'org.projectlombok:lombok:1.18.10'
//
//    testImplementation 'junit:junit:4.13'
//    testCompile 'com.netflix.nebula:nebula-test:7.2.5'
//    testCompileOnly 'org.projectlombok:lombok:1.18.10'
//    testAnnotationProcessor 'org.projectlombok:lombok:1.18.10'
//    testImplementation gradleTestKit()
//
//    testImplementation 'org.codehaus.groovy:groovy-all:2.5.7'
//    testImplementation 'org.spockframework:spock-core:1.3-groovy-2.5'
//}
//
//pluginBundle {
//    website = 'https://github.com/karmann-dm/gradle-versions-plugin'
//    vcsUrl = 'https://github.com/karmann-dm/gradle-versions-plugin'
//    description = 'Plugin for auto increase version'
//    tags = ['version', 'semver', 'automatic-versioning']
//
//    plugins {
//        semverPlugin {
//            displayName = 'Gradle versions plugin'
//        }
//    }
//}
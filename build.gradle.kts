import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", "1.3.31"))
    }
}

plugins {
    java
    kotlin("jvm") version "1.3.71"
    kotlin("plugin.spring") version "1.3.71"
    kotlin("kapt") version "1.3.50" apply false
    kotlin("plugin.jpa") version "1.3.50" apply false
    id ("org.jetbrains.kotlin.plugin.noarg") version "1.3.50" apply false
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.7.RELEASE" apply false
}

allprojects {
    group = "com.learnneo4j"
    version = "0.0.1-SNAPSHOT"
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }
}

subprojects {

    apply(plugin = "java-library")
    apply(plugin = "kotlin")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "kotlin-spring")

    repositories {
        mavenLocal()
        jcenter()
    }

    tasks.withType<Test> {
        //useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }

}
dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
repositories {
    mavenCentral()
}
//val compileKotlin: KotlinCompile by tasks
//compileKotlin.kotlinOptions {
//    jvmTarget = "1.8"
//}
//val compileTestKotlin: KotlinCompile by tasks
//compileTestKotlin.kotlinOptions {
//    jvmTarget = "1.8"
//}
/*
 * This file was generated by the Gradle 'init' task.
 *
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    id("buildlogic.java-conventions")
}

dependencies {
    api(libs.org.springframework.boot.spring.boot.starter.web)
    api(libs.org.springframework.kafka.spring.kafka)
    api(libs.org.projectlombok.lombok)
    api(libs.org.springframework.kafka.spring.kafka.test)
    api(libs.org.springframework.cloud.spring.cloud.starter.netflix.eureka.client)
    api(libs.org.springframework.cloud.spring.cloud.starter.sleuth)
    api(libs.org.springframework.cloud.spring.cloud.sleuth.zipkin)
    testImplementation(libs.org.springframework.boot.spring.boot.starter.test)
}

description = "notification-service"

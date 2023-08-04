plugins {
    `java-library`
    `java-gradle-plugin`
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.plugin.publishing)
}

group = "com.bybutter.sisyphus.tools"
version = "2.0.0"
description = "Plugin for compiling proto files with Gradle in Sisyphus Framework"

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.sisyphus.protoc)
    implementation(libs.sisyphus.protocRunner)

    compileOnly(libs.gradle.android)

    testImplementation(kotlin("test"))
}

gradlePlugin {
    website.set("https://sisyphus.bybutter.com")
    vcsUrl.set("https://github.com/ButterCam/sisyphus-protobuf-gradle-plugin")

    plugins {
        create("protobuf") {
            id = "com.bybutter.sisyphus.protobuf"
            displayName = "Sisyphus Protobuf Plugin"
            description = "Protobuf compiler plugin for sisyphus framework."
            implementationClass = "com.bybutter.sisyphus.protobuf.gradle.ProtobufPlugin"
            tags.set(listOf("sisyphus", "protobuf", "grpc"))
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

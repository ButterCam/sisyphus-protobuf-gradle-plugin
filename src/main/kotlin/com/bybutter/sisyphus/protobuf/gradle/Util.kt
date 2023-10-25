package com.bybutter.sisyphus.protobuf.gradle

import com.bybutter.sisyphus.string.toCamelCase
import org.gradle.api.tasks.SourceSet

fun protoConfigurationName(sourceSetName: String): String {
    return if (sourceSetName == "main") {
        "proto"
    } else {
        "$sourceSetName proto".toCamelCase()
    }
}

fun protoApiConfigurationName(sourceSetName: String): String {
    return "${protoConfigurationName(sourceSetName)} api".toCamelCase()
}

fun extractProtoTaskName(sourceSetName: String): String {
    return "extract $sourceSetName proto".toCamelCase()
}

fun extractMetadataTaskName(sourceSetName: String): String {
    return "extract $sourceSetName proto metadata".toCamelCase()
}

fun generateProtoTaskName(sourceSetName: String): String {
    return "generate $sourceSetName proto".toCamelCase()
}

fun protoSourceTaskName(sourceSetName: String): String {
    return "$sourceSetName proto source".toCamelCase()
}

fun protoResourceTaskName(sourceSetName: String): String {
    return "$sourceSetName proto resource".toCamelCase()
}

fun compileKotlinTaskName(sourceSetName: String): String {
    return if (sourceSetName == "main") {
        "compileKotlin"
    } else {
        "compile $sourceSetName Kotlin".toCamelCase()
    }
}

fun runKtlintCheckTaskName(sourceSetName: String): String {
    return "runKtlintCheckOver $sourceSetName SourceSet".toCamelCase()
}

fun SourceSet.isMainSourceSet(): Boolean {
    return name == "main"
}

fun SourceSet.isTestSourceSet(): Boolean {
    return name == "test"
}

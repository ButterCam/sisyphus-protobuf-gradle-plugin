package com.bybutter.sisyphus.protobuf.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files
import java.nio.file.Paths

open class ExtractMetadataTask : DefaultTask() {
    @get:OutputDirectory
    val resourceOutput: DirectoryProperty = project.objects.directoryProperty()

    @get:InputDirectory
    val resourceInput: DirectoryProperty = project.objects.directoryProperty()

    @get:Internal
    lateinit var protobuf: ProtobufExtension

    @TaskAction
    fun extractProto() {
        resourceOutput.asFile.get().deleteRecursively()
        resourceOutput.asFile.get().mkdirs()

        if (protobuf.mapping.isNotEmpty()) {
            Files.write(
                Paths.get(resourceOutput.asFile.get().toPath().toString(), "protomap"),
                protobuf.mapping.map { "${it.key}=${it.value}" },
            )
        }

        resourceInput.asFile.get().copyRecursively(resourceOutput.asFile.get())
    }
}

package com.bybutter.sisyphus.protobuf.gradle

import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.FileCollection
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.tasks.SourceSet
import org.gradle.plugins.ide.idea.model.IdeaModel

class ProtobufJvmPlugin : BaseProtobufPlugin() {
    override fun doApply() {
        for (sourceSet in project.sourceSets) {
            applySourceSet(sourceSet)
        }
    }

    override fun doAfterApply() {
        for (sourceSet in project.sourceSets) {
            afterApplySourceSet(sourceSet)
        }
    }

    override fun protoExtension(): ProtobufExtension {
        return project.extensions.create("protobuf", ProtobufExtension::class.java)
    }

    override fun protoApiConfiguration(sourceSetName: String): Configuration {
        return project.configurations.findByName(protoApiConfigurationName(sourceSetName)) ?: run {
            val sourceSet = project.sourceSets.getByName(sourceSetName)
            super.protoApiConfiguration(sourceSetName).apply {
                this.extendsFrom(project.configurations.getByName(sourceSet.implementationConfigurationName))
                this.extendsFrom(project.configurations.getByName(sourceSet.compileOnlyConfigurationName))
            }
        }
    }

    override fun protoApiFiles(sourceSetName: String): FileCollection {
        return if (sourceSetName == SourceSet.TEST_SOURCE_SET_NAME) {
            val sourceSet = project.sourceSets.getByName(sourceSetName)
            protoApiConfiguration(sourceSetName) + sourceSet.compileClasspath
        } else {
            protoApiConfiguration(sourceSetName)
        }
    }

    override fun protoCompileFiles(sourceSetName: String): FileCollection {
        return protoConfiguration(sourceSetName) + project.files(protoSrc(sourceSetName))
    }

    private fun applySourceSet(sourceSet: SourceSet) {
        protoConfiguration(sourceSet.name)
        protoApiConfiguration(sourceSet.name)
        generateProtoTask()
        extractProtoTask(sourceSet.name)
        generateProtoTask(sourceSet.name)
        val sourceTask = sourceTask(sourceSet.name)
        val resourceTask = resourceTask(sourceSet.name)

        sourceSet.extensions.add(
            "proto",
            project.objects.sourceDirectorySet(sourceSet.name, "proto source dir").apply {
                srcDir(protoSrc(sourceSet.name))
            }
        )

        sourceSet.resources {
            it.srcDir(resourceTask)
        }

        sourceSet.extensions.configure<SourceDirectorySet>("kotlin") {
            it.srcDir(sourceTask)
        }
    }

    private fun afterApplySourceSet(sourceSet: SourceSet) {
        project.extensions.findByType(IdeaModel::class.java)?.apply {
            module.sourceDirs = module.sourceDirs + protoSrc(sourceSet.name)
            module.generatedSourceDirs.add(outDir(sourceSet.name))
            this.module.scopes["PROVIDED"]?.get("plus")?.add(protoApiConfiguration(sourceSet.name))
            this.module.scopes["COMPILE"]?.get("plus")?.add(protoConfiguration(sourceSet.name))
        }
    }
}

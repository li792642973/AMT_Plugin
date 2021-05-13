package com.amt.code.plugin.inspect

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Android-Gradle-DSL
 *
 * AppExtension        生成 com.android.application   用于建造安卓应用程序
 * LibraryExtension    生成 com.android.library       用于创建安卓库文件
 * TestExtension       生成 com.android.test          用于安卓test模块
 * FeatureExtension    生成 com.android.feature       用于创建及时APP Android Instant Apps
 */
class PluginInspect implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.create("AmtTpLibraryConfig", Config.class)
        Config config = (Config) project.property("AmtTpLibraryConfig")

        InspectTransform inspectTransform = new InspectTransform()
        AppExtension appExtension = (AppExtension)project.getProperties().get( "android")
        appExtension.registerTransform(inspectTransform)

        project.afterEvaluate {
            inspectTransform.initExtension(config)
        }
    }
}
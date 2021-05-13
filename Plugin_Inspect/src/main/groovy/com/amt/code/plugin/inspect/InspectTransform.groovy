package com.amt.code.plugin.inspect

import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.collect.ImmutableSet

class InspectTransform extends Transform {
    Set<String> configSet = new HashSet<>()
    String configValue
    void initExtension(Config config) {
        for (String name : config.whiteList) {
            configSet.add(name)

            configValue = config.toString()
        }
    }

    /**
     * Transform名称，也对应了该Transform所代表的Task名称
     * 这里应该是：transformClassesWithInspectTransformFor***
     *
     * @return 名称
     */
    @Override
    String getName() {
        return "InspectTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    Set<? super QualifiedContent.Scope> getReferencedScopes() {
        return ImmutableSet.of()
    }

    /**
     * 指明当前Transform是否支持增量编译
     * @return
     */
    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)

        String defaultStr = "非法添加第三方库，请在白名单中配置"
        String outputStr = defaultStr

        Set<String> librarySet = new HashSet<>()
        Collection<TransformInput> inputTrans = transformInvocation.inputs
        for (TransformInput transformInput : inputTrans) {
            Collection<JarInput> jarInputCollection = transformInput.jarInputs
            for (JarInput jarInput : jarInputCollection) {
                Set<? super QualifiedContent.Scope> scopeSet = jarInput.getScopes()
                if (scopeSet.contains(QualifiedContent.Scope.EXTERNAL_LIBRARIES)) {
                    String name = jarInput.name
                    if (name.contains("android.local.jars")) {
                        // 工程libs本地SDK
                        String jarName = name.split(":")[1]
                        librarySet.add(jarName.substring(9, jarName.indexOf("-runtime")))
                    } else {
                        if (!name.contains("androidx")) {
                            String[] jarName = name.split(":")
                            librarySet.add(jarName[0] + ":" + jarName[1])
                        }
                    }
                }
            }
        }
        Iterator<String> libraryIterator = librarySet.iterator()
        while (libraryIterator.hasNext()) {
            String name = libraryIterator.next()
            if (!configSet.contains(name)) {
                outputStr += "\n" + name
            }
        }

        if (defaultStr != outputStr) {
            throw new Exception(outputStr)
        }
    }
}
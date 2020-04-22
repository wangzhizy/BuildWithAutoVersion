package com.wangzhi.plugin.buildwithautoversionplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildWithAutoVersionPlugin implements Plugin<Project> {

    /**
     *
     */
    private HashMap<String, Integer> mTaskPosByName

    @Override
    void apply(Project project) {
        if (!project.rootProject.ext.has(Constants.KEY_TASK_LIST)) {
            return
        }
        if (!(project.rootProject.ext.get(Constants.KEY_TASK_LIST) instanceof List)) {
            throw RuntimeException("${Constants.KEY_TASK_LIST}配置有误，请对比文档检查配置")
            return
        }
        mTaskPosByName = new HashMap<>()
        //创建任务
        createTask(project)
        //检查当前执行任务
        checkTargetTask(project)
        project.afterEvaluate {
            //更改文件名
            project.android.applicationVariants.all { variant ->
                if (variant.buildType.name != "debug") {//防止AS无法安装debug包(apk)
                    variant.outputs.all {
                        outputFileName = "${project.rootProject.ext.versionName}.apk"

                    }
                }
            }
        }
    }

    /**
     * 创建任务
     * @param project
     */
    void createTask(Project project) {
        def buildTaskList = project.rootProject.ext.get(Constants.KEY_TASK_LIST)
        def tmpObject, tmpTask, tmpTaskName
        for (int i = 0; i < buildTaskList.size(); i++) {
            tmpObject = buildTaskList.get(i)
            if (!(tmpObject instanceof Map)) {
                throw RuntimeException("${Constants.KEY_TASK_LIST}配置有误，请对比文档检查配置")
                return
            }
            if (!tmpObject.containsKey(Constants.KEY_TASK_NAME)) {
                throw RuntimeException("${Constants.KEY_TASK_NAME} Undefined，${Constants.KEY_TASK_LIST}[${i}]配置有误，请对比文档检查配置")
                return
            }
            tmpTaskName = tmpObject.get(Constants.KEY_TASK_NAME)
            if (mTaskPosByName.containsKey(tmpTaskName)) {
                throw RuntimeException("${Constants.KEY_TASK_NAME} Already exists，${Constants.KEY_TASK_LIST}[${i}]配置有误，请对比文档检查配置")
                return
            }
            mTaskPosByName.put(tmpTaskName, i)
            if (!tmpObject.containsKey(Constants.KEY_DEPEND_TASK_NAME)) {
                throw RuntimeException("${Constants.KEY_DEPEND_TASK_NAME} Undefined，${Constants.KEY_TASK_LIST}[${i}]配置有误，请对比文档检查配置")
                return
            }
            tmpTask = project.tasks.create(tmpTaskName, BuildTask.class)
            tmpTask.dependsOn(tmpObject.get(Constants.KEY_DEPEND_TASK_NAME))
        }
    }

    /**
     * 检查当前执行的任务，执行版本号增加
     * @param project
     */
    void checkTargetTask(Project project) {
        def requestTask = project.gradle.startParameter.taskRequests.args[0].toString()
                .replaceAll("\\[", "").replaceAll("]", "")
        if (!mTaskPosByName.containsKey(requestTask)) {
            return
        }
        def taskConfig = project.rootProject.ext.get(Constants.KEY_TASK_LIST).get(mTaskPosByName.get(requestTask))
        def saveFiledName, prefix = ".", postfix = ""
        if (taskConfig.containsKey(Constants.KEY_SAVE_FIELD_NAME)) {
            saveFiledName = taskConfig.get(Constants.KEY_SAVE_FIELD_NAME)
        } else {
            saveFiledName = requestTask
        }
        if (taskConfig.containsKey(Constants.KEY_PREFIX)) {
            prefix = taskConfig.get(Constants.KEY_PREFIX)
        }
        if (taskConfig.containsKey(Constants.KEY_POSTFIX)) {
            postfix = taskConfig.get(Constants.KEY_POSTFIX)
        }
        VersionUtils versionUtils = new VersionUtils()
        project.rootProject.ext.versionName += prefix
        project.rootProject.ext.versionName += ++versionUtils.getRealVersion(project, saveFiledName)
        project.rootProject.ext.versionName += postfix
    }
}

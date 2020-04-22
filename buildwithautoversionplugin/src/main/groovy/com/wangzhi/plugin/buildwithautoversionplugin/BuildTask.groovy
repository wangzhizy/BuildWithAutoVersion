package com.wangzhi.plugin.buildwithautoversionplugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class BuildTask extends DefaultTask {
    BuildTask() {
        group = "wangzhi"
        description = 'build your app'
    }

    @TaskAction
    void doAction() {
        def tmpObject, saveFiledName
        for (int i = 0; i < project.rootProject.buildTaskList.size(); i++) {
            tmpObject = project.rootProject.buildTaskList.get(i)
            if (getName() == tmpObject.get(Constants.KEY_TASK_NAME)) {
                saveFiledName = tmpObject.get(Constants.KEY_SAVE_FIELD_NAME)
                break
            }
        }
        VersionUtils versionUtils = new VersionUtils()
        versionUtils.saveAutoAddVersion(project, saveFiledName)
        println("编译完成")
    }

}

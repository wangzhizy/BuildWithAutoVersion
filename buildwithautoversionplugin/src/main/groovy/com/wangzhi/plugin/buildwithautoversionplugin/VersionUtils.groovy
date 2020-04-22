package com.wangzhi.plugin.buildwithautoversionplugin

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.gradle.api.Project

class VersionUtils {

    /**
     * @params saveFiledName    保存信息
     * @return 该版本对应的该字段版本号
     */
    int getRealVersion(Project project, String saveFiledName) {
        def versionConfig = getVersionConfig(project)
        return getVersionByKey(project, versionConfig, saveFiledName, false)
    }

    /**
     * 保存自增的版本信息（编译成功）
     * @params saveFiledName    保存信息
     */
    void saveAutoAddVersion(Project project, String saveFiledName) {
        def versionConfig = getVersionConfig(project)
        getVersionByKey(project, versionConfig, saveFiledName, true)
        saveVersionConfig(project, versionConfig)
    }

    /**
     * @param project
     * @return 配置信息
     */
    def private getVersionConfig(Project project) {
        def versionConfigFile = getVersionConfigFile(project)
        if (!versionConfigFile.exists()) {
            versionConfigFile.createNewFile()
            versionConfigFile.write("{}")
        }
        return new JsonSlurper().parseText(versionConfigFile.getText())
    }

    /**
     *
     * @param project
     * @param versionConfig
     * @param key
     * @return 根据Key获取数据
     */
    def private getVersionByKey(Project project, Object versionConfig, String key, boolean isNeedAutoAdd) {
        def resultVersion = 1
        def firstKey = project.rootProject.ext.versionCode.toString()
        if (versionConfig[firstKey] != null) {
            if (versionConfig[firstKey][key] != null) {
                if (isNeedAutoAdd) {
                    resultVersion = ++versionConfig[firstKey][key]
                } else {
                    resultVersion = versionConfig[firstKey][key]
                }
            } else {
                versionConfig[firstKey][key] = resultVersion
            }
        } else {
            versionConfig[firstKey] = new HashMap<String, String>()
            versionConfig[firstKey][key] = resultVersion
        }
        return resultVersion
    }

    /**
     * @param project
     * @param config
     * @return 保存文件
     */
    def private saveVersionConfig(Project project, Object config) {
        def versionConfigFile = getVersionConfigFile(project)
        def result = new JsonOutput().toJson(config)
        versionConfigFile.write(result)
    }

    /**
     *
     * @param project
     * @return 配置文件
     */
    def private getVersionConfigFile(Project project) {
        def path = "", fileName = "version"
        def tmpProject = project
        while ((tmpProject = tmpProject.parent) != null) {
            path += "../"
        }
        if (project.rootProject.ext.has(Constants.KEY_FILE_NAME)) {
            fileName = project.rootProject.ext.get(Constants.KEY_FILE_NAME)
        }
        return project.file(path + "${fileName}.json")
    }
}

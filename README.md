## 简述
有没有经常遇到这种情况，测试拿着测试机跟你演示一个你已经改过的bug，讨论半天发现是测试没有更新安装包的问题，这种问题随着测试人员的增加、打包频率的上升会越来越明显；


1. 在 build.gradle(project) 中添加如下配置
    ```
    
    ```
1. 在 build.gradle(app) 中添加如下配置
    ```
    apply plugin: 'com.huiian.timing.library.buildwithautoversion'
    ```
1. 一切就绪，可以[使用](http://note.youdao.com/noteshare?id=7a53c9b836bbe76a7c635638be1009d8)啦

1. 修改真正的应用版本（可选）

    通过上面的配置，已经可以完成修改文件名称的功能，不过这时应用真正的版本并没有修改，只是修改了文件名称而已；

    如果要修改应用真正的版本号，可在 build.gradle(app) 中添加如下配置
    ```
    android {
        defaultConfig {
            versionCode rootProject.ext.versionCode
            versionName rootProject.ext.versionName
        }
    }
    ```
    好啦，现在真实的版本号也已经修改了；
 
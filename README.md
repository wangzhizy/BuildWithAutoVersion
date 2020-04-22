## 简述
有没有经常遇到这种情况，测试拿着测试机跟你演示一个你已经改过的bug，你检查了半天代码最后发现是测试没有更新安装包的问题，这种问题随着测试人员的增加、打包频率的上升会越来越明显；
所以我们在测试阶段给测试人员打包的时候需要修改一下版本号，更改bug状态的时候也要备注在VX.X.X版本可以验证，如果测试BUG回归失败时，你就可以让测试确认一下版本号，一句话就把他安排的明明白白；
可是问题来了，如果每次打测试包都要手动修改版本号的话，未免也太繁琐了，如果忘记了还要重新再打一次，这种繁琐的工作我们肯定不能干，下面我们来一起搞一个gradle的插件，解放双手吧；
## 核心需求
1. 每次打包自动更新版本号；
1. 针对各个buildVariant做不一样的配置；
1. 用插件实现，方便公司其它项目引用；
##  

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
 
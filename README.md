> [!CAUTION]
>
> **注意：本库是本作者个人私库（不公开分享本库，不建议他人使用），如果想使用本库可以在项目中使用依赖库或克隆分支（可以自己新建一个分支修改本库，不可提交到本库），请不要上传提交，请勿私自外传本项目。**


<div align="center">

<h1>
  Compose Multiplatform Markdown
</h1>

**一款强大的Kotlin多平台“markdown”中文开发包，适用于 Compose Multiplatform 的 Kotlin 多平台项目。**

[![GitHub](https://jitpack.io/v/dxycw/zwkfb.svg)](https://jitpack.io/#dxycw/multiplatform-zwkfb)
[![Kotlin](https://img.shields.io/badge/kotlin-v2.4.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-v1.12.0--beta02-blue)](https://github.com/JetBrains/compose-multiplatform)

![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-ios](http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat)
![badge-desktop](http://img.shields.io/badge/platform-desktop-DB413D.svg?style=flat)
![badge-web](https://img.shields.io/badge/platform-web-59B6EC.svg?style=flat)

</div>

# 使用方法

**1、在项目中添加依赖项的方法：**

```kotlin

// 在项目的 settings.gradle.kts 文件中添加 JitPack 仓库
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }  // 添加 JitPack 仓库
    }
}

// 在项目的 build.gradle.kts 文件中添加依赖项
kotlin {
    sourceSets {
        // 多平台，
        commonMain.dependencies {
            // 如果使用多平台 Jetpack Compose Multiplatform 项目，请添加以下依赖项
            implementation("com.github.dxycw.markdown:markdown:1.0.0")
        }
    }
}

```


# 更新内容

## 1.0.0

* 优化 项目文档，完善使用方法、平台支持、依赖库等信息；
* 创建 Markdown项目，把 "io.github.feiyin0719:markdown-multiplatform:0.3.0" 和 "com.mikepenz:multiplatform-markdown-renderer:0.43.0"导入到本项目中；
* 删除 “server” 和 “core” 两个模块；
* 删除 “logback”、“ktor-serverCore”、“ktor-serverNetty”、“ktor-serverTestHost”和“kotlin-testJunit” 依赖库；
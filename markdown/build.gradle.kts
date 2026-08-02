import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.androidLint)

    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
//    id("maven-publish")

    id("com.vanniktech.maven.publish") version "0.37.0"
}

kotlin {

    android {
        namespace = "com.markdown"
        compileSdk {
            version = release(libs.versions.android.compileSdk.get().toInt()) {
                minorApiLevel = 1
            }
        }
        minSdk = 24

        aarMetadata {
            minCompileSdk = 37  // 但这对上游依赖无效
        }

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }

        withHostTestBuilder {}

        withDeviceTestBuilder { sourceSetTreeName = "test" }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

//    listOf(
        iosArm64()
        iosSimulatorArm64()
//    ).forEach { iosTarget ->
//        iosTarget.binaries.framework {
//            baseName = "markdownKit"
////            isStatic = true
//        }
//    }

    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                // Add KMP dependencies here

                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)

                implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.5.1")
                implementation("io.coil-kt.coil3:coil-compose:3.5.0")

                // Compose Markdown Multiplatform
                api("io.github.feiyin0719:commonmark:0.0.2") // api
                implementation("io.github.feiyin0719:commonmark-ext-gfm-tables:0.0.2")
                implementation("io.github.feiyin0719:commonmark-ext-autolink:0.0.2")
                implementation("io.github.feiyin0719:commonmark-ext-task-list-items:0.0.2")
                implementation("io.github.feiyin0719:commonmark-ext-html-converter:0.0.2")

                implementation("io.coil-kt.coil3:coil-network-ktor3:3.5.0")

                // multiplatform-markdown-renderer
                api("org.jetbrains:markdown:0.7.8") // api
                api("dev.snipme:highlights:1.1.0") // api

            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                // Compose Markdown Multiplatform
                implementation("androidx.core:core-ktx:1.19.0")
                implementation("io.ktor:ktor-client-okhttp:3.5.1")

                // multiplatform-markdown-renderer
                implementation("io.coil-kt:coil-compose:2.7.0")
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.core)
                implementation(libs.androidx.runner)
                implementation(libs.androidx.testExt.junit)
            }
        }

        getByName("desktopMain")  {
            dependencies {
                // Compose Markdown Multiplatform
                implementation("io.ktor:ktor-client-okhttp:3.5.1")
            }
        }

        iosMain {
            dependencies {
                // Compose Markdown Multiplatform
                implementation("io.ktor:ktor-client-darwin:3.5.1")
            }
        }

        jsMain {
            dependencies {
                api(libs.wrappers.browser)
            }
        }

        wasmJsMain {
            dependencies {
                // Compose Markdown Multiplatform
                implementation("io.ktor:ktor-client-js:3.5.1")
            }
        }

    }

}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

mavenPublishing {
    // 发布到 Maven Central，并自动发布（可选）
    publishToMavenCentral()
    // 签名所有发布产物
    signAllPublications()

    // 定义项目坐标
    coordinates("io.github.dxycw", "markdown", "1.0.0")

    // 配置 POM 文件信息
    pom {
        name.set("markdown")
        description.set("Compose Markdown Multiplatform")
        inceptionYear.set("2026")
        url.set("https://github.com/dxycw/markdown/")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("dxycw")
                name.set("dxycw")
                url.set("https://github.com/dxycw/")
            }
        }

        scm {
            url.set("https://github.com/dxycw/markdown/")
            connection.set("scm:git:git://github.com/dxycw/markdown.git")
            developerConnection.set("scm:git:ssh://git@github.com/dxycw/markdown.git")
        }
    }
}
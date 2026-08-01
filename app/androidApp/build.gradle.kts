import com.android.build.api.variant.impl.VariantOutputImpl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

dependencies {
    implementation(project(":app:shared"))

    implementation(libs.androidx.activity.compose)

    implementation(libs.compose.uiToolingPreview)
    debugImplementation(libs.compose.uiTooling)
}

android {

    // 签名配置
    signingConfigs {
        create("release") {
            storeFile = file("../../key.jks")
            storePassword = "12345678"
            keyAlias = "key"
            keyPassword = "12345678"
//            enableV1Signing = true
//            enableV2Signing = true
//            enableV3Signing = true
//            enableV4Signing = true
        }
    }

    namespace = "com.dxyc.markdown"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.dxyc.markdown"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "0.0.1"
    }

    // ABI分割
    splits {
        abi {
            isEnable = true // 是否开启ABI分割
            reset()  // 清空默认的abi配置
            include("armeabi-v7a", "arm64-v8a", "x86", "x86_64") // 指定要支持的ABI
            isUniversalApk = false     // 是否生成通用apk
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    // 混淆配置
    buildTypes {
        getByName("release") {
//            isMinifyEnabled = false

            isMinifyEnabled = true // 是否混淆
            isShrinkResources = true // 是否压缩资源
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }

}

// 修改输出文件名
androidComponents.onVariants { variant ->
    var versionName = "0.0.1"
    // 获取当前 APK 对应的 ABI（架构）
    var abi = "universal"
    // 构建时间
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())
    variant.outputs.forEach { output ->
        if (output is VariantOutputImpl) {
            versionName = output.versionName.get()
            // 获取当前 APK 对应的 ABI（架构）
            output.filters.forEach { abi = it.identifier }
            // 修改输出文件名
            output.outputFileName = "中文开发包-${versionName}-${abi}.apk" // -${timeStamp}
        }
    }
}

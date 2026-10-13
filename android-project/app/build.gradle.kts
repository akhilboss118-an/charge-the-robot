plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.chargetherobot"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.chargetherobot"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = false
        aidl = false
        buildConfig = false
        shaders = false
    }

    androidResources {
        noCompress += listOf("html", "js", "css", "json", "png", "jpg", "jpeg", "svg", "webp", "mp3", "wav", "ogg")
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation("androidx.webkit:webkit:1.12.1")
}

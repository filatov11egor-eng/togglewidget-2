plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.egorf.togglewidget"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.egorf.togglewidget"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        resConfigs("en", "ru")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.21")
}

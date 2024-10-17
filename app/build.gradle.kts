plugins {
    id(libs.plugins.android.application.get().pluginId)
    id("com.google.gms.google-services") // Para Firebase
}

android {
    namespace = "com.example.control"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.control"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Firebase BOM para gestionar las versiones de Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))

    // Dependencias utilizando accesores de versiones desde `libs.versions.toml`
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.firestore)

    // Excluir la versión conflictiva annotation-jvm
    implementation("androidx.annotation:annotation:1.9.0") {
        exclude(group = "androidx.annotation", module = "annotation-jvm")
    }

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

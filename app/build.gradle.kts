plugins {
    // Utiliza solo una opción para aplicar el plugin de la aplicación Android
    id("com.android.application")
    id("com.google.gms.google-services") // Mantén este plugin para los servicios de Firebase
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

    // Dependencias de la librería
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.firestore)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

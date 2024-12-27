plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.sisatu"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sisatu"
        minSdk = 24
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Firebase dependencies using Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:31.0.0")) // Firebase BOM (Bill of Materials)
    implementation("com.google.firebase:firebase-database")  // Firebase Realtime Database
    implementation("com.google.firebase:firebase-storage")    // Firebase Storage

    // Other dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Map dependencies (if needed)
    implementation("com.google.android.gms:play-services-maps:18.0.1")
}

// Apply the Google services plugin to enable Firebase features
apply(plugin = "com.google.gms.google-services")

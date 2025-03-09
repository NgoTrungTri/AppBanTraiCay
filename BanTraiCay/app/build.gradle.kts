plugins {

        id("com.android.application")

        // Add the Google services Gradle plugin
        id("com.google.gms.google-services")
}

android {
    namespace = "com.btlon.bantraicay"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.btlon.bantraicay"
        minSdk = 27
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

    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    // For HmacSHA512
    implementation("org.bouncycastle:bcprov-jdk15on:1.68")

    implementation ("commons-codec:commons-codec:1.15")
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.android.material:material:1.8.0")
    implementation(libs.firebase.auth)
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.storage)
    implementation(libs.navigation.fragment)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.techmovee"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.techmovee"
        minSdk = 30
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

repositories {
    google()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }  // Adicionado repositório do Jitpack para o uCrop
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Dependência Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    // Dependência uCrop
    implementation("com.github.yalantis:ucrop:2.2.8")
    implementation("com.google.android.material:material:1.9.0") // Certifique-se de que o Jitpack foi adicionado
}

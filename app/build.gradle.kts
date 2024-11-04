plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)

}

android {
    buildFeatures {
        viewBinding = true
    }
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
    implementation(libs.firebase.auth)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Dependência Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    //Dependencia Picasso
    implementation ("com.squareup.picasso:picasso:2.71828")


    // Dependência uCrop
    implementation("com.github.yalantis:ucrop:2.2.8")
    implementation("com.google.android.material:material:1.9.0") // Certifique-se de que o Jitpack foi adicionado

    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth:21.0.1")

    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-firestore")
    implementation ("com.google.firebase:firebase-storage:20.1.0")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
}

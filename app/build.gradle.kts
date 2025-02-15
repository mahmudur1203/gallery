
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kover)
}


android {
    namespace = "com.myapp.gallery"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.myapp.gallery"
        minSdk = 21
        targetSdk = 35
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
        debug {
            //isTestCoverageEnabled = true
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
        compose = true
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":data"))




    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.coil.compose)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.accompanist.permissions)

    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.compose.navigation)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation)
    kapt(libs.hilt.compiler)

    implementation(libs.timber)


    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.truth)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.turbine)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.mockito.android)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.runner)
    androidTestUtil(libs.orchestrator)


    implementation(libs.sketch.compose)
    implementation(libs.sketch.extensions.compose)
    implementation(libs.sketch.video)
    implementation(libs.sketch.animated.gif)
    implementation(libs.sketch.animated.heif)
    implementation(libs.sketch.animated.webp)
    implementation(libs.sketch.svg)
    implementation(libs.sketch.http.ktor)

    kover(project(":domain"))
    kover(project(":data"))
    //kover(project(":testing"))

    testImplementation(project(":testing"))
    androidTestImplementation(project(":testing"))


}

kover {

    reports{
        filters{
            excludes{
                classes(
                    "*.BuildConfig.*",
                "*.BuildConfig",
                // DI
                 "*.di.*",
                // Hilt
                "*.*_ComponentTreeDeps*",
                "*.*_HiltComponents*",
                "*.*_HiltModules*",
                "*.*_MembersInjector*",
                "*.*_Factory*",
                "*.Hilt_*",
                "dagger.hilt.internal.*",
                "hilt_aggregated_deps.*",
                // Jetpack Compose
                "*.ComposableSingletons*",
                "*.*\$*Preview\$*"
                )
                packages(

                    "*.di.*",
                    "*.theme",
                    "*.navigation",
                    "*.extensions",
                    "*.state\$.*",
                    "com.myapp.gallery.BuildConfig",
                    "com.myapp.gallery.testing",
                    "com.myapp.gallery.extentions",
                    "com.myapp.gallery.util"
                )
            }
        }
    }

}

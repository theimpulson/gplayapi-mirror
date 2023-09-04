plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("com.google.protobuf")
    id("maven-publish")
    id("org.jlleitschuh.gradle.ktlint")
}

android {
    namespace = "com.aurora.gplayapi"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        aarMetadata {
            minCompileSdk = 21
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            consumerProguardFiles("proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    packaging {
        resources {
            excludes += "**/*.proto"
        }
    }
}

dependencies {

    implementation("com.google.protobuf:protobuf-javalite:3.24.0")
    implementation("com.github.kittinunf.fuel:fuel:2.3.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.22.3"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}

// Run "./gradlew publishReleasePublicationToLocalRepository" to generate release AAR locally
publishing {
    publications {
        afterEvaluate {
            create<MavenPublication>("release") {
                groupId = "com.aurora"
                artifactId = "gplayapi"
                version = "3.1.4"
                from(components["release"])
            }
        }
    }
    repositories {
        maven {
            name = "local"
            url = uri("./build/repo")
        }
    }
}

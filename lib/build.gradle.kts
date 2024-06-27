plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.parcelize)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.protobuf)
    `maven-publish`
}

android {
    namespace = "com.aurora.gplayapi"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        aarMetadata {
            minCompileSdk = 21
        }
        consumerProguardFiles("proguard-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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

    implementation(libs.google.gson)
    implementation(libs.google.protobuf)
    implementation(libs.squareup.okhttp)
    implementation(libs.kotlin.coroutines.android)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.2"
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
                version = "3.3.0"
                from(components["release"])
            }
        }
    }
    repositories {
        maven {
            name = "GitLab"
            url = uri("https://gitlab.com/api/v4/projects/18497829/packages/maven")
            credentials(HttpHeaderCredentials::class) {
                name = "Job-Token"
                value = System.getenv("CI_JOB_TOKEN")
            }
            authentication {
                create("header", HttpHeaderAuthentication::class)
            }
        }
        maven {
            name = "local"
            url = uri("./build/repo")
        }
    }
}

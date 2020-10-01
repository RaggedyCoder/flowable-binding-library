/*
 * Copyright 2020 RaggedyCoder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {

    setCompileSdkVersion(Versions.compileSdk)

    defaultConfig {
        setMinSdkVersion(Versions.minSdk)
        setTargetSdkVersion(Versions.targetSdk)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testBuildType = "debug"

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
            isTestCoverageEnabled = true
        }
    }

    sourceSets {
        getByName("main") {
            java.srcDir("src/main/kotlin")
        }

        getByName("test") {
            java.srcDir("src/test/kotlin")
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        named("main") {
            noAndroidSdkLink.set(false)
        }
    }
}

dependencies {
    api(project(":flowable-binding"))
    api(Libraries.kotlinStdLib)
    api(Libraries.kotlinCoroutines)
    api(Libraries.androidXCore)

    testImplementation(TestLibraries.testCore)
    testImplementation(TestLibraries.junit4)
    testImplementation(TestLibraries.junit5)
    testRuntimeOnly(TestLibraries.junit5Engine)
    testRuntimeOnly(TestLibraries.junit5VintageEngine)
    testImplementation(TestLibraries.junit5Params)
    testImplementation(TestLibraries.mockitoCore)
    testImplementation(TestLibraries.mockitoKotlin)
    testImplementation(TestLibraries.coroutinesTest)
    testImplementation(TestLibraries.roboelectric)
    testImplementation(TestLibraries.roboelectricJUnit)
}

afterEvaluate {
    apply(plugin = "publishable-library")
}

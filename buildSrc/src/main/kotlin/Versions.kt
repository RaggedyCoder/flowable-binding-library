object Versions {

    val kotlin = "1.4.10"
    val coroutines = "1.3.9"

    val minSdk = 21
    val targetSdk = 29
    val compileSdk = 29

    val appCompat = "1.2.0"

    val materialDesign = "1.2.1"

    val androidXCore = "1.3.1"
    val androidXAnnotations = "1.1.0"
    val androidXSwipeRefreshLayout = "1.1.0"
    val androidXDrawerLayout = "1.1.1"
    val androidXSlidingPaneLayout = "1.1.0"
    val androidXRecyclerView = "1.1.0"
    val androidXLeanBack = "1.0.0"
    val androidXViewPager = "1.0.0"
    val androidXViewPager2 = "1.0.0"

    // Test library versions
    val androidXTest = "1.3.0"
    val junit4 = "4.13"
    val junit5 = "5.7.0"
    val mockito = "3.5.7"
    val mockitoKotlin = "2.2.0"
    val roboelectric = "4.4"
}


object Libraries {

    // AppCompat
    val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"

    // Google Material Design
    val materialDesign = "com.google.android.material:material:${Versions.materialDesign}"

    // AndroidX
    val androidXCore = "androidx.core:core:${Versions.androidXCore}"
    val androidXAnnotations = "androidx.annotation:annotation:${Versions.androidXAnnotations}"
    val androidXRecyclerView = "androidx.recyclerview:recyclerview:${Versions.androidXRecyclerView}"
    val androidXSwipeRefreshLayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.androidXSwipeRefreshLayout}"
    val androidXDrawerLayout =
        "androidx.drawerlayout:drawerlayout:${Versions.androidXDrawerLayout}"
    val androidXSlidingPaneLayout =
        "androidx.slidingpanelayout:slidingpanelayout:${Versions.androidXSlidingPaneLayout}"
    val androidXViewPager = "androidx.viewpager:viewpager:${Versions.androidXViewPager}"
    val androidXLeanBack = "androidx.leanback:leanback:${Versions.androidXLeanBack}"
    val androidXViewPager2 = "androidx.viewpager2:viewpager2:${Versions.androidXViewPager2}"

    // Kotlin
    val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val kotlinCoroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
}

object TestLibraries {
    val testCore = "androidx.test:core:${Versions.androidXTest}"
    val junit4 = "junit:junit:${Versions.junit4}"
    val junit5 = "org.junit.jupiter:junit-jupiter-api:${Versions.junit5}"
    val junit5Engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit5}"
    val junit5VintageEngine = "org.junit.vintage:junit-vintage-engine:${Versions.junit5}"
    val junit5Params = "org.junit.jupiter:junit-jupiter-params:${Versions.junit5}"

    val mockitoCore = "org.mockito:mockito-core:${Versions.mockito}"
    val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin}"

    val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"

    val roboelectric = "org.robolectric:robolectric:${Versions.roboelectric}"
    val roboelectricJUnit = "org.robolectric:junit:${Versions.roboelectric}"
}

plugins {
    id("com.gradle.enterprise") version "3.4"
}

include(
    ":flowable-binding",
    ":flowable-binding-widget",
    ":flowable-binding-core",
    ":flowable-binding-appcompat",
    ":flowable-binding-material",
    ":flowable-binding-leanback",
    ":flowable-binding-drawerlayout",
    ":flowable-binding-swiperefreshlayout",
    ":flowable-binding-slidingpanelayout",
    ":flowable-binding-recyclerview",
    ":flowable-binding-viewpager",
    ":flowable-binding-viewpager2"
)
include(":sample")

rootProject.name = "flowable-binding-library"

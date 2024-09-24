rootProject.name = "reform"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("modules:core")
include("modules:file")
include("modules:kotlinx-datetime")

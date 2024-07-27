plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ktlint.gradle)
    alias(libs.plugins.android.library) apply false
}

ktlint {
    filter {
        exclude { element ->
            element.file.path.contains("generated")
        }
    }
}

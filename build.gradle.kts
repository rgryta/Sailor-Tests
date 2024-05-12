import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask


plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false

    alias(libs.plugins.ben.manes)
}

val isNonStable: (ModuleComponentIdentifier) -> Boolean = { candidate ->
    val version = candidate.version
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = Regex("^[0-9,.v-]+(-r)?$")
    val oauth2regex = Regex("^v\\d+-rev\\d{8}-(\\d+\\.){2}\\d+$") // For google-api-services-oauth2
    !stableKeyword && !regex.matches(version) && !oauth2regex.matches(version)
}

allprojects {
    tasks.withType<DependencyUpdatesTask> {
        rejectVersionIf {
            isNonStable(candidate)
        }
    }
}
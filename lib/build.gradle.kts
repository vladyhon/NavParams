import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
    id("com.vanniktech.maven.publish") version "0.31.0-rc2"
    signing
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    jvmToolchain(17)
}

ktlint {
    version.set("0.50.0")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlin.coroutines)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.turbine)
}

mavenPublishing {
    configure(
        KotlinJvm(
            javadocJar = JavadocJar.Javadoc(),
            sourcesJar = true,
        ),
    )
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
}

signing {
    val signingKey = System.getenv("GPG_SIGNING_KEY")
    val signingPassword = System.getenv("GPG_SIGNING_PASSWORD")
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}

// Workaround taken from here:
// https://github.com/gradle/gradle/issues/26091#issuecomment-1722947958
// Maybe fix can be found here:
// https://github.com/gradle/gradle/pull/26292
tasks.withType<AbstractPublishToMaven>().configureEach {
    val signingTasks = tasks.withType<Sign>()
    mustRunAfter(signingTasks)
}

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
    `maven-publish`
    signing
}

group = "io.github.vladyhon"
version = "0.1.0"

kotlin {
    explicitApi()
    jvmToolchain(17)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlin.coroutines)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.turbine)
}

ktlint {
    version.set("0.50.0")
}

publishing {
    publications {
        create<MavenPublication>("release") {
            from(components["kotlin"])
            groupId = "io.github.vladyhon"
            artifactId = "NavParams"
            version = "0.1.0"

            pom {
                name.set("NavParams")
                description.set("A simple Kotlin library with Flow support")
                url.set("https://github.com/vladyhon/NavParams")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("vladyhon")
                        name.set("Vladyslav H.")
                        email.set("vladyhondev@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/vladyhon/NavParams.git")
                    developerConnection.set("scm:git:ssh://github.com:vladyhon/NavParams.git")
                    url.set("https://github.com/vladyhon/NavParams")
                }
            }
        }
    }
    repositories {
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
            name = "SonatypeSnapshot"
            credentials {
                username = System.getenv("SONATYPE_USER")
                password = System.getenv("SONATYPE_PASSWORD")
            }
        }
        maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
            name = "SonatypeStaging"
            credentials {
                username = System.getenv("SONATYPE_USER")
                password = System.getenv("SONATYPE_PASSWORD")
            }
        }
    }
}

signing {
    val signingKey = System.getenv("GPG_SIGNING_KEY")
    val signingPassword = System.getenv("GPG_SIGNING_PASSWORD")
    isRequired = hasProperty("GPG_SIGNING_REQUIRED")
    if (isRequired) useInMemoryPgpKeys(signingKey, signingPassword)
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

tasks.withType<Jar>().configureEach {
    archiveBaseName.set("NavParams")
}

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.24"
    id("org.jetbrains.compose") version "1.4.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation ("ca.gosyer:compose-material-dialogs-core:0.9.3")
    implementation(compose.material3)
    implementation ("ca.gosyer:compose-material-dialogs-datetime:0.9.3")
    implementation("org.apache.pdfbox:pdfbox:3.0.0")
    implementation("com.sun.mail:javax.mail:1.6.2")
    implementation("com.itextpdf:itextpdf:5.0.6")

//    implementation("androidx.compose.material3:material3:1.2.1") {
//        exclude(group = "androidx.compose.foundation")
//        exclude(group = "androidx.compose.ui")
//        exclude(group = "androidx.compose.animation")
//    }
}

tasks.test {
    useJUnitPlatform()
}

//tasks.withType<KotlinCompile> {
//    kotlinOptions.jvmTarget = "1.9"
//}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            includeAllModules = true
            targetFormats(TargetFormat.Exe, TargetFormat.Msi)
            packageName = "NovoVet"
            packageVersion = "1.0.1"
            windows {
                packageVersion = "1.0.1"
                msiPackageVersion = "1.0.1"
                exePackageVersion = "1.0.1"
            }
        }
    }
}

compose {
    kotlinCompilerPlugin.set("androidx.compose.compiler:compiler:1.5.14")
}
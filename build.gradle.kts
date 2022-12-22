import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.22"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "top.e404"
version = "1.0.4"

repositories {
    mavenLocal()
    // spigot
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
    mavenCentral()
}

dependencies {
    // spigot
    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    // eplugin
    implementation("top.e404:eplugin:1.0.3")
    // Bstats
    implementation("org.bstats:bstats-bukkit:3.0.0")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.shadowJar {
    archiveFileName.set("${project.name}-${project.version}.jar")
    relocate("org.bstats", "top.e404.viewslimechunk.relocate.bstats")
    relocate("kotlin", "top.e404.viewslimechunk.relocate.kotlin")
    relocate("top.e404.eplugin", "top.e404.viewslimechunk.relocate.eplugin")
    exclude("META-INF/*")
    doFirst {
        for (file in File("jar").listFiles() ?: arrayOf()) {
            println("正在删除`${file.name}`")
            file.delete()
        }
    }

    doLast {
        File("jar").mkdirs()
        for (file in File("build/libs").listFiles() ?: arrayOf()) {
            println("正在复制`${file.name}`")
            file.copyTo(File("jar/${file.name}"), true)
        }
    }
}

tasks {
    processResources {
        filesMatching("plugin.yml") {
            expand(project.properties)
        }
    }
}
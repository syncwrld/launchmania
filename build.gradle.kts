import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "me.syncwrld.minecraft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.github.paperspigot:paperspigot-api:1.8.8-R0.1-SNAPSHOT")
    implementation("com.github.cryptomorin:XSeries:13.1.0")
    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation("com.github.fierioziy.particlenativeapi:ParticleNativeAPI-core:4.4.0")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    relocate("com.cryptomorin.xseries", "me.syncwrld.minecraft.launchmania.lib.xseries")
    relocate("org.bstats", "me.syncwrld.minecraft.launchmania.lib.bstats")
    relocate("com.google.gson", "me.syncwrld.minecraft.launchmania.lib.gson")
    relocate("com.github.fierioziy.particlenativeapi", "me.syncwrld.minecraft.launchmania.lib.particlenativeapi")
}

val copyJar by tasks.registering(Copy::class) {
    dependsOn(tasks.withType<ShadowJar>())
    from(tasks.shadowJar.get().archiveFile)
    into("/artifacts/")
    rename { "Launchmania.jar" }
}
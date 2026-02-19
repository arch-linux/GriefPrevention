plugins {
    java
}

group = "net.alloymc.mod"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile> {
    options.release.set(21)
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// ---- Alloy SDK location ----
// Points to the local Alloy repo. Override with -PalloyHome=/path/to/alloy
val alloyHome: String = project.findProperty("alloyHome")?.toString()
    ?: "${System.getProperty("user.home")}/Desktop/alloy"

// Resolve Minecraft version from Alloy's cache directory or property override
val mcVersion: String = project.findProperty("mcVersion")?.toString()
    ?: file("$alloyHome/cache").listFiles()?.filter { it.isDirectory }
        ?.maxByOrNull { it.lastModified() }?.name
    ?: "unknown"

val modVersion = "1.1.0"

version = "$modVersion-mc$mcVersion"

tasks.jar {
    archiveBaseName.set("GriefPrevention")
    archiveVersion.set("$modVersion-mc$mcVersion")

    // Bundle dependencies into a fat JAR so the mod is self-contained
    from(configurations.runtimeClasspath.get()
        .filter { it.name.contains("gson") }
        .map { if (it.isDirectory) it else zipTree(it) }
    )

    manifest {
        attributes(
            "Mod-Id" to "griefprevention",
            "Mod-Name" to "GriefPrevention",
            "Mod-Version" to "$modVersion-mc$mcVersion",
            "Mod-Entrypoint" to "net.alloymc.mod.griefprevention.GriefPreventionMod"
        )
    }
}

// ---- Release task: build JAR, copy to Desktop, print path ----

tasks.register<Copy>("release") {
    group = "alloy"
    description = "Build the GriefPrevention JAR and copy it to ~/Desktop"
    dependsOn(tasks.jar)

    from(tasks.jar.get().archiveFile)
    into(System.getProperty("user.home") + "/Desktop")

    doLast {
        val jarName = tasks.jar.get().archiveFile.get().asFile.name
        println("==============================================")
        println("  GriefPrevention $modVersion for MC $mcVersion")
        println("  JAR: ~/Desktop/$jarName")
        println("==============================================")
    }
}

dependencies {
    // Alloy API — the modding surface
    compileOnly(fileTree("$alloyHome/alloy-api/build/libs") { include("*.jar") })

    // Alloy Loader — for ModInitializer interface
    compileOnly(fileTree("$alloyHome/alloy-loader/build/libs") { include("*.jar") })

    // JSON parsing for data persistence
    implementation("com.google.code.gson:gson:2.11.0")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

repositories {
    mavenCentral()
}

import org.apache.commons.lang3.time.DateFormatUtils
import java.util.*

plugins {
    id("com.github.johnrengelman.shadow") version ("8.+")
}

val projectName = project.ext["projectName"] as String
val authors = project.ext["authors"] as String
val javaVersion = project.ext["javaVersion"] as Int
val charset = project.ext["charset"] as String

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.0")
    compileOnly(fileTree("${projectDir}/libraries"))
    compileOnly("net.md-5:bungeecord-api:1.12-SNAPSHOT")

    api(project(":Common"))
}

java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = sourceCompatibility
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
        vendor.set(JvmVendorSpec.AZUL)
    }
}

tasks.compileJava {
    options.encoding = charset
}

tasks.processResources {
    filteringCharset = charset
    includeEmptyDirs = false
    val props = mapOf(
            "name" to projectName,
            "version" to version,
    )
    filesMatching(listOf("bungee.yml")) {
        expand(props)
    }
    val assetsDir = "assets/${projectName}"
    eachFile {
        if (path.startsWith("assets/")) {
            print("$path >> ")
            path = assetsDir + path.substring(6)
            println(path)
        }
    }
}

tasks.shadowJar {
    archiveBaseName.set(projectName)
    archiveClassifier.set("")
    from(project(":Common").tasks.shadowJar)
    relocate("io.netty.", "${project.group}.io.netty.")
    relocate("org.yaml.snakeyaml.", "${project.group}.org.yaml.snakeyaml.")
    manifest {
        attributes(linkedMapOf(
                "Group" to project.group,
                "Name" to projectName,
                "Version" to project.version,
                "Authors" to authors,
                "Updated" to DateFormatUtils.format(Date(), "yyyy-MM-dd HH:mm:ssZ"),
                "Multi-Release" to true,
        ))
    }
}

tasks.jar {
    dependsOn(tasks.shadowJar)
    enabled = false

    finalizedBy("copyToRun")
}

tasks.create<Copy>("copyToRun") {
    from(tasks.shadowJar)
    into("${projectDir}/run/plugins")
    finalizedBy("copyToRootBuildLibs")
}

tasks.create<Copy>("copyToRootBuildLibs") {
    from(tasks.shadowJar)
    into("${rootProject.projectDir}/build/libs")
}

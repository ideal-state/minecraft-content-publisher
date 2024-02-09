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
    api(fileTree("${projectDir}/libraries"))

    val jacksonVersion = "2.15.3"
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${jacksonVersion}")
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
}

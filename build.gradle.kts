plugins {
    java
    eclipse
    idea
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

eclipse {
    classpath {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

println("Java: ${System.getProperty("java.version")}, " +
        "JVM: ${System.getProperty("java.vm.version")} (${System.getProperty("java.vendor")}), " +
        "Arch: ${System.getProperty("os.arch")}")

val junitVersion = "5.10.1"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
	testImplementation(platform("org.junit:junit-bom:6.0.0"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

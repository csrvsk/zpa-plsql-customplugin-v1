import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

plugins {
    java
}

java {
    //sourceCompatibility = JavaVersion.VERSION_17  // Java version
    //targetCompatibility = JavaVersion.VERSION_17  // Java version
    //if we ebanle above java 17 version for compilation, resulting plugin may not be compatible to execute sonar-scanner as it
    //uses or runs on Java version 11. We might need to adjust scanner configuration to use java 17 before we run the scan.
    sourceCompatibility = JavaVersion.VERSION_11  // Java version
    targetCompatibility = JavaVersion.VERSION_11  // Java version
}

/*kotlin {
    jvmTarget = "17"
}*/

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}


/*java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        //languageVersion.set(JavaLanguageVersion.of(11))
    }
}*/

//val minSonarQubeVersion = "8.9.0.43852"
val minSonarQubeVersion = "9.9.1.69595"
//val minSonarQubeVersion = "9.4.0.54424"

dependencies {
    //compileOnly("org.sonarsource.sonarqube:sonar-plugin-api:$minSonarQubeVersion")
    compileOnly("org.sonarsource.sonarqube:sonar-plugin-api:9.4.0.54424")
    compileOnly("com.felipebz.zpa:sonar-zpa-plugin:3.2.1")
    testImplementation("org.sonarsource.sonarqube:sonar-plugin-api-impl:$minSonarQubeVersion")
    testImplementation("com.felipebz.zpa:zpa-checks-testkit:3.2.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.0")
}

configurations {
    // include compileOnly dependencies during test
    testImplementation {
        extendsFrom(configurations.compileOnly.get())
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.jar {
    manifest {
        val buildDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ").withZone(ZoneId.systemDefault()).format(
            Date().toInstant())
        attributes(mapOf(
            "Plugin-BuildDate" to buildDate,
            "Plugin-ChildFirstClassLoader" to "false",
            "Plugin-Class" to "com.company.plsql.PlSqlCustomRulesPlugin",
            "Plugin-Description" to "CMiC PL/SQL Custom Rules",
            "Plugin-Developers" to "",
            "Plugin-Display-Version" to project.version,
            "Plugin-Key" to "CMiC-Custom-PLSQL",
            "Plugin-License" to "",
            "Plugin-Name" to "CMiC PL/SQL Custom Rules",
            "Plugin-Version" to project.version,
            "Sonar-Version" to minSonarQubeVersion,
            "SonarLint-Supported" to "false"
        ))
    }
}

group = "com.company"
version = "1.0"
description = "CMiC PL/SQL Rules"

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

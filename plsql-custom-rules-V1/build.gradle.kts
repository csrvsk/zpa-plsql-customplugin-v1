import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

plugins {
    java
}

java {
    //sourceCompatibility = JavaVersion.VERSION_17  // Java version
    //targetCompatibility = JavaVersion.VERSION_17  // Java version
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
    compileOnly("com.felipebz.zpa:sonar-zpa-plugin:3.2.0")
    testImplementation("org.sonarsource.sonarqube:sonar-plugin-api-impl:$minSonarQubeVersion")
    //testImplementation("org.sonarsource.sonarqube:sonar-plugin-api-impl:9.4.0.54424")
    testImplementation("com.felipebz.zpa:zpa-checks-testkit:3.2.0")
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
            "Plugin-Description" to "PL/SQL Custom Rules",
            "Plugin-Developers" to "",
            "Plugin-Display-Version" to project.version,
            "Plugin-Key" to "myrules",
            "Plugin-License" to "",
            "Plugin-Name" to "Company PL/SQL Rules",
            "Plugin-Version" to project.version,
            "Sonar-Version" to minSonarQubeVersion,
            "SonarLint-Supported" to "false"
        ))
    }
}

group = "com.company"
version = "1.0-SNAPSHOT"
description = "Company PL/SQL Rules"

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

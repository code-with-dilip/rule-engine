import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
val groovyVersion: String by project
val spockGroovyVersion: String by project


plugins {
    //application
    groovy
    kotlin("kapt")
    id ("org.jetbrains.kotlin.plugin.noarg")

}
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.kie:kie-ci:7.36.0.20200331")
    implementation("org.drools:drools-decisiontables:7.36.0.20200331")


    //spock test dependencies
    testImplementation("org.codehaus.groovy:groovy-all:$groovyVersion")
    testImplementation("org.spockframework:spock-core:$spockGroovyVersion")

}

sourceSets {
    test {
        withConvention(GroovySourceSet::class) {
            groovy {
                srcDirs(listOf("src/test/intg", "src/test/unit"))
            }
        }
    }
}
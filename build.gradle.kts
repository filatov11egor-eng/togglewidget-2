// Корневой build.gradle.kts — переадресация на модуль app
plugins {
    // плагин нужен только для настройки, не для сборки
    base
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

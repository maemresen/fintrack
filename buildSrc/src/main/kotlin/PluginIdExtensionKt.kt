import org.gradle.plugin.use.PluginDependenciesSpec

val PluginDependenciesSpec.javaApp
    get() = id("com.maemresen.fintrack.gradle.plugin.java-app-plugin")

val PluginDependenciesSpec.springApp
    get() = arrayOf(id("org.springframework.boot"), id("io.spring.dependency-management"))

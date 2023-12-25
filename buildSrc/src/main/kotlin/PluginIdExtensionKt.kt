import org.gradle.plugin.use.PluginDependenciesSpec

val PluginDependenciesSpec.javaApp
    get() = id("com.maemresen.fintrack.gradle.plugin.java-app-plugin")

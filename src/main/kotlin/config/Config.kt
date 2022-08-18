package top.e404.viewslimechunk.config

import top.e404.eplugin.config.EConfig
import top.e404.eplugin.config.JarConfig
import top.e404.viewslimechunk.PL

object Config : EConfig(
    PL,
    "config.yml",
    JarConfig(PL, "config.yml")
) {
    val range: Int
        get() = config.getInt("range")

    val disable: List<String>
        get() = config.getStringList("message.invalid_world")
}
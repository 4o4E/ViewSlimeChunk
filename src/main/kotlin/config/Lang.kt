package top.e404.viewslimechunk.config

import top.e404.eplugin.config.ELangManager
import top.e404.viewslimechunk.PL

object Lang : ELangManager(PL) {
    val char: String
        get() = this["char"]
}
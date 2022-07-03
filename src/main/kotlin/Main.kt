package top.e404.viewslimechunk

import top.e404.eplugin.EPlugin
import top.e404.viewslimechunk.command.Reload
import top.e404.viewslimechunk.command.Slime
import top.e404.viewslimechunk.config.Config
import top.e404.viewslimechunk.config.Lang
import top.e404.viewslimechunk.update.Updater

class Main : EPlugin() {
    companion object {
        lateinit var instance: Main
    }

    override val bstatsId = 15069
    override val debugPrefix = ""
    override val langManager = Lang
    override val prefix = Lang["prefix"]

    override fun enableDebug(): Boolean {
        TODO("Not yet implemented")
    }

    override fun onEnable() {
        instance = this
        bstats()
        Lang.load(null)
        Config.load(null)
        Slime.register()
        Reload.register()
        Updater.init()
        info("&f加载完成, 作者&b404E")
    }

    override fun onDisable() {
        info("&f卸载完成, 作者&b404E")
    }
}

val PL: Main
    get() = Main.instance
package top.e404.viewslimechunk.update

import top.e404.eplugin.update.EUpdater
import top.e404.viewslimechunk.PL
import top.e404.viewslimechunk.config.Config

object Updater : EUpdater(
    plugin = PL,
    url = "https://api.github.com/repos/4o4E/ViewSlimeChunk/releases",
    mcbbs = "https://www.mcbbs.net/thread-1333162-1-1.html",
    github = "https://github.com/4o4E/ViewSlimeChunk"
) {
    override fun enableUpdate() = Config.config.getBoolean("update")
}
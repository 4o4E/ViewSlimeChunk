package top.e404.viewslimechunk.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import top.e404.eplugin.command.ECommandManager
import top.e404.viewslimechunk.PL
import top.e404.viewslimechunk.config.Config
import top.e404.viewslimechunk.config.Lang

object Reload : ECommandManager(
    PL,
    "slimereload",
) {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ) = mutableListOf<String>()

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>,
    ): Boolean {
        if (!plugin.hasPerm(sender, "viewslimechunk.admin", true)) return true
        plugin.runTaskAsync {
            Lang.load(sender)
            Config.load(sender)
            plugin.sendMsgWithPrefix(sender, Lang["command.reload"])
        }
        return true
    }
}
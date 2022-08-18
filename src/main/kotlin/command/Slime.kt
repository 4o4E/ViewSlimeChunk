package top.e404.viewslimechunk.command

import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import top.e404.eplugin.command.ECommandManager
import top.e404.eplugin.util.hover
import top.e404.viewslimechunk.PL
import top.e404.viewslimechunk.config.Config
import top.e404.viewslimechunk.config.Lang

object Slime : ECommandManager(
    PL,
    "slime",
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
        if (!plugin.isPlayer(sender, true)) return true
        sender as Player
        if (!plugin.hasPerm(sender, "viewslimechunk.use", true)) return true
        if (sender.world.name in Config.disable) {
            plugin.sendMsgWithPrefix(sender, Lang["message.invalid_world"])
            return true
        }
        sendSlimeChunk(sender)
        return true
    }

    private fun sendSlimeChunk(p: Player) {
        val chunk = p.location.chunk
        val chunkX = chunk.x
        val chunkZ = chunk.z
        val list = mutableListOf<TextComponent>()
        for (z in chunkZ - Config.range..chunkZ + Config.range) {
            for (x in chunkX - Config.range..chunkX + Config.range) {
                val c = p.world.getChunkAt(x, z)
                val slime = c.isSlimeChunk
                val b = Lang[if (slime) "hover.is" else "hover.is_not"]
                val cx = x * 16 + 8
                val cz = z * 16 + 8
                val color: String
                val hover: String
                if (x == chunkX && z == chunkZ) {
                    color = if (slime) "color.center.slime" else "color.center.normal"
                    hover = "hover.center"
                } else {
                    color = if (slime) "color.slime" else "color.normal"
                    hover = "hover.normal"
                }
                list.add("${Lang[color]}${Lang.char}".hover(Lang[hover, "x" to x, "z" to z, "cx" to cx, "cz" to cz, "b" to b]))
            }
            list.add(TextComponent("\n"))
        }
        p.spigot().sendMessage(TextComponent(*list.toTypedArray()))
    }
}
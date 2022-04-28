package top.e404.viewslimechunk

import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bstats.bukkit.Metrics
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

@Suppress("UNUSED")
class Main : JavaPlugin() {
    companion object {
        lateinit var instance: Main
        private lateinit var prefix: String
        private lateinit var slimeColor: String
        private lateinit var normalColor: String
        private lateinit var centerSlimeColor: String
        private lateinit var centerNormalColor: String
        private lateinit var char: String
        private var range: Int = 3
    }

    override fun onEnable() {
        Metrics(this, 15069)
        instance = this
        saveDefaultConfig()
        reloadConfig()
        load()
        logger.info("&a加载完成".color())
    }

    private fun String.color() = replace("&", "§")

    private fun CommandSender.sendMsgWithPrefix(s: String) {
        sendMessage("$prefix $s".color())
    }

    private fun load() {
        prefix = config.getString("prefix") ?: "&7[&aSlime&7]"
        slimeColor = config.getString("slime_color") ?: "&a"
        normalColor = config.getString("normal_color") ?: "&7"
        centerSlimeColor = config.getString("center_slime_color") ?: "&2"
        centerNormalColor = config.getString("center_normal_color") ?: "&0"
        char = config.getString("char") ?: "■"
        range = config.getInt("range")
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (command.name == "slimereload") {
            try {
                saveDefaultConfig()
                reloadConfig()
                load()
                sender.sendMsgWithPrefix("&a重载完成")
            } catch (t: Throwable) {
                logger.log(Level.WARNING, "重载时出现异常", t)
                sender.sendMsgWithPrefix("&a重载时出现异常, 报错见控制台")
            }
            return true
        }
        if (sender !is Player) {
            sender.sendMessage("只可由玩家执行")
            return true
        }
        val world = sender.world
        val chunk = sender.location.chunk
        val cx = chunk.x
        val cz = chunk.z
        val list = mutableListOf<TextComponent>()
        for (z in cz - range..cz + range) {
            for (x in cx - range..cx + range) {
                val c = world.getChunkAt(x, z)
                val slime = c.isSlimeChunk
                val center = "中心&a${x * 16 + 8}&f, &a${z * 16 + 8}"
                val s = if (slime) "&a是" else "&c不是"
                if (x == cx && z == cz) {
                    val color = if (slime) centerSlimeColor else centerNormalColor
                    list.add(hoverText("$color$char", "&f你所在的区块&b$x&f, &b$z\n&f$center\n${s}史莱姆区块"))
                } else {
                    val color = if (slime) slimeColor else normalColor
                    list.add(hoverText("$color$char", "&f区块&b$x&f, &b$z\n&f中心&a$center\n${s}史莱姆区块"))
                }
            }
            list.add(TextComponent("\n"))
        }
        sender.spigot().sendMessage(TextComponent(*list.toTypedArray()))
        return true
    }

    private fun hoverText(text: String, hover: String) =
        TextComponent(text.color()).apply {
            hoverEvent = HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                arrayOf(TextComponent(hover.color()))
            )
        }
}
package top.e404.viewslimechunk

import com.google.gson.JsonParser
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import java.net.URL
import java.util.logging.Level

@Suppress("UNUSED")
class Main : JavaPlugin(), Listener {
    companion object {
        private const val api = "https://api.github.com/repos/4o4E/ViewSlimeChunk/releases"
        private const val mcbbs = "https://www.mcbbs.net/thread-1333162-1-1.html"
        private const val github = "https://github.com/4o4E/ViewSlimeChunk"

        private val jp = JsonParser()

        lateinit var instance: Main
        private var update = true
        private lateinit var prefix: String
        private lateinit var slimeColor: String
        private lateinit var normalColor: String
        private lateinit var hover: String
        private lateinit var centerSlimeColor: String
        private lateinit var centerNormalColor: String
        private lateinit var centerHover: String
        private lateinit var char: String
        private var range: Int = 3

        private lateinit var nowVer: String
        private var latestVer: String? = null
        private var hasNewVer = false
    }

    override fun onEnable() {
        instance = this
        nowVer = description.version
        Metrics(this, 15069)
        saveDefaultConfig()
        reloadConfig()
        load()
        Bukkit.getPluginManager().registerEvents(this, this)
        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            latestVer = jp.parse(URL(api).readText())
                .asJsonArray[0]
                .asJsonObject["tag_name"]
                .asString!!
            hasNewVer = hasNewVer()
            for (p in Bukkit.getOnlinePlayers()) p.sendUpdate()
        }, 0, 20 * 60 * 60 * 6)
        logger.info("&a加载完成".color())
    }

    private fun String.color() = replace("&", "§")

    private fun CommandSender.sendMsgWithPrefix(s: String) {
        sendMessage("$prefix $s".color())
    }

    private fun load() {
        update = config.getBoolean("update")
        prefix = config.getString("prefix") ?: "&7[&aSlime&7]"
        slimeColor = config.getString("slime_color") ?: "&a"
        normalColor = config.getString("normal_color") ?: "&7"
        hover = config.getString("hover") ?: ""
        centerSlimeColor = config.getString("center_slime_color") ?: "&2"
        centerNormalColor = config.getString("center_normal_color") ?: "&0"
        centerHover = config.getString("center_hover") ?: ""
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
        val chunkX = chunk.x
        val chunkZ = chunk.z
        val list = mutableListOf<TextComponent>()
        for (z in chunkZ - range..chunkZ + range) {
            for (x in chunkX - range..chunkX + range) {
                val c = world.getChunkAt(x, z)
                val slime = c.isSlimeChunk
                val b = if (slime) "&a是" else "&c不是"
                val cx = x * 16 + 8
                val cz = z * 16 + 8
                if (x == chunkX && z == chunkZ) {
                    val color = if (slime) centerSlimeColor else centerNormalColor
                    list.add(
                        hoverText(
                            "$color$char", centerHover.placeholder(
                                "x" to x, "z" to z,
                                "cx" to cx, "cz" to cz,
                                "b" to b
                            )
                        )
                    )
                } else {
                    val color = if (slime) slimeColor else normalColor
                    list.add(
                        hoverText(
                            "$color$char", hover.placeholder(
                                "x" to x, "z" to z,
                                "cx" to cx, "cz" to cz,
                                "b" to b
                            )
                        )
                    )
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

    private fun String.placeholder(vararg kv: Pair<String, Any>): String {
        var s = this
        for ((from, to) in kv) {
            s = s.replace("{$from}", to.toString())
        }
        return s
    }

    private fun String.asVersion() = replace(".", "").toInt()

    private fun hasNewVer(): Boolean {
        val latest = latestVer ?: return false
        val now = nowVer.split(".")
        for ((i, s) in latest.split(".").withIndex()) {
            val l = s.toInt()
            val c = now[i].toInt()
            if (l == c) continue
            return l > c
        }
        return false
    }

    @EventHandler
    fun PlayerJoinEvent.onOpJoinGame() {
        player.sendUpdate()
    }

    private fun Player.sendUpdate() {
        if (!isOp || latestVer == null || !hasNewVer) return
        sendMsgWithPrefix(
            """&f插件有更新哦, 当前版本: &c$nowVer&f, 最新版本: &a$latestVer
                |&f更新发布于:&b $mcbbs
                |&f开源于:&b $github
            """.trimMargin()
        )
    }
}
<div align="center">

# ViewSlimeChunk

> 基于SpigotAPI的史莱姆区块预览插件, 适用于Spigot或Paper以及其他绝大多数Spigot的下游分支核心(不支持Bukkit)
>
> 版本支持：理论上支持`1.13.x`-`1.19.x`, `1.18.x`和`1.19.x`经过测试

[![Release](https://img.shields.io/github/v/release/4o4E/ViewSlimeChunk?label=Release)](https://github.com/4o4E/ViewSlimeChunk/releases/latest)
[![Downloads](https://img.shields.io/github/downloads/4o4E/ViewSlimeChunk/total?label=Download)](https://github.com/4o4E/ViewSlimeChunk/releases)

</div>

## 指令

- `/slime` 查看周围史莱姆区块, 对应权限`viewslimechunk.use`
- `/slimereload` 重载配置文件, 对应权限`viewslimechunk.admin`

## 权限

- `viewslimechunk.use` 允许使用指令`/slime`, 默认为op拥有
- `viewslimechunk.admin` 允许使用指令`/slimereload`, 默认为op拥有

## 配置

> 插件默认配置见[配置文件](src/main/resources/config.yml)，配置文件内均有注释描述用法和含义

## 效果预览

![example](https://user-images.githubusercontent.com/58851040/165770877-cb1ab16e-7ba7-41fc-901c-9c2606d8e55f.png)

## 下载

- [最新版](https://github.com/4o4E/ViewSlimeChunk/releases/latest)

## 更新记录

```
2022.04.28 - 1.0.0 发布插件
2022.04.28 - 1.0.1 添加更新检查, 添加悬浮文本自定义, 修复错误的文本
2022.07.03 - 1.0.2 优化插件，分离配置文件和语言文件
2022.08.18 - 1.0.3 配置文件中添加禁用的世界
2022.12.22 - 1.0.4 修改api version, 更新kotlin版本
```

## bstats

![bstats](https://bstats.org/signatures/bukkit/ViewSlimeChunk.svg)

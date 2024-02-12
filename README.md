# Minecraft ContentPublisher

![Gradle](https://img.shields.io/badge/Gradle-v8%2E5-g?logo=gradle&style=flat-square)
![Zulu JDK](https://img.shields.io/badge/Zulu%20JDK-8-blue?style=flat-square)
![GitHub License](https://img.shields.io/github/license/ideal-state/minecraft-content-publisher?style=flat-square)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/ideal-state/minecraft-content-publisher?style=flat-square&logo=github)
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/ideal-state/minecraft-content-publisher/release.yml?style=flat-square)
![GitHub Release](https://img.shields.io/github/v/release/ideal-state/minecraft-content-publisher?style=flat-square)
![Discord](https://img.shields.io/discord/1191122625389396098?style=flat-square&logo=discord)

------------------------------------------------------

### 在哪下载 ?

> 前往 [releases](https://github.com/ideal-state/minecraft-content-publisher/releases) 页

### 如何构建

```shell
git clone https://github.com/ideal-state/minecraft-content-publisher.git
```

```shell
cd ./minecraft-content-publisher
```

```shell
./gradlew.bat :clean :BungeeCord:jar :Spigot:jar
```

或

```shell
./gradlew :clean :BungeeCord:jar :Spigot:jar
```

### 如何使用

> 1. 首先在命令行中运行 `java -jar ContentPublisher-BungeeCord.jar` 生成全新的 `RSA 密钥对`
> 2. 在 `BungeeCord` 和 `Spigot` 端分别安装对应版本的插件
> 3. 最好先运行 `BungeeCord` 端以保证 `Spigot` 端插件能够连接上 `BungeeCord` 端插件服务
> 4. 出现 `服务端已启动` 或 `客户端已启动` 字样即为完成插件的启动
> 5. 好了，关闭服务端，将 `步骤 1`
     生成的全新密钥分别替换至服务端的插件数据目录（即 `{你的 BungeeCord 服务端路径}/plugins/ContentPublisher-BungeeCord/private`
     和 `{你的 Spigot 服务端路径}/plugins/ContentPublisher-BungeeCord/public`）
> 6. 再次启动服务端即可
>>
提示：需要同步的文件放在 `{你的 BungeeCord 服务端路径}/plugins/ContentPublisher-BungeeCord/{文件命名空间目录（id）}/{需要同步的文件（path）}`
> > `id` 和 `path` 是内容发布和订阅的必要元素，它们至关重要！！！

### 命令帮助

```shell
# 发布内容
/ContentPublisher-BungeeCord publish {文件命名空间 (id)} {文件相对命名空间目录路径 (path)}

# 重载 service.yml 配置（不推荐）
/ContentPublisher-BungeeCord reload
/ContentPublisher-Spigot reload

# 重启 service 服务端/客户端（不推荐）
/ContentPublisher-BungeeCord restart
/ContentPublisher-Spigot restart
```

### 关于开发配置

> [`local.properties`](./local.properties)

### 怎样成为贡献者 ?

在贡献之前，你需要了解相应的规范。[前往查看](https://github.com/ideal-state)


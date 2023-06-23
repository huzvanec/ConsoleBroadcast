# Console Broadcast
## What is Console Broadcast?
It's a simple minecraft plugin for messaging players from console.
## What platforms are supported?
Paper based minecraft servers **1.19.4+** <br>
**Bukkit and Spigot are not supported!**
## Usage
````
/broadcast <selector> <text-message>
````
or
```
/console <selector> <text-message>
```
## Selectors
A valid selector is any online player's name, or ```@everyone``` which will make all the players online recieve the message.
## Message formatting
If the player has ```consolebroadcast.message-formatting``` permission, they can use minimessage formats in their messages. This includes all the colors, bold, italic, underline... If you want to learn more about this format, read https://docs.advntr.dev/minimessage/.
## Examples:
### 1
````
broadcast XD_CZ <rainbow>Hello!</rainbow> <green>Welcome to the server :D<green>
````
#### XD_CZ's POV:
![](https://raw.githubusercontent.com/Mandlemankiller/ConsoleBroadcast/master/screenshots/welcome.png)
### 2
```
console @everyone <bold><red>There is an event happening right now!</red></bold>
```
#### everyone's POV:
![](https://raw.githubusercontent.com/Mandlemankiller/ConsoleBroadcast/master/screenshots/event.png)

## Build
Requirements: [Git](https://git-scm.com/), [Maven](https://maven.apache.org/)<br>
Clone the repository
```bash
git clone https://github.com/Mandlemankiller/ConsoleBroadcast.git
```
Move to the ConsoleBroadcast directory
```bash
cd ConsoleBroadcast
```
Package with Maven
```bash
mvn package
```

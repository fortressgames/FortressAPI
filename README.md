# FortressAPI
Open source Spigot API Plugin for Minecraft `1.19 - 1.19.2`

### Contains:
- Custom command base
- Custom inventory system
- Many utils for hex colors, custom scoreboard teams, mojang API and more
- Custom armorstand class
- Scoreboard manager

```xml
<dependency>
    <groupId>net.fortressgames</groupId>
    <artifactId>plugin</artifactId>
    <version>1.1-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

## GUI Example:
```java
public class TestInventory extends InventoryMenu {

	public TestInventory(FortressPlayer fortressPlayer) {
		super(fortressPlayer, InventoryRows.ROW6, ChatColor.BLUE + "Test inventory");

		setItem(new ItemBuilder(Material.PAPER).name(ChatColor.WHITE + "Hello World").build(), 0, inventoryClickEvent -> {
			fortressPlayer.sendMessage("Hello World");
		});
	}
}
```
```java
new TestInventory(target).openInventory();
```
## Command Example:
```java
public class TestCommand extends CommandBase {

	public TestCommand() {
		super("test", "example.command.test", "t");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage("Hello World");
	}
}
```

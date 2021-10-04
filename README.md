# BukkitCommandSystem

The Bukkit Command System is a port of "Core Command System" to the Bukkit platform. 

The Command System makes making commands  whole lot easier, with automatic tab completion and argument processing, all you need to do is write the actual command. 
You get the argument objects already parsed making it hassle free to develop commands. 

Unlike many other command systems that attempt to update the Bukkits original command system that they still use and has been the same since version 1. This command system doesn't use any NMS or craftbukkit calls meaning that projects such as Glowstone, Cauldron, etc will work without those developers needing to reroute the calls. 


## Features

- Quickly write commands
- Automatic help page
- Automatic permission management for child commands
- Automatic tab completion
- Removes common processing
- Comes with common arguments
- Ability to create custom arguments


## How do it work

The basic idea is that all the parsing is handled within its own class and then passes you the parsed object from that class. The handling of suggestions is also handled within this class too. 

# package

## maven

```xml
<dependency>
  <groupId>org.mose.command</groupId>
  <artifactId>bukkit-command-system</artifactId>
  <version>1.1.0</version>
  <scope>compile</scope>
</dependency>
```

## gradle

``ToDo``

## basic command

This is the implementation for a command which gives a player the specified item argument
```java
package org.mose.command.temp;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.mose.command.ArgumentCommand;
import org.mose.command.CommandArgument;
import org.mose.command.arguments.collection.id.MaterialArgument;
import org.mose.command.context.CommandContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GiveItemCommand implements ArgumentCommand {

    private final MaterialArgument itemArgument = new MaterialArgument("item", Material::isItem); //An argument which provides a material as a argument, this is then limited to materials that are items with the 2nd constructor argument

    @Override
    public @NotNull List<CommandArgument<?>> getArguments() {
        return Collections.singletonList(this.itemArgument); //registers the order of the argument
    }

    @Override
    public @NotNull String getDescription() {
        return "Gives you the provided item"; //description of the command
    }

    @Override
    public @NotNull Optional<String> getPermissionNode() {
        return Optional.empty(); //permission node for this argument, Optional.empty for no permission required (will not be suggested or can be executed if no permission matches)
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) {
        if (!(commandContext.getSource() instanceof Player player)) {
            return false; //stops the command if the source of the command is not a player
        }
        Material material = commandContext.getArgument(this, this.itemArgument); //Gets the material from the command system
        player.getInventory().addItem(new ItemStack(material)); //adds the item to the players inventory
        return true;
    }
}

```

Note how the need to parse the string argument to a Material. This command would also suggest all item materials to the player in ID form. 

In regular Bukkit, this is the same command

```java
package org.mose.command.temp;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TempClass implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)){
            return false;
        }
        
        String peek = strings[strings.length - 1];
        Optional<Material> opMaterial = Arrays.stream(Material.values())
                .filter(Material::isItem)
                .filter(material -> material.getKey().toString().equalsIgnoreCase(peek))
                .findAny();
        if(opMaterial.isEmpty()){
            commandSender.sendMessage("Cannot find item of " + peek);
            return false;
        }
        player.getInventory().addItem(new ItemStack(opMaterial.get()));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        String peek = strings[strings.length - 1];
        return Arrays.stream(Material.values())
                .filter(Material::isItem)
                .map(material -> material.getKey().toString())
                .filter(name -> name.toLowerCase().contains(peek.toLowerCase()))
                .collect(Collectors.toList());
    }
}

```

Note how in regular Bukkit you need to repeate code, one for the execution and the other for the suggestion, also note how the execution line count is much smaller. While it seems like the class is larger in total, this becomes much smaller when comparing a command with child arguments as well as a help child command as there is no additional code to do that in the BukkitCommandSystem while in regular Bukkit, typically you would have a switch statement, then the child command within that switch block with the help as default. As you can imagine, this takes up more lines of code then the BukkitCommandSystem.






# HallegsCommandHandler
A Libary to manage commands for Minecraft Spigot Plugins.

This Libary aim is to make it easier to manage commands an use features like permissions and autocomplete.

Im already using this in my [RecepieBooks](https://github.com/mrhalleg/RecepieBooks) Plugin. Check it out if you need some examples.


# 1 - Intro:

With this Libary you can make "CommandMehtods" which get executed when a Player issues a Command with the right arguments and permissions.
Such a Mehtod can look like this:
~~~
public class TestPluginCommands {
    @PluginCommand(name = "add")
    public static boolean addCommand(CommandSender sender, int arg1 , int arg2) {
        sender.sendMessage(arg1 + " + " + arg2 + " = " + (arg1 + arg2));
        return true;
    }
}
~~~
This method would be executed when a Player issues a command like
~~~
/add 3 4
~~~
Notice that the two numbers at the end automaticly get convertet to int, so when a player issues a command like
~~~
/add a 4
~~~
the first argument cant be convertet to a number so the method would not run.



For a methode to be recognised it must meet the following requirements:
* the methode must be in one of the classes listed in the later arguments of `manage()` as described below.
* the methode must have the `@PluginCommand` Annotation.
* the methode must be **static**.
* the methode must return a **boolean**.
* the first parameter must be a `CommandSender` or `Player`.
* after the first parameter the methode can have none,one or more parameters. They all need a Converter Class.

For the Libary to initialy load all CommandMehtods you need to call the method `CommandManager.manage()` with all Classes containing CommandMehtds as parameters:
~~~
public class TestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        try {
            CommandManager.manage(this, TestPluginCommands.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
~~~
# 2 - indepth
# 2.1 Subcommands
whith many Plugins you may want a command to have subcommands. This can be achieved by using spaces in the `name` argument the `@PluginCommand` Annotation:
~~~
@PluginCommand(name = "calculator add")
public static boolean addCommand(CommandSender sender, int arg1, int arg2) {
    sender.sendMessage(arg1 + " + " + arg2 + " = " + (arg1 + arg2));
    return true;
}

@PluginCommand(name = "calculator sub")
public static boolean subCommand(CommandSender sender, int arg1, int arg2) {
    sender.sendMessage(arg1 + " - " + arg2 + " = " + (arg1- arg2));
    return true;
}
~~~
This will create the command `/calculator` with the two Subcommands `add` and `sub`. There are no limits to how many subcommands you can have and a subcommand can have even more subcommands.

# 2.2 Permissions and OPs
to restrict Players from using your Commands you can use Permissions or make them op Only.
For this the `@PluginCommand` Annotaion has two Parameters:
 - opOnly: if this is true the method can only be accesed by players that are OP. By default all methods are opOnly for security reasons.
 - permission: the mehtod can only be accesed by players that have this permission. If it contains a empty String anyone can acces this mehtod. By default there is no permission set.

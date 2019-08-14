# HallegsCommandHandler
A Libary to manage commands for Minecraft Spigot Plugins.

This Libary aim is to make it easier to manage commands an use features like permissions and autocomplete.

Im already using this in my [RecepieBooks](https://github.com/mrhalleg/RecepieBooks) Plugin. Check it out if you need some examples.


# 1 - How it works:

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



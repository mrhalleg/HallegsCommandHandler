# HallegsCommandHandler
A Libary to manage commands for Minecraft Spigot Plugins.

# How to use:
To begin you need to call `manage(JavaPlugin p, Class<?>... c)` of `CommandManager` in your `onEnable()` method.
The first Argument is just the JavaPlugin, while the later ones are used to hold the methodes for the Commands:
~~~
CommandManager.manage(this,MyCommands.class);
~~~

## Command Methodes
You can create one methode for every Command listed in the **plugin.yml** file.
For a methode to be connected it must meet the following requirements:
* the methode must be in one of the classes listed in the later arguments of `manage()` as described above.
* the methode name must be the same as the command name.
* the methode must have the `@PluginCommand` Annotation.
* the methode must be static.
* the methode must return a boolean.
* the first parameter must be a `CommandSender`.
* after the first parameter the methode can have none,one or more parameters all of witch extend `Argument`.
~~~
class MyCommands{

    @PluginCommand
    public static boolean commandName(CommandSender sender, SomeArgument arg) {
        return true;
    }
}
~~~
The `Arguments` will contain the information given by the player, but can already be processed.
For example can a **string** already be converted to a **int** when thats expected.

The order of the Arguments is the same as the player will have to enter it ingame.
When **true** is returned the command is executed succesfull, if not the usage as specefied in the **plugin.yml** is displayed.

When a Methode has the `@PluginCommand` Annotation but doesnt match one of the other requirements a exception is thrown by `CommandManager.manage()`.

## Arguments
Arguments are used to specefie what arguments are accepted by a command as well manage autocomplete.

### run()
The `Argument run(String s)` mehtode is used to determine if a argument entered by the player is a valid argumend for the command.
When a command is entered the arguments entered by the player are given to the `run()` mehtode of the classes and its return value is then passed down to the command methode.
When **null** is returned the argument entered by the player is invalid and the usage as specefied in the **plugin.yml** is displayed.

### tap()
The `tap(CommandSender sender, String[] args)` methode ist used for auto complete. the list returned by it is circled through when the player hits tap.

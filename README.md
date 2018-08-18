
<<<<<<< HEAD
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
The `Arguments` will contain the information given by the player, can already be converted to the right datatypes so the command methode can focus on the logic of the command.

When a Methode has the `@PluginCommand` Annotation but doesnt match one of the other requirements a exception is thrown by `CommandManager.manage()`.

### @PluginCommand
The Annotation `@PluginCommand` marks methodes that are executing a command, but they also have optional parameters:

## Arguments
Arguments are used to specefie what arguments are accepted by a command as well manage autocomplete.
They can also refine the inputs of the player by converting them into difrent datatypes.

The two methodes of Argument are `run()` and `tab()`:

### run(String[] args)
The `run(String s)` mehtode is used to determine if a argument entered by the player is a valid argumend for the command.
When a command is entered the arguments entered by the player are given to the `run()` mehtode of the classes and its return value is then passed down to the command methode.
When **null** is returned the argument entered by the player is invalid and the usage as specefied in the **plugin.yml** is displayed.

### tab(CommandSender sender , String args)
The `tab(CommandSender sender, String[] args)` methode ist used for auto complete. the list returned by it is circled through when the player hits tab.


=======
>>>>>>> 09a35060342e7ee0286eb09ee83f7f1db77e31da

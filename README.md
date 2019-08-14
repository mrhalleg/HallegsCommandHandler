# HallegsCommandHandler
A Libary to manage commands for Minecraft Spigot Plugins.

This Libary aim is to make it easier to manage commands an use features like permissions and autocomplete

# 1 - Basics:
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
# 2 - Subcommands
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

A Command with Subcommands can stil have normal methods:
~~~
@PluginCommand(name = "calculator")
public static boolean calcCommand(CommandSender sender, int arg1) {
    //duh
    sender.sendMessage(arg1 + " = " + arg1);
    return true;
}
~~~

Now all of the following commands are valid:

`/calculator add 1 1` with output: `1 + 1 = 2`

`/calculator sub 1 1` with output: `1 - 1 = 0`

`/calculator 1` with output: `1 = 1`

# 3 - Permissions and OPs
to restrict Players from using your Commands you can use Permissions or make them op Only.
For this the `@PluginCommand` is used:

~~~
@PluginCommand(name = "calculator", opOnly = true, permission = "calcualor")
public static boolean calcCommand(CommandSender sender, int arg1) {
    sender.sendMessage(arg1 + " = " + arg1);
    return true;
}
~~~

 - opOnly: if this is true the method can only be accesed by players that are OP. By default all methods are opOnly for security reasons.
 - permission: the mehtod can only be accesed by players that have this permission. If it contains a empty String anyone can acces this mehtod. By default there is no permission set.
 
 # 4 - Converter
Converters are manly used to convert user input(Strings) to the correct datatype for the command mehtod. They also handle tap completion and tips when a invalid dommand was issued.

# 4.1 - Default Converter
By Default these Datatypes can be converted:
 - Boolean
 - Double
 - Integer
 - Intem
 - Material
 - Player
 - String

# 4.2 - Custom Converter
To use diffrent Datatypes in your CommandMehtods you need to write your own Converter. A simple Converter my look like this:
 ~~~
 public class BooleanConverter extends Converter<Boolean> {

	@Override
	public Boolean check(CommandSender sender, String string) {
		if (string.equals("true") || string.equals("1")) {
			return true;
		} else if (string.equals("false") || string.equals("0")) {
			return false;
		}
		return null;
	}

	@Override
	public List<String> complete(CommandSender sender) {
		List<String> ret = new LinkedList<>();
		ret.add("true");
		ret.add("false");
		return ret;
	}
}
 ~~~
 
The `check()` mehtod is responsible for the actual conversion. The String argument is the String that is to be convertet. If it cant be converted null is returned.

The `complete()` method is responsible to give options for tap-completion.

# 4.3 - Using Custom Converter
To use your custom Converter you can use the `@UseConverter` or `@SetDefaultConverter` Annotations.

# 4.4 - Converter Parameter

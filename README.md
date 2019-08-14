# HallegsCommandHandler
A Libary to manage commands for Minecraft Spigot Plugins.

This Libary aim is to make it easier to manage commands an use features like permissions and autocomplete.

Im already using this in my [RecepieBooks](https://github.com/mrhalleg/RecepieBooks) Plugin. Check it out if you need some examples.


# 1 - How it works:

For a methode to be connected it must meet the following requirements:
* the methode must be in one of the classes listed in the later arguments of `manage()` as described above.
* the methode must have the `@PluginCommand` Annotation.
* the methode must be **static**.
* the methode must return a **boolean**.
* the first parameter must be a `CommandSender` or `Player`.
* after the first parameter the methode can have none,one or more parameters. They all need a Converter Class.
~~~

@PluginCommand(name = "add")
public static boolean addCommand(CommandSender sender, int arg1 , int arg2) {
	sender.sendMessage(arg1 + " + " + arg2 + " = " + (arg1 + arg2));
    return true;
}

~~~

# 1.1 - Arguments and Converter:

This Libary can automaticly check user input and convert it to the right data type.
In the example above you can see the two last 

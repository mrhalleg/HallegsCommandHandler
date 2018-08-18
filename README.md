# HallegsCommandHandler
A Libary to manage commands for Minecraft Spigot Plugins.

This Libary aim is to make it easier to manage commands an use features like permissions and autocomplete.

Im already using this in my [RecepieBooks](https://github.com/mrhalleg/RecepieBooks) Plugin. Check it out if you need some examples.


# 1 - How it works:
To handle the commands of a plugin a mehtode is created for every command in the **plugin.yml** file.

Such a methode looks like this:
~~~

@PluginCommand
public static boolean commandName(CommandSender sender, SomeArgument arg1 , AnoherArgument arg2) {
    return true;
}

~~~

In order to manage the arguments of a command the class `Argument` is used.

It contains a `run()` function which decides if a string matches the requirements for this type of argument.
If the player made a valid input it returns a instance of its class, if not *null*.

a example of a Argument class which accepts everyting:
~~~
public class SomeArgument extends Argument{

    public SomeArgument(){
        super();
    }

    @Override
    public Argument run(CommandSender sender, String arg){
        return new SomeArgument();
    }
}
~~~

when the player enters the command
~~~
/commandName hello 15
~~~
'*hello*' will be passed to the `run()` methode of a instance of the `SomeArgument` class and '*15*' to `AnotherArgument`.

The return value of these methodes will then be passed as the later two parameters of commandName.

When the second argument of the command is expected to be a *int*, `AnotherArgument` can have a field containing a *int* value:
~~~
public class AnotherArgument extends Argument{
    private int i;
	
    public AnotherArgument{
        super();
	}
	
    public AnotherArgument(int i){
        super();
        this.i = i;
    }

    @Override
    public Argument run(CommandSender sender, String arg){
        try{
            int i = Integer.parseInt("");
            return new AnotherArgument(i);
        }catch (NumberFormatException e){
            return null;
        }
    }
	
    public int getI(){
        return i;
    }
}
~~~
the command mehtode can then acces this like this:
~~~
@PluginCommand
public static boolean commandName(CommandSender sender, SomeArgument arg1 , AntoherArgument arg2) {
	sender.sendMessage("mine is higher: " + arg2.getI()+1);
	return true;
}
~~~
so the player will get this message:
~~~
mine is higher: 16
~~~

This means the command mehtode can focus on the actual command and doesnt have to worry about checking userinput or converting to difrend datatypes.

this was a biref overview for more details see below.

# 2 - How to use:
To begin you need to call `manage(JavaPlugin p, Class<?>... c)` of `CommandManager` in your `onEnable()` method.
The first parameter is just the `JavaPlugin`, while the later ones are used to hold the methodes for the Commands:
~~~
CommandManager.manage(this,MyCommands.class);
~~~

## 2.1 - Command Mehtodes
You can create one methode for every Command listed in the **plugin.yml** file.
For a methode to be connected it must meet the following requirements:
* the methode must be in one of the classes listed in the later arguments of `manage()` as described above.
* the **methode name** must be the same as the command name.
* the methode must have the `@PluginCommand` Annotation.
* the methode must be **static**.
* the methode must return a **boolean**.
* the first parameter must be a `CommandSender` or `Player`.
* after the first parameter the methode can have none,one or more parameters all of witch extend `Argument`.
### 2.1.1 - Parameters
- **Sender**

  The sender is whoever executed the command. When it is of type `Player` the command will not be executable from the console.

- **args**

  The later parameters will contain the information given by the player.
  They can also be given Annotation, for more information see **2.2.2**.

### 2.1.2 - Return Typ
When a Command returns *true* it means it executet sucessfully.

### 2.1.3 - PluginCommand
The Annotation `@PluginCommand` is used to mark methodes that are executing a command, but they also have **optional** parameters:
- **opOnly**
  
  this command can only be executed by a admin.
  > default: true
- **permission**
  
  this command can only be executed by a player with this permission
  > default: "" (none)

## 2.2 - Arguments
If you need a new type of argument which is not covered by the once provided, you can implement your own.

### 2.2.1 - Methodes
The two methodes of Argument are `run()` and `tab()`:

- **run(CommandSender sender, String[] args)**

  The `run(String s)` mehtode is used to determine if a argument entered by the player is a valid argumend for the command.
  When a command is entered the arguments entered by the player are given to the `run()` mehtode of the classes and its return value is then passed down to the Command Mehtode.

  When *null* is returned the argument entered by the player is invalid and the usage as specefied in the **plugin.yml** is displayed.

- **tab(CommandSender sender, String args)**

  The `tab(CommandSender sender, String[] args)` methode ist used for auto complete. the list returned by it is circled through when the player hits tab.
  
  Implementing `tab` is **optional**. There will just be no autocomplete for this argument.

### 2.2.2 - Annotations
If you want to specefie even more what kind of argument you need in your command mehtode you can give its parameters annotations to pass values to the `Argument`.

# 3 - To Do
- make a list of standard arguments (IntegerArgument, PlayerArgument, etc)
- make it easier to implement new arguments
- make a example plugin
- optional arguments

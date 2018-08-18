package commandManagement;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager {
	public static void manage(JavaPlugin plugin, Class<?>... classes) throws Exception {
		for (Class<?> c : classes) {
			Method[] meth = c.getMethods();
			for (Method m : meth) {

				for (Annotation a : m.getAnnotations()) {
					if (a.annotationType() == PluginCommand.class) {
						if (!Modifier.isStatic(m.getModifiers())) {
							throw new Exception("The methode " + m.getName()
									+ " has PluginCommand Annotation but is not static.");
						}
						if (m.getReturnType() != boolean.class) {
							throw new Exception("The methode " + m.getName()
									+ " has PluginCommand Annotation but does not return a boolean value.");
						} else {
							plugin.getCommand(m.getName()).setExecutor(new Executor(m));
						}

					}
				}
			}
		}
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface PluginCommand {
		boolean opOnly() default true;

		String permission() default "";
	}
}

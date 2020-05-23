package modules.command;

import bot.Constants;
import bot.Main;
import com.jagrosh.jdautilities.command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

class HelpBuilder {
	
	private HelpBuilder () {
	}
	
	static MessageEmbed build (CommandExt command) {
		String name = command.getName();
		String newLine = "`\n";
		String title = name.replace(name.charAt(0), name.substring(0, 1).toUpperCase().toCharArray()[0]) + " Help";
		return Main.embed(title,
		                  null,
		                  null,
		                  "**Usage: **`" +
		                  name +
		                  (command.getAliases() == null || command.getAliases().length <= 0
		                   ? ""
		                   : "/" + aliases(command)) +
		                  (command.getArguments() == null || command.getArguments().isEmpty()
		                   ? newLine
		                   : " " + command.getArguments() + newLine) +
		                  "**Description: **`" +
		                  command.description +
		                  newLine +
		                  "**Category: **`" +
		                  command.getCategory().getName() +
		                  newLine +
		                  "**Guild Only: **`" +
		                  command.isGuildOnly() +
		                  newLine +
		                  (command.getCooldown() <= 0 ? "" : "**Cooldown: **`" + command.getCooldown() + newLine) +
		                  (command.getUserPermissions() == null || command.getUserPermissions().length <= 0
		                   ? ""
		                   : "**Required User Permissions: **`" + userPerms(command) + newLine) +
		                  (command.getRequiredRole() == null || command.getRequiredRole().isEmpty()
		                   ? ""
		                   : "**Requires User Role: **`" + command.getRequiredRole() + newLine) +
		                  (command.getChildren() == null || command.getChildren().length <= 0
		                   ? ""
		                   : "**Children Commands: **`" + children(command) + newLine),
		                  Color.CYAN,
		                  Constants.PREFIX + "help for full list of commands",
		                  null,
		                  Constants.HELP_ICON,
		                  null);
	}
	
	private static String aliases (Command command) {
		StringBuilder res = new StringBuilder();
		for (String s : command.getAliases())
			res.append(s).append("/");
		return res.substring(0, res.length() - 1);
	}
	
	private static String userPerms (Command command) {
		StringBuilder res = new StringBuilder();
		for (Permission p : command.getUserPermissions())
			res.append(p.getName()).append(", ");
		return res.substring(0, res.length() - 2);
	}
	
	private static String children (Command command) {
		StringBuilder res = new StringBuilder();
		for (Command c : command.getChildren())
			res.append(c.getName()).append(", ");
		return res.substring(0, res.length() - 2);
	}
}
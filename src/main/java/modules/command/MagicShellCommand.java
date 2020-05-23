package modules.command;

import bot.Constants;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;

import java.util.Random;

public class MagicShellCommand extends CommandExt {
	
	public MagicShellCommand () {
		this.name            = "magicshell";
		this.aliases         = new String[] {"ms"};
		this.arguments       = "<question>";
		this.description     = "Answers your yes/no question!";
		this.usage           = Constants.PREFIX + this.name + " " + arguments;
		this.example         = description.replace(arguments, "will I ever get married?");
		this.category        = FUN;
		this.botPermissions  = new Permission[] {Permission.MESSAGE_READ, Permission.MESSAGE_WRITE};
		this.userPermissions = new Permission[] {Permission.MESSAGE_READ, Permission.MESSAGE_WRITE};
		this.helpBiConsumer  = (event, command) -> event.reply(HelpBuilder.build(this));
		this.ownerCommand    = false;
		this.requiredRole    = null;
		this.guildOnly       = false;
		this.help            = "`" + this.usage + "`" + "\n" + this.description + "\n`" + this.example + "`";
		this.helpBiConsumer  = (event, command) -> event.reply(HelpBuilder.build(this));
	}
	
	@Override
	protected void execute (CommandEvent commandEvent) {
		if (validate(commandEvent))
			commandEvent.reply(Constants.MAGICSHELL[new Random().nextInt(Constants.MAGICSHELL.length)]);
	}
}

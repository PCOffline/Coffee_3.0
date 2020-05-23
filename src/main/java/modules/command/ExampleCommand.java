package modules.command;

import bot.Constants;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;

public class ExampleCommand extends CommandExt {
	
	public ExampleCommand () {
		this.name            = "name";
		this.aliases         = new String[] {"alias"};
		this.arguments       = "[argument]";
		this.description     = "description";
		this.usage           = Constants.PREFIX + this.name + " " + arguments;
		this.example         = description.replace(arguments, "value");
		this.category        = MOD;
		this.botPermissions  = new Permission[] {Permission.MESSAGE_READ};
		this.userPermissions = new Permission[] {Permission.MESSAGE_READ};
		this.helpBiConsumer  = (event, command) -> event.reply(HelpBuilder.build(this));
		this.ownerCommand    = true;
		this.requiredRole    = "role";
		this.guildOnly       = true;
		this.help            = "`" + this.usage + "`" + "\n" + this.description + "\n`" + this.example + "`";
		this.helpBiConsumer  = (event, command) -> event.reply(HelpBuilder.build(this));
	}
	
	@Override
	protected void execute (CommandEvent commandEvent) {
		throw new UnsupportedOperationException();
	}
}

package bot;


import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.util.Objects;
import java.util.regex.Pattern;

public class Main extends ListenerAdapter {
	
	private static JDA jda;
	
	public static void main (String[] args) throws LoginException, InterruptedException {
		CommandClient commandClient = new CommandClientBuilder().setPrefix(Constants.PREFIX)
		                                                        .setOwnerId(Constants.OWNER_ID + "")
		                                                        .useHelpBuilder(true)
		                                                        .setEmojis(Constants.SUCCESS,
		                                                                   Constants.WARNING,
		                                                                   Constants.ERROR)
		                                                        .setActivity(Activity.playing(Constants.GAME))
		                                                        .build();
		
		jda = JDABuilder.create(Secret.TOKEN, GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
		                .addEventListeners(new Main(), commandClient)
		                .setAutoReconnect(true)
		                .build()
		                .awaitReady();
		
		
		Logger logger = LoggerFactory.getLogger(Main.class);
		logger.info("This is my log");
	}
	
	@Override
	public void onMessageReceived (@Nonnull MessageReceivedEvent event) {
		Message message = event.getMessage();
		Member member = event.getMember();
		String content = message.getContentRaw();
		MessageChannel channel = message.getChannel();
		Guild guild = event.getGuild();
		long id = jda.getSelfUser().getIdLong();
		Pattern pattern = Pattern.compile("i\\s+love\\s+<@!?" + id + ">", Pattern.CASE_INSENSITIVE);
		
		
		if (pattern.matcher(content).matches()) {
			channel.sendMessage("Aww, thank you! " + Constants.BLUSH).queue();
			if (member != null)
				guild.addRoleToMember(member, Objects.requireNonNull(guild.getRoleById(Constants.COFFEE_LOVER)))
				     .queue();
		}
	}
}

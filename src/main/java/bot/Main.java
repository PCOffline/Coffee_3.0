package bot;


import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import modules.command.MagicShellCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.Objects;
import java.util.regex.Pattern;

public class Main extends ListenerAdapter {
	
	private static JDA jda;
	private final Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void main (String[] args) throws LoginException, InterruptedException {
		CommandClient commandClient = new CommandClientBuilder().setPrefix(Constants.PREFIX)
		                                                        .setOwnerId(Constants.OWNER_ID + "")
		                                                        .useHelpBuilder(true)
		                                                        .setEmojis(Constants.SUCCESS,
		                                                                   Constants.WARNING,
		                                                                   Constants.ERROR)
		                                                        .setActivity(Activity.playing(Constants.GAME))
		                                                        .addCommand(new MagicShellCommand())
		                                                        .build();
		
		jda = JDABuilder.create(Secret.TOKEN, GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
		                .addEventListeners(new Main(), commandClient)
		                .setAutoReconnect(true)
		                .build()
		                .awaitReady();
	}
	
	public static TextChannel getChannel (long id) {
		return jda.getTextChannelById(id);
	}
	
	@NotNull
	public static MessageEmbed embed (
			@Nullable String title,
			@Nullable String titleURL,
			@Nullable String img,
			@Nullable String content, @NotNull Color color,
			@Nullable String footer, @Nullable String footerURL, @Nullable String thumbnail, @Nullable String author) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(title, titleURL);
		builder.setImage(img);
		builder.setDescription(content);
		builder.setColor(color);
		builder.setFooter(footer, footerURL);
		builder.setThumbnail(thumbnail);
		builder.setAuthor(author);
		return builder.build();
	}
	
	@Override
	public void onMessageReceived (@Nonnull MessageReceivedEvent event) {
		Message message = event.getMessage();
		Member member = event.getMember();
		String content = message.getContentRaw();
		MessageChannel channel = message.getChannel();
		Guild guild = event.getGuild();
		long id = jda.getSelfUser().getIdLong();
		final Pattern coffeeLoverPattern = Pattern.compile("i\\s+love\\s+<@!?" + id + ">", Pattern.CASE_INSENSITIVE);
		
		
		if (coffeeLoverPattern.matcher(content).matches()) {
			channel.sendMessage("Aww, thank you! " + Constants.BLUSH).queue();
			if (member != null)
				guild.addRoleToMember(member, Objects.requireNonNull(guild.getRoleById(Constants.COFFEE_LOVER)))
				     .queue();
		}
	}
	
	@Override
	public void onGuildMemberJoin (@Nonnull GuildMemberJoinEvent event) {
		event.getMember().getUser().openPrivateChannel().queue(channel -> channel.sendMessage(Constants.WELCOME_RULES)
		                                                                         .queue(ignored -> {},
		                                                                                (error -> Objects.requireNonNull(
				                                                                                jda.getTextChannelById(
						                                                                                Constants.GENERAL))
		                                                                                                 .sendMessage(
				                                                                                                 Constants.GUILD_WELCOME)
		                                                                                                 .queue())),
		                                                       throwable -> Objects.requireNonNull(jda.getTextChannelById(
				                                                       Constants.GENERAL))
		                                                                           .sendMessage(Constants.GUILD_WELCOME)
		                                                                           .queue());
	}
}

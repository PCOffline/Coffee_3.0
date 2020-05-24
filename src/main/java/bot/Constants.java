package bot;

public class Constants {
	
	
	// General
	
	public static final String PREFIX = ",";
	public static final long OWNER_ID = 168066189128499200L;
	public static final long BOT_ID = 710050922201350195L;
	public static final String GAME = "Drinking Tea" + " | type ,help";
	public static final long DELETION_DELAY = 5000;
	public static final String BOT_NAME = "Coffee 3.0";
	public static final String GUILD_WELCOME =
			"Welcome to McDucks! Please make sure to read the rules and information at #welcome and choose a region " +
			"at" +
			" #role-select in order to get your roles!";
	public static final long GUILD_ID = 697879886081294396L;
	public static final String WELCOME_RULES = "Welcome to McDucks! Please follow the next rules:" +
	                                           "\n1. **Don't be toxic**. Swearing and cussing is allowed, however, " +
	                                           "racism," +
	                                           " " +
	                                           "direct insults, bullying and toxicity are not welcome here." +
	                                           "\n2. **Your nickname in the server must be the same as in Wynncraft**" +
	                                           "." +
	                                           " Use" +
	                                           " " +
	                                           "the `/nick name` command anywhere to change your nickname in Discord" +
	                                           "." +
	                                           "\n3. **Don't spam**. Invite links, advertisements, malware, NSFW, " +
	                                           "disturbing" +
	                                           " content and repetitive or annoying messages will not be tolerated." +
	                                           "\n4. **Staff's discretion** is permitted which grants staff the " +
	                                           "ability to " +
	                                           "mute, kick, ban and punish in any other way that seems valid at the " +
	                                           "moment, " +
	                                           "even if you haven't directly broke a rule.";
	
	
	// Emotes
	
	public static final String EDIT = "\u270F";
	public static final String ERROR = "\u274c";
	public static final String TIMER = "\u23F2";
	public static final String REACT = "\uD83D\uDE36";
	public static final String DOWNVOTE = "\uD83D\uDC4E";
	public static final String UPVOTE = "\uD83D\uDC4D";
	public static final String SUCCESS = "\u2705";
	public static final String WARNING = "\u26A0";
	public static final String AGREE = "\uD83D\uDC4C";
	public static final String HAMMER = "\uD83D\uDD28";
	public static final String BOOT = "\uD83D\uDC62";
	public static final String MUTE = "\uD83D\uDD07";
	public static final String SPEAKER = "\uD83D\uDD08";
	public static final String BLUSH = "\uD83D\uDE0A";
	
	// Paths
	
	public static final String PLAYERS_PATH = "src/main/resources/players.txt";
	public static final String CONFIG_PATH = "src/main/resources/config.txt";
	public static final String TEST_PATH = "src/main/resources/test.txt";
	
	// Channels
	
	public static final long GENERAL = 697879886081294399L;
	public static final long COFFEE = 710060921694847009L;
	public static final long BOT_SPAM = 698314639120924682L;
	public static final long WELCOME = 698313380066492467L;
	
	// Roles
	
	public static final long LEADER = 698872527468101702L;
	public static final long CHIEF = 698313919609176064L;
	public static final long CAPTAIN = 698313739723866172L;
	public static final long RECRUITER = 698313630592270378L;
	public static final long RECRUIT = 698313543933493298L;
	public static final long EXTERN = 708755578477871175L;
	public static final long COFFEE_LOVER = 710808154148962395L;
	
	// Commands
	
	public static final String[] MAGICSHELL =
			new String[] {"Yes", "Maybe", "Indeed", "No way!", "Never", "For sure!", "In your dreams!"};
	
	// Icons
	
	public static final String HELP_ICON = "https://cdn2.iconfinder.com/data/icons/metro-uinvert-dock/256/Help.png";
	public static final String INFO_ICON =
			"https://www.renuar.co.il/skin/frontend/customtheme/default/images/info-icon.png";
	public static final String WARNING_ICON = "https://encrypted-tbn0.gstatic.com/images?q=tbn" +
	                                          "%3AANd9GcRkMewaPuCSE5fRFwRzHaTQGHabYxKCst81_bp33pdJmLIPMwFx&usqp=CAU";
	public static final String CHECKMARK_ICON = "https://freeiconshop.com/wp-content/uploads/edd/checkmark-flat.png";
	public static final String X_ICON = "https://cdn4.iconfinder.com/data/icons/web-ui-color/128/Close-512.png";
	public static final String ANNOUNCEMENT_ICON =
			"https://www.littlerockallergy.com/static/img/no_announcement_icon.png";
	
	
	private Constants () {
		throw new UnsupportedOperationException();
	}
	
}

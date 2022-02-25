import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.*;
public class BattleBotDriver
{
    public static void main(String ... args) throws LoginException, InterruptedException
    {
        BattleBot bot = new BattleBot();
        bot.runBot();
        //bot.serverSetup();
        /*
        for (Guild gs : guilds)
        {
            if (gs.getName().equals("Bot Lounge")) {
                List<TextChannel> ch = gs.getTextChannelsByName("pubg", true);


                System.out.println(gs.getName());
                System.out.println(ch.size());
                TextChannel cha = ch.get(0);
                cha.sendMessage("This is a test from within").queue();
                System.out.println(cha.getName());
            }
        }
    */
    }

}

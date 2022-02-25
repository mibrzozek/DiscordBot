


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.awt.*;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;
import static yahoofinance.YahooFinance.*;

public class MessageResponder extends ListenerAdapter
{
    private ArrayList<String> positiveResponses;
    private ArrayList<String> negativeResponses;
    private ArrayList<String> jokes;
    private ArrayList<String> commands;

    private ArrayList<String> dumpsterfireSquad;

    private String message, response, guild, userName;
    private Random random;
    private Map<String, GuildCmds> guildMap;

    private BattleBot bot;

    StratMaker pubg;

    public MessageResponder(BattleBot bot)
    {
        jokes = initializeJokes();
        positiveResponses = initializePosResponse();
        negativeResponses = initializeNegResponse();
        commands = initializeCommands();
        dumpsterfireSquad = initializeSquad();

        this.guildMap = bot.getGuildMap();
        this.bot = bot;

        message = "";
        response = "";
        random = new Random();
        pubg = new StratMaker();

        System.out.println("Finished making message responder");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Guild g = event.getGuild();
        message = event.getMessage().getContentDisplay();
        guild = event.getMessage().getTextChannel().getGuild().getName();
        userName = event.getAuthor().getName();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        //System.out.println(event.getAuthor() + " " + event.getMember());

        response = "";

        if(!bot.contains(g))
            bot.addGuild(g);

        guildMap = bot.getGuildMap();
        Set<String> customGuildCmds = guildMap.get(guild).getCustomCmds();
        // Stock commands
        if(message.startsWith(".DEFAULT"))
        {
            response += "```\n";

            response += "\n```";

            event.getTextChannel().sendMessage(response).queue();
        }
        /*
            ! ~~~~~~~~~~~~~~~~ !     STONK COMMANDS    ! ~~~~~~~~~~~~~~~~ !
            ! ~~~~~~~~~~~~~~~~ !                       ! ~~~~~~~~~~~~~~~~ !
            ! ~~~~~~~~~~~~~~~~ !                       ! ~~~~~~~~~~~~~~~~ !
            ! ~~~~~~~~~~~~~~~~ !                       ! ~~~~~~~~~~~~~~~~ !
            ! ~~~~~~~~~~~~~~~~ !                       ! ~~~~~~~~~~~~~~~~ !
            ! ~~~~~~~~~~~~~~~~ !     STONK COMMANDS    ! ~~~~~~~~~~~~~~~~ !
        */
        if(message.startsWith(".manage"))
        {
            response += "```d\n";
            Map<String, Portfolio> usersPortMap = bot.getUsersPortMap(userName);
            if(usersPortMap.keySet().size() < 1)
            {
                response += "You only have one portfolio to manage!\nUse .new to open a new portfolio!";
            }
            else
            {
                String[] splitter = message.split(" ");

                List<String> selectionList = new ArrayList<>(usersPortMap.keySet());

                if(splitter.length == 2) // if there is an argument to .manage, select account
                {
                    String active = selectionList.get(-1 + Integer.parseInt(splitter[1]));
                    bot.setActiveAccount(userName, active);
                    response += "You are now managing account " + active + "!\n";
                }
                else // display all accounts
                {
                    String line = "";
                    int count = 0;
                    for (String s : usersPortMap.keySet())
                    {
                        count++;
                        for(int i = 0; i < 37; i++)
                        {
                            //System.out.println(i);
                            if(i == 0)
                            {
                                line += count + "." + s;
                                i += s.length();
                            }
                            else if(i == 36)
                            {
                                line += formatter.format(usersPortMap.get(s).getPortfolioValue()).toString() + "";
                                response += line + "\n";
                                line = "";
                                i = 36;
                            }
                            else
                            {
                                line += ".";
                            }
                        }
                    }
                }
            }
            response += "\n```";

            event.getTextChannel().sendMessage(response).queue();
        }
        if(message.startsWith(".new"))
        {
            response += "```\n";

            PlayerAccount account = bot.getAccount(userName);

            if(account != null)
            {
                String randomName = bot.getGenerator().getRandomName();
                account.addPortfolio(new Portfolio(randomName));

                response += "You made a new portfolio under the name " + randomName;
            }
            else
            {
                response += "You don't have an account. Use .stonks to set up your account.";
            }
            response += "\n```";

            bot.savePlayerData();
            event.getTextChannel().sendMessage(response).queue();
        }
        if(message.startsWith(".emb"))
        {
            EmbedBuilder eb = new EmbedBuilder();

            eb.setTitle("This is a test", null);
            eb.setColor(Color.BLUE);
            eb.addField("User", "100000", false);
            eb.setDescription("This is the description");

            event.getTextChannel().sendMessage(eb.build()).queue();
        }
        if(message.startsWith(".script"))
        {
            List<String> makePortfolio = new ArrayList<>();
            makePortfolio.add(".stonks");
            makePortfolio.add(".buy vfiax 10");
            makePortfolio.add(".buy amzn 10");
            makePortfolio.add(".buy fb 100");
            makePortfolio.add(".buy amd 100");
            makePortfolio.add(".buy i 1000");
            makePortfolio.add(".sell i 500");
            makePortfolio.add(".stonks");
            makePortfolio.add(".leader");
            for(String s : makePortfolio)
            {
                event.getTextChannel().sendMessage(s).queue();
            }
        }
        if(message.startsWith(".leader"))
        {
            Map<String, Portfolio> pMap = bot.getAllPortfolios();
            List<Double> portVals = new ArrayList<>();
            response += "```d\n";
            String line = "";
            Map<Double, String> reverseMap = new LinkedHashMap<Double, String>();

            for(String s : pMap.keySet())
            {
                portVals.add(pMap.get(s).getPortfolioValue());
                reverseMap.put(pMap.get(s).getPortfolioValue(), s);
            }
            Collections.sort(portVals);
            Collections.reverse(portVals);

            for(Double d : portVals)
            {
                for(int i = 0; i < 37; i++)
                {
                    //System.out.println(i);
                    if(i == 0)
                    {
                        line += reverseMap.get(d);
                        i += reverseMap.get(d).length();
                    }
                    else if(i == 36)
                    {
                        line += formatter.format(pMap.get(reverseMap.get(d)).getPortfolioValue()).toString() + "";
                        response += line + "\n";
                        line = "";
                        i = 36;
                    }
                    else
                    {
                        line += ".";
                    }
                }
            }
            response += "\n```";

            event.getTextChannel().sendMessage(response).queue();
        }
        if(message.startsWith(".sell"))
        {
            System.out.println("im selling before");

            String[] ticker = message.split(" ");
            System.out.println(ticker[0] + "  "+ ticker[1] + "  "+ ticker[2] + "  "+ "  " + ticker.length);
            response += "```d\n";
            if(ticker.length == 3)
            {
                Stock stonk = null;
                try
                {
                    stonk = YahooFinance.get(ticker[1]);

                    System.out.println(stonk.getQuote().getPrice() + " " + ticker[1]);
                    //Portfolio p = bot.getPortfolioOf(userName);
                    //System.out.println(p.getBuyingPower() + "integer " + ticker[2]);
                    // System.out.println(p.buy(ticker[1], Integer.getInteger(ticker[2])));
                } catch (IOException e)
                {
                    e.printStackTrace();
                    response += "The ticker you entered was wrong";
                }
                if(stonk != null)
                {
                    System.out.println(ticker[0] + ticker[1] + ticker[2] + " " + ticker.length + " " + String.valueOf(ticker[2]));

                    Portfolio p = bot.getPortfolioToManage(userName);
                    System.out.println(p.getBuyingPower() + " in prcie not null");

                    boolean truth = p.sell(ticker[1].toLowerCase(), Integer.parseInt(ticker[2]), stonk);

                    if(truth)
                    {
                        response += "Congrats on your sell of " + ticker[2] + " " + ticker[1] + " stonks!\n"
                                +"Buying power : " +formatter.format(p.getBuyingPower()).toString();
                    }
                    else
                    {
                        response += "Something went wrong try again!";
                    }
                    System.out.println(" Will i everhit?");
                }
                else
                {
                    response += "That ticker doesn't exist";
                }
            }
            else
            {
                response += "There was an error in your command";
            }

            response += "\n```";

            System.out.println("im buying after");
            bot.savePlayerData();
            event.getTextChannel().sendMessage(response).queue();
        }
        if(message.startsWith(".buy"))
        {
            System.out.println("im buying before");

            String[] ticker = message.split(" ");
            System.out.println(ticker[0] + "  "+ ticker[1] + "  "+ ticker[2] + "  "+ "  " + ticker.length);

            response += "```d\n";

            if(ticker.length == 3)
            {
                Stock stonk = null;
                try
                {
                    stonk = YahooFinance.get(ticker[1]);
                    System.out.println(stonk.getQuote().getPrice() + " " + ticker[1]);
                    //Portfolio p = bot.getPortfolioOf(userName);
                    //System.out.println(p.getBuyingPower() + "integer " + ticker[2]);
                    // System.out.println(p.buy(ticker[1], Integer.getInteger(ticker[2])));
                } catch (IOException e)
                {
                    e.printStackTrace();
                    response = "The ticker your entered was wrong";
                }
                if(stonk != null)
                {
                    System.out.println(ticker[0] + ticker[1] + ticker[2] + " " + ticker.length + " " + String.valueOf(ticker[2]));

                    Portfolio p = bot.getPortfolioToManage(userName);
                    System.out.println(p.getBuyingPower() + " in prcie not null");

                    boolean truth = p.buy(ticker[1].toLowerCase(), Integer.parseInt(ticker[2]), stonk);

                    if(truth)
                    {
                        response += "Congrats on your purchase of " + ticker[2] + " " + ticker[1] + " stonks!\n"
                        +"Buying power : " + formatter.format(p.getBuyingPower()).toString();
                    }
                    else
                    {
                        response += "You don't have enough cash!";
                    }
                }
                else
                {
                    response = "That ticker doesn't exist";
                }
            }
            else
            {
                response = "There was an error in your command";
            }

            System.out.println("im buying after");

            response += "\n```";
            bot.savePlayerData();
            event.getTextChannel().sendMessage(response).queue();
        }
        if(message.startsWith(".stonks"))
        {
            if(bot.openPortfolioFor(userName))
            {
                response = "You've opened up your new stonk portfolio, happy trading!";
                System.out.println(bot.getPortfolioOf(userName).getBuyingPower());
            }
            else
            {
                response = "These are the stonks you own :\n";
                Portfolio p = bot.getPortfolioToManage(userName);
                Set<String> stonks = p.getOwnedStocks();
                double totalValue = 0;
                String header = "\nTicker\tAmount\tCurrent Price\tTotal";
                int count = header.length();
                response += "```d\n\nYour portfolio " + p.getPortfolioOwner() +
                        "\n\nTicker\tAmount\tPrice\t\tTotal\n";

                for(String s : stonks)
                {
                    try
                    {
                        Stock stonk = YahooFinance.get(s);

                        if(stonk.getQuote().getPrice() != null)
                        {
                            totalValue += (double)p.getAmountOwned(s) * stonk.getQuote().getPrice().doubleValue();
                            String line = "";
                            for(int i = 0; i < 37; i++)
                            {
                                //System.out.println(i);
                                if(i == 0)
                                {
                                    line += s;
                                    i += s.length();
                                }
                                else if(i == 11)
                                {
                                    int total = p.getAmountOwned(s);
                                    line += total;
                                    System.out.println(Integer.toString(total).length());
                                    i += Integer.toString(total).length();
                                }
                                else if(i == 22)
                                {
                                    line += formatter.format(stonk.getQuote().getPrice().doubleValue()).toString();
                                    i += formatter.format(stonk.getQuote().getPrice().doubleValue()).length();
                                }
                                else if(i == 36)
                                {
                                    //Double total = p.getAmountOwned(s) * stonk.getQuote().getPrice().doubleValue();
                                    //response += total.toString() + "\t\n";
                                    Double total = (double)p.getAmountOwned(s) * stonk.getQuote().getPrice().doubleValue();
                                    line += formatter.format(total).toString() + "";

                                    response += line;
                                    i = 36;
                                }
                                else
                                {
                                    line += " ";
                                }
                            }
                            response += "\n";
                        }
                        else
                            response += "That ticker doesn't exist";
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                        response += "Couldn't get that price, try again.";
                    }
                }
                p.updatePortfolio(totalValue);
                response += "\nPortfolio value : " + formatter.format(totalValue).toString();
                response += "\nBuying power    : " + formatter.format(p.getBuyingPower()).toString();
                response += "\nTotal value     : " + formatter.format(p.getBuyingPower() + totalValue).toString();
                response += "```";

            }
            event.getTextChannel().sendMessage(response).queue();
        }
        if(message.startsWith(".price"))
        {
            String[] price = message.split(" ");

            if(price.length == 2)
            {
                try
                {
                    Stock stonk = YahooFinance.get(price[1]);

                    if(stonk.getQuote().getPrice() != null)
                        response = "```d\n" + price[1] + " price \t\t\t\t\t\t" +  formatter.format(stonk.getQuote().getPrice().doubleValue()).toString() +
                                "\n```";
                    else
                        response = "That ticker doesn't exist";

                } catch (IOException e)
                {
                    e.printStackTrace();
                    response = "Couldn't get that price, try again.";
                }
            }
            else
                response = "There was an error in your command";

            event.getTextChannel().sendMessage(response).queue();
        }
        /*
            ! ~~~~~~~~~~~~~~~~ ! CUSTOM GUILD COMMANDS ! ~~~~~~~~~~~~~~~~ !
            ! ~~~~~~~~~~~~~~~~ !                       ! ~~~~~~~~~~~~~~~~ !
            ! ~~~~~~~~~~~~~~~~ !                       ! ~~~~~~~~~~~~~~~~ !
            ! ~~~~~~~~~~~~~~~~ !                       ! ~~~~~~~~~~~~~~~~ !
            ! ~~~~~~~~~~~~~~~~ !                       ! ~~~~~~~~~~~~~~~~ !
            ! ~~~~~~~~~~~~~~~~ ! CUSTOM GUILD COMMANDS ! ~~~~~~~~~~~~~~~~ !
        */
        if(!customGuildCmds.isEmpty() && !message.equals(""))
        {
            for (String s : customGuildCmds)
            {
                System.out.println(" in the loop " + s + " " + customGuildCmds.size());
                if(message.equals(s))
                {
                    response = guildMap.get(guild).getResponseTo(s);
                    event.getTextChannel().sendMessage(response).queue();
                    message = "";
                }
            }
        }
        if(message.startsWith(".guilds"))
        {
            if(bot.getGuildMap().keySet().isEmpty())
            {
                response = "There are no guilds!";
            }
            else
                response = "" + bot.getGuildMap().keySet().toString();

            event.getTextChannel().sendMessage(response).queue();
        }
        if(message.startsWith(".addCmd"))
        {
            String[] newCmd = message.split(" ");
            String[] newResponse = message.split("'");

            System.out.println(newCmd[1]);

            if(!newCmd[1].startsWith("."))
            {
                response = "```Invalid format - to add a command your message must look like this\n " +
                        "'.add .newCmd 'This will be the response when typing .newCmd'```\n" +
                        newCmd[1] + " " + newResponse[1];
            }
            else
            {
                response = newResponse[1];
                bot.addGuildSpecificCmds(guild, newCmd[1], newResponse[1]);
            }
            event.getTextChannel().sendMessage(response).queue();
        }
        /*
            ! ~~~~~~~~~~~~~~~~ ! Guild specific commands ! ~~~~~~~~~~~~~~~~ !
        */
        if(message.startsWith(".pubg") || (message.contains("dumpster") && message.contains("fire")))
        {
            for (int i = 0; i < dumpsterfireSquad.size(); i++)
            {
                response += dumpsterfireSquad.get(i);
                if(i != dumpsterfireSquad.size()-1)
                    response += ", ";
            }
            response += "\n\n Someone has mentioned you by other names.";

            event.getTextChannel().sendMessage(response).queue();
            event.getAuthor().getAvatarId();
        }
        if(message.startsWith(".optIn"))
        {
            if(!dumpsterfireSquad.contains("<@" + event.getAuthor().getId() + ">"))
            {
                dumpsterfireSquad.add("<@" + event.getAuthor().getId() + ">");
                response = "Success!";
            }
            else
                response = "You're on it already!";

            event.getTextChannel().sendMessage(response).queue();
        }
        if(message.startsWith(".optOut"))
        {
            if(dumpsterfireSquad.contains("<@" + event.getAuthor().getId() + ">"))
            {
                dumpsterfireSquad.remove("<@" + event.getAuthor().getId() + ">");
                response = "Success!";
            }
            else
                response = "You weren't on the list.";

            event.getTextChannel().sendMessage(response).queue();
        }
        /*
            ! ~~~~~~~~~~~~~~~~ ! Responses for all guilds ! ~~~~~~~~~~~~~~~~ !
        */
        if(message.startsWith(".name"))
        {
            response += "```\n";
            String[] splits = message.split(" ");

            System.out.println(splits.length + " splits length");

            if(splits.length == 2) //
            {
                if(Integer.parseInt(splits[1]) < 50)
                {
                    for(int i = 0; i < Integer.parseInt(splits[1]); i++)
                    {
                        response += bot.getNameGen().getRandomName() + "\n";
                    }
                }
            }
            else
                response +=  bot.getNameGen().getRandomName();

            response += "\n```";

            event.getTextChannel().sendMessage(response).queue();
        }
        if(message.startsWith(".test!"))
        {
            for (int i = 0; i < commands.size(); i++)
            {
                try
                {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                event.getTextChannel().sendMessage(commands.get(i)).queue();
            }
        }
        if(message.startsWith(".roast"))
        {
            String msg = message.toString();
            String[] split = msg.split(" ");

            event.getTextChannel().sendMessage(response).queue();
        }
        if(message.contains("pubg") && message.contains("fixed"))
        {
            response = "No.";

            event.getTextChannel().sendMessage(response).queue();
        }
        if(message.startsWith(".hello"))
        {
            String name = event.getAuthor().getName();
            response = "Hi " + name + ", how are you doing?";

            event.getTextChannel().sendMessage(response).queue();
        }
        if(message.startsWith(".joke"))
        {
            response = "```\n"
                    + jokes.get(random.nextInt(jokes.size() + 1))
                    + "```\n";

            event.getTextChannel().sendMessage(response).queue();
        }
        if(message.startsWith(".8ball"))
        {
            message = message.substring(6);

            while(message.charAt(0) == ' ')
            {
                message = message.substring(1);
            }
            response = event.getAuthor().getName() + " asks \""
                    + message + "\" ... ";
            if(random.nextInt(2) > 0)
                response += positiveResponses.get(random.nextInt(positiveResponses.size()));
            else
                response += negativeResponses.get(random.nextInt(negativeResponses.size()));

            event.getTextChannel().sendMessage(response).queue();
        }
        if(message.startsWith(".help"))
        {
            response = "```"+
                    "Here's what to type to get me to listen.\n\n" +
                    "Stonk Market Game\n" +
                    "\t.stonks - set up your stonk account \n" +
                    "\t.price amzn - checks price of any ticker\n" +
                    "\t.buy amzn 10 - buy 10 amzn stocks\n" +
                    "\t.sell amzn 10 - sell 10 amzn stocks\n" +
                    "\t.new - opens a new portfolio\n" +
                    "\t.manage - displays numbered list of your portfolios\n" +
                    "\t.manage 1 - selects to manage portfolio 1\n" +
                    "Global Commands\n" +
                    "\t.8ball - Answers a question\n" +
                    "\t.joke - Generates joke\n" +
                    "\t.help - Generates this message\n\nLocal Commands\n" +
                    "\t.addCmd .cmdName 'custom response'"

                    + "```";
            event.getTextChannel().sendMessage(response).queue();
        }
        /*
            ! ~~~~~~~~~~~~~~~~ ! pubg channel Responses ! ~~~~~~~~~~~~~~~~ !

            Strat requests will only be responded to if they are posted to a channel
            containing "pubg" in its name.
        */
    }
    private ArrayList<String> initializePosResponse()
    {
        ArrayList<String> list = new ArrayList();

        list.add("Today's your lucky day");
        list.add("Yes");
        list.add("It seems to be the case");
        list.add("It's not a no");
        list.add("Maybe...");
        list.add("My thoughts are clouded.");

        return list;
    }
    private ArrayList<String> initializeNegResponse()
    {
        ArrayList<String> list = new ArrayList();

        list.add("Not today");
        list.add("Not in your lifetime.");
        list.add("Not anytime soon.");
        list.add("No.");
        list.add("Most likely not.");
        list.add("My judgement is clouded.");

        return list;
    }
    public ArrayList<String> initializeJokes()
    {
        ArrayList<String> jokes =  new ArrayList<String>();

        String j1 = "\nWhy did the chicken cross the street?\n" +
                "To get to the other side!\n";
        String j2 = "\nWhy did the rock cross the street?\n" +
                "It didn't.\n";
        String j3 = "\nWhy did the PUBG player cross the street?\n" +
                "To get the chicken!\n";
        String j4 = "\nWhat's red, white, and black all over?\n" +
                "A penguin with a sunburn!\n";

        jokes.add(j1);
        jokes.add(j2);
        jokes.add(j3);
        jokes.add(j4);

        return jokes;
    }
    public ArrayList<String> initializeCommands()
    {
        ArrayList<String> cmds =  new ArrayList<String>();

        cmds.add("pubg fixed");
        cmds.add(".8ball Will this work?");
        cmds.add(".joke");
        cmds.add(".hello");

        return cmds;
    }
    public ArrayList<String> initializeSquad()
    {
        ArrayList<String> squad =  new ArrayList<String>();

        return squad;
    }
}
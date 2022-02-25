import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import org.postgresql.jdbc.PgDatabaseMetaData;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.sql.*;
import java.util.*;

public class BattleBot implements Serializable
{
    private static List<String> guilds;
    private static Map<String, GuildCmds> guildMap; // for guild cmds
    private static Map<String, PlayerAccount> playerMap; // for

    private static JDA jda;
    private static JDABuilder builder;

    private boolean running = false;
    private NameGenerator nameGen;

    private static String filePath = "C:\\006 SOURCE\\01 JAVA PROJECTS\\02 JAVA PROJECTS\\DiscordBotGC\\resources\\short_names.txt";


    public BattleBot()
    {
        guildMap = new HashMap<>();
        playerMap = new HashMap<>();
        nameGen = new NameGenerator();

        nameGen.setFileToUse(filePath);
        guilds = new ArrayList<>();
/**/
        try
        {
            loadBotData();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        JDASetup();

        /*
        String url = "jdbc:postgresql:///discord-stonk-db?cloudSqlInstance=precise-treat-292121:us-west2:discord-stonk-db&socketFactory=com.google.cloud.sql.postgres.SocketFactory&user=postgres&password=Mnimailp1!\n";
        String username = "postgres";
        String password = "pw";
        Connection c = null;
        try
        {
            c = DriverManager.getConnection(url);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("Connection to database failed ;[");
        }
        */
    }

    public Map<String,PlayerAccount> getPlayerMap()
    {
        return playerMap;
    }

    private void loadBotData() throws IOException
    {
        Map<String, PlayerAccount> bot = null;

        FileInputStream fileInputStream = new FileInputStream("C:\\006 SOURCE\\playerSaveFile");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        try
        {
            bot = (HashMap<String, PlayerAccount>) objectInputStream.readObject();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        objectInputStream.close();

        if(bot != null)
        {
            //this.guildMap = bot.getGuildMap();
            this.playerMap = bot;
            //this.nameGen = bot.getNameGen();
            System.out.println(bot.size());
        }
        else
            System.out.println("Didn't load anythign!");

    }

    public void JDASetup()
    {
        builder = JDABuilder.createDefault("MzcyNjAzNTY5MDQ5OTYwNDQ5.DNGluA.KJNm9kC7H_OHwfq-hrnqPTTC5Rk");
        builder.setToken("MzcyNjAzNTY5MDQ5OTYwNDQ5.DNGluA.KJNm9kC7H_OHwfq-hrnqPTTC5Rk");
        builder.setActivity(Activity.playing("the game"));
        builder.addEventListeners(new MessageResponder(this));

        try
        {
            jda =  builder.build();
        } catch (LoginException e)
        {
            e.printStackTrace();
        }
    }
    public void savePlayerData()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(new File("D:\\06 SOURCE\\playerSaveFile"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(playerMap);
            oos.close();
            fos.close();

            System.out.println("Save successful ;)");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Save failed");
        }
    }
    public NameGenerator getNameGen()
    {
        return nameGen;
    }

    public boolean contains(Guild g)
    {
        if(guildMap.containsKey(g.getName()))
            return true;
        else
            return false;
    }
    public void addGuild(Guild g)
    {
        if(!guildMap.containsKey(g.getName()))
            guildMap.put(g.getName(), new GuildCmds(g.getName()));
    }
    public void runBot()
    {
        running = true;
        System.out.println("Finished making bot");

    }
    public void serverSetup()
    {
        Connection c = null;
        try // open database
        {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/postgres",
                            "postgres", "Mnimailp1!");
        } catch (Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database  :]");
        try
        {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/postgres",
                            "postgres", "Mnimailp1!");
            DatabaseMetaData meta = c.getMetaData();

            ResultSet res = meta.getTables(null, null, "users", new String[] {"TABLE"});
            //System.out.println(res.getString("TABLE_NAME"));
            if(res.next())
            {
                Statement stmt = c.createStatement();

                String sql = "drop table if exists users;\n" +
                             "drop table if exists portfolios;";
                stmt.executeUpdate(sql);

                sql = "CREATE TABLE USERS(" +
                        " USER_ID        INT        NOT NULL," +
                        " GUILD_ID       CHAR(50)   NOT NULL, " +
                        " BUYING_POWER   INTEGER    NOT NULL," +
                        " PRIMARY        KEY (USER_ID));";
                stmt.executeUpdate(sql);

                sql = "CREATE TABLE PORTFOLIOS " +
                        "(PORTFOLIO_ID   INT        NOT NULL," +
                        " PORTFOLIO_NAME CHAR(50)   NOT NULL, " +
                        " STOCKS_OWNED   INTEGER    NOT NULL," +
                        " PRIMARY KEY(PORTFOLIO_ID))";
                        //" CONSTRAINT FK_USERS" +
                        //" FOREIGN KEY (USER_ID)" +
                       // " REFERENCES USERS(USER_ID));";
                stmt.executeUpdate(sql);

                //sql = "drop table if exists users;\n" +
                //        "drop table if exists portfolios;";
                stmt.executeUpdate(sql);

                stmt.close();
                c.close();
                System.out.println("\tCreated tables");
            }
            else
            {
                System.out.println("\tTables exist already");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void insertIntoDatabase()
    {

    }
    public void addGuildSpecificCmds(String guild, String newCmd, String newRespone)
    {
        if(guildMap.containsKey(guild))
        {
            guildMap.get(guild).addCmdForSpecificGuild(newCmd, newRespone);
        }
    }
    public Map<String, Portfolio> getAllPortfolios()
    {
        Map<String, Portfolio> portfolioMap = new HashMap<>();

        for(String s : playerMap.keySet())
        {
            for(Portfolio p : playerMap.get(s).getAllPortfolios())
            {
                portfolioMap.put(p.getPortfolioOwner(), p);
            }
        }
        return portfolioMap;
    }
    public boolean openPortfolioFor(String userName)
    {
        if(!playerMap.containsKey(userName))
        {
            playerMap.put(userName, new PlayerAccount(new Portfolio(userName)));
            return true;
        }
        else
        {
            return false;
        }
    }
    public Map<String, Portfolio> getUsersPortMap(String userName)
    {
        return playerMap.get(userName).getUsersPortMap();
    }
    public Portfolio getPortfolioToManage(String userName)
    {
        return playerMap.get(userName).getSelectedPortfolio();
    }
    public Portfolio getPortfolioOf(String userName)
    {
        return playerMap.get(userName).getMainPortfolio();
    }
    public void endBot()
    {
        running =  false;
    }

    public Map getGuildMap()
    {
        return guildMap;
    }

    public PlayerAccount getAccount(String userName)
    {
        return playerMap.get(userName);
    }

    public NameGenerator getGenerator() {
        return nameGen;
    }

    public void setActiveAccount(String mainAccount, String activeAccount)
    {
        this.playerMap.get(mainAccount).selectPortfolio(activeAccount);
    }
}

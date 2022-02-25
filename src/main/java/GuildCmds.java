import java.util.*;

public class GuildCmds
{
    private String guildName;
    private List<String> guildSpecificCmds;
    private Map<String, String> guildCmdResponse;

    public GuildCmds(String name)
    {
        guildName = name;
        guildSpecificCmds = new ArrayList<String>();
        guildCmdResponse = new HashMap<>();
    }
    public String getResponseTo(String s)
    {
        return guildCmdResponse.get(s);
    }
    public Set<String> getCustomCmds()
    {
        return guildCmdResponse.keySet();
    }
    public void addCmdForSpecificGuild(String newCmd, String newResponse)
    {
            if(!guildCmdResponse.containsKey(newCmd)) // if custom cmd list for guild is empty
            {
                guildCmdResponse.put(newCmd, newResponse);
            }
            else
            {
                guildCmdResponse.put(newCmd, newResponse);
            }
        }

}

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerAccount implements Serializable
{
    private String name, guild, portfolioSelected;
    private Portfolio mainPort;
    private List<Portfolio> portfolios;
    private Map<String, Portfolio> portMap;

    public PlayerAccount(Portfolio port)
    {
        this.portMap = new HashMap<>();

        this.name = port.getPortfolioOwner();
        this.portfolioSelected = name;
        this.mainPort = port;
        this.portfolios = new ArrayList<>();

        portfolios.add(port);
        portMap.put(name, port);
    }

    public Map getUsersPortMap() {return portMap;}
    public Portfolio getSelectedPortfolio()
    {
        return portMap.get(portfolioSelected);
    }
    public void selectPortfolio(String portName)
    {
        portfolioSelected = portName;
    }
    public Portfolio getSpecificPortfolio(String portfolioName)
    {
        return portMap.get(portfolioName);
    }
    public Portfolio getMainPortfolio()
    {
        return mainPort;
    }
    public void addPortfolio(Portfolio p)
    {
        this.portfolios.add(p);
        this.portMap.put(p.getPortfolioOwner(), p);
    }
    public void removePortfolio(String portName)
    {

    }
    public List<Portfolio> getAllPortfolios()
    {
        return portfolios;
    }
}

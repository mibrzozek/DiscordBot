import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Portfolio implements Serializable
{
    private double buyingPower, portfolioValue;

    private Map<String, Integer> stocksOwned;
    private String userName;

    public Portfolio(String userName)
    {
        this.userName = userName;
        this.buyingPower = 100000.00;
        this.stocksOwned = new HashMap<>();
    }
    public Set getOwnedStocks()
    {
        return stocksOwned.keySet();
    }
    public int getAmountOwned(String ticker)
    {
        return stocksOwned.get(ticker);
    }
    public void updatePortfolio(double val)
    {
        this.portfolioValue = val;
    }
    public double getBuyingPower()
    {
        return buyingPower;
    }
    public boolean sell(String ticker, int amount, Stock stonk)
    {
        System.out.println(ticker + " " + amount + " inside sell");

        if(stocksOwned.containsKey(ticker))
        {
            if(amount <= stocksOwned.get(ticker))
            {
                if(stocksOwned.get(ticker) == amount)
                {
                    stocksOwned.remove(ticker);
                    buyingPower += stonk.getQuote().getPrice().doubleValue() * (double)amount;
                    return true;
                }
                else
                {
                    stocksOwned.put(ticker, stocksOwned.get(ticker) - amount);
                    buyingPower += stonk.getQuote().getPrice().doubleValue() * (double)amount;
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
    public boolean buy(String ticker, int amount, Stock stonk)
    {
        System.out.println(ticker + " " + amount + " inside buy");

        if(stocksOwned.containsKey(ticker))
        {
            if(buyingPower > stonk.getQuote().getPrice().doubleValue() * (double)amount)
            {
                stocksOwned.put(ticker, stocksOwned.get(ticker) + amount);
                buyingPower -= stonk.getQuote().getPrice().doubleValue() * (double)amount;
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            if(buyingPower > stonk.getQuote().getPrice().doubleValue() * (double)amount)
            {
                stocksOwned.put(ticker, amount);
                buyingPower -= stonk.getQuote().getPrice().doubleValue() * (double)amount;
                return true;
            }
            else
            {
                return false;
            }
        }
    }
    public double getPortfolioValue()
    {
        double value = 0;

        for(String s : stocksOwned.keySet())
        {
            Stock stonk = null;
            try
            {
                stonk = YahooFinance.get(s);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            if(stonk!= null)
            {
                value += stonk.getQuote().getPrice().doubleValue() * stocksOwned.get(s);
            }
        }
        portfolioValue = value;
        return portfolioValue;
    }

    public String getPortfolioOwner()
    {
        return userName;
    }
}

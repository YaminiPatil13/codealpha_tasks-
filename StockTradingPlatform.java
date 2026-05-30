import java.util.ArrayList;
import java.util.Scanner;

class Stock {
    private String symbol;
    private String companyName;
    private double price;

    public Stock(String symbol, String companyName, double price) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
    }

    public String getSymbol () {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getPrice() {
        return price;
    }

    public void displayStock() {
        System.out.println(symbol + " - " + companyName + " | Price: Rs. " + price);
    }
}

class Holding {
    private Stock stock;
    private int quantity;

    public Holding(Stock stock, int quantity) {
        this.stock = stock;
        this.quantity = quantity;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public boolean removeQuantity(int quantity) {
        if (this.quantity >= quantity) {
            this.quantity -= quantity;
            return true;
        }
        return false;
    }

    public double getValue() {
        return stock.getPrice() * quantity;
    }

    public void displayHolding() {
        System.out.println(stock.getSymbol() + " - " + stock.getCompanyName()
                + " | Quantity: " + quantity
                + " | Current Value: Rs. " + getValue());
    }
}

class Transaction {
    private String type;
    private String stockSymbol;
    private int quantity;
    private double amount;

    public Transaction(String type, String stockSymbol, int quantity, double amount) {
        this.type = type;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.amount = amount;
    }

    public void displayTransaction() {
        System.out.println(type + " | Stock: " + stockSymbol
                + " | Quantity: " + quantity
                + " | Amount: Rs. " + amount);
    }
}

class Portfolio {
    private ArrayList<Holding> holdings;
    private ArrayList<Transaction> transactions;
    private double balance;

    public Portfolio(double balance) {
        this.balance = balance;
        holdings = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void buyStock(Stock stock, int quantity) {
        double cost = stock.getPrice() * quantity;

        if (cost > balance) {
            System.out.println("Insufficient balance.");
            return;
        }

        balance -= cost;

        Holding holding = findHolding(stock.getSymbol());

        if (holding == null) {
            holdings.add(new Holding(stock, quantity));
        } else {
            holding.addQuantity(quantity);
        }

        transactions.add(new Transaction("BUY", stock.getSymbol(), quantity, cost));
        System.out.println("Stock bought successfully.");
    }

    public void sellStock(String symbol, int quantity) {
        Holding holding = findHolding(symbol);

        if (holding == null) {
            System.out.println("You do not own this stock.");
            return;
        }

        if (!holding.removeQuantity(quantity)) {
            System.out.println("Not enough quantity to sell.");
            return;
        }

        double amount = holding.getStock().getPrice() * quantity;
        balance += amount;

        transactions.add(new Transaction("SELL", symbol, quantity, amount));
        System.out.println("Stock sold successfully.");

        if (holding.getQuantity() == 0) {
            holdings.remove(holding);
        }
    }

    public void showPortfolio() {
        System.out.println("\n===== Portfolio =====");
        System.out.println("Available Balance: Rs. " + balance);

        if (holdings.isEmpty()) {
            System.out.println("No stocks in portfolio.");
            return;
        }

        double totalValue = 0;

        for (Holding holding : holdings) {
            holding.displayHolding();
            totalValue += holding.getValue();
        }

        System.out.println("Total Stock Value: Rs. " + totalValue);
        System.out.println("Total Portfolio Value: Rs. " + (balance + totalValue));
    }

    public void showTransactions() {
        System.out.println("\n===== Transaction History =====");

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        for (Transaction transaction : transactions) {
            transaction.displayTransaction();
        }
    }

    private Holding findHolding(String symbol) {
        for (Holding holding : holdings) {
            if (holding.getStock().getSymbol().equalsIgnoreCase(symbol)) {
                return holding;
            }
        }
        return null;
    }
}

class Market {
    private ArrayList<Stock> stocks;

    public Market() {
        stocks = new ArrayList<>();

        stocks.add(new Stock("TCS", "Tata Consultancy Services", 3800));
        stocks.add(new Stock("INFY", "Infosys", 1500));
        stocks.add(new Stock("RELIANCE", "Reliance Industries", 2900));
        stocks.add(new Stock("HDFC", "HDFC Bank", 1600));
        stocks.add(new Stock("WIPRO", "Wipro", 500));
    }

    public void showStocks() {
        System.out.println("\n===== Stock Market =====");
        for (Stock stock : stocks) {
            stock.displayStock();
        }
    }

    public Stock findStock(String symbol) {
        for (Stock stock : stocks) {
            if (stock.getSymbol().equalsIgnoreCase(symbol)) {
                return stock;
            }
        }
        return null;
    }
}

public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Market market = new Market();
        Portfolio portfolio = new Portfolio(100000);

        int choice;

        do {
            System.out.println("\n===== Stock Trading Platform =====");
            System.out.println("1. View Market Stocks");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transaction History");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    market.showStocks();
                    break;

                case 2:
                    market.showStocks();
                    System.out.print("Enter stock symbol to buy: ");
                    String buySymbol = sc.next();

                    Stock buyStock = market.findStock(buySymbol);

                    if (buyStock == null) {
                        System.out.println("Invalid stock symbol.");
                        break;
                    }

                    System.out.print("Enter quantity: ");
                    int buyQty = sc.nextInt();

                    portfolio.buyStock(buyStock, buyQty);
                    break;

                case 3:
                    System.out.print("Enter stock symbol to sell: ");
                    String sellSymbol = sc.next();

                    System.out.print("Enter quantity: ");
                    int sellQty = sc.nextInt();

                    portfolio.sellStock(sellSymbol, sellQty);
                    break;

                case 4:
                    portfolio.showPortfolio();
                    break;

                case 5: 
                    portfolio.showTransactions();
                    break;

                case 6:
                    System.out.println("Thank you for using Stock Trading Platform.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 6);

        sc.close();
    }
}

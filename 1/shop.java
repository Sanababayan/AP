import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Main {
    public static int getDecimal(String hex){
        String digits = "0123456789ABCDEF";
        hex = hex.toUpperCase();
        int val = 0;
        for (int i = 0; i < hex.length(); i++)
        {
            char c = hex.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }
    static class Customer {
        public int id;
        public String name;
        public int balance;
        public ArrayList<Integer> CartIDs;
        public Customer(int id , String name) {
            this.name = name;
            this.id = id;
            CartIDs = new ArrayList<>();
            this.balance = 0;
        }
    }
    static class Product {
        public int id;
        public String category;
        public String name;
        public int price;
        public int count;
        float rate;
        int rateCount;
        ArrayList<String> comments = new ArrayList<>();
        ArrayList<String> details = new ArrayList<>();
        public Product(int id , String name ,  String category , int price) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.price = price;
            this.count = 0;
            this.rate = 0;
            this.rateCount = 0;
        }
        public void increaseInvetory(int count) {
            this.count = this.count + count;
        }
        public void setDetails(String detail) {
            this.details.add(detail);
        }
        public void setComments(String comment) {
            this.comments.add(comment);
        }
    }
    public static void main(String[] args) {
        HashMap<Integer, Customer> customerId = new HashMap<>();
        HashMap<Integer, Product> commodityId = new HashMap<>();
        ArrayList<String> category = new ArrayList<>();
        ArrayList<Integer> kalahayeAfzode = new ArrayList<>();

        Pattern addCustomerPattern = Pattern.compile("^\\s*add\\s+customer\\s+(?<IDNumber>\\d+)\\s+(?<name>\\S+)\\s*$");
        Pattern addCategoryPattern = Pattern.compile("^\\s*add\\s+category\\s+(?<name>\\S+)\\s*$");
        Pattern addCommodityPattern = Pattern.compile("^\\s*add\\s+commodity\\s+(?<IDNumber>\\d+)\\s+(?<category>\\S+)\\s+(?<name>\\S+)\\s+(?<price>[0-9]+)(\\s+\\?detail (\\{(?<details>.*)}))?\\s*$");
        Pattern increaseCommodity = Pattern.compile("^\\s*increase\\s+inventory\\s+(?<id>\\d+)\\s+(?<amount>\\d+)\\s*$");
        Pattern changeBalance = Pattern.compile("^\\s*change\\s+balance\\s+(?<id>\\d+)\\s+(?<amount>-?\\d+)\\s*$");
        Pattern filterCategory = Pattern.compile("^\\s*filter\\s+commodity\\s+(?<category>\\S+)\\s+\\(0x(?<lowerBound>[0-9A-F]+)\\)\\s+to\\s+\\(0x(?<upperBound>[0-9A-F]+)\\)\\s*$");
        Pattern comInfo = Pattern.compile("^\\s*commodity\\s+info\\s+(?<id>\\d+)\\s*$");
        Pattern cusInfo = Pattern.compile("^\\s*customer\\s+info\\s+(?<id>\\d+)\\s*$");
        Pattern addToCart = Pattern.compile("^\\s*add\\s+to\\s+cart\\s+(?<customerID>\\d+)\\s+(?<commodities>(\\d+\\s*,?\\s*)+)\\s*$");
        Pattern buy = Pattern.compile("^\\s*buy\\s+commodity\\s+(?<customerId>\\d+)\\s+(?<commodityId>\\d+)\\s+(?<count>\\d+)\\s*(\\?comment\\s+\\[(?<comment>.+)]\\s*)?(\\?rate\\s+\\((?<rate>[0-5])\\)\\s*)?$");

        Scanner scanner = new Scanner(System.in);
        String startCommand = scanner.nextLine();
        while(! Pattern.matches("^\\s*start\\s*$", startCommand)) {
            startCommand = scanner.nextLine();
        }
        Matcher matcher;
        while(true) {
            String input = scanner.nextLine();
            matcher = addCustomerPattern.matcher(input);
            if(matcher.matches()) {
                int id = Integer.parseInt(matcher.group("IDNumber"));
                if(customerId.containsKey(id)) {
                    System.out.println("The id already exists");
                }else {
                    String name = matcher.group("name");
                    customerId.put(id , new Customer(id , name));
                }
                continue;
            }
            matcher = addCategoryPattern.matcher(input);
            if(matcher.matches()) {
                String name = matcher.group("name");
                if(Pattern.matches("[a-zA-Z]+[0-9]*" , name)) {
                    if(category.contains(name)) {
                        System.out.println("The category already exists");
                    }
                    else {
                        category.add(name);
                    }
                    continue;
                }
                else if(! Pattern.matches("[a-zA-Z0-9]+" , name)) {
                    System.out.println("Category format is invalid");
                    continue;
                }
            }
            matcher = addCommodityPattern.matcher(input);
            if(matcher.matches()) {
                int id = Integer.parseInt(matcher.group("IDNumber"));
                if(commodityId.containsKey(id)) {
                    System.out.println("The id already exists");
                    continue;
                }
                else {
                    String categoryname = matcher.group("category");
                    if(!category.contains(categoryname)) {
                        System.out.println("The category does not exist");
                        continue;
                    }
                    else {
                        int price = Integer.parseInt(matcher.group("price"));
                        String name = matcher.group("name");
                        commodityId.put(id , new Product(id , name , categoryname , price));
                        kalahayeAfzode.add(id);
                        if(matcher.group("details") != null) {
                            String[] details = matcher.group("details").split("}\\{");
                            for(String detail : details) {
                                commodityId.get(id).setDetails(detail);
                            }
                        }
                    }
                }
                continue;
            }
            matcher = addToCart.matcher(input);
            if(matcher.matches()) {
                int customerIdd = Integer.parseInt(matcher.group("customerID"));
                if(! customerId.containsKey(customerIdd)) {
                    System.out.println("Customer's id does not exist");
                    continue;
                }
                String[] kalaha = matcher.group("commodities").split("\\s*,\\s*");
                for(String kala : kalaha) {
                    int commodityIdd = Integer.parseInt(kala);
                    if(! commodityId.containsKey(commodityIdd)) {
                        System.out.println("Id " + commodityIdd + " does not exist");
                    }
                    else if(customerId.get(customerIdd).CartIDs.contains(commodityIdd)){
                        System.out.println("Id " + commodityIdd + " has already been added");
                    }
                    else {
                        customerId.get(customerIdd).CartIDs.add(commodityIdd);
                    }
                }
                continue;
            }
            matcher = increaseCommodity.matcher(input);
            if(matcher.matches()) {
                int id = Integer.parseInt(matcher.group("id"));
                if(! commodityId.containsKey(id)) {
                    System.out.println("The id does not exist");
                }
                else {
                    int amount = Integer.parseInt(matcher.group("amount"));
                    commodityId.get(id).increaseInvetory(amount);
                }
                continue;
            }
            matcher = changeBalance.matcher(input);
            if(matcher.matches()) {
                int id = Integer.parseInt(matcher.group("id"));
                if(! customerId.containsKey(id)) {
                    System.out.println("The id does not exist");
                }
                else {
                    int amount = Integer.parseInt(matcher.group("amount"));
                    customerId.get(id).balance += amount;
                }
                continue;
            }
            matcher = filterCategory.matcher(input);
            if(matcher.matches()) {
                String categoryy = matcher.group("category");
                if( !category.contains(categoryy)) {
                    System.out.println("The category does not exist");
                }
                else {
                    int lowerBound = getDecimal(matcher.group("lowerBound"));
                    int upperBound = getDecimal(matcher.group("upperBound"));
                    if(lowerBound >= upperBound) {
                        System.out.println("Invalid bounds");
                    }else {
                        int counter = 1;
                        for (int i = 0 ; i < kalahayeAfzode.size() ; i++) {
                            if (commodityId.get(kalahayeAfzode.get(i)).price < upperBound && commodityId.get(kalahayeAfzode.get(i)).price > lowerBound) {
                                if(commodityId.get(kalahayeAfzode.get(i)).category.equals(categoryy)) {
                                    System.out.println(counter + "." + commodityId.get(kalahayeAfzode.get(i)).name);
                                    counter++;
                                }
                            }
                        }
                    }
                }
                continue;
            }
            matcher = buy.matcher(input);
            if(matcher.matches()) {
                int customerIdd = Integer.parseInt(matcher.group("customerId"));
                int commodityIdd = Integer.parseInt(matcher.group("commodityId"));
                if(! customerId.containsKey(customerIdd)) {
                    System.out.println("The customer's id does not exist");
                }else if(! commodityId.containsKey(commodityIdd)){
                    System.out.println("The commodity's id does not exist");
                }else if(! customerId.get(customerIdd).CartIDs.contains(commodityIdd)) {
                    System.out.println("Customer's cart does not include this commodity");
                }else {
                    int count = Integer.parseInt(matcher.group("count"));
                    if(count*commodityId.get(commodityIdd).price > customerId.get(customerIdd).balance) {
                        System.out.println("Balance is not enough");
                    }else if(count > commodityId.get(commodityIdd).count) {
                        System.out.println("Insufficient stock");
                    }else {
                        commodityId.get(commodityIdd).count -= count;
                        customerId.get(customerIdd).balance -= count*commodityId.get(commodityIdd).price;
                        customerId.get(customerIdd).CartIDs.removeAll(Collections.singleton(commodityIdd));
                        if (matcher.group("rate") != null)
                        {
                            commodityId.get(commodityIdd).rate = (commodityId.get(commodityIdd).rate * commodityId.get(commodityIdd).rateCount + Integer.parseInt(matcher.group("rate"))) / (commodityId.get(commodityIdd).rateCount + 1);
                            commodityId.get(commodityIdd).rateCount++;
                        }
                        if (matcher.group("comment") != null)
                            commodityId.get(commodityIdd).setComments(matcher.group("comment"));
                    }
                }
                continue;
            }
            matcher = comInfo.matcher(input);
            if(matcher.matches()) {
                int id = Integer.parseInt(matcher.group("id"));
                if(! commodityId.containsKey(id)) {
                    System.out.println("The id does not exist");
                }
                else {
                    System.out.println("category : " + commodityId.get(id).category);
                    System.out.println("name : " + commodityId.get(id).name);
                    System.out.println("price : " + commodityId.get(id).price);
                    System.out.println("count : " + commodityId.get(id).count);
                    for (int i = 0; i < commodityId.get(id).details.size(); i++)
                    {
                        System.out.println("detail : " + commodityId.get(id).details.get(i));
                    }
                    System.out.printf("rate : %.2f\n", commodityId.get(id).rate);
                    for (int i = 0; i < commodityId.get(id).comments.size(); i++)
                    {
                        System.out.println("comment : " + commodityId.get(id).comments.get(i));
                    }
                }
                continue;
            }
            matcher = cusInfo.matcher(input);
            if(matcher.matches()) {
                int id = Integer.parseInt(matcher.group("id"));
                if(! customerId.containsKey(id)) {
                    System.out.println("The id does not exist");
                }else {
                    System.out.println("name : " + customerId.get(id).name);
                    System.out.println("balance : " + customerId.get(id).balance);
                    for (int i = 0; i < customerId.get(id).CartIDs.size(); i++)
                    {
                        System.out.println((i + 1) + ".commodity : " + commodityId.get(customerId.get(id).CartIDs.get(i)).name);
                    }
                }
                continue;
            }
            if (input.equals("end"))
                return;
            System.out.println("Invalid command");
        }
    }
}

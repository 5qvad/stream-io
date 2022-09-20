import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Basket {

    private String[] products;
    private long[] prices;

    private int[] cart;
    private ClientLog clientLog;

    private static List<String> settingsFromXml;

    // переменные для настройки загрузки, сохранения и логирования
    private static boolean loadFromFile;
    private static String loadFileName;
    private static String typeLoadFile;
    private static boolean saveForFile;
    private static String saveFileName;
    private static String typeSaveFile;
    private static boolean iLog;
    private static String logFileName;

    public ClientLog getClientLog() {
        return clientLog;
    }

    public static boolean isLoadFromFile() {
        return loadFromFile;
    }

    public static String getLoadFileName() {
        return loadFileName;
    }

    public static String getTypeLoadFile() {
        return typeLoadFile;
    }

    public static boolean isSaveForFile() {
        return saveForFile;
    }

    public static String getSaveFileName() {
        return saveFileName;
    }

    public static String getTypeSaveFile() {
        return typeSaveFile;
    }

    public static boolean isiLog() {
        return iLog;
    }

    public static String getLogFileName() {
        return logFileName;
    }

    public Basket(String[] products, long[] prices) {
        this.products = products;
        this.prices = prices;
        cart = new int[products.length];
        clientLog = new ClientLog();
    }

    public Basket() {
    }

    public String[] getProducts() {
        return products;
    }

    public long[] getPrices() {
        return prices;
    }

    public int[] getCart() {
        return cart;
    }

    public void addToCart(int productNum, int amount) {
        cart[productNum] += amount;
        this.clientLog.log(productNum, amount);
    }

    public void printCart() {
        System.out.println("Список покупок:");
        for (int i = 0; i < getCart().length; i++) {
            if (getCart()[i] > 0) {
                System.out.println(getProducts()[i] + " - " + getCart()[i] + " шт. стоимостью " + getCart()[i] * getPrices()[i] + " руб.");
            }
        }
    }

    public void saveTxt(String fileName) {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            for (int item : getCart()) {
                writer.print(item + " ");
            }
            writer.print("\n");
            for (String product : getProducts()) {
                writer.print(product + " ");
            }
            writer.print("\n");
            for (long price : getPrices()) {
                writer.print(price + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveForJson(String fileName) {

        JSONObject obj = new JSONObject();
        JSONArray listProduct = new JSONArray();
        for (String product : products) {
            listProduct.add(product);
        }
        obj.put("products", listProduct);

        JSONArray listPrices = new JSONArray();
        for (long price : prices) {
            listPrices.add(price);
        }
        obj.put("prices", listPrices);

        JSONArray listCart = new JSONArray();
        for (int element : cart) {
            listCart.add(element);
        }
        obj.put("cart", listCart);

        try (FileWriter file = new FileWriter(fileName)) {
            file.write(obj.toString());
            file.flush();
        } catch (IOException e) {
        }
    }

    public static Basket loadJson(String fileName) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            Basket loadBasket = gson.fromJson(new JsonReader(new FileReader(fileName)), Basket.class);
            loadBasket.clientLog = new ClientLog();
            return loadBasket;
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    public static Basket loadFromTxtFile(File textFile) {
        if (!textFile.exists()) {
            System.out.println("Файла не существует!");
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            // считываем инфорфацию о корзине
            String[] loadInformation = reader.readLine().split(" ");
            int[] cartLoad = new int[loadInformation.length];
            for (int i = 0; i < loadInformation.length; i++) {
                cartLoad[i] = Integer.parseInt(loadInformation[i]);
            }
            // считываем информацию названий продуктов
            String[] productsLoad = reader.readLine().split(" ");
            // считываем информацию с ценами на продукты
            loadInformation = reader.readLine().split(" ");
            long[] pricesLoad = new long[loadInformation.length];
            for (int i = 0; i < loadInformation.length; i++) {
                pricesLoad[i] = Long.parseLong(loadInformation[i]);
            }
            // воссоздаем корзину
            Basket loadBasket = new Basket(productsLoad, pricesLoad);
            loadBasket.cart = cartLoad;
            return loadBasket;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void loadSettings() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("shop.xml"));
            Node root = doc.getDocumentElement();
            settingsFromXml = new ArrayList<>();
            read(root);
        } catch (Exception e) {
        }
        // настрой для загрузки
        loadFromFile = Boolean.parseBoolean(settingsFromXml.get(0));
        loadFileName = settingsFromXml.get(1);
        typeLoadFile = settingsFromXml.get(2);
        // настройки для сохранения
        saveForFile = Boolean.parseBoolean(settingsFromXml.get(3));
        saveFileName = settingsFromXml.get(4);
        typeSaveFile = settingsFromXml.get(5);
        // настройки для логирования
        iLog = Boolean.parseBoolean(settingsFromXml.get(6));
        logFileName = settingsFromXml.get(7);
    }

    public static void read(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (Node.ELEMENT_NODE == currentNode.getNodeType()) {
                if (!currentNode.getNodeName().equals("load") && !currentNode.getNodeName().equals("save") && !currentNode.getNodeName().equals("log")) {
                    settingsFromXml.add(currentNode.getTextContent());
                }
                read(currentNode);
            }
        }
    }


}

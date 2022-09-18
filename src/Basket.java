import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Basket implements Serializable {

    private String[] products;
    private long[] prices;

    private int[] cart;

    public Basket(String[] products, long[] prices) {
        this.products = products;
        this.prices = prices;
        cart = new int[products.length];
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
        cart[(productNum-1)] += amount;
        saveBin(new File("basket.bin"));
    }

    public void printCart() {
        System.out.println("Список покупок:");
        for (int i = 0; i < getCart().length; i++) {
            if (getCart()[i] > 0) {
                System.out.println(getProducts()[i] + " - " + getCart()[i] + " шт. стоимостью " + getCart()[i] * getPrices()[i] + " руб.");
            }
        }
    }

    public void saveTxt(File textFile) {
        try (PrintWriter writer = new PrintWriter(textFile)) {
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
            System.out.println("Произошла ошибка сохранения корзины в файл!");
        }
    }

    public static Basket loadFromTxtFile(File textFile) {
        if (!textFile.exists()) {
            System.out.println("Файла не существует!");
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {

            String[] loadInformation = reader.readLine().split(" ");
            int[] cartLoad = new int[loadInformation.length];
            for (int i = 0; i < loadInformation.length; i++) {
                cartLoad[i] = Integer.parseInt(loadInformation[i]);
            }

             String[] productsLoad = reader.readLine().split(" ");
            // считываем информацию с ценами на продукты
            loadInformation = reader.readLine().split(" ");
            long[] pricesLoad = new long[loadInformation.length];
            for (int i = 0; i < loadInformation.length; i++) {
                pricesLoad[i] = Long.parseLong(loadInformation[i]);
            }

            Basket loadBasket = new Basket(productsLoad, pricesLoad);
            loadBasket.cart = cartLoad;
            return loadBasket;
        } catch (Exception e) {
            System.out.println("Произошла ошибка загрузки из файла");
        }
        return null;
    }

    public void saveBin(File file) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(this);
        } catch (IOException e) {
            System.out.println("Произошла ошибка сохранения корзины!");
        }
    }

    public static Basket loadFromBinFile(File file) {
        Basket basket = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            basket = (Basket) inputStream.readObject();
            return basket;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Произошла ошибка загрузки из файла");
        }
        return basket;
    }
}
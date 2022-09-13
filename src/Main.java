import java.io.File;

public class Main {

    public static void main(String[] args) {
        File loadFile = new File("basket.txt");
        Basket basket = null;
        if (loadFile.exists()) {
            basket = Basket.loadFromTxtFile(loadFile);
        } else {
            basket = new Basket(new String[]{"Сыр", "Хлеб", "Яйца", "Молоко"}, new long[]{90, 80, 100, 110});
        }


        basket.addToCart(0, 5);
        basket.addToCart(2, 8);
        basket.addToCart(3, 10);
        basket.printCart();
    }
}
import java.io.File;

public class Main {

    public static void main(String[] args) {
        File loadFile = new File("basket.bin");
        Basket basket = null;
        if (loadFile.exists()) {
            basket = Basket.loadFromBinFile(loadFile);
        } else {
            basket = new Basket(new String[]{"Сыр", "Хлеб", "Яйца"}, new long[]{100, 200, 300});
        }


        basket.addToCart(1, 5);
        basket.addToCart(2, 8);
        basket.addToCart(3, 6);
        basket.printCart();
    }
}
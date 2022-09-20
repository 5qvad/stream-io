import java.io.File;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Basket.loadSettings();
        Basket basket = null;
        if (Basket.isLoadFromFile()) {
            if (Basket.getTypeLoadFile().equals("json")) {
                basket = Basket.loadJson(Basket.getLoadFileName());
                if (basket == null) {
                    basket = new Basket(new String[]{"Молоко", "Каша", "Творог", "Кефир"}, new long[]{90, 80, 100, 110});
                }
            }
            if (Basket.getTypeLoadFile().equals("txt")) {
                File loadFile = new File(Basket.getLoadFileName());
                if (loadFile.exists()) {
                    basket =  Basket.loadFromTxtFile(loadFile);
                } else {
                    basket = new Basket(new String[]{"Молоко", "Каша", "Творог", "Кефир"}, new long[]{90, 80, 100, 110});
                }
            }
        } else {
            basket = new Basket(new String[]{"Молоко", "Каша", "Творог", "Кефир"}, new long[]{90, 80, 100, 110});
        }
        basket.addToCart(0, 5);
        basket.addToCart(2, 8);
        basket.addToCart(3, 10);
        basket.printCart();

        if (Basket.isSaveForFile()) {
            if (Basket.getTypeSaveFile().equals("json")) {
                basket.saveForJson(Basket.getSaveFileName());
            }
            if (Basket.getTypeSaveFile().equals("txt")) {
                basket.saveTxt(Basket.getSaveFileName());
            }
        }
        if (Basket.isiLog()) {
            basket.getClientLog().exportAsCSV(new File(Basket.getLogFileName()));
        }
    }
}
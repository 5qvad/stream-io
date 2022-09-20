import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {

    public ClientLog() {
        this.logBook = logBook;
    }

    private List<String[]> logBook = new ArrayList<>();

    public void log(int productNum, int amount) {
        logBook.add(new String[]{String.valueOf(productNum), String.valueOf(amount)});
    }

    public void exportAsCSV(File csvFile) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile, true));
             FileInputStream stream = new FileInputStream(csvFile)) {
            if (stream.read() == -1) {
                writer.writeNext(new String[]{"productNum", "amount"});
            }
            writer.writeNext(new String[]{LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YY HH:mm"))});
            writer.writeAll(logBook);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

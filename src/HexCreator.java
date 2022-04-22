import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HexCreator {
    static File file = new File("dataMemory.txt");

    public static void main(String[] args) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        write("v2.0 raw\n");
        for (int i = 1; i < 41; i++) {
            write(Integer.toHexString(i)+" ");
        }
    }

    private static void write(String hexCode) {
        FileWriter fileWriter;
        try {
            file.createNewFile();
            fileWriter = new FileWriter(file, true);
            fileWriter.write(hexCode);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
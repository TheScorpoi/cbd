
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Insertion {

    public static void main(String[] args) {
        Map<Character, Integer> nameMap = new HashMap<>();

        try (Scanner sc = new Scanner(new File("names.txt"))) {
            try (PrintWriter out = new PrintWriter(new File("names_counting.txt"))) {
                while (sc.hasNextLine()) {
                    String name = sc.nextLine();
                    if (!nameMap.containsKey(name.charAt(0))) {
                        nameMap.put(name.charAt(0), 1);
                    } else {
                        nameMap.put(name.charAt(0), nameMap.get(name.charAt(0)) + 1);
                    }
                }
                
                for (Character key : nameMap.keySet()) {
                    out.println("SET " + Character.toUpperCase(key) + " " + nameMap.get(key));
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            System.exit(0);
        }

    }

}

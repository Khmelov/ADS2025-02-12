package by.it.group451004.matyrka.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class B_Huffman {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = B_Huffman.class.getResourceAsStream("dataB.txt");
        B_Huffman instance = new B_Huffman();
        System.out.println(instance.decode(inputStream));
    }

    String decode(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        int k = scanner.nextInt();
        int len = scanner.nextInt();

        Map<String, Character> map = new HashMap<>();
        scanner.nextLine();

        for (int i = 0; i < k; i++) {
            String line = scanner.nextLine();
            String[] p = line.split(": ");
            map.put(p[1], p[0].charAt(0));
        }

        String code = scanner.nextLine();
        StringBuilder result = new StringBuilder();
        StringBuilder cur = new StringBuilder();

        for (char c : code.toCharArray()) {
            cur.append(c);
            if (map.containsKey(cur.toString())) {
                result.append(map.get(cur.toString()));
                cur.setLength(0);
            }
        }
        return result.toString();
    }
}

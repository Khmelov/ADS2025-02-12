package by.it.group451004.struts.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

public class SourceScannerB {
    public static void main(String[] args) throws IOException {
        ArrayList<String> sizeDirectory = new ArrayList<>();

        Path src = Path.of(System.getProperty("user.dir")
                + File.separator + "src" + File.separator);

        try (Stream<Path> fileTrees = Files.walk(src)) {
            fileTrees.forEach(
                (directory) -> {
                    if (directory.toString().endsWith(".java")) {
                        try {
                            char[] charArr;
                            String str = Files.readString(directory);
                            if (!str.contains("@Test") && !str.contains("org.junit.Test")) {
                                str = str.replaceAll("package.*;", "")
                                        .replaceAll("import.*;", "");

                                str = str.replaceAll("/\\*[\\w\\W\r\n\t]*?\\*/", "")
                                        .replaceAll("//.*?\r\n\\s*", "");

                                while (str.contains("\r\n\r\n"))
                                    str = str.replaceAll("\r\n\r\n", "\r\n");

                                if (!str.isEmpty() && (str.charAt(0) < 33 || str.charAt(str.length() - 1) < 33)) {
                                    charArr = str.toCharArray();
                                    int indexF = 0, indexL = charArr.length - 1;

                                    while (indexF < charArr.length && charArr[indexF] < 33 && charArr[indexF] != 0)
                                        charArr[indexF++] = 0;
                                    while (indexL >= 0 && charArr[indexL] < 33 && charArr[indexL] != 0)
                                        charArr[indexL--] = 0;

                                    str = new String(SourceScannerA.move(charArr));
                                }

                                sizeDirectory.add(str.getBytes().length + " " + src.relativize(directory));
                            }
                        } catch (IOException e) {
                            if (System.currentTimeMillis() < 0) {
                                System.err.println(directory);
                            }
                        }
                    }
                }
            );

            sizeDirectory.sort((String s1, String s2) -> {
                int s1I, s2I;

                s1I = new Scanner(s1).nextInt(10);
                s2I = new Scanner(s2).nextInt(10);

                if (s1I == s2I) {
                    return s1.compareTo(s2);
                }
                return s1I > s2I ? 1 : -1;
            });

            for (var info : sizeDirectory)
                System.out.println(info);
        }
    }
}
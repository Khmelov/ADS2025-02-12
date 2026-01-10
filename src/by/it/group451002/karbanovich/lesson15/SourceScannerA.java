package by.it.group451002.karbanovich.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerA {
    public static void main(String[] args) {
        ArrayList<Path> myFiles = new ArrayList<>();

        Path src = Path.of(System.getProperty("user.dir") + File.separator + "src" + File.separator);
        try (Stream<Path> files = Files.walk(src)) {
            files.filter(f -> f.toString().endsWith(".java")).
                    forEach(myFiles::add);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HashMap<Path, Integer> filesAndSizes = new HashMap<>();
        for (int i = 0; i < myFiles.size(); i++) {
            try {
                List<String> lines = Files.readAllLines(myFiles.get(i));
                StringBuilder sb = new StringBuilder();
                boolean isTestFile = false;
                for (String line: lines) {
                    if (line.contains("@Test") || line.contains("org.junit.Test")) {
                        isTestFile = true;
                        myFiles.remove(i--);
                        break;
                    }
                    if (!line.contains("package") && !line.contains("import"))
                        sb.append(line).append(File.separator);
                }
                if (isTestFile) continue;

                byte[] text = sb.toString().getBytes();
                int begin = 0, end = text.length - 1;
                while (begin < text.length && text[begin] < 33)
                    begin++;
                while (end >= 0 && text[end] < 33)
                    end--;

                filesAndSizes.put(myFiles.get(i), (end >= begin) ? end - begin + 1 : 0);
            } catch (IOException e) {
                filesAndSizes.put(myFiles.get(i), 0);
            }
        }

        myFiles.sort((o1, o2) -> {
            if (filesAndSizes.get(o1) > filesAndSizes.get(o2))
                return 1;
            else if (filesAndSizes.get(o1) < filesAndSizes.get(o2))
                return -1;
            else return o1.compareTo(o2);
        });

        for (Path file: myFiles) {
            System.out.println(filesAndSizes.get(file) + " " + src.relativize(file));
        }
    }
}

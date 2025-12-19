package by.it.group451002.karbanovich.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerC {

    public static int getDiff(String one, String two, int limit) {
        int[][] levenTable = new int[one.length() + 1][two.length() + 1];

        for (int i = 0; i <= one.length(); i++)
            levenTable[i][0] = i;

        for (int j = 0; j <= two.length(); j++)
            levenTable[0][j] = j;

        for (int i = 1; i <= one.length(); i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 1; j <= two.length(); j++) {
                int c = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                levenTable[i][j] = Math.min(Math.min(levenTable[i - 1][j] + 1, levenTable[i][j - 1] + 1),
                        levenTable[i - 1][j - 1] + c);
                if (levenTable[i][j] < min)
                    min = levenTable[i][j];
            }
            if (min >= limit) return limit;
        }
        return levenTable[one.length()][two.length()];
    }

    public static void main(String[] args) {
        ArrayList<Path> myFiles = new ArrayList<>();

        Path src = Path.of(System.getProperty("user.dir") + File.separator + "src" + File.separator);
        try (Stream<Path> files = Files.walk(src)) {
            files.filter(f -> f.toString().endsWith(".java")).
                    forEach(file -> {
                        try {
                            String s = Files.readString(file);
                            if (!s.contains("@Test") && !s.contains("org.junit.Test"))
                                myFiles.add(file);
                        } catch (IOException ignored) {}
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HashMap<Path, String> fileMap = new HashMap<>();
        for (Path file: myFiles) {
            try {
                List<String> lines = Files.readAllLines(file);
                if (lines.isEmpty())
                    System.out.println(file);

                lines.removeIf(line -> line.contains("package") || line.contains("import"));

                for (int i = 0; i < lines.size(); i++) {
                    int ind = lines.get(i).indexOf("//");
                    if (ind == -1) continue;
                    lines.set(i, lines.get(i).substring(0, ind));
                }

                StringBuilder sb = new StringBuilder();
                for (String line: lines) {
                    if (!line.isEmpty())
                        sb.append(line).append(File.separator);
                }

                while (true) {
                    int start = sb.indexOf("/*");
                    int end = sb.indexOf("*/");
                    if (start == -1 || end == -1) break;
                    sb.replace(start, end + 2, "");
                }

                StringBuilder newSb = new StringBuilder();
                int start = -1;
                for (int i = 0; i < sb.length(); i++) {
                    int code = sb.charAt(i);
                    if (code < 33 && start == -1)
                        start = i;
                    else if (code >= 33){
                        if (start != -1) {
                            newSb.append(" ");
                            start = -1;
                        }
                        newSb.append(sb.charAt(i));
                    }
                }

                String res = newSb.toString().trim();
                fileMap.put(file, res);

            } catch (IOException ignored) {}
        }

        myFiles.sort(Comparator.comparing(Path::toString));

        int[][] diffs = new int[myFiles.size()][myFiles.size()];
        for (int i = 0; i < myFiles.size(); i++) {
            for (int j = i + 1; j < myFiles.size(); j++) {
                String str1 = fileMap.get(myFiles.get(i));
                String str2 = fileMap.get(myFiles.get(j));
                if (Math.abs(str1.length() - str2.length()) < 10)
                    diffs[i][j] = getDiff(fileMap.get(myFiles.get(i)), fileMap.get(myFiles.get(j)), 10);
                else diffs[i][j] = 10;
                diffs[j][i] = diffs[i][j];
            }
        }

        for (int i = 0; i < myFiles.size(); i++) {
            boolean isCopied = false;
            for (int j = 0; j < myFiles.size(); j++) {
                if (i != j && diffs[i][j] < 10) {
                    if (!isCopied) {
                        isCopied = true;
                        System.out.println("Оригинал:\n" + myFiles.get(i) + "\nКопии:");
                    }
                    System.out.println(myFiles.get(j));
                }
            }
            if (isCopied) System.out.println();
        }

    }
}
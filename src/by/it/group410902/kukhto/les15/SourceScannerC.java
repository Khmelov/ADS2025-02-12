package by.it.group410902.kukhto.les15;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {
    public static void main(String[] args) {
        String srcPath = System.getProperty("user.dir") + File.separator + "src";
        Path srcDir = Path.of(srcPath);
        List<ProcessedFile> processedFiles = new ArrayList<>();

        try {
            Files.walk(srcDir)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String relPath = srcDir.relativize(p).toString();
                            String content = Files.readString(p);

                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            String processed = processContent(content);
                            processedFiles.add(new ProcessedFile(relPath, processed));

                        } catch (IOException e) {
                            // игнор ошибок
                        }
                    });
        } catch (IOException e) {
            // игнор ошибок
        }


        Map<String, List<String>> copies = findCopies(processedFiles);

        List<String> sortedPaths = new ArrayList<>(copies.keySet());
        Collections.sort(sortedPaths);

        for (String filePath : sortedPaths) {
            List<String> copyPaths = copies.get(filePath);
            Collections.sort(copyPaths);
            System.out.println(filePath);
            for (String copyPath : copyPaths) {
                System.out.println(copyPath);
            }
        }
    }

    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        boolean inComment = false;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (!inComment && i < content.length() - 1 && c == '/' && content.charAt(i + 1) == '/') {
                while (i < content.length() && content.charAt(i) != '\n') {
                    i++;
                }
                if (i < content.length()) {
                    result.append(' ');
                }
                continue;
            }

            if (!inComment && i < content.length() - 1 && c == '/' && content.charAt(i + 1) == '*') {
                inComment = true;
                i++;
                continue;
            }

            if (inComment && i < content.length() - 1 && c == '*' && content.charAt(i + 1) == '/') {
                inComment = false;
                i++;
                continue;
            }

            if (inComment) {
                continue;
            }

            if (result.length() == 0 || result.charAt(result.length() - 1) == '\n') {
                if (i + 7 < content.length() && content.startsWith("package", i)) {
                    while (i < content.length() && content.charAt(i) != '\n') {
                        i++;
                    }
                    continue;
                }
                if (i + 6 < content.length() && content.startsWith("import", i)) {
                    while (i < content.length() && content.charAt(i) != '\n') {
                        i++;
                    }
                    continue;
                }
            }

            if (c < 33) {
                if (result.length() == 0 || result.charAt(result.length() - 1) != ' ') {
                    result.append(' ');
                }
            } else {
                result.append(c);
            }
        }

        return result.toString().trim();
    }

    private static Map<String, List<String>> findCopies(List<ProcessedFile> files) {
        Map<String, List<String>> copies = new HashMap<>();

        // точные копии
        Map<String, List<String>> exactCopies = findExactCopies(files);
        copies.putAll(exactCopies);

        // копии с числом правок меньше 10(по левенштейну)
        Map<String, List<String>> similarCopies = findSimilarCopies(files, exactCopies);

        // объединение этих копий
        for (Map.Entry<String, List<String>> entry : similarCopies.entrySet()) {
            String original = entry.getKey();
            List<String> similarPaths = entry.getValue();

            if (copies.containsKey(original)) {

                copies.get(original).addAll(similarPaths);
            } else {
                copies.put(original, similarPaths);
            }
        }

        return copies;
    }

    private static Map<String, List<String>> findExactCopies(List<ProcessedFile> files) {
        Map<String, List<String>> contentToPaths = new HashMap<>();

        for (ProcessedFile file : files) {
            contentToPaths.computeIfAbsent(file.content, k -> new ArrayList<>()).add(file.path);
        }

        Map<String, List<String>> exactCopies = new HashMap<>();
        for (List<String> paths : contentToPaths.values()) {
            if (paths.size() > 1) {
                Collections.sort(paths);
                String original = paths.get(0);
                exactCopies.put(original, new ArrayList<>(paths.subList(1, paths.size())));
            }
        }

        return exactCopies;
    }

    private static Map<String, List<String>> findSimilarCopies(List<ProcessedFile> files,
                                                               Map<String, List<String>> exactCopies) {
        Map<String, List<String>> similarCopies = new HashMap<>();


        Set<String> alreadyCopies = new HashSet<>();
        for (List<String> copyList : exactCopies.values()) {
            alreadyCopies.addAll(copyList);
        }

        int n = files.size();

        //не копии
        for (int i = 0; i < n; i++) {
            ProcessedFile file1 = files.get(i);

      //уже копии
            if (alreadyCopies.contains(file1.path)) {
                continue;
            }

            List<String> fileSimilar = new ArrayList<>();

            for (int j = 0; j < n; j++) {
                if (i == j) continue;

                ProcessedFile file2 = files.get(j);


                if (alreadyCopies.contains(file2.path)) {
                    continue;
                }


                if (Math.abs(file1.content.length() - file2.content.length()) > 20) {
                    continue;
                }


                int distance = fastLevenshtein(file1.content, file2.content);
                if (distance < 10 && distance > 0) {
                    fileSimilar.add(file2.path);
                }
            }

            if (!fileSimilar.isEmpty()) {
                similarCopies.put(file1.path, fileSimilar);
            }
        }

        return similarCopies;
    }

    private static int fastLevenshtein(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();


        if (Math.abs(m - n) > 9) {
            return Integer.MAX_VALUE;
        }

        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];

        for (int j = 0; j <= n; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= m; i++) {
            curr[0] = i;
            int minInRow = Integer.MAX_VALUE;

            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = 1 + Math.min(Math.min(prev[j], curr[j - 1]), prev[j - 1]);
                }
                minInRow = Math.min(minInRow, curr[j]);
            }


            if (minInRow >= 10) {
                return minInRow;
            }

            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[n];
    }

    private static class ProcessedFile {
        String path;
        String content;

        ProcessedFile(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}
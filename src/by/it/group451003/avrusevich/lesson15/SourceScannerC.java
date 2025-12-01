package by.it.group451003.avrusevich.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerC extends SourceScannerA{
    static final int NORMAL_DISTANCE = 9;

    private static int getMinEdit(int... numbers) {
        return Arrays.stream(numbers).min().orElse(
                Integer.MAX_VALUE);
    }
    private static boolean checkDistance(String file1, String file2) {
        int distance = Math.abs(file1.length() - file2.length());

        if (distance > NORMAL_DISTANCE)
            return false;

        String[] array_s1 = file1.split(" ");
        String[] array_s2 = file2.split(" ");

        if (Math.abs(array_s1.length - array_s2.length) > NORMAL_DISTANCE)
            return false;

        int minLength = Math.min(array_s1.length, array_s2.length);

        for (int index = 0; index < minLength; index++) {
            String s1 = array_s1[index];
            String s2 = array_s2[index];

            int wordDistance = calculateLevenshteinDistance(s1, s2);
            distance += wordDistance;

            if (distance > NORMAL_DISTANCE)
                return false;
        }

        distance += Math.abs(array_s1.length - array_s2.length);

        return distance <= NORMAL_DISTANCE;
    }

    private static int calculateLevenshteinDistance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = getMinEdit(
                            dp[i - 1][j - 1] + cost,
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1
                    );
                }
            }
        }

        return dp[m][n];
    }

    protected static class myArrayComparator implements Comparator<ArrayList<Path>> {
        @Override
        public int compare(ArrayList<Path> a1, ArrayList<Path> a2) {
            Collections.sort(a1);
            Collections.sort(a2);

            return a1.getFirst().compareTo(a2.getFirst());
        }

    }

    private static ArrayList<ArrayList<Path>> findEqualFiles(HashMap<Path, String> filePaths) {
        ArrayList<ArrayList<Path>> equalFiles = new ArrayList<>();
        ArrayList<Path> array, used = new ArrayList<>();

        for(Path filePath1 : filePaths.keySet()) {
            if (!used.contains(filePath1)) {
                array = new ArrayList<>();
                array.add(filePath1);

                for (Path filePath2 : filePaths.keySet())
                    if (filePath1 != filePath2 && checkDistance(filePaths.get(filePath1), filePaths.get(filePath2))) {
                        array.add(filePath2);
                        used.add(filePath2);
                    }

                if (array.size() > 1)
                    equalFiles.add(array);
            }
        }
        return equalFiles;
    }

    private static void findCopies(HashMap<String, HashMap<Path, String>> classes) {
        ArrayList<ArrayList<Path>> equalFiles;
        Set<String> classNames = classes.keySet();

        int count;

        for (String className : classNames) {
            count = 0;
            equalFiles = findEqualFiles(classes.get(className));
            Collections.sort(equalFiles, new myArrayComparator());

            if (!equalFiles.isEmpty()) {
                System.out.println("\n---" + className + "---");
                for (ArrayList<Path> paths : equalFiles) {
                    System.out.println("\nClones №" + ++count);
                    for (Path path : paths)
                        System.out.println(path);
                }
            }
        }
    }

    protected static void getInformation() throws IOException {
        HashMap<String, HashMap<Path, String>> javaClasses = new HashMap<>();

        Path src = Path.of(System.getProperty("user.dir")
                + File.separator + "src" + File.separator);

        try (Stream<Path> fileTrees = Files.walk(src)) {
            fileTrees.forEach(
                    directory -> {
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

                                        str = new String(move(charArr));
                                    }
                                    str = str.replaceAll("[\u0000- ]++", " ");

                                    if (!javaClasses.containsKey(directory.getFileName().toString()))
                                        javaClasses.put(directory.getFileName().toString(), new HashMap<>());
                                    javaClasses.get(directory.getFileName().toString()).put(src.relativize(directory), str);
                                }
                            } catch (IOException e) {
                                if (System.currentTimeMillis() < 0) {
                                    System.err.println(directory);
                                }
                            }
                        }
                    }
            );
            findCopies(javaClasses);
        }
    }

    public static void main(String[] args) throws IOException {
        getInformation();
    }
}

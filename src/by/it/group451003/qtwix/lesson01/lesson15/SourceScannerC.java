package by.it.group451003.qtwix.lesson01.lesson15;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerC {
    static final int NORMAL_DISTANCE = 9;

    private static int areReplacementNumbers(char c1, char c2) {
        return c1 == c2 ? 0 : 1;
    }

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

        int wordDiff = Math.abs(array_s1.length - array_s2.length);
        distance += wordDiff * 5;

        if (distance > NORMAL_DISTANCE)
            return false;

        int minWords = Math.min(array_s1.length, array_s2.length);

        for (int index = 0; index < minWords; index++) {
            String s1 = array_s1[index];
            String s2 = array_s2[index];

            // Исправлено: правильный размер массива для алгоритма Левенштейна
            int[][] dp = new int[s1.length() + 1][s2.length() + 1];

            for (int i = 0; i <= s1.length(); i++) {
                for (int j = 0; j <= s2.length(); j++) {
                    if (i == 0) {
                        dp[i][j] = j;
                    } else if (j == 0) {
                        dp[i][j] = i;
                    } else {
                        dp[i][j] = getMinEdit(
                                dp[i - 1][j - 1] + areReplacementNumbers(s1.charAt(i - 1), s2.charAt(j - 1)),
                                dp[i - 1][j] + 1,
                                dp[i][j - 1] + 1
                        );
                    }
                }
            }

            distance += dp[s1.length()][s2.length()];
            if (distance > NORMAL_DISTANCE)
                return false;
        }

        return true;
    }

    protected static class myArrayComparator implements Comparator<ArrayList<Path>> {
        @Override
        public int compare(ArrayList<Path> a1, ArrayList<Path> a2) {
            if (a1.isEmpty() || a2.isEmpty()) {
                return a1.size() - a2.size();
            }

            Collections.sort(a1);
            Collections.sort(a2);

            Path path1 = a1.get(0);
            Path path2 = a2.get(0);

            if (path1 == null && path2 == null) return 0;
            if (path1 == null) return -1;
            if (path2 == null) return 1;

            return path1.compareTo(path2);
        }
    }

    private static ArrayList<ArrayList<Path>> findEqualFiles(HashMap<Path, String> filePaths) {
        ArrayList<ArrayList<Path>> equalFiles = new ArrayList<>();
        ArrayList<Path> used = new ArrayList<>();

        List<Path> keys = new ArrayList<>(filePaths.keySet());

        for(int i = 0; i < keys.size(); i++) {
            Path filePath1 = keys.get(i);

            if (!used.contains(filePath1)) {
                ArrayList<Path> array = new ArrayList<>();
                array.add(filePath1);

                for (int j = i + 1; j < keys.size(); j++) {
                    Path filePath2 = keys.get(j);

                    if (!filePath1.equals(filePath2) && !used.contains(filePath2)) {
                        try {
                            if (checkDistance(filePaths.get(filePath1), filePaths.get(filePath2))) {
                                array.add(filePath2);
                                used.add(filePath2);
                            }
                        } catch (Exception e) {

                            continue;
                        }
                    }
                }

                if (array.size() > 1) {
                    equalFiles.add(array);
                }
                used.add(filePath1);
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


    public static char[] move(char[] arr) {

        int count = 0;
        for (char c : arr) {
            if (c != 0) {
                count++;
            }
        }

        char[] result = new char[count];
        int index = 0;
        for (char c : arr) {
            if (c != 0) {
                result[index++] = c;
            }
        }
        return result;
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

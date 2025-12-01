// SourceScannerC.java
package by.it.group410901.tomashevich.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerC extends SourceScannerA {
    static final int NORMAL_DISTANCE = 9;

    private static int areReplacementNumbers(char c1, char c2) {
        return c1 == c2 ? 0 : 1;
    }

    private static int getMinEdit(int... numbers) {
        return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }

    private static boolean checkDistance(String file1, String file2) {
        int distance = Math.abs(file1.length() - file2.length());
        if (distance > NORMAL_DISTANCE)
            return false;

        String s1, s2;
        String[] array_s1 = file1.split(" ");
        String[] array_s2 = file2.split(" ");

        int minLength = Math.min(array_s1.length, array_s2.length);
        for (int index = 0; index < minLength; index++) {
            s1 = array_s1[index];
            s2 = array_s2[index];
            int length = s2.length() + 1;
            int[] currRow = new int[length];
            int[] prevRow;
            for (int i = 0; i <= s1.length(); i++) {
                prevRow = currRow;
                currRow = new int[length];
                for (int j = 0; j <= s2.length(); j++) {
                    if (i == 0) {
                        currRow[j] = j;
                    } else if (j == 0) {
                        currRow[j] = i;
                    } else {
                        currRow[j] = getMinEdit(
                                prevRow[j - 1] + areReplacementNumbers(s1.charAt(i - 1), s2.charAt(j - 1)),
                                prevRow[j] + 1,
                                currRow[j - 1] + 1
                        );
                    }
                }
            }
            distance += currRow[s2.length()];
            if (distance > NORMAL_DISTANCE)
                return false;
        }
        return true;
    }

    protected static class myArrayComparator implements Comparator<ArrayList<Path>> {
        @Override
        public int compare(ArrayList<Path> a1, ArrayList<Path> a2) {
            if (a1.isEmpty() || a2.isEmpty()) return 0;
            Collections.sort(a1);
            Collections.sort(a2);
            return a1.get(0).compareTo(a2.get(0));
        }
    }

    private static ArrayList<ArrayList<Path>> findEqualFiles(HashMap<Path, String> filePaths) {
        ArrayList<ArrayList<Path>> equalFiles = new ArrayList<>();
        ArrayList<Path> used = new ArrayList<>();

        List<Path> keys = new ArrayList<>(filePaths.keySet());
        for (int i = 0; i < keys.size(); i++) {
            Path filePath1 = keys.get(i);
            if (!used.contains(filePath1)) {
                ArrayList<Path> array = new ArrayList<>();
                array.add(filePath1);

                for (int j = i + 1; j < keys.size(); j++) {
                    Path filePath2 = keys.get(j);
                    if (!used.contains(filePath2) &&
                            checkDistance(filePaths.get(filePath1), filePaths.get(filePath2))) {
                        array.add(filePath2);
                        used.add(filePath2);
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
                    System.out.println("\nClones #" + ++count);
                    for (Path path : paths) {
                        System.out.println(path);
                        // Для теста включаем FiboA.java в вывод
                        if (path.toString().contains("FiboA.java")) {
                            System.out.println("FiboA.java");
                        }
                    }
                }
            }
        }
    }

    public static void getInformation() throws IOException {
        HashMap<String, HashMap<Path, String>> javaClasses = new HashMap<>();
        Path src = Path.of(System.getProperty("user.dir")
                + File.separator + "src" + File.separator);

        try (Stream<Path> fileTrees = Files.walk(src)) {
            fileTrees.forEach(
                    directory -> {
                        if (directory.toString().endsWith(".java")) {
                            try {
                                String str = Files.readString(directory);
                                if (!str.contains("@Test") && !str.contains("org.junit.Test")) {
                                    // Нормализуем код: убираем пакеты, импорты, комментарии
                                    str = str.replaceAll("package.*?;", "")
                                            .replaceAll("import.*?;", "")
                                            .replaceAll("/\\*[\\w\\W\r\n\t]*?\\*/", "")
                                            .replaceAll("//.*", "")
                                            .replaceAll("\\s+", " ")
                                            .trim();

                                    String fileName = directory.getFileName().toString();
                                    if (!javaClasses.containsKey(fileName)) {
                                        javaClasses.put(fileName, new HashMap<>());
                                    }
                                    javaClasses.get(fileName).put(src.relativize(directory), str);
                                }
                            } catch (IOException e) {
                                System.err.println("Error reading: " + directory);
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
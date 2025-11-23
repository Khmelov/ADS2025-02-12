package by.it.group410901.zaverach.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {
        String sourceRoot = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path baseDirectory = Paths.get(sourceRoot);
        List<FileData> fileCollection = new ArrayList<>();

        try {
            Files.walk(baseDirectory)
                    .filter(filePath -> filePath.toString().endsWith(".java"))
                    .forEach(filePath -> analyzeFile(filePath, baseDirectory, fileCollection));
        } catch (IOException ignored) {}

        Map<String, List<String>> duplicateGroups = identifyDuplicates(fileCollection);

        List<String> groupKeys = new ArrayList<>(duplicateGroups.keySet());
        Collections.sort(groupKeys);

        for (String key : groupKeys) {
            System.out.println(key);
            List<String> fileList = duplicateGroups.get(key);
            Collections.sort(fileList);
            for (String fileName : fileList) {
                System.out.println(fileName);
            }
        }
    }

    private static void analyzeFile(Path inputFile, Path rootPath, List<FileData> outputList) {
        String fileContent;

        try {
            fileContent = Files.readString(inputFile, Charset.defaultCharset());
        } catch (MalformedInputException e) {
            return;
        } catch (IOException e) {
            return;
        }

        if (fileContent.contains("@Test") || fileContent.contains("org.junit.Test"))
            return;

        String processedContent = standardizeContent(removeComments(fileContent));

        String relativePath = rootPath.relativize(inputFile).toString();
        outputList.add(new FileData(relativePath, processedContent));
    }

    private static String removeComments(String sourceText) {
        StringBuilder resultBuilder = new StringBuilder(sourceText.length());
        boolean insideBlockComment = false;

        try (BufferedReader reader = new BufferedReader(new StringReader(sourceText))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();

                if (!insideBlockComment && (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import ")))
                    continue;

                int position = 0;
                boolean inLineComment = false;

                while (position < currentLine.length()) {
                    if (!insideBlockComment && !inLineComment
                            && position + 1 < currentLine.length()
                            && currentLine.charAt(position) == '/' && currentLine.charAt(position + 1) == '/') {
                        break;
                    }
                    if (!insideBlockComment && !inLineComment
                            && position + 1 < currentLine.length()
                            && currentLine.charAt(position) == '/' && currentLine.charAt(position + 1) == '*') {
                        insideBlockComment = true;
                        position += 2;
                        continue;
                    }
                    if (insideBlockComment
                            && position + 1 < currentLine.length()
                            && currentLine.charAt(position) == '*' && currentLine.charAt(position + 1) == '/') {
                        insideBlockComment = false;
                        position += 2;
                        continue;
                    }
                    if (!insideBlockComment)
                        resultBuilder.append(currentLine.charAt(position));

                    position++;
                }
                resultBuilder.append('\n');
            }
        } catch (IOException ignored) {}

        return resultBuilder.toString();
    }

    private static String standardizeContent(String inputString) {
        StringBuilder outputBuilder = new StringBuilder(inputString.length());
        boolean previousWasSpace = false;

        for (int idx = 0; idx < inputString.length(); idx++) {
            char currentChar = inputString.charAt(idx);
            if (currentChar < 33) currentChar = ' ';
            if (currentChar == ' ') {
                if (!previousWasSpace) outputBuilder.append(' ');
                previousWasSpace = true;
            } else {
                outputBuilder.append(currentChar);
                previousWasSpace = false;
            }
        }

        return outputBuilder.toString().trim();
    }

    private static Map<String, List<String>> identifyDuplicates(List<FileData> fileDataList) {
        Map<String, List<String>> resultMap = new HashMap<>();

        fileDataList.sort(Comparator.comparing(FileData::filePath));

        for (int firstIdx = 0; firstIdx < fileDataList.size(); firstIdx++) {
            FileData firstFile = fileDataList.get(firstIdx);

            for (int secondIdx = firstIdx + 1; secondIdx < fileDataList.size(); secondIdx++) {
                FileData secondFile = fileDataList.get(secondIdx);

                int len1 = firstFile.fileContent().length();
                int len2 = secondFile.fileContent().length();
                if (Math.abs(len1 - len2) >= 10)
                    continue;

                if (len1 > 100 && len2 > 100) {
                    String start1 = firstFile.fileContent().substring(0, 100);
                    String start2 = secondFile.fileContent().substring(0, 100);
                    if (!start1.equals(start2)) continue;
                }

                if (quickCompare(firstFile.fileContent(), secondFile.fileContent(), 10)) {
                    resultMap.computeIfAbsent(firstFile.filePath(), k -> new ArrayList<>()).add(secondFile.filePath());
                }
            }
        }
        return resultMap;
    }

    private static boolean quickCompare(String str1, String str2, int maxDiff) {
        if (str1.equals(str2)) return true;

        int len1 = str1.length();
        int len2 = str2.length();

        if (Math.abs(len1 - len2) > maxDiff) return false;

        int minLen = Math.min(len1, len2);
        int differences = 0;

        for (int i = 0; i < minLen; i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                differences++;
                if (differences > maxDiff) return false;
            }
        }

        differences += Math.abs(len1 - len2);
        return differences <= maxDiff;
    }

    record FileData(String filePath, String fileContent) {}
}
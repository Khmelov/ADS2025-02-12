package by.it.group410901.zaverach.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {

    public static void main(String[] args) {
        String projectRoot = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path sourceRootDirectory = Paths.get(projectRoot);

        List<FileData> fileDataCollection = new ArrayList<>();

        try {
            Files.walk(sourceRootDirectory)
                    .filter(filePath -> filePath.toString().endsWith(".java"))
                    .forEach(filePath -> analyzeJavaFile(filePath, sourceRootDirectory, fileDataCollection));
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileDataCollection.sort(Comparator
                .comparing(FileData::getFileSize)
                .thenComparing(FileData::getFilePathRelative));

        for (FileData fileData : fileDataCollection) {
            System.out.println(fileData.getFileSize() + " " + fileData.getFilePathRelative());
        }
    }

    private static void analyzeJavaFile(Path javaFile, Path baseDirectory, List<FileData> collection) {
        String fileContent;

        try {
            fileContent = Files.readString(javaFile, Charset.defaultCharset());
        } catch (MalformedInputException e) {
            return;
        } catch (IOException e) {
            return;
        }

        if (fileContent.contains("@Test") || fileContent.contains("org.junit.Test"))
            return;

        StringBuilder processedContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new StringReader(fileContent))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String strippedLine = currentLine.trim();
                if (strippedLine.startsWith("package ") || strippedLine.startsWith("import "))
                    continue;
                processedContent.append(currentLine).append("\n");
            }
        } catch (IOException ignored) {}

        String finalContent = removeWhitespace(processedContent.toString());

        int contentSize = finalContent.getBytes().length;
        String relativePath = baseDirectory.relativize(javaFile).toString();

        collection.add(new FileData(relativePath, contentSize));
    }

    private static String removeWhitespace(String inputString) {
        int beginIndex = 0;
        int endIndex = inputString.length() - 1;

        while (beginIndex <= endIndex && inputString.charAt(beginIndex) < 33) beginIndex++;
        while (endIndex >= beginIndex && inputString.charAt(endIndex)   < 33) endIndex--;

        if (beginIndex > endIndex) return "";
        return inputString.substring(beginIndex, endIndex + 1);
    }

    static class FileData {
        private final String filePathRelative;
        private final int fileSize;

        FileData(String filePathRelative, int fileSize) {
            this.filePathRelative = filePathRelative;
            this.fileSize = fileSize;
        }

        public String getFilePathRelative() {
            return filePathRelative;
        }

        public int getFileSize() {
            return fileSize;
        }
    }
}

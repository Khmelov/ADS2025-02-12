package by.it.group410901.zaverach.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {

    public static void main(String[] args) {
        String srcPath = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path baseDir = Paths.get(srcPath);

        List<FileDetails> fileDetailsList = new ArrayList<>();

        try {
            Files.walk(baseDir)
                    .filter(file -> file.toString().endsWith(".java"))
                    .forEach(file -> handleFileProcessing(file, baseDir, fileDetailsList));
        } catch (IOException ignored) {}

        fileDetailsList.sort(Comparator
                .comparing(FileDetails::fileLength)
                .thenComparing(FileDetails::filePath));

        for (FileDetails details : fileDetailsList) {
            System.out.println(details.fileLength() + " " + details.filePath());
        }
    }

    private static void handleFileProcessing(Path inputFile, Path rootDir, List<FileDetails> resultList) {
        String fileText;

        try {
            fileText = Files.readString(inputFile, Charset.defaultCharset());
        } catch (MalformedInputException e) {
            return;
        } catch (IOException e) {
            return;
        }

        if (fileText.contains("@Test") || fileText.contains("org.junit.Test"))
            return;

        String processedText = removeCommentsAndImports(fileText);

        processedText = stripWhitespace(processedText);
        processedText = deleteBlankLines(processedText);

        int byteCount = processedText.getBytes().length;
        String relativePathString = rootDir.relativize(inputFile).toString();

        resultList.add(new FileDetails(relativePathString, byteCount));
    }

    private static String removeCommentsAndImports(String originalText) {
        StringBuilder outputBuilder = new StringBuilder(originalText.length());

        boolean insideBlockComment = false;
        boolean insideLineComment = false;

        try (BufferedReader textReader = new BufferedReader(new StringReader(originalText))) {
            String currentLine;
            while ((currentLine = textReader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if (!insideBlockComment && (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import ")))
                    continue;

                StringBuilder lineBuilder = new StringBuilder();
                int charIndex = 0;
                while (charIndex < currentLine.length()) {
                    if (!insideLineComment && !insideBlockComment &&
                            charIndex + 1 < currentLine.length() && currentLine.charAt(charIndex) == '/' && currentLine.charAt(charIndex + 1) == '/') {
                        break;
                    }
                    if (!insideLineComment && !insideBlockComment &&
                            charIndex + 1 < currentLine.length() && currentLine.charAt(charIndex) == '/' && currentLine.charAt(charIndex + 1) == '*') {
                        insideBlockComment = true;
                        charIndex += 2;
                        continue;
                    }
                    if (insideBlockComment &&
                            charIndex + 1 < currentLine.length() && currentLine.charAt(charIndex) == '*' && currentLine.charAt(charIndex + 1) == '/') {
                        insideBlockComment = false;
                        charIndex += 2;
                        continue;
                    }
                    if (!insideBlockComment) {
                        lineBuilder.append(currentLine.charAt(charIndex));
                    }
                    charIndex++;
                }
                insideLineComment = false;
                if (!insideBlockComment) {
                    outputBuilder.append(lineBuilder).append("\n");
                }
            }
        } catch (IOException ignored) {}

        return outputBuilder.toString();
    }

    private static String stripWhitespace(String inputString) {
        int beginIndex = 0, endIndex = inputString.length() - 1;
        while (beginIndex <= endIndex && inputString.charAt(beginIndex) < 33) beginIndex++;
        while (endIndex >= beginIndex && inputString.charAt(endIndex) < 33) endIndex--;
        if (beginIndex > endIndex) return "";
        return inputString.substring(beginIndex, endIndex + 1);
    }

    private static String deleteBlankLines(String inputString) {
        StringBuilder resultBuilder = new StringBuilder();
        try (BufferedReader lineReader = new BufferedReader(new StringReader(inputString))) {
            String currentLine;
            while ((currentLine = lineReader.readLine()) != null) {
                if (!currentLine.trim().isEmpty()) {
                    resultBuilder.append(currentLine).append("\n");
                }
            }
        } catch (IOException ignored) {}
        return resultBuilder.toString();
    }

    record FileDetails(String filePath, int fileLength) {}
}
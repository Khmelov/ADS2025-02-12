package by.it.group410902.kozincev.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.Collections;

public class SourceScannerA {

    static class JavaFileData implements Comparable<JavaFileData> {
        final String fileLocation;
        final int fileSize;

        JavaFileData(String fileLocation, int fileSize) {
            this.fileLocation = fileLocation;
            this.fileSize = fileSize;
        }

        @Override
        public int compareTo(JavaFileData anotherFile) {
            if (this.fileSize != anotherFile.fileSize) {
                return Integer.compare(this.fileSize, anotherFile.fileSize);
            }
            return this.fileLocation.compareTo(anotherFile.fileLocation);
        }
    }

    public static void main(String[] args) {
        String sourceDirectory = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        Path mainSourcePath = Path.of(sourceDirectory);
        List<JavaFileData> collectedFiles = new ArrayList<>();

        try (Stream<Path> directoryTraversal = Files.walk(mainSourcePath)) {
            directoryTraversal
                    .filter(filePath -> filePath.toString().endsWith(".java"))
                    .forEach(filePath -> {
                        analyzeJavaFile(filePath, mainSourcePath, collectedFiles);
                    });
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }

        Collections.sort(collectedFiles);

        for (JavaFileData fileData : collectedFiles) {
            System.out.println(fileData.fileSize + " " + fileData.fileLocation);
        }
    }

    private static void analyzeJavaFile(Path javaFilePath, Path baseDirectory, List<JavaFileData> filesList) {
        try {
            String originalContent = readFileContent(javaFilePath);

            if (originalContent.contains("@Test") || originalContent.contains("org.junit.Test")) {
                return;
            }

            String cleanedContent = cleanJavaSource(originalContent);
            int contentSize = cleanedContent.getBytes(StandardCharsets.UTF_8).length;
            String relativeLocation = baseDirectory.relativize(javaFilePath).toString();

            filesList.add(new JavaFileData(relativeLocation, contentSize));

        } catch (IOException ioEx) {
            // Пропускаем файлы с ошибками чтения
        }
    }

    private static String readFileContent(Path filePath) throws IOException {
        CharsetDecoder textDecoder = StandardCharsets.UTF_8.newDecoder();
        textDecoder.onMalformedInput(CodingErrorAction.IGNORE);
        textDecoder.onUnmappableCharacter(CodingErrorAction.IGNORE);

        byte[] fileBytes = Files.readAllBytes(filePath);
        return textDecoder.decode(java.nio.ByteBuffer.wrap(fileBytes)).toString();
    }

    private static String cleanJavaSource(String sourceText) {
        StringBuilder filteredContent = new StringBuilder();
        int currentPosition = 0;
        int textLength = sourceText.length();

        while (currentPosition < textLength) {
            // Пропускаем начальные пробелы
            while (currentPosition < textLength &&
                    sourceText.charAt(currentPosition) <= ' ' &&
                    sourceText.charAt(currentPosition) != '\n') {
                currentPosition++;
            }

            if (currentPosition >= textLength) break;

            // Проверяем объявление пакета
            if (sourceText.startsWith("package", currentPosition) &&
                    isJavaKeyword(sourceText, currentPosition, "package")) {
                currentPosition = moveToNextLine(sourceText, currentPosition, textLength);
                continue;
            }

            // Проверяем импорты
            if (sourceText.startsWith("import", currentPosition) &&
                    isJavaKeyword(sourceText, currentPosition, "import")) {
                currentPosition = moveToNextLine(sourceText, currentPosition, textLength);
                continue;
            }

            // Копируем полезное содержимое
            int lineBeginning = currentPosition;
            while (currentPosition < textLength && sourceText.charAt(currentPosition) != '\n') {
                currentPosition++;
            }
            filteredContent.append(sourceText, lineBeginning, currentPosition);
            if (currentPosition < textLength) {
                filteredContent.append('\n');
                currentPosition++;
            }
        }

        return removeEdgeWhitespace(filteredContent.toString());
    }

    private static boolean isJavaKeyword(String text, int position, String keyword) {
        int endPos = position + keyword.length();
        return (endPos >= text.length() || Character.isWhitespace(text.charAt(endPos))) &&
                (position == 0 || Character.isWhitespace(text.charAt(position - 1)));
    }

    private static int moveToNextLine(String text, int currentPos, int maxLength) {
        while (currentPos < maxLength && text.charAt(currentPos) != '\n') {
            currentPos++;
        }
        return currentPos < maxLength ? currentPos + 1 : currentPos;
    }

    private static String removeEdgeWhitespace(String inputText) {
        int beginIndex = 0;
        int endIndex = inputText.length();

        while (beginIndex < endIndex && inputText.charAt(beginIndex) < 33) {
            beginIndex++;
        }

        while (endIndex > beginIndex && inputText.charAt(endIndex - 1) < 33) {
            endIndex--;
        }

        return inputText.substring(beginIndex, endIndex);
    }
}
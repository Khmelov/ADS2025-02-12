package by.it.group410901.bandarzheuskaya.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        List<FileInfo> fileInfos = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(src))) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String content = readFileIgnoringMalformed(p);

                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            content = removePackageAndImports(content);
                            content = removeComments(content);
                            content = trimLowChars(content);
                            content = removeEmptyLines(content);

                            long size = content.getBytes(StandardCharsets.UTF_8).length;
                            String relativePath = srcPathRelative(src, p.toFile());
                            fileInfos.add(new FileInfo(size, relativePath));

                        } catch (IOException e) {
                            System.err.println("Ошибка при чтении файла: " + p);
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка при обходе каталога src: " + e.getMessage());
        }

        fileInfos.sort(Comparator.comparingLong(FileInfo::size)
                .thenComparing(FileInfo::path));

        fileInfos.forEach(info -> System.out.println(info.size() + " " + info.path()));
    }

    private static String readFileIgnoringMalformed(Path path) throws IOException {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            return "";
        }
    }

    private static String removePackageAndImports(String content) {
        StringBuilder sb = new StringBuilder();
        for (String line : content.split("\n")) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("package") && !trimmed.startsWith("import")) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    private static String removeComments(String content) {
        StringBuilder sb = new StringBuilder();
        boolean inBlockComment = false;

        for (String line : content.split("\n")) {
            if (!inBlockComment) {
                int blockStart = line.indexOf("/*");
                int lineComment = line.indexOf("//");

                if (blockStart >= 0 && (lineComment == -1 || blockStart < lineComment)) {
                    inBlockComment = true;
                    sb.append(line, 0, blockStart);
                } else if (lineComment >= 0) {
                    sb.append(line, 0, lineComment);
                } else {
                    sb.append(line);
                }
            } else {
                int blockEnd = line.indexOf("*/");
                if (blockEnd >= 0) {
                    inBlockComment = false;
                    if (blockEnd + 2 < line.length()) {
                        sb.append(line, blockEnd + 2, line.length());
                    }
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static String trimLowChars(String content) {
        int start = 0, end = content.length() - 1;
        while (start <= end && content.charAt(start) < 33) start++;
        while (end >= start && content.charAt(end) < 33) end--;
        return start <= end ? content.substring(start, end + 1) : "";
    }

    private static String removeEmptyLines(String content) {
        StringBuilder sb = new StringBuilder();
        for (String line : content.split("\n")) {
            if (!line.trim().isEmpty()) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    private static String srcPathRelative(String src, File file) {
        Path srcPath = Paths.get(src).toAbsolutePath();
        Path filePath = file.toPath().toAbsolutePath();
        return srcPath.relativize(filePath).toString();
    }

    private record FileInfo(long size, String path) {}
}

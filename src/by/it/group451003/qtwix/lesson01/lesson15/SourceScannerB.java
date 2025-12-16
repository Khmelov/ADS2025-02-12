package by.it.group451003.qtwix.lesson01.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerB {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        List<FileInfo> fileInfos = new ArrayList<>();

        try (var walk = Files.walk(root)) {
            walk.filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String content = Files.readString(p, StandardCharsets.UTF_8);

                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            String processed = processContentB(content);

                            String relativePath = root.relativize(p).toString();

                            fileInfos.add(new FileInfo(relativePath, processed.getBytes(StandardCharsets.UTF_8).length));

                        } catch (IOException e) {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        fileInfos.sort(Comparator
                .comparingInt(FileInfo::getSize)
                .thenComparing(FileInfo::getPath));

        for (FileInfo info : fileInfos) {
            System.out.println(info.getSize() + " " + info.getPath());
        }
    }

    private static String processContentB(String content) {
        content = removePackageAndImports(content);

        content = removeComments(content);

        content = removeLowCharsFromStartAndEnd(content);

        content = removeEmptyLines(content);

        return content;
    }

    private static String removePackageAndImports(String content) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package") && !trimmedLine.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;
        boolean inChar = false;

        for (int i = 0; i < content.length(); i++) {
            char current = content.charAt(i);
            char next = (i < content.length() - 1) ? content.charAt(i + 1) : 0;

            if (inBlockComment) {
                if (current == '*' && next == '/') {
                    inBlockComment = false;
                    i++;
                }
                continue;
            }

            if (inLineComment) {
                if (current == '\n') {
                    inLineComment = false;
                    result.append(current);
                }
                continue;
            }

            if (inString) {
                result.append(current);
                if (current == '\\' && next == '"') {
                    result.append(next);
                    i++;
                } else if (current == '"') {
                    inString = false;
                }
                continue;
            }

            if (inChar) {
                result.append(current);
                if (current == '\\' && next == '\'') {
                    result.append(next);
                    i++;
                } else if (current == '\'') {
                    inChar = false;
                }
                continue;
            }

            if (current == '"') {
                inString = true;
                result.append(current);
            } else if (current == '\'') {
                inChar = true;
                result.append(current);
            } else if (current == '/' && next == '*') {
                inBlockComment = true;
                i++;
            } else if (current == '/' && next == '/') {
                inLineComment = true;
                i++;
            } else {
                result.append(current);
            }
        }

        return result.toString();
    }

    private static String removeLowCharsFromStartAndEnd(String str) {
        int start = 0;
        int end = str.length();

        while (start < end && str.charAt(start) < 33) {
            start++;
        }

        while (end > start && str.charAt(end - 1) < 33) {
            end--;
        }

        return str.substring(start, end);
    }

    private static String removeEmptyLines(String content) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static class FileInfo {
        private final String path;
        private final int size;

        public FileInfo(String path, int size) {
            this.path = path;
            this.size = size;
        }

        public String getPath() {
            return path;
        }

        public int getSize() {
            return size;
        }
    }
}


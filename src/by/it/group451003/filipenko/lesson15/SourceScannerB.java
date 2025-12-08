package by.it.group451003.filipenko.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {
    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Path.of(src);

        List<FileEntry> entries = new ArrayList<>();

        try (var walk = Files.walk(srcPath)) {
            walk.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String content = Files.readString(p);
                            if (!content.contains("@Test") && !content.contains("org.junit.Test")) {
                                // Удаляем package и imports
                                content = content.replaceAll("^\\s*package.*$", "")
                                        .replaceAll("^\\s*import.*$", "");

                                // Удаляем комментарии (упрощенно)
                                content = content.replaceAll("//.*", "")
                                        .replaceAll("/\\*.*?\\*/", "");

                                // Удаляем символы с кодом <33 по краям
                                content = content.replaceAll("^[\\x00-\\x20]+", "")
                                        .replaceAll("[\\x00-\\x20]+$", "");

                                // Удаляем пустые строки
                                content = content.replaceAll("(?m)^\\s*$\\n?", "");

                                String relativePath = srcPath.relativize(p).toString();
                                long size = content.getBytes(StandardCharsets.UTF_8).length;
                                entries.add(new FileEntry(relativePath, size));
                            }
                        } catch (IOException e) {
                            // Игнорируем
                        }
                    });
        } catch (IOException e) {
            return;
        }

        entries.sort((a, b) -> {
            int sizeComp = Long.compare(a.size, b.size);
            return sizeComp != 0 ? sizeComp : a.relativePath.compareTo(b.relativePath);
        });

        for (FileEntry entry : entries) {
            System.out.println(entry.size + " " + entry.relativePath);
        }
    }

    static class FileEntry {
        String relativePath;
        long size;

        FileEntry(String relativePath, long size) {
            this.relativePath = relativePath;
            this.size = size;
        }
    }
}
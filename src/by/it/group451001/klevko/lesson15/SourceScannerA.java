package by.it.group451001.klevko.lesson15;

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

public class SourceScannerA {

    static class FileInfo implements Comparable<FileInfo> {
        String relativePath;
        int size;

        FileInfo(String relativePath, int size) {
            this.relativePath = relativePath;
            this.size = size;
        }

        @Override
        public int compareTo(FileInfo other) {
            if (this.size != other.size) {
                return Integer.compare(this.size, other.size);
            }
            return this.relativePath.compareTo(other.relativePath);
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        Path rootPath = Path.of(src);
        List<FileInfo> fileInfos = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(rootPath)) {
            walk.filter(p -> p.toString().endsWith(".java")).forEach(p -> {
                try {
                    String content = readFileIgnoringErrors(p);

                    if (content.contains("@Test") || content.contains("org.junit.Test")) {
                        return;
                    }

                    String processed = processText(content);

                    int size = processed.getBytes(StandardCharsets.UTF_8).length;

                    String relativePath = rootPath.relativize(p).toString();

                    fileInfos.add(new FileInfo(relativePath, size));

                } catch (IOException e) { }});
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileInfos.sort(null);

        for (FileInfo info : fileInfos) {
            System.out.println(info.size + " " + info.relativePath);
        }
    }

    private static String readFileIgnoringErrors(Path path) throws IOException {
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
        decoder.onMalformedInput(CodingErrorAction.IGNORE);
        decoder.onUnmappableCharacter(CodingErrorAction.IGNORE);

        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static String processText(String text) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int len = text.length();

        while (i < len) {
            while (i < len && text.charAt(i) <= ' ') {
                if (text.charAt(i) == '\n') {
                    i++;
                    break;
                }
                i++;
            }

            if (i >= len) break;

            if (text.startsWith("package ", i)) {
                while (i < len && text.charAt(i) != '\n') {
                    i++;
                }
                if (i < len) i++;
                continue;
            }

            if (text.startsWith("import ", i)) {
                while (i < len && text.charAt(i) != '\n') {
                    i++;
                }
                if (i < len) i++;
                continue;
            }

            int lineStart = i;
            while (i < len && text.charAt(i) != '\n') i++;
            result.append(text, lineStart, i);
            if (i < len) {
                result.append('\n');
                i++;
            }
        }

        String trimmed = result.toString();
        int start = 0;
        int end = trimmed.length();

        while (start < end && trimmed.charAt(start) < 33) {
            start++;
        }

        while (end > start && trimmed.charAt(end - 1) < 33) {
            end--;
        }

        return trimmed.substring(start, end);
    }
}

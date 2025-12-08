package by.it.group451003.platonova.lesson15;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class SourceScannerC {

    public static void main(String[] args) {
        Path root = Path.of(System.getProperty("user.dir"), "src");

        try (Stream<Path> walk = Files.walk(root)) {
            walk.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .filter(SourceScannerC::notJUnitTest)
                    .forEach(p -> System.out.println(p.getFileName()));
        } catch (IOException ignored) {
        }
    }

    private static boolean notJUnitTest(Path p) {
        try {
            String s = Files.readString(p);
            return !s.contains("@Test") && !s.contains("org.junit.Test");
        } catch (IOException e) {
            return false;
        }
    }
}

package by.it.group451003.kharkevich.lesson15;

import by.it.HomeWork;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NewClassNamingConvention")
public class Test_Part2_Lesson15Test extends HomeWork {

    private static List<String> samples;

    @Test(timeout = 15000)
    public void testSourceScannerA() {
        HomeWork run = run("");
        for (String sample : getSamples()) {
            run.include(sample);
        }
    }

    @Test(timeout = 15000)
    public void testSourceScannerB() {
        HomeWork run = run("");
        for (String sample : getSamples()) {
            run.include(sample);
        }
    }

    @Test(timeout = 15000)
    public void testSourceScannerC() {
        run("").include("FiboA.java");
    }

    private static List<String> getSamples() {
        if (samples == null) {
            samples = lazyWalk();
        }
        return samples;
    }

    private static List<String> lazyWalk() {
        List<String> result = new ArrayList<>();
        Path root = Path.of(System.getProperty("user.dir")
                + File.separator + "src" + File.separator);
        try (var walk = Files.walk(root)) {
            walk.forEach(
                    p -> {
                        if (p.toString().endsWith(".java")) {
                            try {
                                String s = Files.readString(p);
                                if (!s.contains("@Test") && !s.contains("org.junit.Test")) {
                                    result.add(root.relativize(p).toString());
                                }
                            } catch (IOException e) {
                            }
                        }
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
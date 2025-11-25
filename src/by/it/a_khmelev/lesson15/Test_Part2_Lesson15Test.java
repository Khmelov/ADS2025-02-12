package by.it.a_khmelev.lesson15;

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

    @Test(timeout = 5000) //можно изменить под свою производительность
    public void testSourceScannerA() {
        HomeWork run = run("");
        for (String sample : lazyWalk()) {
            run.include(sample);
        }
    }

    @Test(timeout = 5000) //можно изменить под свою производительность
    public void testSourceScannerB() {
        HomeWork run = run("");
        for (String sample : lazyWalk()) {
            run.include(sample);
        }
    }


    @Test(timeout = 5000) //можно изменить под свою производительность
    public void testSourceScannerC() {
        run("").include("FiboA.java");
    }

    private static List<String> lazyWalk() {
        if (samples == null) {
            samples = new ArrayList<>();
            Path root = Path.of(System.getProperty("user.dir")
                                + File.separator + "src" + File.separator);
            try (var walk = Files.walk(root)) {
                walk.forEach(
                        p -> {
                            if (p.toString().endsWith(".java")) {
                                try {
                                    String s = Files.readString(p);
                                    if (!s.contains("@Test") && !s.contains("org.junit.Test")) {
                                        samples.add(root.relativize(p).toString());
                                    }
                                } catch (IOException e) {
                                    if (System.currentTimeMillis() < 0) {
                                        System.err.println(p);
                                    }
                                }
                            }
                        }
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return samples;
    }

}
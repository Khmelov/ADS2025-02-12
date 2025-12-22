package by.it.group451003.rashchenya.lesson15;

import by.it.HomeWork;
import org.junit.BeforeClass;
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

    @BeforeClass
    public static void init() throws IOException {
        samples = samples();
    }

    @Test(timeout = 5000) //можно изменить под свою производительность
    public void testSourceScannerA() {
        HomeWork run = run("");
        for (String sample : samples) {
            run.include(sample);
        }
    }

    @Test(timeout = 5000) //можно изменить под свою производительность
    public void testSourceScannerB() {
        HomeWork run = run("");
        for (String sample : samples) {
            run.include(sample);
        }
    }


    @Test(timeout = 5000) //можно изменить под свою производительность
    public void testSourceScannerC() {
        run("").include("FiboA.java");
    }

    private static List<String> samples() throws IOException {
        ArrayList<String> files = new ArrayList<>();
        Path root = Path.of(System.getProperty("user.dir")
                + File.separator + "src" + File.separator);
        try (var walk = Files.walk(root)) {
            walk.forEach(
                    p -> {
                        if (p.toString().endsWith(".java")) {
                            try {
                                String s = Files.readString(p);
                                if (!s.contains("@Test") && !s.contains("org.junit.Test")) {
                                    files.add(root.relativize(p).toString());
                                }
                            } catch (IOException e) {
                                if (System.currentTimeMillis() < 0) {
                                    System.err.println(p);
                                }
                            }
                        }
                    }
            );
        }
        return files;
    }

}
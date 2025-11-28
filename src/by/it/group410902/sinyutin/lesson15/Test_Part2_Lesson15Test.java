package by.it.group410902.sinyutin.lesson15;

import by.it.HomeWork;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import org.junit.Test;

public class Test_Part2_Lesson15Test extends HomeWork {
    private static List<String> samples;

    public Test_Part2_Lesson15Test() {
    }

    @Test(
            timeout = 5000L
    )
    public void testSourceScannerA() {
        HomeWork run = this.run("");
        Iterator var2 = lazyWalk().iterator();

        while(var2.hasNext()) {
            String sample = (String)var2.next();
            run.include(sample);
        }

    }

    @Test(
            timeout = 5000L
    )
    public void testSourceScannerB() {
        HomeWork run = this.run("");
        Iterator var2 = lazyWalk().iterator();

        while(var2.hasNext()) {
            String sample = (String)var2.next();
            run.include(sample);
        }

    }

    @Test(
            timeout = 5000L
    )
    public void testSourceScannerC() {
        this.run("").include("FiboA.java");
    }

    private static List<String> lazyWalk() {
        if (samples == null) {
            samples = new ArrayList();
            Path root = Path.of(System.getProperty("user.dir") + File.separator + "src" + File.separator);

            try {
                Stream<Path> walk = Files.walk(root);

                try {
                    walk.forEach((p) -> {
                        if (p.toString().endsWith(".java")) {
                            try {
                                String s = Files.readString(p);
                                if (!s.contains("@Test") && !s.contains("org.junit.Test")) {
                                    samples.add(root.relativize(p).toString());
                                }
                            } catch (IOException var3) {
                                if (System.currentTimeMillis() < 0L) {
                                    System.err.println(p);
                                }
                            }
                        }

                    });
                } catch (Throwable var5) {
                    if (walk != null) {
                        try {
                            walk.close();
                        } catch (Throwable var4) {
                            var5.addSuppressed(var4);
                        }
                    }

                    throw var5;
                }

                if (walk != null) {
                    walk.close();
                }
            } catch (IOException var6) {
                IOException e = var6;
                throw new RuntimeException(e);
            }
        }

        return samples;
    }
}

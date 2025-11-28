//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package by.it.group410902.sinyutin.lesson14;

import by.it.HomeWork;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import org.junit.Test;

public class Test_Part2_Lesson14 extends HomeWork {
    public static final int MAX_DISTANCE = 25;
    public static final int TEST_COUNT = 100;
    private int distance;
    private final Random random = new Random(123L);

    public Test_Part2_Lesson14() {
    }

    @Test(
            timeout = 5000L
    )
    public void testPointsA() {
        for(int i = 0; i < 100; ++i) {
            List<int[]> points = new ArrayList();
            String input = this.generatePointInput(points);
            String out = this.calculateTestOut(points, (x, y) -> {
                return Math.hypot(Math.hypot((double)(x[0] - y[0]), (double)(x[1] - y[1])), (double)(x[2] - y[2])) < (double)this.distance;
            });
            this.run(input).include(out);
        }

    }

    @Test(
            timeout = 5000L
    )
    public void testSitesB() {
        for(int i = 0; i < 100; ++i) {
            Set<String> sites = new HashSet();
            String input = this.generateSiteInput(sites);
            String out = this.calculateTestOut(new ArrayList(sites), (x, y) -> {
                return input.contains(x + "+" + y) || input.contains(y + "+" + x);
            });
            this.run(input).include(out);
        }

    }

    @Test(
            timeout = 5000L
    )
    public void testStatesHanoiTowerC() {
        this.run("1").include("1");
        this.run("2").include("1 2");
        this.run("3").include("1 2 4");
        this.run("4").include("1 4 10");
        this.run("5").include("1 4 8 18");
        this.run("10").include("1 4 38 64 252 324 340");
        this.run("21").include("1 4 82 152 1440 2448 14144 21760 80096 85120 116480 323232 380352 402556 669284");
    }

    private String generatePointInput(List<int[]> points) {
        StringBuilder out = new StringBuilder();
        this.distance = this.random.nextInt(25);
        int diapason = 1 + this.distance + this.random.nextInt(25) * this.random.nextInt(2);
        out.append(this.distance).append(" ");
        int n = 1 + this.random.nextInt(250);
        out.append(n);

        for(int i = 0; i < n; ++i) {
            int[] point = new int[]{this.random.nextInt(-diapason, diapason), this.random.nextInt(diapason), this.random.nextInt(diapason)};
            out.append('\n').append(point[0]).append(" ").append(point[1]).append(" ").append(point[2]);
            points.add(point);
        }

        out.append('\n');
        return out.toString();
    }

    private String generateSiteInput(Set<String> sites) {
        List<String> words = List.of("application java test hello world computer science course".split("\\s+"));
        List<String> zones = List.of("com org mobile net app io info ru by ua".split("\\s+"));
        StringJoiner out = new StringJoiner("");
        int pairCount = 5 + this.random.nextInt(50);

        for(int i = 0; i < pairCount * 2; ++i) {
            String var10000 = (String)words.get(this.random.nextInt(words.size()));
            String site = var10000 + "." + (String)zones.get(this.random.nextInt(words.size()));
            sites.add(site);
            out.add(site).add(i % 2 == 0 ? "+" : "\n");
        }

        out.add("end\n");
        return out.toString();
    }

    private <T> String calculateTestOut(List<T> elements, BiPredicate<T, T> checkUnion) {
        List<Set<T>> fakeDsu = new ArrayList();
        Iterator var4 = elements.iterator();

        while(var4.hasNext()) {
            T x = (T) var4.next();
            Set<T> set = new HashSet();
            set.add(x);
            fakeDsu.add(set);
        }

        for(int i = 0; i < fakeDsu.size(); ++i) {
            Iterator var13 = fakeDsu.iterator();

            while(var13.hasNext()) {
                Set<T> set = (Set)var13.next();
                boolean union = false;
                if (fakeDsu.get(i) != set) {
                    Iterator var8 = ((Set)fakeDsu.get(i)).iterator();

                    label46:
                    while(var8.hasNext()) {
                        T x = (T) var8.next();
                        Iterator var10 = set.iterator();

                        while(var10.hasNext()) {
                            T y = (T) var10.next();
                            if (x != y && checkUnion.test(x, y)) {
                                union = true;
                                if (true) {
                                    break label46;
                                }
                            }
                        }
                    }
                }

                if (union) {
                    ((Set)fakeDsu.get(i)).addAll(set);
                    set.clear();
                    i = 0;
                }
            }
        }

        fakeDsu.removeIf(Set::isEmpty);
        return ((String)fakeDsu.stream().map(Set::size).sorted((n, m) -> {
            return m - n;
        }).map(String::valueOf).collect(Collectors.joining(" "))).trim();
    }
}

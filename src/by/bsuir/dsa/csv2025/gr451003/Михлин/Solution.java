package by.bsuir.dsa.csv2025.gr451003.Михлин;

import java.io.*;
import java.util.*;
import static java.lang.Math.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

// ============================================================
//  SINGLE-CLASS SOLUTION WITH JUNIT4 TESTS INSIDE
// ============================================================
@RunWith(Enclosed.class)
public class Solution {

    // -----------------------------------------------
    // Point structure
    // -----------------------------------------------
    public static class Point {
        public final double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Point)) return false;
            Point p = (Point) o;
            return abs(p.x - x) < 1e-6 && abs(p.y - y) < 1e-6;
        }

        @Override
        public String toString() {
            return String.format(Locale.US, "%.3f %.3f", x, y);
        }
    }

    // -----------------------------------------------
    // Douglas–Peucker simplification
    // -----------------------------------------------
    public static List<Point> simplify(List<Point> pts, double eps) {
        if (pts.size() <= 2) return pts;

        boolean[] keep = new boolean[pts.size()];
        keep[0] = true;
        keep[pts.size() - 1] = true;

        dp(pts, keep, 0, pts.size() - 1, eps);

        List<Point> out = new ArrayList<>();
        for (int i = 0; i < pts.size(); i++) {
            if (keep[i]) out.add(pts.get(i));
        }
        return out;
    }

    private static void dp(List<Point> pts, boolean[] keep, int l, int r, double eps) {
        if (r - l < 2) return;

        double maxDist = -1;
        int idx = -1;

        Point A = pts.get(l);
        Point B = pts.get(r);

        for (int i = l + 1; i < r; i++) {
            double d = dist(pts.get(i), A, B);
            if (d > maxDist) {
                maxDist = d;
                idx = i;
            }
        }

        if (maxDist > eps) {
            keep[idx] = true;
            dp(pts, keep, l, idx, eps);
            dp(pts, keep, idx, r, eps);
        }
    }

    private static double dist(Point p, Point A, Point B) {
        double dx = B.x - A.x;
        double dy = B.y - A.y;

        if (abs(dx) < 1e-12 && abs(dy) < 1e-12)
            return hypot(p.x - A.x, p.y - A.y);

        double t = ((p.x - A.x) * dx + (p.y - A.y) * dy) / (dx * dx + dy * dy);
        double px = A.x + t * dx;
        double py = A.y + t * dy;
        return hypot(p.x - px, p.y - py);
    }

    // -----------------------------------------------
    // Console main
    // -----------------------------------------------
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] first = br.readLine().split("\\s+");
        int N = Integer.parseInt(first[0]);
        double eps = Double.parseDouble(first[1]);

        List<Point> pts = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            String[] s = br.readLine().split("\\s+");
            pts.add(new Point(Double.parseDouble(s[0]), Double.parseDouble(s[1])));
        }

        List<Point> out = simplify(pts, eps);

        System.out.println(out.size());
        for (Point p : out) System.out.println(p);
    }


    // ============================================================
    //  JUNIT4 TESTS (STATIC INNER CLASS)
    // ============================================================
    public static class Tests {

        @Test
        public void testCollinear() {
            List<Point> in = Arrays.asList(
                    new Point(0,0), new Point(1,1),
                    new Point(2,2), new Point(3,3)
            );
            List<Point> out = simplify(in, 0.01);
            assertEquals(2, out.size());
            assertEquals(new Point(0,0), out.get(0));
            assertEquals(new Point(3,3), out.get(1));
        }

        @Test
        public void testVShape() {
            List<Point> in = Arrays.asList(
                    new Point(0,0),
                    new Point(1,1),
                    new Point(2,0)
            );
            assertEquals(in, simplify(in, 0.2));
        }

        @Test
        public void testLargeEps() {
            List<Point> in = Arrays.asList(
                    new Point(0,0), new Point(1,1),
                    new Point(2,2), new Point(3,3)
            );
            List<Point> out = simplify(in, 100);
            assertEquals(2, out.size());
        }

        @Test
        public void testZeroEps() {
            List<Point> in = Arrays.asList(
                    new Point(0,0),
                    new Point(1,3),
                    new Point(2,6)
            );
            assertEquals(in, simplify(in, 0));
        }

        @Test
        public void testSquare() {
            List<Point> in = Arrays.asList(
                    new Point(0,0), new Point(0,1),
                    new Point(1,1), new Point(1,0),
                    new Point(0,0)
            );
            List<Point> out = simplify(in, 0.3);
            assertEquals(5, out.size());
        }

        @Test
        public void testSaw() {
            List<Point> in = Arrays.asList(
                    new Point(0,0), new Point(1,1),
                    new Point(2,0), new Point(3,1),
                    new Point(4,0)
            );
            assertEquals(in, simplify(in, 0.1));
        }

        @Test
        public void testFlatThenAngle() {
            List<Point> in = Arrays.asList(
                    new Point(0,0), new Point(1,0),
                    new Point(2,0), new Point(3,2)
            );
            List<Point> out = simplify(in, 0.01);
            assertEquals(3, out.size());
        }

        @Test
        public void testRepeatingPoints() {
            List<Point> in = Arrays.asList(
                    new Point(0,0), new Point(0,0),
                    new Point(1,1), new Point(2,2),
                    new Point(2,2)
            );
            List<Point> out = simplify(in, 0.05);
            assertEquals(2, out.size());
        }

        @Test
        public void testClosedNotEqualEndpoints() {
            List<Point> in = Arrays.asList(
                    new Point(0,0),
                    new Point(0,1),
                    new Point(1,1),
                    new Point(1,0)
            );
            List<Point> out = simplify(in, 0.01);
            assertEquals(4, out.size());
        }

        @Test
        public void testSharpAngle() {
            List<Point> in = Arrays.asList(
                    new Point(0,0),
                    new Point(1,5),
                    new Point(2,0)
            );
            List<Point> out = simplify(in, 0.5);
            assertEquals(3, out.size());
        }
    }
}

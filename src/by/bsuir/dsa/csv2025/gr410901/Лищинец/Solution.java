package by.bsuir.dsa.csv2025.gr410901.Лищинец;



import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class Solution {

    private final Set<Long> coordinatesSet = new HashSet<>();
    private long[] sortedCoordinates = null;
    private boolean compressed = false;

    // Только один конструктор — обязательно для JUnit 4
    public Solution() {}

    public boolean addCoordinate(long coordinate) {
        if (compressed) throw new IllegalStateException();
        return coordinatesSet.add(coordinate);
    }

    public void addCoordinates(long[] coordinates) {
        if (compressed) throw new IllegalStateException();
        for (long coord : coordinates) coordinatesSet.add(coord);
    }

    public void compress() {
        sortedCoordinates = new long[coordinatesSet.size()];
        int i = 0;
        for (Long v : coordinatesSet) sortedCoordinates[i++] = v;
        Arrays.sort(sortedCoordinates);
        compressed = true;
    }

    public int compressCoordinate(long coordinate) {
        if (!compressed) throw new IllegalStateException();
        int idx = Arrays.binarySearch(sortedCoordinates, coordinate);
        if (idx < 0) throw new IllegalArgumentException();
        return idx;
    }

    public long restoreCoordinate(int compressedIndex) {
        if (!compressed) throw new IllegalStateException();
        if (compressedIndex < 0 || compressedIndex >= sortedCoordinates.length)
            throw new IndexOutOfBoundsException();
        return sortedCoordinates[compressedIndex];
    }

    public int[] getCompressedRange(long from, long to) {
        if (!compressed) throw new IllegalStateException();
        int start = lowerBound(from);
        int end = upperBound(to) - 1;
        return start <= end ? new int[]{start, end} : null;
    }

    public int size() { return coordinatesSet.size(); }

    public void clear() {
        coordinatesSet.clear();
        sortedCoordinates = null;
        compressed = false;
    }

    // Чистый вывод: только A-Z a-z 0-9 пробелы переносы и дефис
    @Override
    public String toString() {
        if (!compressed) {
            return "not compressed " + size();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("compressed ").append(sortedCoordinates.length).append('\n');
        for (int i = 0; i < sortedCoordinates.length; i++) {
            sb.append(sortedCoordinates[i]).append(" - ").append(i).append('\n');
        }
        return sb.toString();
    }

    private int lowerBound(long value) {
        int l = 0, r = sortedCoordinates.length;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (sortedCoordinates[m] < value) l = m + 1;
            else r = m;
        }
        return l;
    }

    private int upperBound(long value) {
        int l = 0, r = sortedCoordinates.length;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (sortedCoordinates[m] <= value) l = m + 1;
            else r = m;
        }
        return l;
    }

    // =====================================================================
    // JUnit 4 тесты — работают в IntelliJ IDEA
    // =====================================================================

    @Test
    public void testBasicCompression() {
        Solution c = new Solution();
        c.addCoordinate(1000L);
        c.addCoordinate(100L);
        c.addCoordinate(10L);
        c.addCoordinate(100L);
        c.compress();
        assertEquals(0, c.compressCoordinate(10L));
        assertEquals(1, c.compressCoordinate(100L));
        assertEquals(2, c.compressCoordinate(1000L));
    }

    @Test
    public void testRangeCompression() {
        Solution c = new Solution();
        c.addCoordinates(new long[]{5, 1, 8, 3, 10, 2, 7});
        c.compress();
        int[] r = c.getCompressedRange(3, 8);
        assertNotNull(r);
        assertEquals(2, r[0]);
        assertEquals(5, r[1]);
        assertNull(c.getCompressedRange(20, 30));
    }

    @Test
    public void testLargeNumbers() {
        Solution c = new Solution();
        c.addCoordinate(-1000000000L);
        c.addCoordinate(1000000000L);
        c.addCoordinate(0L);
        c.compress();
        assertEquals(-1000000000L, c.restoreCoordinate(0));
        assertEquals(1000000000L, c.restoreCoordinate(2));
    }

    @Test
    public void testAddAfterCompression() {
        Solution c = new Solution();
        c.addCoordinate(10L);
        c.compress();
        try {
            c.addCoordinate(20L);
            fail();
        } catch (IllegalStateException ignored) {}
    }

    @Test
    public void testUnknownCoordinate() {
        Solution c = new Solution();
        c.addCoordinate(10L);
        c.compress();
        try {
            c.compressCoordinate(999L);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testClearAndReuse() {
        Solution c = new Solution();
        c.addCoordinate(42L);
        c.compress();
        c.clear();
        c.addCoordinate(100L);
        c.compress();
        assertEquals(100L, c.restoreCoordinate(0));
    }

    // =====================================================================
    // main — только английский текст и разрешённые символы
    // =====================================================================

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Solution c = new Solution();

        System.out.println("enter coordinates");

        while (true) {
            System.out.print("> ");
            String line = sc.nextLine().trim();
            if (line.isEmpty()) break;
            try {
                c.addCoordinate(Long.parseLong(line));
            } catch (NumberFormatException e) {
                System.out.println("invalid number");
            }
        }

        if (c.size() == 0) {
            System.out.println("no data");
            return;
        }

        c.compress();
        System.out.print(c);

        System.out.print("range from to: ");
        String input = sc.nextLine().trim();
        if (!input.isEmpty()) {
            String[] p = input.split("\\s+");
            if (p.length == 2) {
                try {
                    long from = Long.parseLong(p[0]);
                    long to = Long.parseLong(p[1]);
                    int[] r = c.getCompressedRange(from, to);
                    if (r == null) {
                        System.out.println("empty");
                    } else {
                        for (int i = r[0]; i <= r[1]; i++) {
                            System.out.println(c.restoreCoordinate(i) + " - " + i);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("bad range");
                }
            }
        }
        sc.close();
    }
}
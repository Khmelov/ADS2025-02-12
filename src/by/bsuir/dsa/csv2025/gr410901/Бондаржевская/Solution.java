package by.bsuir.dsa.csv2025.gr410901.Бондаржевская;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

@SuppressWarnings("all")
public class Solution {
    static class Query implements Comparable<Query> {
        int L, R;
        int time;
        int queryIndex;
        int blockL, blockTime;

        public Query(int L, int R, int time, int queryIndex, int blockSize) {
            this.L = L;
            this.R = R;
            this.time = time;
            this.queryIndex = queryIndex;
            this.blockL = L / blockSize;
            this.blockTime = time / blockSize;
        }

        @Override
        public int compareTo(Query other) {
            if (this.blockTime != other.blockTime) {
                return Integer.compare(this.blockTime, other.blockTime);
            }
            if (this.blockL != other.blockL) {
                return Integer.compare(this.blockL, other.blockL);
            }
            return Integer.compare(this.R, other.R);
        }
    }

    static class PriceUpdate {
        int productPos;
        int oldPrice;
        int newPrice;

        public PriceUpdate(int productPos, int oldPrice, int newPrice) {
            this.productPos = productPos;
            this.oldPrice = oldPrice;
            this.newPrice = newPrice;
        }
    }

    static class QueryResult {
        int median;
        int countAboveMedian;

        public QueryResult(int median, int countAboveMedian) {
            this.median = median;
            this.countAboveMedian = countAboveMedian;
        }
    }

    static int[] prices;
    static Map<Integer, Integer> priceFrequency;
    static List<PriceUpdate> updates;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out);

        int n = Integer.parseInt(br.readLine().trim());

        prices = new int[n];
        String[] priceData = br.readLine().trim().split("\\s+");
        for (int i = 0; i < n; i++) {
            prices[i] = Integer.parseInt(priceData[i]);
        }

        int[] initialPrices = prices.clone();

        List<Query> queries = new ArrayList<>();
        updates = new ArrayList<>();
        priceFrequency = new HashMap<>();
        int queryCount = 0;
        int updateCount = 0;

        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] command = line.split("\\s+");
            String action = command[0];

            if (action.equalsIgnoreCase("END")) {
                break;
            } else if (action.equalsIgnoreCase("Update")) {
                int pos = Integer.parseInt(command[1]);
                int newPrice = Integer.parseInt(command[2]);

                updates.add(new PriceUpdate(pos, prices[pos], newPrice));
                prices[pos] = newPrice;
                updateCount++;

            } else if (action.equalsIgnoreCase("Query")) {
                int L = Integer.parseInt(command[1]);
                int R = Integer.parseInt(command[2]);

                int blockSize = Math.max(1, (int) Math.pow(n, 2.0 / 3.0) + 1);
                queries.add(new Query(L, R, updateCount, queryCount++, blockSize));
            }
        }

        if (queries.isEmpty()) {
            pw.close();
            br.close();
            return;
        }

        prices = initialPrices.clone();
        priceFrequency.clear();

        queries.sort(Query::compareTo);

        QueryResult[] results = new QueryResult[queryCount];

        int currentL = 0;
        int currentR = -1;
        int currentTime = 0;

        for (Query query : queries) {
            while (currentTime < query.time) {
                applyUpdate(currentTime, currentL, currentR);
                currentTime++;
            }

            while (currentTime > query.time) {
                currentTime--;
                revertUpdate(currentTime, currentL, currentR);
            }

            while (currentR < query.R) {
                currentR++;
                addPrice(prices[currentR]);
            }

            while (currentR > query.R) {
                removePrice(prices[currentR]);
                currentR--;
            }

            while (currentL < query.L) {
                removePrice(prices[currentL]);
                currentL++;
            }

            while (currentL > query.L) {
                currentL--;
                addPrice(prices[currentL]);
            }

            results[query.queryIndex] = calculateMedianAndCount(query.L, query.R);
        }

        for (int i = 0; i < queryCount; i++) {
            pw.println(results[i].median);
            pw.println(results[i].countAboveMedian);
        }

        pw.flush();
        pw.close();
        br.close();
    }

    static QueryResult calculateMedianAndCount(int L, int R) {
        TreeSet<Integer> uniquePrices = new TreeSet<>(priceFrequency.keySet());

        List<Integer> sortedPrices = new ArrayList<>(uniquePrices);
        int n = sortedPrices.size();

        int median;
        if (n % 2 == 1) {
            median = sortedPrices.get(n / 2);
        } else {
            int mid1 = sortedPrices.get(n / 2 - 1);
            int mid2 = sortedPrices.get(n / 2);
            median = (mid1 + mid2) / 2;
        }

        int countAboveMedian = 0;
        for (int i = L; i <= R; i++) {
            if (prices[i] >= median) {
                countAboveMedian++;
            }
        }

        return new QueryResult(median, countAboveMedian);
    }

    static void addPrice(int price) {
        int currentFreq = priceFrequency.getOrDefault(price, 0);
        priceFrequency.put(price, currentFreq + 1);
    }

    static void removePrice(int price) {
        int currentFreq = priceFrequency.get(price);
        currentFreq--;
        if (currentFreq == 0) {
            priceFrequency.remove(price);
        } else {
            priceFrequency.put(price, currentFreq);
        }
    }

    static void applyUpdate(int updateIdx, int currentL, int currentR) {
        PriceUpdate update = updates.get(updateIdx);

        if (update.productPos >= currentL && update.productPos <= currentR) {
            removePrice(update.oldPrice);
            addPrice(update.newPrice);
        }

        prices[update.productPos] = update.newPrice;
    }

    static void revertUpdate(int updateIdx, int currentL, int currentR) {
        PriceUpdate update = updates.get(updateIdx);

        if (update.productPos >= currentL && update.productPos <= currentR) {
            removePrice(update.newPrice);
            addPrice(update.oldPrice);
        }

        prices[update.productPos] = update.oldPrice;
    }


    @Test
    public void testSingleQuerySingleUpdate() throws IOException {
        String input = "3\n1 2 3\nUpdate 1 5\nQuery 0 2\nEND\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(in); System.setOut(new PrintStream(out));
        main(new String[]{});
        String[] lines = out.toString().trim().split("\\R");
        assertEquals("3", lines[0]);
        assertEquals("2", lines[1]);
    }

    @Test
    public void testUpdateOutsideQuery() throws IOException {
        String input = "4\n5 5 5 5\nUpdate 0 100\nQuery 1 3\nEND\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(in); System.setOut(new PrintStream(out));
        main(new String[]{});
        String[] lines = out.toString().trim().split("\\R");
        assertEquals("5", lines[0]);
        assertEquals("3", lines[1]);
    }

    @Test
    public void testInterleaved() throws IOException {
        String input =
                "5\n1 2 3 4 5\n" +
                        "Query 0 2\n" +
                        "Update 1 100\n" +
                        "Query 0 2\n" +
                        "Update 2 200\n" +
                        "Query 0 4\n" +
                        "END\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(in); System.setOut(new PrintStream(out));
        main(new String[]{});
        String[] lines = out.toString().trim().split("\\R");
        assertEquals("2", lines[0]);
        assertEquals("2", lines[1]);
        assertEquals("3", lines[2]);
        assertEquals("2", lines[3]);
        assertEquals("5", lines[4]);
        assertEquals("3", lines[5]);
    }

    @Test
    public void testMultipleQueriesMultipleUpdates() throws IOException {
        String input = "5\n1 2 3 4 5\nUpdate 2 10\nQuery 1 3\nUpdate 4 1\nQuery 0 4\nEND\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(in); System.setOut(new PrintStream(out));
        main(new String[]{});
        String[] lines = out.toString().trim().split("\\R");
        assertEquals("4", lines[0]);
        assertEquals("2", lines[1]);
        assertEquals("3", lines[2]);
        assertEquals("2", lines[3]);
    }

    @Test
    public void testRepeatsAndMixedUpdates() throws IOException {
        String input =
                "6\n2 2 2 10 10 20\n" +
                        "Query 0 5\n" +
                        "Update 1 5\n" +
                        "Query 0 3\n" +
                        "Update 4 1\n" +
                        "Query 2 4\n" +
                        "END\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(in); System.setOut(new PrintStream(out));
        main(new String[]{});
        String[] lines = out.toString().trim().split("\\R");
        assertEquals("10", lines[0]);
        assertEquals("3",  lines[1]);
        assertEquals("5",  lines[2]);
        assertEquals("2",  lines[3]);
        assertEquals("2",  lines[4]);
        assertEquals("2",  lines[5]);
    }

}

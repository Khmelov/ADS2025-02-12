package by.bsuir.dsa.csv2025.gr410902.Габрукович;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;
public class Solution {
    static final long MOD1 = 1_000_000_007L;
    static final long MOD2 = 1_000_000_009L;
    static final long BASE = 91138233L;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String S = sc.next();
        int n = S.length();

        int Q = sc.nextInt();

        while (Q-- > 0) {
            int L1 = sc.nextInt();
            int R1 = sc.nextInt();
            int L2 = sc.nextInt();
            int R2 = sc.nextInt();

            int len1 = R1 - L1 + 1;
            int len2 = R2 - L2 + 1;

            if (len1 != len2) {
                System.out.println("NO");
                continue;
            }

            int len = len1;

            long h1_1 = 0, h1_2 = 0;
            long h2_1 = 0, h2_2 = 0;

            int[] map1 = new int[256];
            int[] map2 = new int[256];
            Arrays.fill(map1, -1);
            Arrays.fill(map2, -1);

            int nextCode1 = 0, nextCode2 = 0;

            for (int i = 0; i < len; i++) {
                char c1 = S.charAt(L1 - 1 + i);
                char c2 = S.charAt(L2 - 1 + i);

                if (map1[c1] == -1) map1[c1] = nextCode1++;
                if (map2[c2] == -1) map2[c2] = nextCode2++;

                long v1 = map1[c1];
                long v2 = map2[c2];

                h1_1 = (h1_1 * BASE + v1) % MOD1;
                h1_2 = (h1_2 * BASE + v1) % MOD2;

                h2_1 = (h2_1 * BASE + v2) % MOD1;
                h2_2 = (h2_2 * BASE + v2) % MOD2;
            }

            if (h1_1 == h2_1 && h1_2 == h2_2)
                System.out.println("YES");
            else
                System.out.println("NO");
        }
    }
    @Test
    public void testIsomorphicSubstrings() throws Exception {
        String input = """
                abca
                5
                1 3 2 4
                1 4 1 4
                1 2 3 4
                2 3 1 2
                1 3 1 4
                """;

        String expected = """
                YES
                YES
                YES
                YES
                NO
                """.trim();

        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Solution.main(new String[]{});

        String actual = out.toString().trim();
        assertEquals(expected, actual);
    }
}

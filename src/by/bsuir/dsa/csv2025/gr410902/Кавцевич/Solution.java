package by.bsuir.dsa.csv2025.gr410902.Кавцевич;

import org.junit.Test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;
import static org.junit.Assert.assertArrayEquals;

public class Solution
{
    static class SqrtDecomposition
    {
        int n;
        int B;
        int blocks;
        long[] a;

        long[] sum;
        long[] min;
        long[] max;

        SqrtDecomposition(long[] arr)
        {
            this.n = arr.length - 1;
            this.a = arr;

            B = (int) Math.sqrt(n) + 1;
            blocks = (n + B - 1) / B;

            sum = new long[blocks];
            min = new long[blocks];
            max = new long[blocks];

            Arrays.fill(min, Long.MAX_VALUE);
            Arrays.fill(max, Long.MIN_VALUE);

            for (int i = 1; i <= n; i++)
            {
                int b = (i - 1) / B;
                sum[b] += a[i];
                min[b] = Math.min(min[b], a[i]);
                max[b] = Math.max(max[b], a[i]);
            }
        }

        void update(int i, long x)
        {
            int b = (i - 1) / B;

            sum[b] -= a[i];
            sum[b] += x;
            a[i] = x;

            int l = b * B + 1;
            int r = Math.min(n, (b + 1) * B);

            min[b] = Long.MAX_VALUE;
            max[b] = Long.MIN_VALUE;

            for (int j = l; j <= r; j++)
            {
                min[b] = Math.min(min[b], a[j]);
                max[b] = Math.max(max[b], a[j]);
            }
        }

        long querySum(int l, int r)
        {
            long ans = 0;
            int bl = (l - 1) / B;
            int br = (r - 1) / B;

            if (bl == br)
            {
                for (int i = l; i <= r; i++) ans += a[i];
                return ans;
            }

            for (int i = l; i <= (bl + 1) * B; i++) ans += a[i];
            for (int b = bl + 1; b < br; b++) ans += sum[b];
            for (int i = br * B + 1; i <= r; i++) ans += a[i];

            return ans;
        }

        long queryMin(int l, int r)
        {
            long ans = Long.MAX_VALUE;
            int bl = (l - 1) / B;
            int br = (r - 1) / B;

            if (bl == br)
            {
                for (int i = l; i <= r; i++) ans = Math.min(ans, a[i]);
                return ans;
            }

            for (int i = l; i <= (bl + 1) * B; i++) ans = Math.min(ans, a[i]);
            for (int b = bl + 1; b < br; b++) ans = Math.min(ans, min[b]);
            for (int i = br * B + 1; i <= r; i++) ans = Math.min(ans, a[i]);

            return ans;
        }

        long queryMax(int l, int r)
        {
            long ans = Long.MIN_VALUE;
            int bl = (l - 1) / B;
            int br = (r - 1) / B;

            if (bl == br)
            {
                for (int i = l; i <= r; i++) ans = Math.max(ans, a[i]);
                return ans;
            }

            for (int i = l; i <= (bl + 1) * B; i++) ans = Math.max(ans, a[i]);
            for (int b = bl + 1; b < br; b++) ans = Math.max(ans, max[b]);
            for (int i = br * B + 1; i <= r; i++) ans = Math.max(ans, a[i]);

            return ans;
        }
    }

    public static void main(String[] args) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        // читаем N и Q
        st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());

        long[] a = new long[n + 1];
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++)
        {
            a[i] = Long.parseLong(st.nextToken());
        }

        SqrtDecomposition sq = new SqrtDecomposition(a);

        StringBuilder output = new StringBuilder();

        while (q-- > 0)
        {
            st = new StringTokenizer(br.readLine());
            int type = Integer.parseInt(st.nextToken());

            if (type == 1)
            {
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                output.append(sq.querySum(l, r)).append("\n");
            }
            else if (type == 2)
            {
                int i = Integer.parseInt(st.nextToken());
                long x = Long.parseLong(st.nextToken());
                sq.update(i, x);
            }
            else if (type == 3)
            {
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                output.append(sq.queryMin(l, r)).append("\n");
            }
            else if (type == 4)
            {
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                output.append(sq.queryMax(l, r)).append("\n");
            }
        }

        System.out.print(output);
    }

    @Test
    public void checkSqrtDecomposition() 
    {

        long[] data = {0, 1, 2, 3, 4, 5};
        SqrtDecomposition sq = new SqrtDecomposition(data);

        long[] results = new long[7];
        int idx = 0;

        results[idx++] = sq.querySum(1, 5);

        results[idx++] = sq.queryMin(2, 4);

        results[idx++] = sq.queryMax(1, 3);

        sq.update(3, 10);

        results[idx++] = sq.querySum(1, 5);
        results[idx++] = sq.queryMin(1, 5);
        results[idx++] = sq.queryMax(1, 5);
        results[idx++] = sq.querySum(2, 4);

        long[] expected = {15, 2, 3, 22, 1, 10, 16};

        assertArrayEquals("SqrtDecomposition test failed", expected, results);
    }
}

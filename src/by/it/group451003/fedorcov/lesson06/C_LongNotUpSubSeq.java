package by.it.group451003.fedorcov.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        int[] tailIndices = new int[n];
        int[] prevIndices = new int[n];
        Arrays.fill(prevIndices, -1);
        int len = 0;

        for (int i = 0; i < n; i++) {
            int left = 0;
            int right = len;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (m[tailIndices[mid]] >= m[i]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            if (left < len && m[tailIndices[left]] < m[i]) {
                prevIndices[i] = tailIndices[left - 1];
                tailIndices[left] = i;
            } else {
                if (left > 0) {
                    prevIndices[i] = tailIndices[left - 1];
                }
                tailIndices[left] = i;
            }
            if (left == len) {
                len++;
            }
        }

        int[] resultIndices = new int[len];
        int current = tailIndices[len - 1];
        for (int i = len - 1; i >= 0; i--) {
            resultIndices[i] = current + 1;
            current = prevIndices[current];
        }

        System.out.println(len);
        for (int index : resultIndices) {
            System.out.print(index + " ");
        }
        System.out.println();

        return len;
    }
}
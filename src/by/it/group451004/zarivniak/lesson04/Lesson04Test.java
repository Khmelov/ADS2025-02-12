package by.it.group451004.zarivniak.lesson04;


import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class Lesson04Test {
    @Test
    public void checkA() throws Exception {
        InputStream inputStream = A_BinaryFind.class.getResourceAsStream("dataA.txt");
        A_BinaryFind instance = new A_BinaryFind();
        int[] result = instance.findIndex(inputStream);
        StringBuilder sb = new StringBuilder();
        for (int index : result) {
            sb.append(index).append(" ");
        }
        boolean ok = sb.toString().trim().equals("3 1 -1 1 -1");
        assertTrue("A failed", ok);
    }


    @Test
    public void checkB() throws Exception {
        InputStream inputStream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        int[] result = instance.getMergeSort(inputStream);
        boolean ok = result.length > 3;
        int test[] = new int[result.length];
        System.arraycopy(result, 0, test, 0, result.length);
        Arrays.sort(test);
        for (int i = 0; i < result.length; i++) {
            ok = ok && (result[i] == test[i]);
        }
        assertTrue("B failed", ok);
    }


    @Test
    public void checkC() throws Exception {
        InputStream inputStream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        int result = instance.calc(inputStream);
        boolean ok = (2 == result);
        assertTrue("C failed", ok);

    }

}

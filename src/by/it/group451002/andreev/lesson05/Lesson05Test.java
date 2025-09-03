package by.it.group451002.andreev.lesson05;

import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class Lesson05Test {
    @Test
    public void checkA() throws Exception {
        InputStream inputStream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(inputStream);
        boolean ok = Arrays.equals(result, new int[]{1, 0, 0});
        assertTrue("A failed", ok);
    }


    @Test
    public void checkB() throws Exception {
        InputStream inputStream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(inputStream);
        boolean ok = Arrays.equals(result, new int[]{2, 2, 3, 9, 9});
        assertTrue("B failed", ok);
    }


    @Test
    public void checkC() throws Exception {
        InputStream inputStream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(inputStream);
        boolean ok = Arrays.equals(result, new int[]{1, 0, 0});
        assertTrue("C failed", ok);
    }

}

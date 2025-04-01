package by.it.group410902.kukhto.les4;


import by.it.group410902.kukhto.les4.A_BinaryFind;
import by.it.group410902.kukhto.les4.B_MergeSort;
import by.it.group410902.kukhto.les4.C_GetInversions;
import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class Lesson04Test {
    @Test
    public void checkA() throws Exception {
        InputStream inputStream = by.it.group410902.kukhto.les4.A_BinaryFind.class.getResourceAsStream("dataA.txt");
        by.it.group410902.kukhto.les4.A_BinaryFind instance = new A_BinaryFind();
        //long startTime = System.currentTimeMillis();
        int[] result = instance.findIndex(inputStream);
        //long finishTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int index : result) {
            sb.append(index).append(" ");
        }
        boolean ok = sb.toString().trim().equals("3 1 -1 1 -1");
        assertTrue("A failed", ok);
    }


    @Test
    public void checkB() throws Exception {
        InputStream inputStream = by.it.group410902.kukhto.les4.B_MergeSort.class.getResourceAsStream("dataB.txt");
        by.it.group410902.kukhto.les4.B_MergeSort instance = new B_MergeSort();
        //long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(inputStream);
        //long finishTime = System.currentTimeMillis();
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
        InputStream inputStream = by.it.group410902.kukhto.les4.C_GetInversions.class.getResourceAsStream("dataC.txt");
        by.it.group410902.kukhto.les4.C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(inputStream);
        //long finishTime = System.currentTimeMillis();
        boolean ok = (2 == result);
        assertTrue("C failed", ok);

    }

}

package by.it.group410902.kukhto.les8;

import by.it.group410902.kukhto.les8.A_Knapsack;
import by.it.group410902.kukhto.les8.B_Knapsack;
import by.it.group410902.kukhto.les8.C_Stairs;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class Lesson08Test {
    @Test
    public void A() throws Exception {
        InputStream stream = by.it.group410902.kukhto.les8.A_Knapsack.class.getResourceAsStream("dataA.txt");
        by.it.group410902.kukhto.les8.A_Knapsack instance = new A_Knapsack();
        int res=instance.getMaxWeight(stream);
        assertEquals("A failed", res, 14);
    }

    @Test
    public void B() throws Exception {
        InputStream stream = by.it.group410902.kukhto.les8.B_Knapsack.class.getResourceAsStream("dataB.txt");
        by.it.group410902.kukhto.les8.B_Knapsack instance = new B_Knapsack();
        int res=instance.getMaxWeight(stream);
        assertEquals("B failed", res, 9);
    }

    @Test
    public void C() throws Exception {
        InputStream stream = by.it.group410902.kukhto.les8.C_Stairs.class.getResourceAsStream("dataC.txt");
        by.it.group410902.kukhto.les8.C_Stairs instance = new C_Stairs();
        int res=instance.getMaxSum(stream);
        assertEquals("C failed", res, 3);
    }

}

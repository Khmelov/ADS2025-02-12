package by.it.group410902.kukhto.les6;

import by.it.group410902.kukhto.les6.A_LIS;
import by.it.group410902.kukhto.les6.B_LongDivComSubSeq;
import by.it.group410902.kukhto.les6.C_LongNotUpSubSeq;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertTrue;

public class Lesson06Test {
    @Test
    public void checkA() throws Exception {
        InputStream inputStream = by.it.group410902.kukhto.les6.A_LIS.class.getResourceAsStream("dataA.txt");
        by.it.group410902.kukhto.les6.A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(inputStream);
        boolean ok = (result == 3);
        assertTrue("A failed", ok);
    }


    @Test
    public void checkB() throws Exception {
        InputStream inputStream = by.it.group410902.kukhto.les6.B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        by.it.group410902.kukhto.les6.B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(inputStream);
        boolean ok = (result == 3);
        assertTrue("B failed", ok);
    }

    @Test(timeout = 1000)
    public void checkC() throws Exception {
        InputStream inputStream = by.it.group410902.kukhto.les6.C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        by.it.group410902.kukhto.les6.C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(inputStream);
        boolean ok = (result == 4);
        assertTrue("C failed", ok);
    }

}

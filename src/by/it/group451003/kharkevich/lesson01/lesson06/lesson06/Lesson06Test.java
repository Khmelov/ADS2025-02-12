package by.it.group451003.kharkevich.lesson01.lesson06.lesson06;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertTrue;

public class Lesson06Test {
    @Test
    public void checkA() throws Exception {
            String root = System.getProperty("user.dir") + "/src/";
            InputStream stream = new FileInputStream(root + "by/it/a_khmelev/lesson06/dataA.txt");
            A_LIS instance = new A_LIS();
            int result=instance.getSeqSize(stream);
            boolean ok=(result==3);
            assertTrue("A failed", ok);
        }


    @Test
    public void checkB() throws Exception {
        String root = System.getProperty("user.dir") + "/src/";
        InputStream stream = new FileInputStream(root + "by/it/a_khmelev/lesson06/dataB.txt");
        B_LongDivComSubSeq instance=new B_LongDivComSubSeq();
        int result=instance.getDivSeqSize(stream);
        boolean ok=(result==3);
        assertTrue("B failed", ok);
    }

    @Test(timeout = 1000)
    public void checkC() throws Exception {
        InputStream inputStream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(inputStream);
        boolean ok = (result == 4);
        assertTrue("C failed", ok);
    }

}

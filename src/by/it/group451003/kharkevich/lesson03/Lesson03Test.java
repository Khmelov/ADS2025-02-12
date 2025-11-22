package by.it.group451003.kharkevich.lesson03;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertTrue;

public class Lesson03Test {
    /*
    для прохождения тестов создайте JUnit-конфигурацию на свой пакет:
    Поля:
    Name:               Test a_khmelev (тут ваша фамилия)
    Test kind:          All in package
    Package:            by.it.a_khmelev (тут ваша фамилия)
    Search for test:    In whole project
    */


    @Test
    public void checkA() throws Exception {
        String root = System.getProperty("user.dir") + "/src/";
        File f = new File(root + "by/it/a_khmelev/lesson03/dataA.txt");
        A_Huffman instance = new A_Huffman();
        String result = instance.encode(f);
        boolean ok=result.equals("01001100100111");
        assertTrue("A failed", ok);
    }

    @Test
    public void checkB() throws Exception {
        String root = System.getProperty("user.dir") + "/src/";
        File f = new File(root + "by/it/a_khmelev/lesson03/dataB.txt");
        B_Huffman instance = new B_Huffman();
        String result = instance.decode(f);
        boolean ok=result.equals("abacabad");
        assertTrue("B failed", ok);
    }

    @Test
    public void checkC() throws Exception {
        String root = System.getProperty("user.dir") + "/src/";
        InputStream stream = new FileInputStream(root + "by/it/a_khmelev/lesson03/dataC.txt");
        C_HeapMax instance = new C_HeapMax();
        Long res=instance.findMaxValue(stream);
        boolean ok=(res==500);
        assertTrue("C failed", ok);
    }

}

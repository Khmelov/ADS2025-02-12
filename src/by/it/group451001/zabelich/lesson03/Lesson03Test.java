package by.it.group451001.zabelich.lesson03;

import org.junit.Test;

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
        InputStream inputStream = A_Huffman.class.getResourceAsStream("dataA.txt");
        A_Huffman instance = new A_Huffman();
        String result = instance.encode(inputStream);
        boolean ok = result.equals("01001100100111");
        assertTrue("A failed", ok);
    }

    @Test
    public void checkB() throws Exception {
        InputStream inputStream = B_Huffman.class.getResourceAsStream("dataB.txt");
        B_Huffman instance = new B_Huffman();
        String result = instance.decode(inputStream);
        boolean ok = result.equals("abacabad");
        assertTrue("B failed", ok);
    }

    @Test
    public void checkC() throws Exception {
        InputStream inputStream = C_HeapMax.class.getResourceAsStream("dataC.txt");
        C_HeapMax instance = new C_HeapMax();
        Long res = instance.findMaxValue(inputStream);
        boolean ok = (res == 500);
        assertTrue("C failed", ok);
    }

}

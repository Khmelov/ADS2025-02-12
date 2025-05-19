package by.it.group451002.jasko.lesson07;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Lesson07Test {
    @Test
    public void A() throws Exception {
        A_EditDist instance = new A_EditDist();
        assertEquals("A1 failed", 0, instance.getDistanceEdinting("ab","ab"));
        assertEquals("A2 failed", 3, instance.getDistanceEdinting("short","ports"));
        assertEquals("A3 failed", 5, instance.getDistanceEdinting("distance","editing"));
    }


    @Test
    public void B() throws Exception {
        B_EditDist instance = new B_EditDist();
        assertEquals("B1 failed", 0, instance.getDistanceEdinting("ab","ab"));
        assertEquals("B2 failed", 3, instance.getDistanceEdinting("short","ports"));
        assertEquals("B3 failed", 5, instance.getDistanceEdinting("distance","editing"));
    }

    @Test
    public void C() throws Exception {
        C_EditDist instance = new C_EditDist();
        assertEquals("C1 failed", "#,#,", instance.getDistanceEdinting("ab","ab"));
        //путей может быть много, поэтому тут жестко проверить все сложно
        //надо найти и проверить их все, что делает тест сложнее реализации
        //возможно, что хватит только подсчета повторов.

        //ожидается     -s,~p,#,#,#,+s,
        assertEquals("C2 failed", 4, instance.getDistanceEdinting("short","ports").split("#").length);

        //ожидается     +e,#,#,-s,#,~i,#,-c,~g,
        assertEquals("C3 failed", 5, instance.getDistanceEdinting("distance","editing").split("#").length);
    }

}

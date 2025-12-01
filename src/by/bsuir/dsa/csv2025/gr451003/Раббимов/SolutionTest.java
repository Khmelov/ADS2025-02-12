package by.bsuir.dsa.csv2025.gr451003.Раббимов;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.List;

public class SolutionTest {

    @Test
    public void findValidSubstrings_Test1() {
        String input = "Минск76оырKil*-sdhdhdHygd5Тепtay87()adv";

        List<String> result = Solution.findValidSubstrings(input);

        List<String> expected = new ArrayList<>();
        expected.add("Минск");
        expected.add("Kil");
        expected.add("Hygd");
        expected.add("Теп");

        assertEquals(result, expected);
    }

    @Test
    public void findValidSubstrings_Test2() {
        String input = "hsdfyu78Пир7прKuyt98jdudH7hh9*/)Ар";

        List<String> result = Solution.findValidSubstrings(input);

        List<String> expected = new ArrayList<>();
        expected.add("Пир");
        expected.add("Kuyt");

        assertEquals(result, expected);
    }

    @Test
    public void findValidSubstrings_Test3() {
        String input = "щвНеуе789)Gtfh98Hghd989&пвнв";

        List<String> result = Solution.findValidSubstrings(input);

        List<String> expected = new ArrayList<>();
        expected.add("Неуе");
        expected.add("Gtfh");
        expected.add("Hghd");

        assertEquals(result, expected);
    }

    @Test
    public void findValidSubstrings_Test4() {
        String input = "9887рвпЕнот732Gtss6757)ubdvdЕне";

        List<String> result = Solution.findValidSubstrings(input);

        List<String> expected = new ArrayList<>();
        expected.add("Енот");
        expected.add("Gtss");
        expected.add("Ене");

        assertEquals(result, expected);
    }

    @Test
    public void findValidSubstrings_Test5() {
        String input = "8887ырЕЕневтирыавшм67VGGddjh677823*tdtМаво";

        List<String> result = Solution.findValidSubstrings(input);

        List<String> expected = new ArrayList<>();
        expected.add("Еневтирыавшм");
        expected.add("Gddjh");
        expected.add("Маво");

        assertEquals(result, expected);
    }

    @Test
    public void findValidSubstrings_Test6() {
        String input = "Когда2474*gdВывлвDsa8732hgdg";

        List<String> result = Solution.findValidSubstrings(input);

        List<String> expected = new ArrayList<>();
        expected.add("Когда");
        expected.add("Вывлв");
        expected.add("Dsa");

        assertEquals(result, expected);
    }

    @Test
    public void findValidSubstrings_Test7() {
        String input = "987Ночь68gdddghThis76випвОрда";

        List<String> result = Solution.findValidSubstrings(input);

        List<String> expected = new ArrayList<>();
        expected.add("Ночь");
        expected.add("This");
        expected.add("Орда");

        assertEquals(result, expected);
    }

    @Test
    public void findValidSubstrings_Test8() {
        String input = "Apple\uD83C\uDF4E Google1999Amazon$123Microsoft.com";

        List<String> result = Solution.findValidSubstrings(input);

        List<String> expected = new ArrayList<>();
        expected.add("Apple");
        expected.add("Google");
        expected.add("Amazon");
        expected.add("Microsoft");

        assertEquals(result, expected);
    }

    @Test
    public void findValidSubstrings_Test9() {
        String input = "уДублин2993hyddJony9Щука";

        List<String> result = Solution.findValidSubstrings(input);

        List<String> expected = new ArrayList<>();
        expected.add("Дублин");
        expected.add("Jony");
        expected.add("Щука");

        assertEquals(result, expected);
    }

    @Test
    public void findValidSubstrings_Test10() {
        String input = "ывп 67 Дуб рв hdgsd Hide 7 Почта 76 паа";

        List<String> result = Solution.findValidSubstrings(input);

        List<String> expected = new ArrayList<>();
        expected.add("Дуб");
        expected.add("Hide");
        expected.add("Почта");

        assertEquals(result, expected);
    }
}

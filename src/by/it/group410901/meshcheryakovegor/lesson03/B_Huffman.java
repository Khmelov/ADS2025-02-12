package by.it.group410901.meshcheryakovegor.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Lesson 3. B_Huffman.
// Восстановите строку по её коду и беспрефиксному коду символов.

// В первой строке входного файла заданы два целых числа
// kk и ll через пробел — количество различных букв, встречающихся в строке,
// и размер получившейся закодированной строки, соответственно.
//
// В следующих kk строках записаны коды букв в формате "letter: code".
// Ни один код не является префиксом другого.
// Буквы могут быть перечислены в любом порядке.
// В качестве букв могут встречаться лишь строчные буквы латинского алфавита;
// каждая из этих букв встречается в строке хотя бы один раз.
// Наконец, в последней строке записана закодированная строка.
// Исходная строка и коды всех букв непусты.
// Заданный код таков, что закодированная строка имеет минимальный возможный размер.
//
//        Sample Input 1:
//        1 1
//        a: 0
//        0

//        Sample Output 1:
//        a


//        Sample Input 2:
//        4 14
//        a: 0
//        b: 10
//        c: 110
//        d: 111
//        01001100100111

//        Sample Output 2:
//        abacabad

public class B_Huffman {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = B_Huffman.class.getResourceAsStream("dataB.txt");
        B_Huffman instance = new B_Huffman();
        String result = instance.decode(inputStream);
        System.out.println(result);
    }

    String decode(InputStream inputStream) throws FileNotFoundException {
        StringBuilder result = new StringBuilder();
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(inputStream);
        Integer count = scanner.nextInt();
        Integer length = scanner.nextInt();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //тут запишите ваше решение
        String[] codes = new String[count];
        // Читаем коды для каждой буквы из файла
        for (int i = 0; i < count; i++) {
            String s = scanner.next(); // Читаем строку в формате "letter: code"
            char ch = s.charAt(0); // Извлекаем букву из строки
            String code = scanner.next(); // Читаем код буквы
            codes[ch - 'a'] = code;  // Сохраняем код буквы в массиве.
            // Индекс в массиве соответствует букве (0 для 'a', 1 для 'b' и т.д.)
        }
        // Читаем закодированную строку
        String s = scanner.next();
        // Создаем StringBuilder для построения текущего кода и результата
        StringBuilder sb = new StringBuilder();
        // Проходим по каждому символу в закодированной строке
        for (int i = 0; i < s.length(); i++) {
            sb.append(s.charAt(i)); // Добавляем символ к текущему коду
            // Проверяем, совпадает ли текущий код с одним из кодов букв
            for (int j = 0; j < count; j++) {
                if (codes[j].equals(sb.toString())) {  // Если совпадает...
                    result.append((char) (j + 'a'));  // ...добавляем соответствующую букву в результат...
                    sb = new StringBuilder();  // ...и сбрасываем текущий код
                    break; // Прерываем цикл, так как мы уже нашли совпадение
                }
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString(); //01001100100111
    }


}

package by.it.group410901.bandarzheuskaya.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;


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
        StringBuilder result = new StringBuilder(); // Здесь будет храниться результат декодирования
        Scanner scanner = new Scanner(inputStream);

        // Чтение количества уникальных символов и длины закодированной строки
        Integer count = scanner.nextInt(); // Количество уникальных символов
        Integer length = scanner.nextInt(); // Длина закодированной строки

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        // Создаем карту для хранения кодов символов
        Map<String, Character> codeToChar = new HashMap<>();

        // Чтение кодов символов и заполнение карты
        for (int i = 0; i < count; i++) {
            String line = scanner.next(); // Чтение строки формата "буква: код"
            char letter = line.charAt(0); // Извлекаем букву
            String code = scanner.next(); // Извлекаем код
            codeToChar.put(code, letter); // Заполняем карту: код -> буква
        }

        // Чтение закодированной строки
        String encodedString = scanner.next();

        // Декодирование строки
        StringBuilder currentCode = new StringBuilder(); // Текущий код, который мы собираем
        for (char bit : encodedString.toCharArray()) {
            currentCode.append(bit); // Добавляем текущий бит к коду
            // Если текущий код есть в карте, значит, мы нашли символ
            if (codeToChar.containsKey(currentCode.toString())) {
                result.append(codeToChar.get(currentCode.toString())); // Добавляем символ к результату
                currentCode.setLength(0); // Сбрасываем текущий код для поиска следующего символа
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1

        return result.toString(); // Возвращаем декодированную строку
    }


}

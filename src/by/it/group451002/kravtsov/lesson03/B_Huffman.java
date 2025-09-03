package by.it.group451002.kravtsov.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
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
        // Получаем входной поток из файла dataB.txt
        InputStream inputStream = B_Huffman.class.getResourceAsStream("dataB.txt");
        B_Huffman instance = new B_Huffman(); // Создаём экземпляр класса
        String result = instance.decode(inputStream); // Вызываем метод decode
        System.out.println(result); // Выводим результат декодирования
    }

    String decode(InputStream inputStream) throws FileNotFoundException {
        // Создаём StringBuilder для формирования результата декодирования
        StringBuilder result = new StringBuilder();

        // Инициализируем Scanner для чтения данных из входного потока
        Scanner scanner = new Scanner(inputStream);

        // Считываем количество символов и длину закодированной строки
        Integer count = scanner.nextInt(); // Количество уникальных символов
        Integer length = scanner.nextInt(); // Длина закодированной строки

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        // Чтение кодов и их маппинг на символы
        scanner.nextLine(); // Переходим на следующую строку
        HashMap<String, String> codesMap = new HashMap<>(); // Хранилище для пар "код -> символ"
        String[] temp = new String[1]; // Временный массив для обработки строк

        // Заполняем HashMap данными о символах и их кодах
        for (int i = 0; i < count; i++) {
            String symbol = scanner.nextLine(); // Считываем строку формата "символ: код"
            temp = symbol.split(": "); // Разделяем строку на символ и его код
            codesMap.put(temp[1], temp[0]); // Сохраняем в HashMap: код -> символ
        }

        // Считываем закодированную строку
        String encodeLine = scanner.nextLine();
        int start = 0; // Начальная позиция подстроки
        int end = 1; // Конечная позиция подстроки

        // Декодирование строки, используя HashMap
        while (end <= length) {
            // Проверяем, содержится ли текущая подстрока в кодах
            if (codesMap.containsKey(encodeLine.substring(start, end))) {
                // Если код найден, добавляем соответствующий символ в результат
                result.append(codesMap.get(encodeLine.substring(start, end)));
                // Обновляем стартовую позицию
                start = end;
                end++;
            } else {
                // Если код не найден, увеличиваем конечную позицию
                end++;
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1

        // Возвращаем декодированную строку
        return result.toString();
    }
}



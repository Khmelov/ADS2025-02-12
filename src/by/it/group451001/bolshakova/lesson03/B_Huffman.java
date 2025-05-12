package by.it.group451001.bolshakova.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
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

        Scanner scanner = new Scanner(inputStream);
        // Считываем два целых числа:
        // count - количество различных букв,
        // length - длину закодированной строки.
        Integer count = scanner.nextInt();
        Integer length = scanner.nextInt();
        // После считывания двух чисел переходим к следующей строке.
        scanner.nextLine();

        // Создаем отображение (словарь) для декодирования:
        // ключ – Хаффман-код (строка вида "0", "10", "110" и т.д.),
        // значение – соответствующий символ.
        Map<String, Character> codeToChar = new HashMap<>();

        // Считываем next count строк, где каждая строка имеет формат "letter: code"
        // например, "a: 0" или "b: 10"
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine();
            // Если строка пуста, пропускаем ее (а счетчик i корректируем)
            if (line.isEmpty()) {
                i--;
                continue;
            }
            // Разбиваем строку по символу ":", чтобы отделить букву от кода.
            String[] parts = line.split(":");
            // Из первой части (до двоеточия) берется буква, обрезав лишние пробелы.
            char letter = parts[0].trim().charAt(0);
            // Из второй части (после двоеточия) извлекается код, также обрезав лишние пробелы.
            String code = parts[1].trim();
            // Помещаем соответствие в словарь.
            codeToChar.put(code, letter);
        }

        // Далее считываем закодированную строку (последнюю строку файла).
        String encoded = scanner.nextLine().trim();

        // Для декодирования будем последовательно накапливать символы в currentCode.
        StringBuilder currentCode = new StringBuilder();
        // Проходим по каждому символу закодированной строки.
        for (int i = 0; i < encoded.length(); i++) {
            // Добавляем очередной символ (букву '0' или '1') к текущей последовательности.
            currentCode.append(encoded.charAt(i));
            // Если накопленный код найден в словаре, значит он соответствует одному символу.
            if (codeToChar.containsKey(currentCode.toString())) {
                // Дописываем соответствующий символ в результат.
                result.append(codeToChar.get(currentCode.toString()));
                // Обнуляем накопленный код для декодирования следующего символа.
                currentCode.setLength(0);
            }
        }
        // Возвращаем декодированную строку.
        return result.toString();
    }
}

package by.it.group410902.sinyutin.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.HashMap;
//использовала коллекцию, которая позволяет хранить данные в виде пар "ключ-значение", надеюсь тут так можно

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
        //карта для хранения кодов символов
        HashMap<String, Character> codes = new HashMap<>();

        //прочитаем коды букв
        scanner.nextLine(); //пропустим оставшийся перевод строки после целых чисел
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine(); //читаем строку
            String[] parts = line.split(": "); //разделим строку на букву и код
            if (parts.length == 2) { //проверим, что строка разделена правильно
                char symbol = parts[0].charAt(0); //буква (первая часть)
                String code = parts[1]; //код (вторая часть)
                codes.put(code, symbol); //сохраняем в карту
            } else {
                //логирование или обработка ошибки в случае неверного формата
                System.out.println("Неверный формат строки: " + line);
            }
        }

        //закодированная строка
        String encoded = scanner.nextLine();

        //декодируем строку
        StringBuilder currentCode = new StringBuilder();

        for (char c : encoded.toCharArray()) {
            currentCode.append(c); //добавляем по одному биту
            if (codes.containsKey(currentCode.toString())) { //если код совпадает
                result.append(codes.get(currentCode.toString())); //добавляем символ
                currentCode.setLength(0); //очищаем текущий код
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString(); //01001100100111
    }

}

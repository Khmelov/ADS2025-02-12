package by.it.group410902.derzhavskaya_e.lesson03;

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

// Создаем карту для хранения кодов
HashMap<String, String> codesMap = new HashMap<>();
 scanner.nextLine(); // Пропускаем символ новой строки после чисел

// Чтение символов и их кодов
 for (int i = 0; i < count; i++) {
String symbol = scanner.nextLine();
 String[] temp = symbol.split(": ");
 codesMap.put(temp[1], temp[0]); // код -> символ
 }

// Чтение закодированной строки
 String encodedString = scanner.nextLine();
 int start = 0;
 int end = 1;

 // Декодирование строки
 while (end <= encodedString.length()) {
 String code = encodedString.substring(start, end);
 if (codesMap.containsKey(code)) {
 result.append(codesMap.get(code)); // Добавляем символ в результат
 start = end; // Перемещаем начало на конец
 }
 end++;
 }

 //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
 return result.toString(); // Возвращаем восстановленную строку
 }


}
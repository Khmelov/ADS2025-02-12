package by.it.group451002.buyel.lesson03;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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

    public static void main(String[] args) throws IOException {
        InputStream inputStream = B_Huffman.class.getResourceAsStream("dataB.txt");
        B_Huffman instance = new B_Huffman();
        String result = instance.decode(inputStream);
        System.out.println(result);
    }

    String decode(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(inputStream);
        int count = scanner.nextInt();
        int length = scanner.nextInt();
        scanner.nextLine();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //тут запишите ваше решение

        Map<String, Integer> hashCharacter = new HashMap<>();

        String[] parts = new String[2];
        byte i;
        for (i = 0; i < count; i++) {
            parts = scanner.nextLine().split(": ");
            hashCharacter.put(parts[0], Integer.valueOf(parts[1]));
        }

        char[] check;
        check = scanner.nextLine().toCharArray();

        int ind = 0, j = ind;
        while (j < length) {
            do {
                ind += 1;
            } while ((check[ind-1] != '0') && (ind < check.length));

            char[] elem = new char[ind-j/*+1*/];
            for (byte k = 0; k < ind-j; k++) {
                elem[k] = check[j+k];
            }
            for (var element : hashCharacter.entrySet()) {
                // Если значение элемента совпала с массивом elem
                if (Arrays.equals(elem, element.getValue().toString().toCharArray())) {
                    result.append(element.getKey());
                }
            }
            j = ind;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString(); //01001100100111
    }


}

package by.it.group410902.plekhova.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Рекурсивно вычислить расстояние редактирования двух данных непустых строк

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5

*/

public class A_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
// Метод для вычисления расстояния Левенштейна рекурсивно

            // Базовые случаи: если одна из строк пустая, расстояние равно длине другой строки
            if (one.isEmpty()) {
                return two.length();
            }
            if (two.isEmpty()) {
                return one.length();
            }

            // Сравниваем последние символы строк
            char lastCharOne = one.charAt(one.length() - 1);
            char lastCharTwo = two.charAt(two.length() - 1);

            // Стоимость замены (0, если символы одинаковые, иначе 1)
            int substitutionCost = (lastCharOne == lastCharTwo) ? 0 : 1;

            // Рекурсивно вычисляем минимальное из трех возможных операций:
            // 1. Удаление последнего символа из первой строки
            // 2. Вставка последнего символа второй строки в первую
            // 3. Замена последнего символа первой строки на последний символ второй (если нужно)
            return Math.min(
                    Math.min(
                            getDistanceEdinting(one.substring(0, one.length() - 1), two) + 1,  // Удаление
                            getDistanceEdinting(one, two.substring(0, two.length() - 1)) + 1   // Вставка
                    ),
                    getDistanceEdinting(one.substring(0, one.length() - 1), two.substring(0, two.length() - 1)) + substitutionCost  // Замена
            );
        }

        public static void main(String[] args) throws FileNotFoundException {
            InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
            A_EditDist instance = new A_EditDist();
            Scanner scanner = new Scanner(stream);
            System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
            System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
            System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        }
    }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!






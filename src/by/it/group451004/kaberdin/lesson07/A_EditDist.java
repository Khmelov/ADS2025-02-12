package by.it.group451004.kaberdin.lesson07;

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
    short //1: (del s) hort 2: (rep h) port 3: (ins s) ports
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

    // Главный метод программы
    public static void main(String[] args) throws FileNotFoundException {
        // Получаем входной поток данных из файла
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        // Создаем экземпляр класса
        A_EditDist instance = new A_EditDist();
        // Создаем сканер для чтения данных
        Scanner scanner = new Scanner(stream);
        // Читаем и выводим расстояние Левенштейна для трех пар строк
        System.out.println(instance.getDistanceEditing(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEditing(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEditing(scanner.nextLine(), scanner.nextLine()));
    }

    // Матрица для хранения расстояний между подстроками
    static private int distances[][];
    // Первая строка для сравнения
    static private String str1 = new String("");
    // Вторая строка для сравнения
    static private String str2 = new String("");

    // Основной метод для вычисления расстояния Левенштейна
    int getDistanceEditing(String one, String two) {
        // Сохраняем строки в статические переменные
        str1 = one;
        str2 = two;
        // Инициализируем матрицу расстояний (размером [длина1+1][длина2+1])
        distances = new int[str1.length() + 1][str2.length() + 1];

        // Заполняем матрицу значением -1 (означает, что значение еще не вычислено)
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances[0].length; j++) {
                distances[i][j] = -1;
            }
        }

        // Базовые случаи:
        // Расстояние между пустой строкой и строкой длины j равно j (требуется j вставок)
        for (int j = 0; j < distances[0].length; j++) {
            distances[0][j] = j;
        }
        // Расстояние между строкой длины i и пустой строкой равно i (требуется i удалений)
        for (int i = 0; i < distances.length; i++) {
            distances[i][0] = i;
        }

        // Запускаем рекурсивное вычисление для полных строк
        return recursion(str1.length(), str2.length());
    }

    // Рекурсивная функция для вычисления расстояния между подстроками str1[0..i-1] и str2[0..j-1]
    int recursion(int i, int j) {
        // Если значение уже вычислено, возвращаем его
        if (distances[i][j] != -1) return distances[i][j];

        // Вычисляем три возможных варианта:
        // 1. Вставка символа (расстояние для str1[0..i-1] и str2[0..j-2] + 1)
        int insert = recursion(i, j - 1) + 1;
        // 2. Удаление символа (расстояние для str1[0..i-2] и str2[0..j-1] + 1)
        int delete = recursion(i - 1, j) + 1;
        // 3. Замена символа (расстояние для str1[0..i-2] и str2[0..j-2] + стоимость замены)
        int replace = recursion(i - 1, j - 1) + M(i, j);

        // Выбираем минимальное из трех значений
        distances[i][j] = getMin(insert, delete, replace);
        return distances[i][j];
    }

    // Вспомогательная функция для нахождения минимума из трех чисел
    int getMin(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

    // Функция для определения стоимости замены символов
    int M(int i, int j) {
        // Если символы совпадают, стоимость замены 0, иначе 1
        if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
            return 0;
        } else {
            return 1;
        }
    }
}
package by.bsuir.dsa.csv2025.gr410902.Сёмчена;

import org.junit.Test;
import org.junit.Assert;

import java.io.*;
import java.util.Scanner;

public class Solution {

    static class Student {
        String fio;
        int grade;

        public Student(String fio, int grade) {
            this.fio = fio;
            this.grade = grade;
        }

        @Override
        public String toString() {
            return fio + " - " + grade;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();

        Student[] students = new Student[n];

        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int grade = Integer.parseInt(parts[parts.length - 1]);
            String fio = line.substring(0, line.lastIndexOf(" "));

            students[i] = new Student(fio, grade);
        }

        selectionSort(students);

        for (int i = 0; i < students.length; i++) {
            System.out.println((i + 1) + ". " + students[i]);
        }
    }

    public static void selectionSort(Student[] students) {
        int n = students.length;

        for (int i = 0; i < n - 1; i++) {
            int maxIndex = i;

            for (int j = i + 1; j < n; j++) {
                if (students[j].grade > students[maxIndex].grade) {
                    maxIndex = j;
                }
            }

            Student temp = students[i];
            students[i] = students[maxIndex];
            students[maxIndex] = temp;
        }
    }

    @Test
    public void testSelectionSortStudents() throws Exception {
        String input =
                "5\n" +
                        "Иванов Иван Иванович 4\n" +
                        "Петрова Мария Сергеевна 5\n" +
                        "Сидоров Алексей Петрович 3\n" +
                        "Кузнецова Елена Владимировна 5\n" +
                        "Смирнов Дмитрий Алексеевич 4\n";

        String[] expectedLines = {
                "1. Петрова Мария Сергеевна - 5",
                "2. Кузнецова Елена Владимировна - 5",
                "3. Иванов Иван Иванович - 4",
                "4. Смирнов Дмитрий Алексеевич - 4",
                "5. Сидоров Алексей Петрович - 3"
        };

        InputStream is = new ByteArrayInputStream(input.getBytes("UTF-8"));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setIn(is);
        System.setOut(new PrintStream(output));

        Solution.main(null);

        System.setOut(oldOut);
        String[] outputLines = output.toString().split("\r\n|\n|\r");

        Assert.assertArrayEquals(expectedLines, outputLines);
    }
}
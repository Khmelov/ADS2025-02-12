package by.bsuir.dsa.csv2025.gr451002.Кольцов;

import org.junit.Assert;
import org.junit.Test;

import java.util.Scanner;

public class Solution {
    // Строка, содержащая битовое представление значения экземпляра
    double value;
    
    public static Solution getBinaryString(double value)
    {
        Solution bs = new Solution();
        bs.value = value;
        return bs;
    }

    // this.value должно быть проинициализировано строкой, соответствующей битовому представлению нуля
    public Solution() {
        this.value = 0;
    }

    // После вызова функции, this.value должно быть равно строке, соответствующей битовому представлению суммы числовых представлений
    // this.value и параметра
    public Solution add(Solution value) {
        this.value += value.value;
        return this;
    }

    // После вызова функции, this.value должно быть равно строке, соответствующей битовому представлению разности числовых представлений
    // this.value и параметра
    public Solution sub(Solution value) {
        this.value -= value.value;
        return this;
    }

    // После вызова функции, this.value должно быть равно строке, соответствующей битовому представлению произведения числовых представлений
    // this.value и параметра
    public Solution mul(Solution value) {
        this.value *= value.value;
        return this;
    }

    // После вызова функции, this.value должно быть равно строке, соответствующей битовому представлению частного числовых представлений
    // this.value и параметра
    public Solution div(Solution value) {
        if (value.getDoubleValue() == 0)
            throw new IllegalArgumentException();
        this.value /= value.value;
        return this;
    }

    // Возвращает десятичное представление this.value
    public double getDoubleValue() {
        return value;
    }

    // Возвращение значения
    public String getValue()
    {
        double value1 = value;
        if ((int)value1 == 0)
            return "0";
        StringBuilder wholePart = new StringBuilder();
        while ((int)value1 != 0)
        {
            wholePart.insert(0, (int)value1 > 0 ? (int)value1 % 2 : (int)(Math.abs(value1) % 2) ^ 1);
            value1 /= 2;
        }

        value1 = value;
        StringBuilder fractPart = new StringBuilder();
        double fractValue = Math.abs(value1) - Math.abs((int) value1);
        boolean isZero = true;
        for (int i = 0; i < 4; i++)
        {
            if ((int)(fractValue * 2) == 1)
                isZero = false;
            fractPart.append((int)value1 > 0 ? (int)(fractValue * 2) : (int)(fractValue * 2) ^ 1);
            fractValue = (fractValue * 2) - (int) (fractValue * 2);
        }
        return isZero ? wholePart.toString() : wholePart + "." + fractPart;
    }

    @Test
    public void ConstructorTests()
    {
        Assert.assertEquals("0", getBinaryString(0).getValue());
        Assert.assertEquals("1", getBinaryString(1).getValue());
        Assert.assertEquals("1111.1000", getBinaryString(15.5).getValue());
        Assert.assertEquals("100000", getBinaryString(32).getValue());
        Assert.assertEquals("10001000010", getBinaryString(1090).getValue());

        Assert.assertEquals("0", getBinaryString(-0).getValue());
        Assert.assertEquals("0", getBinaryString(-1).getValue());
        Assert.assertEquals("010.0011", getBinaryString(-5.75).getValue());
        Assert.assertEquals("011111", getBinaryString(-32).getValue());
        Assert.assertEquals("01110111101", getBinaryString(-1090).getValue());

        Assert.assertEquals("0", new Solution().getValue());
    }

    @Test
    public void ConvertionTests()
    {
        Assert.assertEquals(String.valueOf(0.0), String.valueOf(getBinaryString(0).getDoubleValue()));
        Assert.assertEquals(String.valueOf(1.0), String.valueOf(getBinaryString(1).getDoubleValue()));
        Assert.assertEquals(String.valueOf(15.0), String.valueOf(getBinaryString(15).getDoubleValue()));
        Assert.assertEquals(String.valueOf(32.0), String.valueOf(getBinaryString(32).getDoubleValue()));
        Assert.assertEquals(String.valueOf(1090.0), String.valueOf(getBinaryString(1090).getDoubleValue()));

        Assert.assertEquals(String.valueOf(0.0), String.valueOf(getBinaryString(-0).getDoubleValue()));
        Assert.assertEquals(String.valueOf(-1.0), String.valueOf(getBinaryString(-1).getDoubleValue()));
        Assert.assertEquals(String.valueOf(-15.0), String.valueOf(getBinaryString(-15).getDoubleValue()));
        Assert.assertEquals(String.valueOf(-32.0), String.valueOf(getBinaryString(-32).getDoubleValue()));
        Assert.assertEquals(String.valueOf(-1090.0), String.valueOf(getBinaryString(-1090).getDoubleValue()));
        Assert.assertEquals(String.valueOf(0.0), String.valueOf(new Solution().getDoubleValue()));
    }

    @Test
    public void Subtraction()
    {
        Assert.assertEquals(String.format("%.1f", 0.0 - 10), String.format("%.1f", getBinaryString(0).sub(getBinaryString(10)).getDoubleValue()));
        Assert.assertEquals(String.format("%.1f", 5.3 - 8.14), String.format("%.1f", getBinaryString(5.3).sub(getBinaryString(8.14)).getDoubleValue()));
        Assert.assertEquals(String.format("%.1f", 10 - 14.48), String.format("%.1f", getBinaryString(10).sub(getBinaryString(14.48)).getDoubleValue()));
        Assert.assertEquals(String.format("%.1f", 18.908 - 13), String.format("%.1f", getBinaryString(18.908).sub(getBinaryString(13)).getDoubleValue()));
        Assert.assertEquals(String.format("%.1f", 0.0 - 13), String.format("%.1f", getBinaryString(0).sub(getBinaryString(13)).getDoubleValue()));
        Assert.assertEquals(String.format("%.1f", -17.8 - 0), String.format("%.1f", getBinaryString(-17.8).sub(getBinaryString(0)).getDoubleValue()));
    }

    @Test
    public void Addition()
    {
        Assert.assertEquals(String.format("%.1f", 0 + 10.0), String.format("%.1f", getBinaryString(0).add(getBinaryString(10)).getDoubleValue()));
        Assert.assertEquals(String.format("%.1f", 5.3 + 8.14), String.format("%.1f", getBinaryString(5.3).add(getBinaryString(8.14)).getDoubleValue()));
        Assert.assertEquals(String.format("%.1f", 10 + 14.48), String.format("%.1f", getBinaryString(10).add(getBinaryString(14.48)).getDoubleValue()));
        Assert.assertEquals(String.format("%.1f", 18.908 + 13), String.format("%.1f", getBinaryString(18.908).add(getBinaryString(13)).getDoubleValue()));
        Assert.assertEquals(String.format("%.1f", 0 + 13.0), String.format("%.1f", getBinaryString(0).add(getBinaryString(13)).getDoubleValue()));
        Assert.assertEquals(String.format("%.1f", -17.8 + 0), String.format("%.1f", getBinaryString(-17.8).add(getBinaryString(0)).getDoubleValue()));
    }

    @Test
    public void Multiplication()
    {
        Assert.assertEquals(String.format("%.4f", 0.0 * 10), String.format("%.4f", getBinaryString(0).mul(getBinaryString(10)).getDoubleValue()));
        Assert.assertEquals(String.format("%.4f", 5.3 * 8.14), String.format("%.4f", getBinaryString(5.3).mul(getBinaryString(8.14)).getDoubleValue()));
        Assert.assertEquals(String.format("%.4f", 10 * 14.48), String.format("%.4f", getBinaryString(10).mul(getBinaryString(14.48)).getDoubleValue()));
        Assert.assertEquals(String.format("%.4f", -18.908 * 13), String.format("%.4f", getBinaryString(-18.908).mul(getBinaryString(13)).getDoubleValue()));
        Assert.assertEquals(String.format("%.4f", 0.0 * 13), String.format("%.4f", getBinaryString(0).mul(getBinaryString(13)).getDoubleValue()));
        Assert.assertEquals(String.format("%.4f", -17.8 * 0), String.format("%.4f", getBinaryString(-17.8).mul(getBinaryString(0)).getDoubleValue()));
    }

    @Test
    public void Division()
    {
        Assert.assertEquals(String.format("%.4f", 0.0), String.format("%.4f",getBinaryString(0).div(getBinaryString(10)).getDoubleValue()));
        Assert.assertEquals(String.format("%.4f", 5.3 / 8.14), String.format("%.4f", getBinaryString(5.3).div(getBinaryString(8.14)).getDoubleValue()));
        Assert.assertEquals(String.format("%.4f", 10 / 14.48), String.format("%.4f", getBinaryString(10).div(getBinaryString(14.48)).getDoubleValue()));
        Assert.assertEquals(String.format("%.4f", -18.908 / 13), String.format("%.4f", getBinaryString(-18.908).div(getBinaryString(13)).getDoubleValue()));
        Assert.assertEquals(String.format("%.4f", 0.0 / 13), String.format("%.4f", getBinaryString(0).div(getBinaryString(13)).getDoubleValue()));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int methodIndex = sc.nextInt();
        switch (methodIndex)
        {
            case 0:
                // Вывод двоичного представления экземпляра, созданного при помощи конструктора без параметров
                System.out.println(new Solution().getValue());
                break;
            case 1:
                // Вывод двоичного представления экземпляра, созданного при помощи конструктора с параметром
                double value1 = sc.nextDouble();
                System.out.println(getBinaryString(value1).getValue());
                break;
            case 2:
                // Вывод десятичного представления BinaryString
                double value2 = sc.nextDouble();
                System.out.printf("%.2f%n",getBinaryString(value2).getDoubleValue());
                break;
            case 3:
                // Вывод десятичного представления разности двух BinaryString
                double strValue3 = sc.nextDouble();
                double parameterValue3 = sc.nextDouble();
                System.out.printf("%.2f%n", getBinaryString(strValue3).sub(getBinaryString(parameterValue3)).getDoubleValue());
                break;
            case 4:
                // Вывод десятичного представления суммы двух BinaryString
                double strValue4 = sc.nextDouble();
                double parameterValue4 = sc.nextDouble();
                System.out.printf("%.2f%n", getBinaryString(strValue4).add(getBinaryString(parameterValue4)).getDoubleValue());
                break;
            case 5:
                // Вывод десятичного представления произведения двух BinaryString
                double strValue5 = sc.nextDouble();
                double parameterValue5 = sc.nextDouble();
                System.out.printf("%.2f%n", getBinaryString(strValue5).mul(getBinaryString(parameterValue5)).getDoubleValue());
                break;
            case 6:
                // Вывод десятичного представления частного двух BinaryString
                double strValue6 = sc.nextDouble();
                double parameterValue6 = sc.nextDouble();
                System.out.printf("%.2f%n", getBinaryString(strValue6).div(getBinaryString(parameterValue6)).getDoubleValue());
                break;
            default:
                // Вывод сообщения о некорректности введённого индекса метода
                System.out.println("Method index error");
                break;
        }
    }
}
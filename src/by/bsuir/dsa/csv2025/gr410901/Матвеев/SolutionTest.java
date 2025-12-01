package by.bsuir.dsa.csv2025.gr410901.Матвеев;
// Класс с JUnit тестами
import org.junit.Test;
import static org.junit.Assert.*;

public class SolutionTest {
    
    @Test
    public void testModularAddition() {
        assertEquals(0, Solution.processInput("7 + 3 mod 5"));
        assertEquals(2, Solution.processInput("8 + 7 mod 13"));
        assertEquals(1, Solution.processInput("10 + 5 mod 7"));
    }
    
    @Test
    public void testModularMultiplication() {
        assertEquals(5, Solution.processInput("4 * 3 mod 7"));
        assertEquals(6, Solution.processInput("3 * 5 mod 9"));
        assertEquals(2, Solution.processInput("7 * 8 mod 9"));
    }
    
    @Test
    public void testModularExponentiation() {
        assertEquals(10, Solution.processInput("2^10 mod 13"));
        assertEquals(1, Solution.processInput("7^6 mod 13"));
        assertEquals(4, Solution.processInput("3^4 mod 7"));
    }
    
    @Test
    public void testModularInverse() {
        assertEquals(4, Solution.processInput("3^-1 mod 11"));
        assertEquals(9, Solution.processInput("5^-1 mod 11"));
        assertEquals(-1, Solution.processInput("2^-1 mod 4")); // не существует
    }
    
    @Test
    public void testNegativeNumbers() {
        assertEquals(1, Solution.processInput("-5 mod 3"));
        assertEquals(2, Solution.processInput("-7 mod 3"));
        assertEquals(4, Solution.processInput("-1 mod 5"));
    }
    
    @Test
    public void testChineseRemainderTheorem() {
        assertEquals(8, Solution.processInput("x ≡ 2 mod 3, x ≡ 3 mod 5"));
        assertEquals(23, Solution.processInput("x ≡ 1 mod 2, x ≡ 2 mod 3, x ≡ 3 mod 5"));
        assertEquals(53, Solution.processInput("x ≡ 2 mod 3, x ≡ 3 mod 5, x ≡ 2 mod 7"));
    }
    
    @Test
    public void testComplexExpressions() {
        assertEquals(3, Solution.processInput("(15 * 8 + 27) mod 11"));
        assertEquals(6, Solution.processInput("(20 - 7 * 2) mod 8"));
        assertEquals(1, Solution.processInput("(5^3 + 10) mod 13"));
    }
    
    @Test
    public void testLargeNumbers() {
        assertEquals(699, Solution.processInput("123 * 456 mod 789"));
        assertEquals(1, Solution.processInput("1000^0 mod 7"));
        assertEquals(0, Solution.processInput("1000^1000 mod 10"));
    }
    
    @Test
    public void testModPowFunction() {
        assertEquals(1, Solution.modPow(5, 0, 7));
        assertEquals(5, Solution.modPow(5, 1, 7));
        assertEquals(4, Solution.modPow(2, 2, 7));
        assertEquals(6, Solution.modPow(3, 3, 7));
    }
    
    @Test
    public void testModInverseFunction() {
        assertEquals(2, Solution.modInverse(4, 7));
        assertEquals(4, Solution.modInverse(2, 7));
        assertEquals(-1, Solution.modInverse(2, 4)); // не существует
        assertEquals(1, Solution.modInverse(1, 5));
    }
    
    @Test
    public void testGcdFunction() {
        assertEquals(1, Solution.gcd(7, 5));
        assertEquals(2, Solution.gcd(4, 6));
        assertEquals(5, Solution.gcd(15, 10));
        assertEquals(1, Solution.gcd(13, 17));
    }
}
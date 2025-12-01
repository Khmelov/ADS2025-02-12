package by.bsuir.dsa.csv2025.gr451004.Козлов;

import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {

    public static class CustomBitSet {
        private static final int ADDRESS_BITS_PER_WORD = 6;
        private static final int BITS_PER_WORD = 1 << ADDRESS_BITS_PER_WORD;
        private long[] words;
        private int sizeInBits;
        private transient boolean sizeIsSticky = false;

        public CustomBitSet() {
            this(BITS_PER_WORD);
            this.sizeIsSticky = false;
        }

        public CustomBitSet(int nbits) {
            if (nbits < 0) throw new NegativeArraySizeException("nbits < 0: " + nbits);
            this.sizeInBits = nbits;
            this.words = new long[wordIndex(nbits - 1) + 1];
            this.sizeIsSticky = true;
        }

        private static int wordIndex(int bitIndex) {
            return bitIndex >> ADDRESS_BITS_PER_WORD;
        }

        private void ensureCapacity(int wordIndex) {
            int wordsRequired = wordIndex + 1;
            if (words.length < wordsRequired && !sizeIsSticky) {
                long[] newWords = new long[Math.max(words.length * 2, wordsRequired)];
                System.arraycopy(words, 0, newWords, 0, words.length);
                words = newWords;
                sizeInBits = Math.max(sizeInBits, wordsRequired * BITS_PER_WORD);
            }
        }

        public void set(int bitIndex) {
            if (bitIndex < 0) throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);
            int wordIndex = wordIndex(bitIndex);
            ensureCapacity(wordIndex);
            words[wordIndex] |= (1L << bitIndex);
        }

        public void clear(int bitIndex) {
            if (bitIndex < 0) throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);
            int wordIndex = wordIndex(bitIndex);
            if (wordIndex < words.length) {
                words[wordIndex] &= ~(1L << bitIndex);
            }
        }

        public boolean get(int bitIndex) {
            if (bitIndex < 0) throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);
            int wordIndex = wordIndex(bitIndex);
            return wordIndex < words.length && (words[wordIndex] & (1L << bitIndex)) != 0;
        }

        public void flip(int bitIndex) {
            if (bitIndex < 0) throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);
            int wordIndex = wordIndex(bitIndex);
            ensureCapacity(wordIndex);
            words[wordIndex] ^= (1L << bitIndex);
        }

        public void and(CustomBitSet other) {
            if (this == other) return;
            int wordsInCommon = Math.min(this.words.length, other.words.length);
            for (int i = 0; i < wordsInCommon; i++) this.words[i] &= other.words[i];
            for (int i = wordsInCommon; i < this.words.length; i++) this.words[i] = 0;
        }

        public void or(CustomBitSet other) {
            if (this == other) return;
            int wordsInCommon = Math.min(this.words.length, other.words.length);
            for (int i = 0; i < wordsInCommon; i++) this.words[i] |= other.words[i];
            if (wordsInCommon < other.words.length) {
                ensureCapacity(other.words.length - 1);
                for (int i = wordsInCommon; i < other.words.length; i++) this.words[i] = other.words[i];
            }
        }

        public int cardinality() {
            int count = 0;
            for (long word : words) count += Long.bitCount(word);
            return count;
        }

        public boolean isEmpty() {
            for (long word : words) if (word != 0) return false;
            return true;
        }

        public int length() {
            if (words.length == 0) return 0;
            int lastNonZeroWord = words.length - 1;
            while (lastNonZeroWord >= 0 && words[lastNonZeroWord] == 0) lastNonZeroWord--;
            if (lastNonZeroWord < 0) return 0;
            long lastWord = words[lastNonZeroWord];
            return (lastNonZeroWord + 1) * BITS_PER_WORD - Long.numberOfLeadingZeros(lastWord);
        }

        public boolean intersects(CustomBitSet other) {
            int wordsInCommon = Math.min(this.words.length, other.words.length);
            for (int i = 0; i < wordsInCommon; i++) {
                if ((this.words[i] & other.words[i]) != 0) return true;
            }
            return false;
        }

        public CustomBitSet copy() {
            CustomBitSet result = new CustomBitSet(this.sizeInBits);
            System.arraycopy(this.words, 0, result.words, 0, this.words.length);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof CustomBitSet)) return false;
            CustomBitSet other = (CustomBitSet) obj;
            int minWords = Math.min(this.words.length, other.words.length);
            for (int i = 0; i < minWords; i++) if (this.words[i] != other.words[i]) return false;
            CustomBitSet longer = this.words.length > other.words.length ? this : other;
            for (int i = minWords; i < longer.words.length; i++) if (longer.words[i] != 0) return false;
            return true;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('{');
            boolean first = true;
            for (int i = 0; i < words.length * BITS_PER_WORD; i++) {
                if (get(i)) {
                    if (!first) sb.append(", ");
                    sb.append(i);
                    first = false;
                }
            }
            sb.append('}');
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CustomBitSet bitSet = new CustomBitSet();

        System.out.println("Enter 3 bit positions to set:");
        System.out.print("First bit position: ");
        int bit1 = scanner.nextInt();
        System.out.print("Second bit position: ");
        int bit2 = scanner.nextInt();
        System.out.print("Third bit position: ");
        int bit3 = scanner.nextInt();

        System.out.println("\nSetting bits " + bit1 + ", " + bit2 + ", " + bit3 + ":");
        bitSet.set(bit1);
        bitSet.set(bit2);
        bitSet.set(bit3);
        System.out.println("State: " + bitSet.toString());
        System.out.println("Number of set bits: " + bitSet.cardinality());

        scanner.close();
    }

    @Test
    public void testBasicOperations() {
        CustomBitSet bitSet = new CustomBitSet();
        bitSet.set(5);
        bitSet.set(10);
        assertTrue(bitSet.get(5));
        assertTrue(bitSet.get(10));
        assertFalse(bitSet.get(7));
        assertEquals(2, bitSet.cardinality());
    }

    @Test
    public void testClearOperations() {
        CustomBitSet bitSet = new CustomBitSet();
        bitSet.set(1);
        bitSet.set(2);
        bitSet.set(3);
        bitSet.clear(2);
        assertTrue(bitSet.get(1));
        assertFalse(bitSet.get(2));
        assertTrue(bitSet.get(3));
        assertEquals(2, bitSet.cardinality());
    }

    @Test
    public void testFlipOperation() {
        CustomBitSet bitSet = new CustomBitSet();
        bitSet.set(5);
        assertTrue(bitSet.get(5));
        bitSet.flip(5);
        assertFalse(bitSet.get(5));
        bitSet.flip(5);
        assertTrue(bitSet.get(5));
    }

    @Test
    public void testLengthOperation() {
        CustomBitSet bitSet = new CustomBitSet();
        assertEquals(0, bitSet.length());
        bitSet.set(50);
        assertEquals(51, bitSet.length());
        bitSet.set(100);
        assertEquals(101, bitSet.length());
        bitSet.clear(100);
        assertEquals(51, bitSet.length());
    }

    @Test
    public void testToStringOperation() {
        CustomBitSet set = new CustomBitSet();
        set.set(1);
        set.set(3);
        set.set(5);
        assertEquals("{1, 3, 5}", set.toString());
        set.clear(3);
        assertEquals("{1, 5}", set.toString());
    }
}
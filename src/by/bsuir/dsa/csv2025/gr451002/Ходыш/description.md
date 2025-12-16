# Name

Bit Inversion

# Task

## Problem Statement in English
The user inputs two integers: the original number and the bit position (the least significant bit is considered position 0). It is required to invert the bit at the specified position of the original number, and then output the resulting value both in decimal representation and as a bit string.

## Program Requirements
### Data Input
The program must prompt the user for:
- The original integer (int type)
- The bit position to invert (an integer from 0 to 31)

### Input Data Validation:
If the bit position is outside the range 0-31, the program must display an error message and terminate.

### Result Output
The program should output:
- Result in decimal representation with formatting
- Result in binary representation as a 32-bit string

# Theory

## Theoretical Reference in English

### Bit Shifts

Shifting a number's bits to the left (x << y) is equivalent to multiplying x by 2 to the y power. Shifting it to the right (x >> y) is equivalent to dividing x by 2 to the y power (with the fractional part discarded).

### Bitwise Operations

For working with individual bits of a number, bitwise operations are used. The main operations are:

- **AND (&)**: `0 & 0 = 0`, `0 & 1 = 0`, `1 & 0 = 0`, `1 & 1 = 1`
- **OR (|)**: `0 | 0 = 0`, `0 | 1 = 1`, `1 | 0 = 1`, `1 | 1 = 1`
- **XOR (^)**: `0 ^ 0 = 0`, `0 ^ 1 = 1`, `1 ^ 0 = 1`, `1 ^ 1 = 0`
- **NOT (~)**: `~0 = 1`, `~1 = 0`

### Mask

Each binary sequence (for example, 1011) can be thought of as an instruction for forming a subset. Each position in the sequence corresponds to a specific element of the original set:
- 1 means "include this element in the subset"
- 0 means "do not include this element"

### Bit Inversion

To invert (toggle) a bit at a specified position, the **XOR** operation is used with a mask where only the target bit is set to 1.

**Algorithm:**
1. Create a mask: `1 << position`
2. Apply XOR: `number ^ mask`

**Example:**  
Invert the 2nd bit of 10 (binary: `1010`)  
Original number: 10 = 1010₂  
Position: 2  
Mask: 1 << 2 = 0100₂  
Result: 1010 ^ 0100 = 1110₂ = 14

**Another example:**  
Invert the 1st bit of 6 (binary: `0110`)  
Original number: 6 = 0110₂  
Position: 1  
Mask: 1 << 1 = 0010₂  
Result: 0110 ^ 0010 = 0100₂ = 4  


# Name

Bit representation of numbers

# Task

Bit representation of numbers

It is necessary to implement the BinaryString class for working with numbers in binary format. 
It is necessary to implement:
* Constructors:
    * Without parameters: the object instance must be initialized with 0
    * With a parameter of type Double: the object instance must be initialized with the argument value

* Methods:
    * BinaryString add(BinaryString value): Addition of two binary numbers (the result is stored in the calling object)
    * BinaryString sub(BinaryString value): Subtraction of two binary numbers (the result is stored in the calling object)
    * BinaryString mul(BinaryString value): Multiplication of two binary numbers (the result is stored in the calling object)
    * BinaryString div(BinaryString value): Division of two binary numbers (the result is stored in the calling object)
    * double getDoubleValue(): Get the decimal representation of the calling object's value
    * String getValue(): returns a string of the binary value of the calling object. The string consists of 0 and 1, and its value is the binary representation of the object's value in reverse code. The fractional part contains 4 characters and is formed by discarding the characters after it (NOT by mathematical rounding).
    Examples:
    1. -5.75 = "010.0011" (Non-significant 0s in the positive number and non-significant 1s in the negative number are discarded)
    2. 11 = "1011"The fractional part is discarded if it contains only zeros in the positive number or only ones in the negative number)

# Theory

 # Theory

Reverse code is a method of computational mathematics that allows you to subtract one number from another using only the addition operation on natural numbers.

In the binary number system, the reverse code is very simply obtained by inverting each bit (replacing "0" with "1" and vice versa). The additional code can be obtained from the Reverse code by adding one to the least significant bit (bit).
For example, consider the subtraction of two integers: 01100100−11101001 . We write each number as an 8-bit code, with the most significant 8-bit being the sign bit.

0110 0100 (x)  \
 \+             \
 1110 1001 (first complement of y) \
 \+             \
 1 (to get the second complement) \
———————————— \
 1 0100 1110


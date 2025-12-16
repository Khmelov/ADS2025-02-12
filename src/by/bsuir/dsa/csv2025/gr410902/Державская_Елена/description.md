# Name

Password strength assessment

# Task

Password Strength Assessment

## Problem Statement

Your task is to help the system assess the strength of a password created by a user. The system does not store the password in plain text, but rather stores its hash - the product of prime numbers corresponding to the password.

## Process Mechanics

- The password is encoded as a number `N` (for example, the sum of character codes)
- The system stores not `N` itself, but its **factorization** - the product of prime numbers in the form: `p1^a1 * p2^a2 * ... * pk^ak`
- Password strength is determined as follows:

### Strength Criteria

- **High strength**: Number `N` is prime. Such a password is extremely difficult to crack
- **Medium strength**: Number `N` is semiprime (product of exactly two prime numbers). This is a good level of protection
- **Low strength**: Number `N` has more than two prime factors (or one factor but with high multiplicity). Such a password is considered weak

## Constraints

- Password length no more than 20 characters, therefore `1 < N < 10^6`
- Number 1 is considered an "empty password" and is invalid

## Data Format

- **Input**: string `S` - password consisting of Latin letters and digits
- **Output**: string describing the strength level

## Output Format

- `HIGH` - if the password has high strength
- `MEDIUM` - if the password has medium strength
- `LOW` - if the password is weak
- `NULL` - if the password is empty or encoding results in number 1

## Implementation Requirements

The problem must be solved by implementing a number factorization algorithm (e.g., trial division).

## Examples

### Sample Input 1:
AB
### Sample Output 1:
HIGH

### Sample Input 2:
A
### Sample Output 2:
MEDIUM

### Sample Input 3:
abc
### Sample Output 3:
LOW

## Notes

- Password is encoded as the sum of ASCII codes of all characters
- The factorization algorithm should work efficiently for numbers up to 10⁶
- All prime factors are considered with their multiplicities


# Theory

**Theory: Factorization in Password Assessment**

Factorization is used to analyze the mathematical structure of a password: we convert the password into a number by summing character codes, factorize this number into prime factors, and the simpler the factorization - the stronger the password. A prime number (one factor) → high protection, semiprime (two factors) → medium protection, complex factorization (three+ factors) → low strength, as it indicates vulnerability to mathematical attacks.



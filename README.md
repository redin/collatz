# collatz
An arbitrary-precision Brute-force checker for [Collatz Conjecture](https://en.wikipedia.org/wiki/Collatz_conjecture).

## COMPILE
`javac com/conjecture/Collatz.java`
- Requires Java 8 or above.

## RUN
`java -cp . com.conjecture.Collatz 295147905179352825856`
- First program argument is the number to start from with default of 5.
- This will run forever outputting checks of each positive Integer number. 
- It will only stop and exit if it ever finds a loop above 5.

## Limitations
- It uses as many threads as the number of cores seen by the running JVM
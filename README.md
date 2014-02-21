Description :
--------------------

This parser is a simple math expression parser that
parses and evaluates various mathematical functions.

Triginometric functions : sin, cos, tan, arctan, arccos,arcsin, sec, cosec,cot 

Logarithms : ln is used for natural log, log10 is log base 10 and log2 is log base 2

Other functions : exp for exponentiation, abs for absolute value, sqrt for square root

Numbers like 45.3e-13 are also supported 

This parser can also take functions of one variable like
a math relation log10(x*sin(30*x)) with a variable x. Whenever such
expressions are encountered you are asked to enter the value of x.

Please Note :
-------------------- 

1. Trigonometric functions work with radians and not degrees
2. sin x is not valid but sin(x) or sin (x) is valid. Similarly its true for others too
3. This parser also skips whitespaces and so it will work as long as parentheisation is in correct order

--------------------

Type help, description, usage for assistence and exit to leave.

Usage :Type any mathematical expression with the functions stated above in the description
example : sin(2*x)*cos(x)

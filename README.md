# Linear-Equations-Solver

A linear equations solver capable of linear equations systems involving complex numbers.

Prerequisites
-------------
This program requires Java to compile and run.

Installation
------------

1. Download this repository and unzip the .zip file in your desired location.
2. Using the command line, navigate to \Linear-Equations-Solver-master\src\solver.
3. Compile the program using the command `javac Main.java`.

Usage
-----

Once Linear Equations Solver is compiled, it can be run from the command line by navigating to \Linear-Equations-Solver-master\src and using the command `java solver.Main` with the arguments `-in file`, where `file` is the file path and name of a .txt file containing the linear equations system, and `-out file`, where `file` is the file path and name of a .txt file to which the solver's results should be written. If the specified file does not already exist, it will be created; otherwise, the existing file will be overwritten.

The text in the input file should be formatted such that the first line consists of two integers, the number of variables and the number of equations, separated by a space and each following line consists of the coefficients and result of a single equation, each separated by a space. For example, the equations 2x + 3y = 12 and x + 2y = 7 become:

2 2

2 3 12

1 2 7

Complex numbers should not have any spaces. `1+2i` is good, but `1 + 2i` is not.

Linear Equations Solver will output the results with each variable's value on a separate line. Continuing the example above, this would mean that the value of x appears on line 1 and the value of y appears on line 2.

If the solver determines that the given equations system has no solutions or an infinite number of solutions, it will write this result to the output file.

Credits
-------

**Author:** Kate Jordan - [sinnenicht](https://github.com/sinnenicht/)

This program is based on the Linear Equations Solver project on [Jet Brains Academy](https://hyperskill.org/projects/40?goal=7).

License
-------

This project is licensed under the GNU General Public License v3.0. See the [LICENSE](https://github.com/sinnenicht/Linear-Equations-Solver/blob/master/LICENSE) for details.

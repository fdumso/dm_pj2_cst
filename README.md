# Discrete Math Project CST

## Build
You can run this program by either simply running the jar package or import the project into IntelliJ IDEA and compile it by yourself.

You can use command line with the following command to run the jar package:
    $ java -jar cst.jar [INPUT_FILE]
The you will get an output file with a prefix "result_" added on the input file name.

## Implementation
1. Parse the input lines
2. Construct CST with the standard algorithm
3. Output as instruction in the document tells
4. Construct counter example by trace back from a leaf node that finish normally to the root node and list all the proposition letter on the path


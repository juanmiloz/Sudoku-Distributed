module Sudoku{

    sequence<string> StringMatrix;
    ["java:type:String[][]"]

    interface MatrixGeneratorI{
        string generateStage(int quantityNumbers);
    };

    interface ControllerI{
        void addSolution(StringMatrix solution);
        void addElementToStack(StringMatrix element);
    };

    interface Solver{
        void claimMatrix();
    };
};
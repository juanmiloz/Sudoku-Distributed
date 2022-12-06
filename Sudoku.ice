module Sudoku{


    sequence<bool> BooleanArray;
    sequence<BooleanArray> BooleanMatrix;
    struct MutableByte{
        byte value;
    };

    sequence<MutableByte> MutableByteArray;
    sequence<MutableByteArray> MutableByteMatrix;
    struct Node{
        MutableByte row;
        MutableByte col;
        MutableByteMatrix sol;
    };

    ["java:type:java.util.LinkedList<Node>"]
    sequence<Node> Queue;

    interface PersistentQueueControllerI{
        Node pollNode();
        bool offerNode(Node node);
        bool isEmpty();
    };

    interface ControllerI{
        void addSolution(MutableByteMatrix solution);
        bool addElementToQueue(Node element);
        void notifyFreeNode();
    };

    interface Solver{
        bool solve(Node sudoku, BooleanMatrix initial, ControllerI* controllerCl);
    };
};
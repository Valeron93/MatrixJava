package com.valeron;

import com.valeron.matrix.*;

public class Main {

    public static void main(String[] args) {

        var matrix = new FinalMatrix(new double[][] {
                {0, 1, 0, -2, 1},
                {1, 0, 3, 1, 1},
                {1, -1, 1, 1, 1},
                {2, 2, 1, 0, 1},
                {3, 1, 1, 1, 2}
        });


        System.out.println(matrix.det());

        var m = new FinalMatrix(new double[][] {
                {1, 2, 6},
                {6, 2, 0},
                {11, 5, 4}
        });

        System.out.println(m.det());

        System.out.println(Matrix.mul(m, m.inverse()) + "\n");

        System.out.println(Matrix.mul(matrix, matrix.inverse()));


    }
}
package com.valeron.matrix;

import org.junit.Test;

import static org.junit.Assert.*;

public class MatrixTest {

    @Test
    public void getRows() {

        var matrix = new Matrix(4, 5);

        assertEquals(matrix.getRows(), 4);

    }

    @Test
    public void getCols() {

        var matrix = new Matrix(4, 5);

        assertEquals(matrix.getCols(), 5);
    }

    @Test
    public void set() {

        var matrix = new Matrix(2, 2);
        matrix.set(0, 0, 1.0);

        var matrix2 = new Matrix(new double[][]{ {1, 0}, {0, 0} });

        assertEquals(matrix, matrix2);
    }

    @Test
    public void identity() {

        var identity = new Matrix(new double[][] {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        });

        assertEquals(identity, Matrix.identity(3));
    }

    @Test
    public void get() {

        var matrix = new Matrix(new double[][] {{22.8}});

        assertEquals(22.8, matrix.get(0,0), Matrix.EPSILON);

    }

    @Test
    public void getRow() {

        var matrix = new Matrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
        });

        var expected = new Matrix(new double[][] {
                {4, 5, 6}
        });

        assertEquals(expected, matrix.getRow(1));
    }

    @Test
    public void getColumn() {

        var matrix = new Matrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
        });

        var expected = new Matrix(new double[][] {
                {2},
                {5},
                {8}
        });

        assertEquals(expected, matrix.getColumn(1));
    }

    @Test
    public void inverse() {

        var matrix = new Matrix(new double[][]{
                {1, 2, 3},
                {3, 2, 1},
                {2, 1, 3}
        });

        var expected = new Matrix(new double[][]{
                {-5.0/12.0,  1.0/4.0,  1.0/3.0},
                { 7.0/12.0,  1.0/4.0, -2.0/3.0},
                { 1.0/12.0, -1.0/4.0,  1.0/3.0},
        });

        matrix.inverse();


        assertEquals(expected, matrix);

    }

    @Test(expected = ArithmeticException.class)
    public void nonInvertibleMatrix() {


        var matrix = new Matrix(new double[][]{
                {1, 1, 2},
                {2, 2, 4},
                {0, 2, 6}
        });


        matrix.inverse();

    }

    @Test
    public void testEquals() {

        var matrix = new Matrix(new double[][] {
                {2,4,8},
                {3,7,1},
                {1,2,7}
        });


        var matrix2 = new Matrix(new double[][] {
                {2,4,8},
                {3,7,1},
                {1,2,7}
        });

        assert matrix.equals(matrix2);
    }
}
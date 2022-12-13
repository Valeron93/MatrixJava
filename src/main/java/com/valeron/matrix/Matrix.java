package com.valeron.matrix;

import java.util.Arrays;

public class Matrix {

    protected double[][] array;

    protected int rows;
    protected int cols;

    public static final double EPSILON = 0.000001d;

    public Matrix(int rows, int cols) {

        if (rows < 1 || cols < 1) {
            throw new IndexOutOfBoundsException("Wrong matrix size! Rows: " + rows + ", columns: " + cols);
        }

        this.rows = rows;
        this.cols = cols;
        array = new double[rows][cols];
    }

    public Matrix(double[][] array) {

        this(array.length, array[0].length);

        for (int i = 0; i < rows; i++) {

            if (array[i] == null || array[i].length != cols) {
                throw new IndexOutOfBoundsException("Failed to copy array to matrix");
            }

        }

        for (int i = 0; i < rows; i++) {
            System.arraycopy(array[i], 0, this.array[i], 0, cols);
        }

    }
    public Matrix() {
        this(1, 1);
    }

    public Matrix(Matrix other) {
        this(other.rows, other.cols);

        for (int j = 0; j < cols; j++) {
            System.arraycopy(other.array[j], 0, array[j], 0, rows);
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Matrix set(int row, int col, double value) {
        array[row][col] = value;
        return this;
    }

    public static Matrix identity(int size) {

        var matrix = new Matrix(size, size);

        for (int i = 0; i < size; i++) {
            matrix.set(i, i, 1);
        }

        return matrix;
    }

    public double get(int row, int col) {
        return array[row][col];
    }

    public Matrix getRow(int row) {

        var matrix = new Matrix(1, cols);

        for (int i = 0; i < cols; i++) {
            matrix.set(0, i, get(row, i));
        }

        return matrix;

    }

    public Matrix getColumn(int col) {

        var matrix = new Matrix(rows, 1);

        for (int i = 0; i < rows; i++) {
            matrix.set(i, 0, get(i, col));
        }

        return matrix;
    }


    public static Matrix mul(Matrix a, Matrix b) {

        if (a.cols != b.rows) {
            throw new ArithmeticException("failed to multiply matrices");
        }

        int rows = a.rows;
        int cols = b.cols;

        var result = new Matrix(rows, cols);


        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < cols; j++) {

                double sum = 0.0;

                for (int k = 0; k < b.rows; k++) {
                    sum += a.get(i, k) * b.get(k, j);
                }

                result.set(i, j, sum);
            }
        }
        return result;
    }


    public String formatted(String format) {

        var sb = new StringBuilder();

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < cols; j++) {
                sb.append(String.format(format, array[i][j])).append(" ");
            }
            if (i != cols-1) {
                sb.append('\n');
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return formatted("%8.3f");
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(array);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Matrix matrix = (Matrix) o;

        if (rows != matrix.rows || cols != matrix.cols) {
            return false;
        }

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < cols; j++) {
                if (!doubleEquals(get(i, j), matrix.get(i, j))) {
                    return false;
                }
            }
        }

        return true;
    }

    private static int powerOne(int num) {
        return num % 2 == 0 ? 1 : -1;
    }

    private Matrix deleteRowAndColumn(int idx, int jdx) {

        var r = new Matrix(rows-1, cols-1);

        for (int i = 0; i < idx; i++) {

            for (int j = 0; j < jdx; j++) {
                r.set(i, j, get(i, j));
            }

            for (int j = jdx+1; j < cols; j++) {
                r.set(i, j-1, get(i, j));
            }

        }

        for (int i = idx+1; i < rows; i++) {

            for (int j = 0; j < jdx; j++) {
                r.set(i-1, j, get(i, j));
            }

            for (int j = jdx+1; j < cols; j++) {
                r.set(i-1, j-1, get(i, j));
            }

        }

        return r;
    }

    public double det() {

        if (rows != cols) {
            throw new ArithmeticException("width has to be equal to height");
        }

        int size = rows;

        if (size == 1) {
            return get(0, 0);
        }

        double det = 0;
        for (int k = 0; k < size; k++) {
            var matrix = deleteRowAndColumn(0, k);
            det += get(0, k) * powerOne(k+1) * matrix.det();
        }

        return det;
    }

    public Matrix inverse() {

        if (rows != cols) {
            throw new ArithmeticException("Matrix is not invertible");
        }

        int size = rows;

        var det = det();
        if (doubleEquals(det, 0)) {
            throw new ArithmeticException("Matrix is not invertible");
        }

        var result = new Matrix(size, size);

        for (int i = 0; i < size; i++) {

            for (int j = 0; j < size; j++) {

                double value = (powerOne(i+j+1) * deleteRowAndColumn(i, j).det()) / det;

                result.set(j, i, value);
            }

        }

        this.array = result.array;
        return this;
    }

    private static boolean doubleEquals(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

}

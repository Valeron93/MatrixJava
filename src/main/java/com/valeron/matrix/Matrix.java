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

    public Matrix inverse() {

        Matrix result = augment().reducedRowEchelonForm();

        if (!result.slice(0, 0, rows, rows).equals(Matrix.identity(rows))) {
            throw new ArithmeticException("Matrix is not invertible");
        }

        for (int row = 0; row < rows; ++row) {
            System.arraycopy(result.array[row], cols, array[row], 0, cols);
        }

        return this;
    }

    private Matrix slice(int startRow, int startCol, int endRow, int endCol) {

        var result = new Matrix(endRow - startRow, endCol - startCol);

        for (int i = startRow; i < endRow; i++) {

            for (int j = startCol; j <  endCol; j++) {
                result.set(i - startRow, j - startCol, get(i, j));
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

    private Matrix reducedRowEchelonForm() {

        int lead = 0;

        for (int r = 0; r < rows; r++) {
            if (cols <= lead) {
                return this;
            }
            var i = r;
            while (get(i, lead) == 0) {
                i++;
                if (rows == i) {
                    i = r;
                    lead++;
                    if (cols == lead) {
                        return this;
                    }
                }
            }

            swapRows(i, r);

            var val = get(r, lead);
            for (int j = 0; j < cols; j++) {
                var b = get(r, j) / val;
                set(r, j, b);
            }

            for (i = 0; i < rows; i++) {
                if (i == r) continue;
                val = get(i, lead);
                for (int j = 0; j < cols; j++) {
                    var b =  get(i, j) - val * get(r, j);
                    set(i, j, b);
                }
            }
            lead++;
        }
        return this;
    }

    private Matrix augment() {
        if (cols != rows) {
            throw new ArithmeticException("Cannot augment non-square matrix");
        }

        Matrix result = new Matrix(rows, cols * 2);
        for (int row = 0; row < rows; ++row) {
            System.arraycopy(array[row], 0, result.array[row], 0, cols);
            result.array[row][row + cols] = 1;
        }

        return result;
    }

    private void swapRows(int i, int j) {
        double[] temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private static boolean doubleEquals(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

}

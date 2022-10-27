package com.valeron.matrix;

public final class FinalMatrix extends Matrix {
    public FinalMatrix() {
        super();
    }

    public FinalMatrix(int rows, int cols) {
        super(rows, cols);
    }

    public FinalMatrix(Matrix other) {
        super(other);
    }

    public FinalMatrix(FinalMatrix other) {
        super(other);
    }

    public FinalMatrix(double[][] array) {
        super(array);
    }

    @Override
    public FinalMatrix set(int row, int col, double value) {
        Matrix result = new Matrix(this);
        result.set(row, col, value);
        return copyOwnedMatrix(result);
    }

    public static FinalMatrix identity(int size) {
        return copyOwnedMatrix(Matrix.identity(size));
    }

    @Override
    public FinalMatrix inverse() {
        var copy = new Matrix(this);

        return copyOwnedMatrix(copy.inverse());
    }

    @Override
    public FinalMatrix getRow(int row) {
        return copyOwnedMatrix(super.getRow(row));
    }

    @Override
    public FinalMatrix getColumn(int col) {
        return copyOwnedMatrix(super.getColumn(col));
    }

    private static FinalMatrix copyOwnedMatrix(Matrix other) {
        FinalMatrix result = new FinalMatrix();

        result.array = other.array;
        result.cols = other.cols;
        result.rows = other.rows;

        return result;
    }

}

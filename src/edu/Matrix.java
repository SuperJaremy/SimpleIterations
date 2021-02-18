package edu;

public class Matrix {
    private double[][] A;
    private double[] b;
    private int size;
    private double epsilon;

    public Matrix(int size) {
        this.size = size;
        A = new double[size][size];
        b = new double[size];
    }

    public int getSize() {
        return size;
    }

    public double[][] getA() {
        return A;
    }

    public double[] getB() {
        return b;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }
}

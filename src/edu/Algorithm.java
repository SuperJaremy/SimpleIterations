package edu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Algorithm {
    private final List<double[]> solutions;
    private final List<Double> errors;
    private double[] solution;
    private int counter;
    private HashMap<Integer, List<Row>> weakPositions;

    public Algorithm(Matrix matrix) {
        solutions = new ArrayList<>();
        errors = new ArrayList<>();
        counter = 0;
        weakPositions = new HashMap<>();
        solution = solve(matrix.getA(), matrix.getB(), matrix.getSize(), matrix.getEpsilon());
    }

    private double[] solve(double[][] A, double[] b, int size, double epsilon) {
        double[][] _A = A.clone();
        double[] _b = b.clone();
        if (!normalize(_A, _b, size)) {
            System.out.println("Диагональное преобладание не может быть достигнуто!");
            return null;
        }
        express(_A, _b, size);
        solutions.add(_b);
        errors.add(null);
        counter = 0;
        do {
            counter++;
            findSolutions(_A, _b, solutions.get(counter - 1), size);
        } while (!checkAccuracy(errors.get(counter), epsilon));
        for (int i = 0; i <= counter; i++) {
            System.out.print("Шаг " + i + ": ");
            double[] solution = solutions.get(i);
            for (int j = 0; j < size; j++) {
                System.out.print("x_" + (j + 1) + ": " + solution[j] + ", ");
            }
            System.out.println("Погрешность: " + errors.get(i));
        }
        return solutions.get(counter);
    }

    public double[] getSolution() {
        return solution;
    }

    public double[] getErrorVector() {
        return errors.stream().mapToDouble(aDouble -> aDouble).toArray();
    }

    public int getCounter() {
        return counter;
    }

    private boolean normalize(double[][] A, double[] b, int size) {
        boolean strict = false;
        Row[] rows = new Row[size];
        int[] order = new int[size];
        for (int i = 0; i < size; i++) {
            order[i] = -1;
        }
        for (int i = 0; i < size; i++) {
            rows[i] = new Row(A[i], i, b[i]);
            if (rows[i].strict)
                strict = true;
        }
        if (!strict)
            return false;
        for (Row i : rows) {
            if (i.strongPos > -1 && order[i.strongPos] == -1) {
                order[i.strongPos] = i.position;
                if (weakPositions.containsKey(i.strongPos))
                    for (Row j : weakPositions.get(i.strongPos)) {
                        j.weakPos.remove(Integer.valueOf(i.strongPos));
                    }
                weakPositions.remove(i.strongPos);
            }
        }
        for (int i = 0; i < size; i++) {
            if (order[i] == -1) {
                if (!weakPositions.containsKey(i))
                    return false;
                if (weakPositions.get(i).size() == 1)
                    order[i] = weakPositions.get(i).get(0).position;
                else {
                    Row positionedRow = null;
                    List<Row> positions = weakPositions.get(i);
                    for (Row j : positions)
                        if (j.weakPos.size() == 1) {
                            order[i] = j.position;
                            positionedRow = j;
                            break;
                        }
                    if (positionedRow == null) {
                        order[i] = weakPositions.get(i).get(0).position;
                        positionedRow = weakPositions.get(i).get(0);
                    }
                    for (Row j : weakPositions.get(i)) {
                        j.weakPos.remove(Integer.valueOf(i));
                    }
                    for (Integer j : weakPositions.keySet()) {
                        weakPositions.get(j).remove(positionedRow);
                    }
                }
            }
        }
        if (Arrays.stream(order).min().getAsInt() < 0)
            return false;
        for (int i = 0; i < size; i++) {
            A[i] = rows[order[i]].row;
            b[i] = rows[order[i]].b;
        }
//               printMatrix(A,b,size);
        return true;
    }

    private void express(double[][] A, double[] b, int size) {
        for (int i = 0; i < size; i++) {
            double coefficient = A[i][i];
            for (int j = 0; j < size; j++) {
                if (i == j)
                    A[i][j] = 0;
                else
                    A[i][j] /= -coefficient;
            }
            b[i] /= coefficient;
        }
    }

    private void findSolutions(double[][] C, double[] d, double[] x, int size) {
        double[] solution = new double[size];
        for (int i = 0; i < size; i++) {
            double x_i = 0;
            for (int j = 0; j < size; j++) {
                x_i += C[i][j] * x[j];
            }
            x_i += d[i];
            solution[i] = x_i;
        }
        solutions.add(solution);
        checkErrors(solution, x, size);
    }

    private void checkErrors(double[] solution, double[] lastSolution, int size) {
        if (size >= 0) {
            double max = Math.abs(solution[0] - lastSolution[0]);
            for (int i = 1; i < size; i++) {
                double err = Math.abs(solution[i] - lastSolution[i]);
                max = Math.max(max, err);
            }
            errors.add(max);
        }
    }

    private boolean checkAccuracy(double error, double epsilon) {
        return error <= epsilon;
    }

    private void printMatrix(double[][] A, double[] b, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
                System.out.print(A[i][j] + " ");
            System.out.println("| "+b[i]);
        }
    }

    private class Row {
        double[] row;
        double b;
        int strongPos = -1;
        List<Integer> weakPos = null;
        int position;
        boolean strict = false;

        private Row(double[] row, int position, double b) {
            this.position = position;
            this.row = row;
            this.b = b;
            double[] _row = new double[row.length];
            for (int i = 0; i < row.length; i++)
                _row[i] = Math.abs(row[i]);
            for (int i = 0; i < _row.length; i++) {
                double coefficient = _row[i];
                double sum = 0;
                for (int j = 0; j < _row.length; j++) {
                    if (i != j)
                        sum += _row[j];
                }
                if (coefficient > sum) {
                    strongPos = i;
                    strict = true;
                } else if (coefficient == sum) {
                    if (weakPos == null)
                        weakPos = new ArrayList<>();
                    weakPos.add(i);
                    if (!weakPositions.containsKey(i))
                        weakPositions.put(i, new ArrayList<>());
                    weakPositions.get(i).add(this);
                }
            }
        }
    }
}

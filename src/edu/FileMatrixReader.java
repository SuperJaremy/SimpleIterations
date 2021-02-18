package edu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class FileMatrixReader extends MatrixReader {

    @Override
    public Matrix readMatrix() {
        System.out.println(" -------------------###!!!Требования к оформлению файла!!!###-------------------");
        System.out.println("|                        Первая строка - размер матрицы                         |");
        System.out.println("| Следущие n строк - строки матрицы системы, элементы перчисляются через пробел |");
        System.out.println("|         Следующая строка - свободные члены, перечисленные через пробел        |");
        System.out.println("|                          Последняя строка - точность                          |");
        System.out.println(" -------------------------------------------------------------------------------");
        System.out.println();
        System.out.println("Введите имя файла");
        Scanner input = new Scanner(System.in);
        String filename = input.nextLine().trim();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            int size = Integer.parseInt(line.trim());
            Matrix matrix = new Matrix(size);
            double[][] A = matrix.getA();
            double[] b = matrix.getB();
            for (int i = 0; i < size; i++){
                line = reader.readLine().trim();
                String[] numbers = line.split(" ");
                for(int j = 0; j<size; j++){
                    A[i][j] = Double.parseDouble(numbers[j]);
                }
            }
            line = reader.readLine().trim();
            String[] numbers = line.split(" ");
            for(int i =0; i<size;i++){
                b[i]=Double.parseDouble(numbers[i]);
            }
            line = reader.readLine().trim();
            matrix.setEpsilon(Double.parseDouble(line));
            return matrix;
        } catch (IOException | NumberFormatException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
}

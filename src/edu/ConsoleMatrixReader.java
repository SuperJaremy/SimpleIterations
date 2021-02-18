package edu;

import java.util.Scanner;

public class ConsoleMatrixReader extends MatrixReader {
    @Override
    public Matrix readMatrix(){
        Scanner input = new Scanner(System.in);
        System.out.println("Введите количество неизвестных");
        int size = input.nextInt();
        Matrix matrix = new Matrix(size);
        double[][] A = matrix.getA();
        double[] b = matrix.getB();
        System.out.println("Сейчас вы введёте матрицу коэффициентов");
        for(int i=0;i<size;i++){
            System.out.println("Введите "+size+" чисел через пробел, а затем нажмите Enter");
            for(int j=0;j<size;j++){
                A[i][j]=input.nextDouble();
            }
        }
        System.out.println("Теперь вы введёте матрицу свободных членов");
        System.out.println("Введите "+size+" чисел через пробел, а затем нажмите Enter");
        for(int i=0;i<size;i++){
            b[i]=input.nextDouble();
        }
        System.out.println("Введите точность");
        matrix.setEpsilon(input.nextDouble());
        return matrix;
    }
}

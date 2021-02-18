package edu;


import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Как будет производится ввод?[f]ile/[c]onsole");
        MatrixReader reader;
        Scanner input = new Scanner(System.in);
        String line = input.nextLine().trim();
        if(line.equals("f"))
            reader = new FileMatrixReader();
        else
            reader = new ConsoleMatrixReader();
        Matrix matrix = reader.readMatrix();
        if(matrix==null)
            return;
        Algorithm algorithm = new Algorithm(matrix);
    }
}

import java.util.Scanner;

public class Sevomi {
    public static boolean dfsRecursion(int i , int j , int[][] array , int val , boolean[] containedNumbers , int maxxi) {
        if (array[i][j] != 0 && array[i][j] != val)
            return false;
        else if (containedNumbers[val-1] && array[i][j] != val)
            return false;
        else
        {
            int temp = array[i][j];
            array[i][j] = val;
            if (val == maxxi)
                return true;
            else {
                if (dfsRecursion(i, j - 1, array, (val + 1), containedNumbers , maxxi))
                    return true;
                if (dfsRecursion(i, j + 1, array, (val + 1), containedNumbers , maxxi))
                    return true;
                if (dfsRecursion(i - 1, j - 1, array, (val + 1), containedNumbers , maxxi))
                    return true;
                if (dfsRecursion(i + 1, j + 1, array, (val + 1), containedNumbers , maxxi))
                    return true;
                if (dfsRecursion(i - 1, j, array, (val + 1), containedNumbers , maxxi))
                    return true;
                if (dfsRecursion(i + 1, j, array, (val + 1), containedNumbers , maxxi))
                    return true;
                array[i][j] = temp;
                return false;
            }
        }
    }
    public static void main (String[]args){
            Scanner sc = new Scanner(System.in);
            int toleZel = sc.nextInt();
            int vasat = (2 * toleZel) - 1;
            int[][] jadval = new int[vasat + 2][vasat + 2];
            int maxNumber = 0;
            int maxRow = 0;
            int maxCol = 0;
            boolean[] containedNumbers = new boolean[3*toleZel*toleZel - 3*toleZel +1];
            for (int i = 0; i < vasat + 2; i++) {
                if(i == 0 || i == vasat + 1) {
                    for (int k = 0; k < vasat + 2; k++) {
                        jadval[i][k] = -1;
                    }
                }
                else if (1 <= i && i < toleZel + 1) {
                    for (int j = 0; j < vasat + 2 ; j++) {
                        if (1 <= j && j < i + toleZel) {
                            int adadVorodi = sc.nextInt();
                            jadval[i][j] = adadVorodi;
                            if (jadval[i][j] > 0) {
                                containedNumbers[jadval[i][j] - 1] = true;
                                if (jadval[i][j] == 1) {
                                    maxRow = i;
                                    maxCol = j;
                                }
                                if(jadval[i][j] > maxNumber) {
                                    maxNumber = jadval[i][j];
                                }
                            }
                        } else {
                            jadval[i][j] = -1;
                        }
                    }
                }
                else if(i >= toleZel + 1 && i < vasat + 2){
                    for (int j = 0; j < vasat + 2; j++) {
                        if ((i - toleZel + 1) <= j && j < vasat + 1) {
                            int adadVorodi = sc.nextInt();
                            jadval[i][j] = adadVorodi;
                            if (jadval[i][j] > 0) {
                                containedNumbers[jadval[i][j] - 1] = true;
                                if (jadval[i][j] == 1) {
                                    maxRow = i;
                                    maxCol = j;
                                }
                                if(jadval[i][j] > maxNumber) {
                                    maxNumber = jadval[i][j];
                                }
                            }
                    }
                        else {
                            jadval[i][j] = -1;
                        }
                    }
                }
            }
            dfsRecursion(maxRow , maxCol , jadval , 1 , containedNumbers , maxNumber);
            for (int i = 0; i < vasat + 1; i++) {
                   if (1 <= i && i < toleZel + 1) {
                        for (int j = 0; j < vasat + 2 ; j++) {
                            if (1 <= j && j < i + toleZel) {
                                System.out.print(jadval[i][j] + " ");
                            }
                        }
                       System.out.println("");
                    }
                    else if(i >= toleZel + 1 && i < vasat + 1){
                        for (int j = 0; j < vasat + 2; j++) {
                            if ((i - toleZel + 1) <= j && j < vasat + 1) {
                                System.out.print(jadval[i][j] + " ");
                            }
                        }
                       System.out.println("");
                    }
                }
        }
    }
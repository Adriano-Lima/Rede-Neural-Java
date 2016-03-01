
import java.util.ArrayList;

public class Matriz {

    public static void preencherZero(Double matriz[][], int li, int co) {
        for (int i = 0; i < li; i++) {
            for (int j = 0; j < co; j++) {
                matriz[i][j] = 0.0;
            }
        }
    }

    public static void imprimir(Double matriz[][], int linhas, int colunas) {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.out.printf("%f ", matriz[i][j]);
            }
            System.out.println("");
        }
    }

}

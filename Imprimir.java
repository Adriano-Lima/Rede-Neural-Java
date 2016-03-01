
public class Imprimir {

    public static void imprimir_matriz3(Double mat[][][], int linhas, int colunas, int dimensoes) {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.err.printf("[%d][%d]:[", i, j);
                for (int n = 0; n < dimensoes; n++) {
                    System.err.printf("%.2f ", mat[i][j][n]);
                }
                System.err.printf("]");
                System.err.printf("\n");
            }
        }
    }
    
        public static void imprimir_matriz3(Integer mat[][][], int linhas, int colunas, int dimensoes) {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.err.printf("[%d][%d]:[", i, j);
                for (int n = 0; n < dimensoes; n++) {
                    System.err.printf("%d ", mat[i][j][n]);
                }
                System.err.printf("]");
                System.err.printf("\n");
            }
        }
    }

    public static void imprimir_matriz2(Double mat[][], int linhas, int colunas) {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.err.printf("[%d][%d]:%f", i, j, mat[i][j]);
            }
            System.err.println("");
        }
    }

    public static void imprimir_matriz2(Integer mat[][], int linhas, int colunas) {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.err.printf("[%d][%d]:%d ", i, j, mat[i][j]);
            }
            System.err.println("");
        }
    }

}


import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Padrao {

    Double padroes[][];
    int linhas, colunas;

    public Padrao(int linhas, int colunas) {
        padroes = new Double[linhas][colunas];
        this.linhas = linhas;
        this.colunas = colunas;
    }

    public void preencherVetoresPadrao(BufferedReader rd, ArrayList<Integer> tipos) throws IOException {
        for (int i = 0; i < linhas; i++) {
            String leitura = rd.readLine();
            String vec[] = leitura.split(" ");
            tipos.add(Integer.parseInt(vec[0])); //na primeira coluna da linha tem o tipo do padrão 
            for (int j = 0; j < colunas; j++) {//preenchendo as colunas do vetor padrão
                padroes[i][j] = Double.parseDouble(vec[j+1]);
            }
        }
    }

    public void imprimir() {
        System.out.printf("-- Vetores Padrão --\n");
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.out.printf("[%d][%d]:%f ", i, j, padroes[i][j]);
            }
            System.out.printf("\n");
        }
    }
}

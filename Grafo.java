
import java.util.ArrayList;


public class Grafo {

    public void imprimir_grafo(Double neu[][][],
            Double mat_cont[][][],
            int r,
            int s,
            int D,
            int quant_tipos,
            int OP) {
        int di = 0, menorlinha = 0, menorcoluna = 0, maiorlinha = 0, maiorcoluna = 0, cont = 0, cont2 = 0, teste = 0;
//Sepando por vizinhos e imprimindo
//grafo de relacionamento dos neurônios
        Vizinho vizinho = new Vizinho();
        System.err.printf("graph {\n");
        System.err.printf("node [style=filled];\n");
        System.err.printf("\n");
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < s; j++) {
                System.err.printf("\"%d,%d\"", i, j);
                if (quant_tipos > 4) {
                    System.err.printf("[fontsize=8,label=\"(");
                } else {
                    System.err.printf("[label=\"("); //descommentar aqui se quiser imprimir tb a matriz de contagem junto
                }
                for (di = 0; di < quant_tipos; di++) {
                    if (di < (quant_tipos - 1)) {
                        System.err.printf("%.f,", mat_cont[i][j][di]); //descommentar aqui se quiser imprimir tb a matriz de contagem junto
                    } else {
                        System.err.printf("%.f", mat_cont[i][j][di]); //descommentar aqui se quiser imprimir tb a matriz de contagem junto
                    }
                }
                System.err.printf(")\"");  //descommentar aqui se quiser imprimir tb a matriz de contagem junto
                System.err.printf(",color=\"");
                for (di = 0; di < D; di++) {
                    System.err.printf("%.2f ", neu[i][j][di]);
                }
                if (D <= 2) {
                    for (int k = di; k < 3; k++) {
                        System.err.printf(".5 ");
                    }
                }
                System.err.printf("\"];\n");
            }
        }

        System.err.printf("\n");
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < s; j++) {
                if (i == r - 1 && j == s - 1) //para ignorar última posição da matriz
                {
                    continue;
                }
                System.err.printf("\"%d,%d\" ", i, j);
                System.err.printf(" -- ");
                System.err.printf("{");
                menorlinha = i - 1;
                if (menorlinha < 0) {
                    menorlinha = 0;
                }
                menorcoluna = j - 1;
                if (menorcoluna < 0) {
                    menorcoluna = 0;
                }
                maiorlinha = i + 1;
                if (maiorlinha >= r) {
                    maiorlinha = r - 1;
                }
                maiorcoluna = j + 1;
                if (maiorcoluna >= s) {
                    maiorcoluna = s - 1;
                }
                for (int k = menorlinha; k <= maiorlinha; k++) {
                    for (int l = menorcoluna; l <= maiorcoluna; l++) {
                        cont++; //contar quantos são os vizinhos
                    }
                }
                cont--;//para tirar do cálculo próprio neurônio

                for (int k = menorlinha; k <= maiorlinha; k++) {
                    for (int l = menorcoluna; l <= maiorcoluna; l++) {
                        if (k == i && l == j) // para não imprimir o prórpio neurônio
                        {
                            continue;
                        } else {
                            if ((k <= i && l <= j) || (k < i && l >= j)) {
                                teste = vizinho.ver_seeh_vizinho(r, s, i, j, k, l, OP);
                                if (teste == 1) {
                                    continue;
                                }
                            } else {
                                cont2++;
                                if (cont2 == cont) {
                                    System.err.printf("\"%d,%d\" ", k, l);
                                } else {
                                    System.err.printf("\"%d,%d\" ; ", k, l);
                                }
                            }
                        }
                    }
                }
                System.err.printf("};\n");
                cont = cont2 = 0;
            }
        }
        System.err.printf("\n}\n");
    }

   
    public void organiza_grafo(Rede neu) {
// vetores para obter o menor e maior valor de cada dimensão D da matriz neu
        Double menor[] = new Double[neu.dimensoes];
        Double maior[] = new Double[neu.dimensoes];

//inicializando os vetores
        for (int di = 0; di < neu.dimensoes; di++) {
            maior[di] = menor[di] = neu.rede[0][0][0];
        }
// procurando o menor e maior valor de cada dimensão da matriz neu
        for (int i = 0; i < neu.linhas; i++) {
            for (int j = 0; j < neu.colunas; j++) {
                for (int di = 0; di < neu.dimensoes; di++) {
                    if (neu.rede[i][j][di] < menor[di]) {
                        menor[di] = neu.rede[i][j][di];
                    }
                    if (neu.rede[i][j][di] > maior[di]) {
                        maior[di] = neu.rede[i][j][di];
                    }
                }
            }
        }
// alterando os valores da matriz neu para ficarem entre 0 e 1
//seguindo formula proposta pelo orientador (x[i]-min(x))/(max(x)-min(x))
        for (int i = 0; i < neu.linhas; i++) {
            for (int j = 0; j < neu.colunas; j++) {
                for (int di = 0; di < neu.dimensoes; di++) {
                    if (maior[di] > 1) {
                        neu.rede[i][j][di] = (neu.rede[i][j][di] - menor[di]) / (maior[di] - menor[di]);
                    }
                }
            }
        }
    }

}

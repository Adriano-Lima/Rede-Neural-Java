
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Rede {

    Double rede[][][];
    int linhas, colunas, dimensoes;
    Random gerador;
    Map<Integer, Bmu> map;

    //construtor
    public Rede(int linhas, int colunas, int dimensoes, int seed) {
        rede = new Double[linhas][colunas][dimensoes];
        this.linhas = linhas;
        this.colunas = colunas;
        this.dimensoes = dimensoes;
        this.gerador = new Random(seed);
        this.map = new LinkedHashMap<Integer, Bmu>();;

    }

    public void preencherRedeAleatoriamente() throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                for (int di = 0; di < dimensoes; di++) {
                    rede[i][j][di] = gerador.nextDouble();
                }
            }
        }
    }

    public void imprimir() {
        System.out.printf("-- Imprimindo a Rede Neural --\n");
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.out.printf("[%d][%d]:[", i, j);
                for (int n = 0; n < dimensoes; n++) {
                    System.out.printf("%.2f ", rede[i][j][n]);
                }
                System.out.printf("]");
                System.out.printf("\n");
            }
        }
    }

    public synchronized void adicionaBmu(int chave, Bmu bmu) {
        map.put(chave, bmu);
    }

    public void treino(Parametros parametros, Padrao vetorespadrao, ArrayList<Integer> tipos) {
        int l = 0, c = 0, epocas = 0;
        double vt = 1.0, vt1 = 0.0, contador = 1.0, distancia = 0.0, h = 1.0, E_m = 0.0, medidaF = 0.0, erroQ = 0.0, erroT = 0.0;
        Double d_ast[][] = null, d_Y[][] = null, X[][] = null, Y[][] = null, mat_cont[][][] = null;
        mat_cont = new Double[parametros.r][parametros.s][parametros.d]; //mesmo tamanho da rede neural
        ArrayList<Vizinho> V = null; //estrutura para armazenar as posições dos vizinhos
        //X = new Double[(parametros.r * parametros.s)][parametros.d];
        //Y = new Double[(parametros.r * parametros.s)][parametros.d];
        //d_ast = new Double[(parametros.r * parametros.s)][(parametros.r * parametros.s)];
        //d_Y = new Double[(parametros.r * parametros.s)][(parametros.r * parametros.s)];
        Vizinho vizinho = new Vizinho();
        Calculo calculo = new Calculo();
        while (epocas < parametros.quantIteracoes) {
            //*epoca=epocas; // da funcao teste*************
            if (epocas > 0) {
                System.err.printf("%d %f %f %f %f \n", epocas, vt, medidaF, erroQ, erroT);
            }
            erroQ = 0.0;
            erroT = 0.0;
            //fflush(stdout);
            epocas++;
            vt1 = vt;
            vt = 0.0;
            //variando o valor de epsilon ao longo de cada iteracao, diminuinido seu valor ate chegar ao valor minimo possivel
            if (parametros.epsilon > parametros.min_epsilon) {
                parametros.epsilon = parametros.epsilon / contador;
                contador += 0.00005;
            } else {
                parametros.epsilon = parametros.min_epsilon;
            }
            // para cada padrao P
            for (int padrao = 0; padrao < parametros.quantVetoresPadrao; padrao++) {
                ThreadAcharBmu thread = new ThreadAcharBmu(parametros, padrao, this, vetorespadrao);
                thread.start();
            }
            while (map.size() < parametros.quantVetoresPadrao) {//para garantir que todas as threads terminaram
                try{
                    Thread.sleep(5);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }

            erroQ = calculo.somaErroQ(map);
            vt = calculo.somaEms(map);
            erroT = calculo.somaErroT(map);

            erroT /= (double) parametros.quantVetoresPadrao;
            // treino: atualiza pesos dos neuronios
            // w_{BMU}(t+1) = w_{BMU} (t) + h (p[padrao] - w_{BMU})

            for (int padrao = 0; padrao < parametros.quantVetoresPadrao; padrao++) {
                // l, c denotam a posicao do BMU encontrado pelo achar_BMU_SOM
                l = map.get(padrao).linha[0];
                c = map.get(padrao).coluna[0];
                V = vizinho.vizinhanca(parametros.r, parametros.s, l, c, parametros.OP);
                //percorrendo a vizinhança
                for (int i = 0; i < V.size(); i++) {
                    distancia = (Math.abs(V.get(i).linha - l) + Math.abs(V.get(i).coluna - c));
                    h = 1.0 / (Math.pow(2.0, distancia / parametros.H));
                    for (int k = 0; k < parametros.d; k++) {
                        //taxa de atualizacao (aprendizado) == epsilon
                        rede[V.get(i).linha][V.get(i).coluna][k] += parametros.epsilon * h * (vetorespadrao.padroes[padrao][k] - rede[l][c][k]);
                    }
                }
            }// fim de rodar todos os vetores padrao
            //imprimir_mapa(neu, r, s, d);
            medidaF = calculo.calcularMedidaF(map, parametros.quantVetoresPadrao, linhas, colunas, dimensoes, tipos, parametros.quant_tipos);
            map.clear();//limpando o map
        } // fim while

        imprimir();//imprindo a rede neural
        System.out.printf("ErroQ:%f MedidaF:%f ErroT:%f\n", erroQ, medidaF, erroT);
    }

}

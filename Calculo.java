
import java.util.ArrayList;

public class Calculo {

    public void calcular_d(Double X[][], Double d_ast[][], int li, int co) {
        double soma = 0.0;
        for (int i = 0; i < li; i++) {
            d_ast[i][i] = 0.0;	// i e j iguais distancia 0
            for (int j = 0; j < li; j++) {
                if (i < j) {
                    for (int c = 0; c < co; c++) {// dif nas cols de X
                        soma += Math.pow(X[i][c] - X[j][c], 2.0);
                    }
                    d_ast[i][j] = d_ast[j][i] = Math.sqrt(soma);
                }
                soma = 0.0;
            }//para cada coluna
        }//para cada linha
    }

    public void contagem(Double mat_cont[][][], int r, int s, int d, int quant_vetores, ArrayList<Euclidiana> Bmus, ArrayList<Integer> tipos) {
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < s; j++) {
                for (int di = 0; di < d; di++) {
                    mat_cont[i][j][di] = 0.0; //inicializando a matriz com zero
                }
            }
        }

        for (int padrao = 0; padrao < quant_vetores; padrao++) { //percorrendo todos os vetores
            int l = Bmus.get(padrao).linha;
            int c = Bmus.get(padrao).coluna;
            int k = tipos.get(padrao) - 1;//-1 para ficar numa posicao valida dentro da matriz
            mat_cont[l][c][k]++; //a mat_cont tem as mesmas dimensoes da rede neural rxsxd
        }

    }//fim função contagem

    public double calcular_precisao(Double mat_confusao[][]) {
        Double precisao = new Double(0.0);
        precisao = mat_confusao[0][0] / (mat_confusao[0][0] + mat_confusao[0][1]);        

        if (precisao.isNaN()) {
            System.err.printf("Warning: deu um NaN na precisao: VP = %f, FP = %f \n", mat_confusao[0][0], mat_confusao[0][1]);
            precisao = 0.0;
        }
        return precisao;
    }

    public double calcular_recuperacao(Double mat_confusao[][]) {
        Double recuperacao = new Double(0.0);
        recuperacao = mat_confusao[0][0] / (mat_confusao[0][0] + mat_confusao[1][0]);
        if (recuperacao.isNaN()) {
            System.err.printf("Warning: deu um NaN na recuperacao: VP = %f, FP = %f \n", mat_confusao[0][0], mat_confusao[1][0]);
            recuperacao = 0.0;
        }
        return recuperacao;
    }

    public void calcular_matriz_confusao(Double mat_cont[][][], Double mat_confusao[][], int r, int s, int d, int tipo){
        // tipo que define quem eh POSITIVO
        double maiordalinha = 0.0;
        double VP = 0.0, // verdadeiros positivos
                FN = 0.0, // falsos negativos
                FP = 0.0, // falsos positivos
                VN = 0.0; // verdadeiros negativos

        int i = 0, j = 0, di = 0, posmaiordalinha = 0; // verificar quem é dominante e contar V ou F

        for (i = 0; i < r; i++) {
            for (j = 0; j < s; j++) { //percorre na matriz de neuronios
                maiordalinha = 0.0;
                posmaiordalinha = 0; // ?????

                /* procurar qual é a maior contagem de tipo da linha
                 * o maior vai ser considerado como o tipo POSITIVO --> deveria ser do "tipo" ???

                 * */
                for (di = 0; di < d; di++) {
                    if (mat_cont[i][j][di] > maiordalinha) {
                        maiordalinha = mat_cont[i][j][di];
                        posmaiordalinha = di; // encontra o tipo com maior contagem no neuronio
                    }
                }

                if (posmaiordalinha == tipo) { //ver os que são e julguei que fossem
                    VP += maiordalinha; //(00)
                    for (di = 0; di < d; di++) { //ver os não são mas julguei que fossem
                        if (di != tipo) {
                            FP += mat_cont[i][j][di]; //(01)
                        }
                    }
                } else {
                    //ver os que são mas julguei que não fossem
                    FN += mat_cont[i][j][tipo]; //(10)

                    for (di = 0; di < d; di++) { //ver os que não são e julguei que não fossem
                        if (di != tipo) {
                            VN += mat_cont[i][j][di]; //(11)
                        }
                    }
                }
            }
        }

        mat_confusao[0][0] = VP;
        mat_confusao[0][1] = FP;
        mat_confusao[1][0] = FN;
        mat_confusao[1][1] = VN;

    }//fim da funcao

// Funcao para calcular a MedidaF
    public double calcularMedidaF(ArrayList<Euclidiana> Bmus, int n, int r, int s, int d, ArrayList<Integer> tipos, int quantidadeTipos) {
        Double mat_confusao[][] = null;
        Double mat_cont[][][] = null;
        mat_cont = new Double[r][s][d];
        mat_confusao = new Double[2][2]; //sempre esse tamanho
        contagem(mat_cont, r, s, d, n, Bmus, tipos);//preechendo a matriz contagem

// calculo da precisao/recuperacao por tipo
        double precisao = 0.0, recuperacao = 0.0, medidasF = 0.0;

        for (int tipo = 0; tipo < quantidadeTipos; tipo++) {
            calcular_matriz_confusao(mat_cont, mat_confusao, r, s, d, tipo);

            precisao = calcular_precisao(mat_confusao);
            recuperacao = calcular_recuperacao(mat_confusao);
            if (precisao + recuperacao != 0.0) {
                medidasF += 2.0 * (precisao * recuperacao) / (precisao + recuperacao);
            }
        }

        medidasF /= 3.0;
        return medidasF; // media entre todas as precisoes e recuperacoes
    }

}

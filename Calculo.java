
import java.util.ArrayList;
import java.util.Map;

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

    public void contagem(Double mat_cont[][][], int r, int s, int d, int quant_vetores, Map<Integer, Bmu> Bmus, ArrayList<Integer> tipos) {
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < s; j++) {
                for (int di = 0; di < d; di++) {
                    mat_cont[i][j][di] = 0.0; //inicializando a matriz com zero
                }
            }
        }

        for (int padrao = 0; padrao < quant_vetores; padrao++) { //percorrendo todos os vetores
            int l = Bmus.get(padrao).linha[0];
            int c = Bmus.get(padrao).coluna[0];
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

    public void calcular_matriz_confusao(Double mat_cont[][][], Double mat_confusao[][], int r, int s, int d, int tipo) {
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
    public double calcularMedidaF(Map<Integer, Bmu> Bmus, int n, int r, int s, int d, ArrayList<Integer> tipos, int quantidadeTipos) {
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
    
    public static Bmu achar_BMU_SOM(Rede neu, Double vetorpadrao[], Parametros pa) {
        double soma = 0.0;
        double E_m = Double.MAX_VALUE, E_mtemp = 0.0;
        ArrayList<Euclidiana> Bmus = new ArrayList<Euclidiana>(2);//BMU e sencond BMU
        Bmu bmu = new Bmu();

        for (int i = 0; i < neu.linhas; i++) { //calculando a distancia euclidiana entre o neuronio e o vetor padrao
            for (int j = 0; j < neu.colunas; j++) {
                soma = 0.0;
                for (int k = 0; k < neu.dimensoes; k++) {
                    soma += Math.pow((neu.rede[i][j][k] - vetorpadrao[k]), 2.0);
                }
                E_mtemp = Math.sqrt(soma);
                if (i == 0 && j == 0) {
                    E_m = E_mtemp;                    
                    Euclidiana e1 = new Euclidiana(E_m, i, j);
                    Euclidiana e2 = new Euclidiana(E_m, i, j);
                    Bmus.add(e1);
                    Bmus.add(e2);
                } else {
                    if (E_mtemp < E_m) { // verificando se o erro atual é menor que o anterior
                        E_m = E_mtemp;
                        Bmus.get(1).distancia = Bmus.get(0).distancia; //passando o que era o BMU como sendo agora o segundo BMU
                        Bmus.get(1).linha = Bmus.get(0).linha;
                        Bmus.get(1).coluna = Bmus.get(0).coluna;
                        //atualizando os dados do BMU
                        Bmus.get(0).distancia = E_m;
                        Bmus.get(0).linha = i;
                        Bmus.get(0).coluna = j;
                    } else {
                        if (E_mtemp < Bmus.get(1).distancia) {
                            Bmus.get(1).distancia = E_mtemp;
                            Bmus.get(1).linha = i;
                            Bmus.get(1).coluna = j;
                        }
                    }
                }//else i==0 e j ==0
            }//proxima coluna
        } //proxima linha

        //devolvendo em que posicao esta o neuronio com menor distancia euclidiana
        bmu.linha[0] = Bmus.get(0).linha;
        bmu.linha[1] = Bmus.get(1).linha;
        bmu.coluna[0] = Bmus.get(0).coluna;
        bmu.coluna[1] = Bmus.get(1).coluna;
        bmu.E_m = bmu.erroQ = Bmus.get(0).distancia;

        Vizinho vizinho = new Vizinho();
        int teste = vizinho.ver_seeh_vizinho(neu.linhas, neu.colunas, bmu.linha[0], bmu.coluna[0], bmu.linha[1], bmu.coluna[1], pa.OP);
        if (teste == 1) {
            bmu.erroT = 1;
        } else {
            bmu.erroT = 0;
        }
        return bmu;
    }

    public static Bmu achar_BMU_usando_Sammon(Rede neu, Double vetorespadrao[], Parametros pa) {

        double E_m = Double.MAX_VALUE, E_mtemp = 0.0;
        int cont = 0, teste = 0;
        ArrayList<Euclidiana> Bmus = new ArrayList<Euclidiana>(2);
        Bmu bmu = new Bmu();
        Vizinho vizinho = new Vizinho();
        Calculo calculo = new Calculo();

        for (int i = 0; i < neu.linhas; i++) {//percorrendo linhas da rede neural
            for (int j = 0; j < neu.colunas; j++) {//percorrendo colunas da rede neural
                //printf ("i:%d j:%d\n", i, j);
                Double X[][] = null, Y[][] = null, d_ast[][] = null, d_Y[][] = null;

                X = vizinho.formatos_vizinhanca(neu, vetorespadrao, i, j, pa.OP);

                Y = new Double[cont][neu.dimensoes];
                d_ast = new Double[cont][cont];
                d_Y = new Double[cont][cont];

                //copiando os dados de X para Y atÃ© D dimensÃµes
                for (int k = 0; k < (cont - 1); k++) {
                    for (int l = 0; l < pa.D; l++) {
                        Y[k][l] = X[k][l];
                    }
                }

                // serve para copiar parte do padrao na matriz Y de dim reduzida
                for (int k = 0; k < pa.D; k++) {
                    Y[cont - 1][k] = vetorespadrao[k];
                }

                calculo.calcular_d(X, d_ast, cont, pa.d); //preenchendo as matrizes de distÃ¢ncias
                calculo.calcular_d(Y, d_Y, cont, pa.d);
                Sammon sammon = new Sammon();
                E_mtemp = sammon.calcularSammon(d_ast, d_Y, Y, cont, pa.D, pa.MF, pa.EPSILON, pa.MAX_ITERACOES);

                if (i == 0 && j == 0) {
                    E_m = E_mtemp;
                    Euclidiana e1 = new Euclidiana(E_m, i, j);
                    Euclidiana e2 = new Euclidiana(E_m, i, j);
                    Bmus.add(e1);
                    Bmus.add(e2);
                } else {
                    if (E_mtemp < E_m) { // verificando se o erro atual é menor que o anterior
                        E_m = E_mtemp;
                        Bmus.get(1).distancia = Bmus.get(0).distancia; //passando o que era o BMU como sendo agora o segundo BMU
                        Bmus.get(1).linha = Bmus.get(0).linha;
                        Bmus.get(1).coluna = Bmus.get(0).coluna;
                        //atualizando os dados do BMU
                        Bmus.get(0).distancia = E_m;
                        Bmus.get(0).linha = i;
                        Bmus.get(0).coluna = j;
                    } else {
                        if (E_mtemp < Bmus.get(1).distancia) {
                            Bmus.get(1).distancia = E_mtemp;
                            Bmus.get(1).linha = i;
                            Bmus.get(1).coluna = j;
                        }
                    }
                }
            }//encerra for coluna rede neural
        }//encerra for linha rede neural

        bmu.linha[0] = Bmus.get(0).linha;
        bmu.linha[1] = Bmus.get(1).linha;
        bmu.coluna[0] = Bmus.get(0).coluna;
        bmu.coluna[1] = Bmus.get(1).coluna;
        bmu.E_m = Bmus.get(0).distancia;
        Bmu bm = achar_BMU_SOM(neu, vetorespadrao, pa);
        bmu.erroQ = bm.erroQ;

        teste = vizinho.ver_seeh_vizinho(neu.linhas, neu.colunas, bmu.linha[0], bmu.coluna[0], bmu.linha[1], bmu.coluna[1], pa.OP);
        if (teste == 1) {
            bmu.erroT = 1;
        } else {
            bmu.erroT = 0;
        }
        return bmu;
    }
    
    
    
    
    
    
    

    public double somaEms(Map<Integer, Bmu> hash) {
        double resultado = 0.0;
        for (Integer i : hash.keySet()) {
            resultado += hash.get(i).E_m;
        }
        return resultado;
    }

    public double somaErroQ(Map<Integer, Bmu> hash) {
        double resultado = 0.0;
        for (Integer i : hash.keySet()) {
            resultado += hash.get(i).erroQ;
        }
        return resultado;
    }

    public int somaErroT(Map<Integer, Bmu> hash) {
        int resultado = 0;
        for (Integer i : hash.keySet()) {
            resultado += hash.get(i).erroT;
        }
        return resultado;
    }

}


import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Calculo {

    public void calcular_d(Double X[][], Double d_ast[][], int li, int co) {
        double soma = 0.0;
        for (int i = 0; i < li; i++) {
            d_ast[i][i] = 0.0;
            //d_ast[i][i] = 0.0;	// i e j iguais distancia 0
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

    public Matriz3 contagem(Parametros pa, Map<Integer, Bmu> Bmus, ArrayList<Integer> tipos) {
        Matriz3 mat_cont = new Matriz3(pa.r, pa.s, pa.quant_tipos);

        for (Integer padrao : Bmus.keySet()) { //percorrendo todos os vetores
            int l = Bmus.get(padrao).linha[0];
            int c = Bmus.get(padrao).coluna[0];
            int k = tipos.get(padrao);
            k--;//-1 para ficar numa posicao valida dentro da matriz
            double x = mat_cont.getMatriz3(l, c, k);
            x++;
            mat_cont.setMatriz3(l, c, k, x);
        }

        return mat_cont;
    }//fim função contagem

    public double calcular_precisao(Double mat_confusao[][]) {
        Double precisao = new Double(0.0);
        // mat_confusao[0][0] -> VP
        // mat_confusao[0][1] -> FP       
        precisao = mat_confusao[0][0] / (mat_confusao[0][0] + mat_confusao[0][1]);

        if (precisao.isNaN()) {
            System.err.printf("Warning: deu um NaN na precisao: VP = %f, FP = %f \n", mat_confusao[0][0], mat_confusao[0][1]);
            precisao = 0.0;
        }
        return precisao;
    }

    public double calcular_recuperacao(Double mat_confusao[][]) {
        Double recuperacao = new Double(0.0);
        // mat_confusao[0][0] -> VP
        // mat_confusao[1][0] -> FN
        recuperacao = mat_confusao[0][0] / (mat_confusao[0][0] + mat_confusao[1][0]);
        if (recuperacao.isNaN()) {
            System.err.printf("Warning: deu um NaN na recuperacao: VP = %f, FP = %f \n", mat_confusao[0][0], mat_confusao[1][0]);
            recuperacao = 0.0;
        }
        return recuperacao;
    }

    public void calcularMatrizConfusao(Matriz3 mat_cont, Double mat_confusao[][], Parametros pa, int tipo) {
        // tipo que define quem eh POSITIVO
        double maiordalinha = 0.0;
        double VP = 0.0, // verdadeiros positivos
                FN = 0.0, // falsos negativos
                FP = 0.0, // falsos positivos
                VN = 0.0; // verdadeiros negativos

        int posmaiordalinha = 0; // verificar quem é dominante e contar V ou F
        for (int i = 0; i < pa.r; i++) {
            for (int j = 0; j < pa.s; j++) { //percorre na matriz de neuronios
                maiordalinha = Double.MIN_VALUE;
                posmaiordalinha = 0;
                
                for (int di = 0; di < pa.quant_tipos; di++) {
                    if (mat_cont.getMatriz3(i, j, di) > maiordalinha) {
                        maiordalinha = mat_cont.getMatriz3(i, j, di);
                        posmaiordalinha = di; // encontra o tipo com maior contagem no neuronio
                    }
                }

                if (posmaiordalinha == tipo) { //ver os que são e julguei que fossem      
                    VP += mat_cont.getMatriz3(i, j, posmaiordalinha); //(00)
                    for (int di = 0; di < pa.quant_tipos; di++) { //ver os não são mas julguei que fossem
                        if (di != posmaiordalinha) {
                            FP += mat_cont.getMatriz3(i, j, di); //(01)
                        }
                    }
                } else {
                    //ver os que são mas julguei que não fossem
                    FN += mat_cont.getMatriz3(i, j, posmaiordalinha); //(10)

                    for (int di = 0; di < pa.quant_tipos; di++) { //ver os que não são e julguei que não fossem
                        if (di != posmaiordalinha && di!= tipo) {
                            VN += mat_cont.getMatriz3(i, j, di); //(11)
                        }else{
                            if(di == tipo){
                                FP += mat_cont.getMatriz3(i, j, di); //(01)
                            }
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
    public double calcularMedidaF(Map<Integer, Bmu> Bmus, Parametros pa, ArrayList<Integer> tipos) {
        Matriz3 mat_cont = contagem(pa, Bmus, tipos);//preechendo a matriz contagem

// calculo da precisao/recuperacao por tipo
        double precisao = 0.0, recuperacao = 0.0, medidasF = 0.0;

        for (int tipo = 0; tipo < pa.quant_tipos; tipo++) {
            //System.out.println("Para o tipo:" + tipo);/////////////////////////////////
            Double mat_confusao[][] = new Double[2][2]; //sempre esse tamanho
            Matriz.preencherZero(mat_confusao, 2, 2);
            calcularMatrizConfusao(mat_cont, mat_confusao, pa, tipo);

            precisao = calcular_precisao(mat_confusao);
            recuperacao = calcular_recuperacao(mat_confusao);
            if (precisao + recuperacao != 0.0) {
                medidasF += 2.0 * (precisao * recuperacao) / (precisao + recuperacao);
            }
        }

        medidasF /= 3.0;
        return medidasF; // media entre todas as precisoes e recuperacoes
    }

    public static void imprimirDistancias(Rede neu, Padrao padrao) {

        neu.imprimir();
        for (int p = 0; p < padrao.linhas; p++) {
            System.out.print("Padrao " + p + ": ");
            for (int j = 0; j < padrao.colunas; j++) {
                System.out.print(padrao.padroes[p][j] + " ");
            }
            System.out.println();

            for (int i = 0; i < neu.linhas; i++) { //calculando a distancia euclidiana entre o neuronio e o vetor padrao
                for (int j = 0; j < neu.colunas; j++) {
                    double soma = 0.0;
                    for (int k = 0; k < neu.dimensoes; k++) {
                        soma += Math.pow((neu.rede[i][j][k] - padrao.padroes[p][k]), 2.0); //
                    }
                    System.out.printf("%.2f ", soma);
                }
                System.out.println();

                //proxima coluna
            } //proxima linha
        }
        System.exit(-1);
    }

    public Bmu achar_BMU_SOM(Double neu[][][], Double vetorpadrao[], Parametros pa) {
        double soma = 0.0;
        double E_m = Double.MAX_VALUE, E_mtemp = 0.0;
        ArrayList<Euclidiana> Bmus = new ArrayList<Euclidiana>();
        Bmu bmu = new Bmu();

        for (int i = 0; i < pa.r; i++) { //calculando a distancia euclidiana entre o neuronio e o vetor padrao
            for (int j = 0; j < pa.s; j++) {
                soma = 0.0;
                for (int k = 0; k < pa.d; k++) {
                    soma += Math.pow((neu[i][j][k] - vetorpadrao[k]), 2.0);
                }
                E_mtemp = Math.sqrt(soma);
                Euclidiana e = new Euclidiana(E_mtemp, i, j);
                Bmus.add(e);
            }//proxima coluna
        } //proxima linha

        EuclidianaComparator comparador = new EuclidianaComparator();
        Collections.sort(Bmus, comparador);

        //devolvendo em que posicao esta o neuronio com menor distancia euclidiana
        bmu.linha[0] = Bmus.get(0).linha;
        bmu.linha[1] = Bmus.get(1).linha;
        bmu.coluna[0] = Bmus.get(0).coluna;
        bmu.coluna[1] = Bmus.get(1).coluna;
        bmu.E_m = bmu.erroQ = Bmus.get(0).distancia;
        //System.out.println("Os menores foram:"+Bmus.get(0).distancia +" e:"+Bmus.get(1).distancia);/////////////////////////////
        Vizinho vizinho = new Vizinho();
        int teste = vizinho.ver_seeh_vizinho(pa.r, pa.s, bmu.linha[0], bmu.coluna[0], bmu.linha[1], bmu.coluna[1], pa.OP);
        if (teste == 1) {
            bmu.erroT = 0.0;
        } else {
            bmu.erroT = 1.0;
        }

        return bmu;
    }

    public Bmu achar_BMU_usando_Sammon(Double neu[][][], Double vetoresPadrao[], Parametros pa) {
        int cont = 0, cont2 = 0;
        ArrayList<Euclidiana> Bmus = new ArrayList<Euclidiana>();
        Bmu bmu = new Bmu();
        Vizinho vizinho = new Vizinho();
        Sammon sammon = new Sammon();

        for (int i = 0; i < pa.r; i++) {//percorrendo linhas da rede neural
            for (int j = 0; j < pa.s; j++) {//percorrendo colunas da rede neural
                Double X[][], Y[][], d_ast[][], d_Y[][];
                cont2 = 0;
                ArrayList<Vizinho> V = null;
                V = vizinho.vizinhanca(pa.r, pa.s, i, j, pa.OP);
                cont = V.size();
                cont++; // para incluir o padrao sendo aprendido
                X = new Double[cont][pa.d];

                //preenchendo a matriz X
                for (Vizinho viz : V) {
                    for (int di = 0; di < pa.d; di++) {
                        X[cont2][di] = neu[viz.linha][viz.coluna][di];
                    }
                    cont2++;
                }

                for (int di = 0; di < pa.d; di++) {
                    X[cont - 1][di] = vetoresPadrao[di];
                }
                pa.cont = cont;

                //vizinho.formatos_vizinhanca(X, neu, vetoresPadrao, i, j, pa);
                Y = new Double[cont][pa.d];
                Matriz.preencherZero(Y, cont, pa.d);
                d_ast = new Double[cont][cont];
                Matriz.preencherZero(d_ast, cont, cont);
                d_Y = new Double[cont][cont];
                Matriz.preencherZero(d_Y, cont, cont);
                //copiando os dados de X para Y ate D dimensoes
                for (int k = 0; k < (cont - 1); k++) {
                    for (int l = 0; l < pa.D; l++) {
                        Y[k][l] = X[k][l];
                    }
                }
                // serve para copiar parte do padrao na matriz Y de dimensao reduzida
                for (int k = 0; k < pa.D; k++) {
                    Y[cont - 1][k] = vetoresPadrao[k];
                }

                calcular_d(X, d_ast, cont, pa.d); //preenchendo as matrizes de distancias
                calcular_d(Y, d_Y, cont, pa.d);
                
                double E_mtemp = sammon.calcularSammon(d_ast, d_Y, Y, cont, pa.D, pa.MF, pa.EPSILON, pa.MAX_ITERACOES);

                Euclidiana e = new Euclidiana(E_mtemp, i, j);
                Bmus.add(e);
            }//encerra for coluna rede neural
        }//encerra for linha rede neural

        EuclidianaComparator comparador = new EuclidianaComparator();
        Collections.sort(Bmus, comparador);

        bmu.linha[0] = Bmus.get(0).linha;
        bmu.linha[1] = Bmus.get(1).linha;
        bmu.coluna[0] = Bmus.get(0).coluna;
        bmu.coluna[1] = Bmus.get(1).coluna;
        bmu.E_m = Bmus.get(0).distancia;
        Bmu bm = achar_BMU_SOM(neu, vetoresPadrao, pa);
        bmu.erroQ = bm.erroQ;

        int teste = vizinho.ver_seeh_vizinho(pa.r, pa.s, bmu.linha[0], bmu.coluna[0], bmu.linha[1], bmu.coluna[1], pa.OP);
        if (teste == 1) {
            bmu.erroT = 0.0;
        } else {
            bmu.erroT = 1.0;
        }
        return bmu;
    }

    public double somaEm(Map<Integer, Bmu> hash) {
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

    public double somaErroT(Map<Integer, Bmu> hash) {
        double resultado = 0.0;
        for (Integer i : hash.keySet()) {
            resultado += hash.get(i).erroT;
        }
        return resultado;
    }

}

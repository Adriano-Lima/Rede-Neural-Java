
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

public class Rede {

    Double rede[][][];
    int linhas, colunas, dimensoes;
    Random gerador;
    double erroQuantizacao, erroTopografico;
    HashMap<String, ArrayList<Integer>> representacoes; //hash para saber quais padroes o neuronio representou 
    HashMap<Integer, Bmu> map; //hash para saber quem foi o Bmu de um dado padrao
    TreeMap<String, Double> corNeuronio; //hash para saber a cor de cada BMu o H de HSV
    TreeMap<String, Double> corV; //hash para saber a cor V do HSV

    //construtor
    public Rede(int linhas, int colunas, int dimensoes, long seed) {
        rede = new Double[linhas][colunas][dimensoes];
        representacoes = new HashMap<String, ArrayList<Integer>>();
        map = new HashMap<Integer, Bmu>();
        corNeuronio = new TreeMap<String, Double>();
        corV = new TreeMap<String, Double>();
        this.linhas = linhas;
        this.colunas = colunas;
        this.dimensoes = dimensoes;
        this.gerador = new Random(seed);
    }

    public void preencherRedeAleatoriamente() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                for (int di = 0; di < dimensoes; di++) {
                    rede[i][j][di] = gerador.nextDouble();
                }
            }
        }
    }

    public void preencherRedeComPadroes(Padrao vetorespadrao) {
        ArrayList<Integer> posicoes = new ArrayList<Integer>();
        boolean flag = true;
        int padrao = 0;
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                flag = true;
                while (flag) {
                    padrao = gerador.nextInt(vetorespadrao.linhas);
                    if (!posicoes.contains(padrao)) {
                        flag = false;
                    }
                }
                posicoes.add(padrao);
                for (int k = 0; k < dimensoes; k++) {
                    rede[i][j][k] = vetorespadrao.padroes[padrao][k];
                }
            }
        }
    }

    public void preencherRedeComPadroesAleatorios(Padrao vetorespadrao, Parametros pa, ArrayList<Integer> tipos) {
        //descobrindo as posicoes de cada tipo
        HashMap<Integer, ArrayList<Integer>> baldes = new HashMap<Integer, ArrayList<Integer>>();
        for (int tipo = 0; tipo < pa.quant_tipos; tipo++) {
            for (int i = 0; i < tipos.size(); i++) {
                if (tipo + 1 == tipos.get(i)) { //+1 pq tipo comeca em 1, ... (no arquivo com os tipos)
                    if (!baldes.containsKey(tipo + 1)) {
                        ArrayList<Integer> array = new ArrayList<Integer>();
                        array.add(i);
                        baldes.put(tipo + 1, array);
                    } else {
                        baldes.get(tipo + 1).add(i);
                    }
                }
            }
        }
        Random g = new Random();
        for (int i = 0; i < linhas; i++) { //percorrendo a rede
            for (int j = 0; j < colunas; j++) {
                int tipo = g.nextInt(pa.quant_tipos) + 1; //sorteando um tipo
                int padrao = baldes.get(tipo).remove(g.nextInt(baldes.get(tipo).size()));
                //int padrao = (int) baldes[tipo].remove(g.nextInt(baldes[tipo].size()));
                for (int k = 0; k < dimensoes; k++) {
                    rede[i][j][k] = vetorespadrao.padroes[padrao][k];
                }
            }
        }
    }

    public void imprimir() {
        System.err.printf("-- Imprimindo a Rede Neural --\n");
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.err.printf("[%d][%d]:[", i, j);
                for (int n = 0; n < dimensoes; n++) {
                    System.err.printf("%.2f ", rede[i][j][n]);
                }
                System.err.printf("]");
                System.err.printf("\n");
            }
        }
    }

    static Double vectorNorm(ArrayList<Double> vector) {
        Double ret = 0.0;
        for (int i = 0; i < vector.size(); i++) {
            ret += vector.get(i) * vector.get(i);
        }

        return Math.sqrt(ret);
    }

    static ArrayList<Double> vectorOperation(ArrayList<Double> v1, ArrayList<Double> v2, char operand) {
        ArrayList<Double> ret = new ArrayList<>();
        if (v1.size() != v2.size()) {
            System.err.println("!!VectorOperation erro arrayList com tamanhos diferentes:" + v1.size() + ", " + v2.size());
            System.exit(1);
        }
        for (int i = 0; i < v1.size(); i++) {
            if (operand == '+') {
                ret.add(v1.get(i) + v2.get(i));
            } else if (operand == '-') {
                ret.add(v1.get(i) - v2.get(i));
            }
        }

        return ret;
    }

    static Double gaussianNeighborhood(ArrayList<Double> v1, ArrayList<Double> v2, double lambda) {
        ArrayList<Double> dif = vectorOperation(v2, v1, '-');
        double normDif = vectorNorm(dif);
        double ret = Math.pow(Math.E, -(normDif * normDif) / (lambda * lambda));
        return ret;
    }

    public void identificarRepresentacao(String s, int padrao) {
        if (!representacoes.containsKey(s)) {
            ArrayList<Integer> array = new ArrayList<Integer>();
            array.add(padrao);
            representacoes.put(s, array);
        } else {
            if (!representacoes.get(s).contains(padrao)) {
                representacoes.get(s).add(padrao);
            }
        }
    }

    public void treino(Parametros parametros, Double vetoresPadrao[][], ArrayList<Integer> tipos) {
        double vt = 1.0, vt1 = 0.0, contador = 1.0,
                distancia = 0.0, h = 1.0, E_m = 0.0,
                medidaF = 0.0, erroQ = 0.0, erroT = 0.0;
        double alphaInitial = parametros.alphaInitial;
        double alphaFinal = parametros.alphaFinal;
        double lambdaInitial = parametros.lambdaInitial;
        double lambdaFinal = parametros.lambdaFinal;
        int ep = 0;

        Calculo calculo = new Calculo();

        //while (Math.abs(vt - vt1) / (vt > vt1 ? vt : vt1) > 0.00001 && ep < parametros.quantIteracoes) {
        while (ep < parametros.quantIteracoes) {
            double alpha = alphaInitial * Math.pow(alphaFinal / alphaInitial, (double) ep / parametros.quantIteracoes);
            double lambda = lambdaInitial * Math.pow(lambdaFinal / lambdaInitial, (double) ep / parametros.quantIteracoes);
            if (ep > 0) {
                System.err.printf("%d %f %f %f \n", ep, medidaF, erroQ, erroT);
            }
            ep++;
            erroQ = 0.0;
            erroT = 0.0;
            vt1 = vt;
            vt = 0.0;

            map.clear();
            representacoes.clear();
//se for usar thread
//            try {
//                for (int padrao = 0; padrao < parametros.quantVetoresPadrao; padrao++) {
//                    AcharBmu thread = new AcharBmu(parametros, padrao, this, vetoresPadrao);
//                    thread.start();
//                    thread.join();
//                }
//            } catch (Exception e) {
//                System.err.println("Erro nas threads" + e.getMessage());
//            }
//            while (map.size() < parametros.quantVetoresPadrao) {//para garantir que todas as threads terminaram
//                try {
//                    Thread.sleep(2);
//                } catch (Exception e) {
//                    System.err.println(e.getMessage());
//                }
//            }

            // para cada padrao P
            for (int padrao = 0; padrao < parametros.quantVetoresPadrao; padrao++) {
                Bmu bmu = new Bmu();
                if (parametros.usarSammon == 1) {
                    bmu = calculo.achar_BMU_usando_Sammon(rede, vetoresPadrao[padrao], parametros);
                    String s = String.valueOf(bmu.linha[0]).concat(String.valueOf(bmu.coluna[0]));
                    identificarRepresentacao(s, padrao);
                    map.put(padrao, bmu);
                } else {
                    bmu = calculo.achar_BMU_SOM(rede, vetoresPadrao[padrao], parametros);
                    //salvando qual foi o neuronio vencedor e qual padrao ele esta representando
                    String s = String.valueOf(bmu.linha[0]).concat(String.valueOf(bmu.coluna[0]));
                    identificarRepresentacao(s, padrao);
                    map.put(padrao, bmu);
                }
            //atualizando os pesos dos neuronios da rede neural  
           // for (Integer padrao : map.keySet()) {/////////////////////////// alterei para as threads
                ArrayList<Double> coordWinner = new ArrayList<Double>();//arraylist com as coordenadas do BMU
                //coordWinner.add(new Double(map.get(padrao).linha[0])); //thread
                //coordWinner.add(new Double(map.get(padrao).coluna[0])); //thread
                coordWinner.add(new Double(bmu.linha[0]));
                coordWinner.add(new Double(bmu.coluna[0]));
                for (int i = 0; i < parametros.r; i++) {
                    for (int j = 0; j < parametros.s; j++) {
                        ArrayList<Double> coordNeuron = new ArrayList<Double>();//coordenadas no neuronio atual
                        coordNeuron.add(new Double(i));
                        coordNeuron.add(new Double(j));
                        for (int di = 0; di < parametros.d; di++) {
                            double oldWeight = rede[i][j][di];
                            double diff = vetoresPadrao[padrao][di] - oldWeight;
                            double deltaW = alpha * gaussianNeighborhood(coordWinner, coordNeuron, lambda) * diff;
                            rede[i][j][di] = oldWeight + deltaW;
                        }
                    }
                }
            //}///////////////////////////////////////alterei para as threads

            }// alterei aqui para threads
            erroQ = calculo.somaErroQ(map);
            vt = calculo.somaEm(map);
            erroT = calculo.somaErroT(map);
            erroT /= (double) parametros.quantVetoresPadrao;

            medidaF = calculo.calcularMedidaF(map, parametros, tipos);
        } // fim do for quantIteracoes

        System.out.printf("//ErroQ:%f MedidaF:%f ErroT:%f\n", erroQ, medidaF, erroT);
        erroQuantizacao = erroQ;
        erroTopografico = erroT;

        parametros.imprimir();
        //map com a ultima configuracao de Bmus
    }

}

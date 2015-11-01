
import java.util.ArrayList;

public class Bmu {

    double valor;
    Integer linha[], coluna[];

    //construtor
    public Bmu() {
        linha = new Integer[3];
        coluna = new Integer[3];
    }

    public static Bmu achar_BMU_SOM(Rede neu, Double vetorpadrao[], Parametros pa) {
        double soma = 0.0;
        double E_m = Double.MAX_VALUE, E_mtemp = 0.0;
        ArrayList<Euclidiana> Bmus = new ArrayList<Euclidiana>(2);//BMU e sencond BMU
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
        Bmu bmu = new Bmu();
        bmu.linha[0] = Bmus.get(0).linha;
        bmu.linha[1] = Bmus.get(1).linha;
        bmu.coluna[0] = Bmus.get(0).coluna;
        bmu.coluna[1] = Bmus.get(1).coluna;
        bmu.valor = Bmus.get(0).distancia;

        Vizinho vizinho = new Vizinho();
        int teste = vizinho.ver_seeh_vizinho(neu.linhas, neu.colunas, bmu.linha[0], bmu.coluna[0], bmu.linha[1], bmu.coluna[1], pa.OP);
        if (teste == 1) {
            bmu.linha[2] = bmu.coluna[2] = 1;
        } else {
            bmu.linha[2] = bmu.coluna[2] = 0;
        }
        return bmu;
    }

    public static Bmu achar_BMU_usando_Sammon(Rede neu, Double vetorespadrao[], Parametros pa) {

        double E_m = Double.MAX_VALUE, E_mtemp = 0.0;
        int cont = 0, teste = 0;
        ArrayList<Euclidiana> Bmus = new ArrayList<Euclidiana>(2);
        Vizinho vizinho = new Vizinho();
        Calculo calculo = new Calculo();

        for (int i = 0; i < neu.linhas; i++) {//percorrendo linhas da rede neural
            for (int j = 0; j < neu.colunas; j++) {//percorrendo colunas da rede neural
                //printf ("i:%d j:%d\n", i, j);
                Double X[][]=null, Y[][]=null, d_ast[][]=null, d_Y[][]=null;

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

        Bmu bmu = new Bmu();
        bmu.linha[0] = Bmus.get(0).linha;
        bmu.linha[1] = Bmus.get(1).linha;
        bmu.coluna[0] = Bmus.get(0).coluna;
        bmu.coluna[1] = Bmus.get(1).coluna;
        bmu.valor = Bmus.get(0).distancia;

        teste = vizinho.ver_seeh_vizinho(neu.linhas, neu.colunas, bmu.linha[0], bmu.coluna[0], bmu.linha[1], bmu.coluna[1], pa.OP);
        if (teste == 1) {
            bmu.linha[2] = bmu.coluna[2] = 1;
        } else {
            bmu.linha[2] = bmu.coluna[2] = 0;
        }
        return bmu;
    }

}

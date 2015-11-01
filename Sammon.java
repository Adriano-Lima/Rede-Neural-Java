
public class Sammon {

    //funcao para calcular a constante c utilizada no calculo do erro de projecao E(m) *
    public double calcular_c(Double d_ast[][], //matriz NxN de distancias original
            int N) {

        double c = 0.0;
        for (int j = 1; j < N; j++) {
            for (int i = 0; i < j; i++) {
                c += d_ast[i][j];
            }
        }
        return c;
    }

    // Funcao para calcular o E_m *
    public double calcular_E_m(Double d_ast[][], //matriz com as distancias orginais
            Double d[][], //matriz com as distancias em d espaço
            double c, //constante c
            int N)//quantidade de linhas tanto em D quanto L espaços
    {
        double soma = 0.0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i < j) {
                    if (d_ast[i][j] < 1e-12 && d_ast[i][j] > -1e-12) {
                        System.err.printf("d_ast[%d][%d] == %f\n", i, j, d_ast[i][j]);
                        System.err.printf("ERRO: dois padroes de entrada sao iguais\n");
                        continue;
                    } else {
                        soma += Math.pow(d_ast[i][j] - d[i][j], 2.0) / d_ast[i][j];
                    }
                }
            }
        }
        soma = (1.0 / c) * soma;
        return soma;
    }

    //funcao para calcular a primeira derivada aE(m) *
    public double calcular_aem(Double d_ast[][], //matriz com as distancias orginais
            Double d[][], //matriz com as distancias em d espaco
            Double Y[][], //matriz com valores de entrada em d espaço
            double c, //constante c
            int p, int q, int N) {
        double soma = 0.0, termo1 = 0.0, termo2 = 0.0;
        for (int j = 0; j < N; j++) {
            if (j != p) {
                termo1 = (d_ast[p][j] - d[p][j]) / (d[p][j] * d_ast[p][j]);

                //printf("termo1 == %f\n", termo1);
                termo2 = Y[p][q] - Y[j][q];

                //printf("termo2 == %f\n", termo2);
                Double teste = new Double(termo1 * termo2);
                if (teste.isNaN() || teste.isInfinite()) {
                    continue;
                } else {
                    soma += teste;
                }
            }
        }
        soma = (-2.0 / c) * soma;
        //printf("[%d][%d] aem:%f\n",p,q,soma);
        return soma;
    }

    //funcao para calcular a segunda derivada a2E(m)
    public double calcular_a2em(Double d_ast[][], //matriz com as distancias orginais
            Double d[][], //matriz com as distancias em d espaço
            Double Y[][], //matriz com valores de entrada em d espaço
            double c, //constante c
            int p, int q, int N) {
        double soma = 0.0, termo1 = 0.0, termo2 = 0.0, termo3 = 0.0, termo4 = 0.0, termos = 0.0;
        for (int j = 0; j < N; j++) {
            if (j != p) {
                termo1 = 1.0 / (d_ast[p][j] * d[p][j]);
                termo2 = d_ast[p][j] - d[p][j];
                termo3 = (Math.pow(Y[p][q] - Y[j][q], 2.0)) / d[p][j];
                termo4 = (1.0 + (d_ast[p][j] - d[p][j]) / d[p][j]);
                termos = termo1 * (termo2 - termo3 * termo4);
                Double teste = new Double(termos);
                if (teste.isNaN() || teste.isInfinite()) {
                    continue;
                } else {
                    soma += termos;
                    //printf("%d %0.3f %0.3f %0.3f %0.3f\n", j, termo1, termo2, termo3, termo4);
                }
            }
        }
        soma = (-2.0 / c) * soma;

        //printf("[%d][%d] a2em:%f\n",p,q,soma);
        return soma;
    }

    //funcao para calcular o deltapq(m) em cada m epoca
    public double calcular_delta_pq(Double d_ast[][], //matriz com as distancias orginais
            Double d[][], //matriz com as distancias em d espaço
            Double Y[][], //matriz com valores de entrada em d espaço
            double c, //constante c
            int p, int q, int N) {
        double aem = calcular_aem(d_ast, d, Y, c, p, q, N);

//printf("aem == %f\n", aem);
        double a2em = calcular_a2em(d_ast, d, Y, c, p, q, N);

//printf("a2em == %f\n", a2em);
        return aem / Math.abs(a2em);
    }

    /**
     * A Nonlinear Mapping or data structure analysis, John W. Sammon Jr. IEEE
     * Transactions on Computers. 1969.
     *
     */
    public double calcularSammon(Double d_ast[][], Double d[][], Double Y[][], int N, int D, double MF, double EPSILON, int MAX_ITERACOES) {
        int m = 0;
        double c = 0.0, E_m = 0.0, E_m1 = 1.0, delta_pq = 0.0;
        Calculo calculo = new Calculo();
        c = calcular_c(d_ast, N);

        while (m++ < MAX_ITERACOES && (Math.abs(E_m1 - E_m) / (E_m1 > E_m ? E_m1 : E_m) > EPSILON)) {
            //printf("#E_%d == %f, fracE_%d == %f \n", m, E_m, m, (fabs(E_m1 -E_m)/(E_m1>E_m?E_m1:E_m)));
            E_m1 = E_m;
            calculo.calcular_d(Y, d, N, D);
            for (int p = 0; p < N; p++) {			//p in N
                for (int q = 0; q < D; q++) {			//q in D
                    delta_pq = calcular_delta_pq(d_ast, d, Y, c, p, q, N);
                    //printf("deltapq:%f\n rodada:%d",delta_pq,m);
                    Y[p][q] += -MF * delta_pq;
                    //printf("Y[%d][%d]:%f rodada:%d\n",p,q,Y[p][q],m);
                }
            }

            E_m = calcular_E_m(d_ast, d, c, N);
        }
        return E_m;
    }

    public void calcular_d2(Double X[][], //representa os padroes, com li linhas e co colunas
            Double d_ast[][], //representa matriz de saida com os valores das distancias, com li linhas e co colunas
            int li,
            int co,
            int posicao
    ) {
        int i = 0, j = 0, c = 0;
        double soma = 0.0;// variavel de soma para calculo de distancias
        i = posicao; //posicao do neuronio atual
        d_ast[i][i] = 0.0;  // i e j iguais distancia 0

        for (j = 0; j < li; j++) {  // para padrao j
            if (i < j) {
                for (c = 0; c < co; c++) {  // dif nas cols de X
                    soma += Math.pow(X[i][c] - X[j][c], 2.0);
                }
                d_ast[i][j] = d_ast[j][i] = Math.sqrt(soma);
            }
            soma = 0.0;
        }
    }

}

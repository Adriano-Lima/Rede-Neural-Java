import java.util.ArrayList;

public class Vizinho {

    int linha, coluna;

    //construtor
    public Vizinho() {
    }

    //construtor
    public Vizinho(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public ArrayList<Vizinho> vizinhanca(int maxlinha, //maxlinha e maxcoluna recebem os valores das variaveis de r e s 
            int maxcoluna,
            int i, //posição linha BMU
            int j, //posição coluna BMU
            int OP //parâmetro que define qual o tipo de vizinhaça a ser utilizado 4, 6, 8
    ) {
        ArrayList<Vizinho> V = new ArrayList<Vizinho>();
        int k = 0, l = 0, menorlinha = 0, maiorlinha = 0, menorcoluna = 0, maiorcoluna = 0;

        if (OP == 8) {
            menorlinha = i - 1;
            if (menorlinha < 0) {
                menorlinha = 0;
            }
            menorcoluna = j - 1;
            if (menorcoluna < 0) {
                menorcoluna = 0;
            }
            maiorlinha = i + 1;
            if (maiorlinha >= maxlinha) {
                maiorlinha = maxlinha - 1;
            }
            maiorcoluna = j + 1;
            if (maiorcoluna >= maxcoluna) {
                maiorcoluna = maxcoluna - 1;
            }

            //preenchendo V
            for (k = menorlinha; k <= maiorlinha; k++) {
                for (l = menorcoluna; l <= maiorcoluna; l++) {
                    Vizinho viz = new Vizinho(k, l);
                    V.add(viz);
                }
            }
        } else if (OP == 4) {
            Vizinho viz = new Vizinho(i, j);
            V.add(viz);
            if (i + 1 >= 0 && i + 1 < maxlinha) {
                viz = new Vizinho(i + 1, j);
                V.add(viz);
            }
            if (i - 1 >= 0 && i - 1 < maxlinha) {
                viz = new Vizinho(i - 1, j);
                V.add(viz);
            }

            if (j + 1 >= 0 && j + 1 < maxcoluna) {
                viz = new Vizinho(i, j + 1);
                V.add(viz);
            }

            if (j - 1 >= 0 && j - 1 < maxcoluna) {
                viz = new Vizinho(i, j - 1);
                V.add(viz);
            }
        } else if (OP == 6 && j % 2 == 0) {// caso par
            Vizinho viz = new Vizinho(i, j);
            V.add(viz);
            if (i + 1 >= 0 && i + 1 < maxlinha) {
                viz = new Vizinho(i + 1, j);
                V.add(viz);
            }
            if (i - 1 >= 0 && i - 1 < maxlinha) {
                viz = new Vizinho(i - 1, j);
                V.add(viz);
            }

            if (j + 1 >= 0 && j + 1 < maxcoluna) {
                viz = new Vizinho(i, j + 1);
                V.add(viz);
            }

            if (j - 1 >= 0 && j - 1 < maxcoluna) {
                viz = new Vizinho(i, j - 1);
                V.add(viz);
            }
            if ((i + 1 >= 0 && i + 1 < maxlinha) && (j - 1 >= 0 && j - 1 < maxcoluna)) {
                viz = new Vizinho(i + 1, j - 1);
                V.add(viz);
            }
            if ((i + 1 >= 0 && i + 1 < maxlinha) && (j + 1 >= 0 && j + 1 < maxcoluna)) {
                viz = new Vizinho(i + 1, j + 1);
                V.add(viz);
            }
        } else if (OP == 6 && j % 2 != 0) {//caso impar
            Vizinho viz = new Vizinho(i, j);
            V.add(viz);
            if (i + 1 >= 0 && i + 1 < maxlinha) {
                viz = new Vizinho(i + 1, j);
                V.add(viz);
            }
            if (i - 1 >= 0 && i - 1 < maxlinha) {
                viz = new Vizinho(i - 1, j);
                V.add(viz);
            }

            if (j + 1 >= 0 && j + 1 < maxcoluna) {
                viz = new Vizinho(i, j + 1);
                V.add(viz);
            }

            if (j - 1 >= 0 && j - 1 < maxcoluna) {
                viz = new Vizinho(i, j - 1);
                V.add(viz);
            }
            if ((i - 1 >= 0 && i - 1 < maxlinha) && (j - 1 >= 0 && j - 1 < maxcoluna)) {
                viz = new Vizinho(i - 1, j - 1);
                V.add(viz);
            }
            if ((i - 1 >= 0 && i - 1 < maxlinha) && (j + 1 >= 0 && j + 1 < maxcoluna)) {
                viz = new Vizinho(i - 1, j + 1);
                V.add(viz);
            }
        }
        return V;
    }

    public int ver_seeh_vizinho(int maxlinha, //dimensoes da matriz de vizinhanca
            int maxcoluna,
            int i, //posicoes de um dado objeto de matriz
            int j,
            int linha, //posicoes do objeto que se deseja ver se eh vizinho
            int coluna,
            int OP) {
        ArrayList<Vizinho> V = vizinhanca(maxlinha, maxcoluna, i, j, OP);
        int retorno = 0;
        for (Vizinho viz : V) {
            if (viz.linha == linha && viz.coluna == coluna) {
                retorno = 1;
                break;
            }
        }
        return retorno;
    }

    public Double[][] formatos_vizinhanca(Double X[][], Double neu[][][], Double vetorespadrao[], int i, int j, Parametros pa) {
        int cont = 0, cont2 = 0;
        ArrayList<Vizinho> V = null;

        V = vizinhanca(pa.r, pa.s, i, j, pa.OP);
        cont = V.size();
        cont++; // para incluir o padrao sendo aprendido
        X = new Double[cont][pa.d];

        //preenchendo a matriz X
        for (int l = 0; l < (cont - 1); l++) {
            for (int di = 0; di < pa.d; di++) {
                X[cont2][di] = neu[V.get(l).linha][V.get(l).coluna][di];
            }
            cont2++;
        }

        for (int di = 0; di < pa.d; di++) {
            X[cont - 1][di] = vetorespadrao[di]; //preechendo a ultima posicao da matriz X com o vetor padrao
        }
        pa.cont = cont;
        return X;
    }

}

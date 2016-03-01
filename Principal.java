
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Principal {

    public static void main(String args[]) throws IOException {

        if (args.length < 14) {
            System.err.println("Faltou parametro.\nSão 12.\nSOM(0) CSSOM(2)\nMF\nQuantdeViz\nQuantTipos \nAlphaInitial \nAlphaFinal"
                    + "\nLambdaInitial\nLambdaFinal\nLinhasRede \nColunasRede\nArquivo de Cores\nArquivo de Paises\nForma de inicializar a rede");
            System.exit(-1);
        }

        Parametros parametros = new Parametros();
        parametros.usarSammon = Integer.parseInt(args[0]);
        parametros.MF = Double.parseDouble(args[1]); // SAMMON
        parametros.MAX_ITERACOES = 100; //atoi(argv[4]); // SAMMON
        parametros.EPSILON = 0.0001; //atof(argv[5]); // SAMMON
        parametros.OP = Integer.parseInt(args[2]); //montar vizinhaça
        parametros.quant_tipos = Integer.parseInt(args[3]);
        parametros.alphaInitial = Double.parseDouble(args[4]);
        parametros.alphaFinal = Double.parseDouble(args[5]);
        parametros.lambdaInitial = Double.parseDouble(args[6]);
        parametros.lambdaFinal = Double.parseDouble(args[7]);
        parametros.quantIteracoes = Integer.parseInt(args[8]);
        parametros.r = Integer.parseInt(args[9]); //entrada de linhas da matriz da rede neural
        parametros.s = Integer.parseInt(args[10]); //entrada de colunas da matriz da rede neural
        parametros.tabelaCores = args[11]; //arquivo com as cores a serem utilizadas na hora de imprimir o grafo
        parametros.tabelaPaises = args[12];
        parametros.preencherNeu = Integer.parseInt(args[13]);
        parametros.D = 3; //valor para dimensão reduzida 

        parametros.seed = System.currentTimeMillis();

        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        String leitura = rd.readLine();
        String vec[] = leitura.split(" ");
        //vec[0] quantidade de linhas vec[1] quantiadade de colunas 
        parametros.quantVetoresPadrao = Integer.parseInt(vec[0]);
        parametros.d = Integer.parseInt(vec[1]); //entrada da quantidade de dimensoes dos vetores

        Padrao vetorespadrao = new Padrao(parametros.quantVetoresPadrao, parametros.d);

        ArrayList<Integer> tipos = new ArrayList<Integer>(parametros.quantVetoresPadrao);

        vetorespadrao.preencherVetoresPadrao(rd, tipos);//preencher a matriz de vetores padrão e o vetor de tipos
        //é realizado dessa forma pq o tipo é a primeira coluna de cada linha onde se encontram os valores dos padroes

        Rede neu = new Rede(parametros.r, parametros.s, parametros.d, parametros.seed);

        if (parametros.preencherNeu == 1) {
            neu.preencherRedeAleatoriamente();//preencher a rede neural com valores aleatórios com base na semente seed ********************
        } else if (parametros.preencherNeu == 2) {
            neu.preencherRedeComPadroesAleatorios(vetorespadrao, parametros, tipos);
        }

        Cores cor = new Cores(parametros.quantVetoresPadrao);
        cor.leitura(parametros.tabelaCores); //salvando as cores de cada padrao de entrada

        neu.treino(parametros, vetorespadrao.padroes, tipos); //metodo que realiza o treino da rede neural   

        neu.imprimir();
        cor.calcularCoresH(neu);
        cor.calcularCoresV(neu, parametros);

        if (parametros.r * parametros.s > neu.corNeuronio.size()) {
            System.err.println("!!Erro nem todos os neuronios se tornam BMU");
            for (String s : neu.corNeuronio.keySet()) {
                System.err.println(s);
            }
        }
        Grafo g = new Grafo();
        Calculo c = new Calculo();
        Matriz3 mat_cont = c.contagem(parametros, neu.map, tipos);
        g.organiza_grafo(neu);
        System.err.println("\n");
        g.imprimir_grafo(neu, vetorespadrao.padroes, mat_cont, parametros);

        System.err.println("\nseed = " + parametros.seed);

    }//fim do metodo main

}

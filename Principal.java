
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Principal {

    public static void main(String args[]) throws IOException {

        if (args.length < 9) {
            System.err.println("H grau influencia dos vizinhos do BMU (positivo maior que 0)\nMIn_H\nQuant Iteracoes \n"
                    + "Faltou parametro.\nSão 8.\nSOM(0) ou CSSOM(1)\nEpsilon\n MinEpsilon\nMF\nVizinhaca(4)(8)(6)\nQuantidade de tipos\n");
            System.exit(-1);
        }

        Parametros parametros = new Parametros();
        parametros.usarSammon = Integer.parseInt(args[0]);
        parametros.epsilon = Double.parseDouble(args[1]);
        parametros.min_epsilon = Double.parseDouble(args[2]);
        parametros.MF = Double.parseDouble(args[3]); // SAMMON
        parametros.MAX_ITERACOES = 100; //atoi(argv[4]); // SAMMON
        parametros.EPSILON = 0.0001; //atof(argv[5]); // SAMMON
        parametros.OP = Integer.parseInt(args[4]); //montar vizinhaça
        parametros.quant_tipos = Integer.parseInt(args[5]);
        parametros.H = Double.parseDouble(args[6]);
        parametros.min_H = Double.parseDouble(args[7]);
        parametros.quantIteracoes = Integer.parseInt(args[8]);
        parametros.seed = -903136025;
        
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        String leitura = rd.readLine();
        String vec[] = leitura.split(" ");
        //vec[0] quantidade de linhas vec[1] quantiadade de colunas 
        //vec[2] e vec[3] dimensoes da rede neural
        parametros.quantVetoresPadrao = Integer.parseInt(vec[0]);
        parametros.d = Integer.parseInt(vec[1]); //entrada da quantidade de dimensoes dos vetores
        
        Padrao vetorespadrao = new Padrao(parametros.quantVetoresPadrao, parametros.d);
        
        parametros.r = Integer.parseInt(vec[2]); //entrada de linhas da matriz da rede neural
        parametros.s = Integer.parseInt(vec[3]); //entrada de colunas da matriz da rede neural
        
        Rede neu = new Rede(parametros.r, parametros.s, parametros.d, parametros.seed);
        
        neu.preencherRedeAleatoriamente();//preencher a rede neural com valores aleatórios com base na semente seed
        
        ArrayList<Integer> tipos = new ArrayList<Integer>(parametros.quantVetoresPadrao);
        
        vetorespadrao.preencherVetoresPadrao(rd, tipos);//preencher a matriz de vetores padrão e o vetor de tipos
        //é realizado dessa forma for o tipo é a primeira coluna de cada linha onde se encontram os valores dos padroes
        
        neu.treino(parametros, vetorespadrao, tipos); //metodo que realiza o treino da rede neural        

    }//fim do metodo main

}

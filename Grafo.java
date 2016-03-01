
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.*;
import java.util.*;
import java.util.ArrayList;

public class Grafo {
    
    private String[] descobrirPais(Rede redeneural, Double vetoresPadrao[][], String BMU, ArrayList<String> paises) {
        ArrayList<Euclidiana> array = new ArrayList<Euclidiana>();
        double soma = 0.0;
        String pais[] = null;
        
        if (redeneural.representacoes.containsKey(BMU)) {
            char vec[] = BMU.toCharArray();
            //posicao de linha e coluna do BMU na rede
            int l = Integer.parseInt(String.valueOf(vec[0]));
            int c = Integer.parseInt(String.valueOf(vec[1]));
            for (Integer i : redeneural.representacoes.get(BMU)) { //array com os padroes que o Bmu representou 
                soma = 0.0;
                for (int k = 0; k < redeneural.dimensoes; k++) {
                    soma += Math.pow((redeneural.rede[l][c][k] - vetoresPadrao[i][k]), 2.0);
                }
                Double E_mtemp = Math.sqrt(soma);
                Euclidiana e = new Euclidiana(E_mtemp, i, 0);
                array.add(e);
            }
            EuclidianaComparator comparador = new EuclidianaComparator();
            Collections.sort(array, comparador);
            
            int tamanho = array.size();
            if (tamanho >= 3) {
                pais = new String[3];
                for (int i = 0; i < 3; i++) {
                    pais[i] = paises.get(array.get(i).linha);
                }
            } else {
                pais = new String[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    pais[i] = paises.get(array.get(i).linha);
                }
            }
            array.clear();
        }
        return pais;
    }
    
    private void fazerLeituraPaises(ArrayList<String> paises, Parametros pa) {
        try {
            BufferedReader leituta = new BufferedReader(new FileReader(new File(pa.tabelaPaises)));
            for (int w = 0; w < pa.quantVetoresPadrao; w++) {
                String linha = leituta.readLine();
                paises.add(linha);
            }
            leituta.close();
        } catch (Exception e) {
            System.err.println("Erro na leitura dos paises:" + e.getMessage());
        }
    }
    
    private String clusterCores(String arquivo) {
        String s = "";
        
        if (arquivo.contains("_privadas")) {
            s = "\nsubgraph cluster_0 { ";
            s = s.concat("\nlabel=\"\";");
            s = s.concat("\nnode [shape=record,fontcolor=black];");
            s = s.concat("\na[label=\"1 Norte\",color=\"0.60 1 0.871 \"];");
            s = s.concat("\nb[label=\"2 Nordeste\",color=\"0.66 1 0.846 \"];");
            s = s.concat("\nc[label=\"3 Centro-Oeste\",color=\"0.77 1 0.859 \"];");
            s = s.concat("\nd[label=\"4 Sudeste\",color=\"0.88 1 0.859 \"];");
            s = s.concat("\ne[label=\"5 Sul\",color=\"0.99 1 0.859 \"];");
            s = s.concat("\n}");
        } else if (arquivo.contains("enem")) {
            s = "\nsubgraph cluster_0 {";
            s = s.concat("\nlabel=\"\";");
            s = s.concat("\nnode [shape=record,fontcolor=black];"); //para deixar o nó quadrado ou --> shape=circle
            s = s.concat("\na[label=\"1 Escola Privada\",color=\"0.66 1 0.871 \"];");
            s = s.concat("\nb[label=\"2 Escola Municipal\",color=\"0.77 1 0.846 \"];");
            s = s.concat("\nc[label=\"3 Escola Estadual\",color=\"0.88 1 0.859 \"];");
            s = s.concat("\nd[label=\"4 Escola Federal\",color=\"0.99 1 0.859 \"];");
            s = s.concat("\n}");
        } else if (arquivo.contains("vinho")) {
            s = "\nsubgraph cluster_0 {";
            s = s.concat("\nlabel=\"\";");
            s = s.concat("\nnode [shape=record,fontcolor=black];"); //para deixar o nó quadrado ou --> shape=circle
            s = s.concat("\na[label=\"(Variedade 1)\",color=\"0.66 1 0.923 \"];");
            s = s.concat("\nb[label=\"(Variedade 2)\",color=\"0.80 1 0.923 \"];");
            s = s.concat("\nc[label=\"(Variedade 3)\",color=\"0.99 1 0.88 \"];");
            s = s.concat("\n}");
        } else if (arquivo.contains("iris")) {
            s = "\nsubgraph cluster_0 {";
            s = s.concat("\nlabel=\"\";");
            s = s.concat("\nnode [shape=record,fontcolor=black];");
            s = s.concat("\na[label=\"(Setosa)\",color=\"0.66 1 0.871 \"];");
            s = s.concat("\nb[label=\"(Versicolor)\",color=\"0.80 1 0.846 \"];");
            s = s.concat("\nc[label=\"(Virginica)\",color=\"0.99 1 0.859 \"];");
            s = s.concat("\n}");
        }
        
        return s;
    }
    
    public void imprimir_grafo(Rede redeneural, Double vetoresPadrao[][], Matriz3 mat_cont, Parametros pa) {
        int di = 0, cont = 0, cont2 = 0, teste = 0;
        Vizinho vizinho = new Vizinho();
        ArrayList<String> paises = new ArrayList<String>();
        if (pa.tabelaCores.contains("idh")) {
            fazerLeituraPaises(paises, pa);
        }
        System.out.printf("graph {\n");
        System.out.printf("node [style=filled, fontcolor=white];\nrankdir=\"BT\";\n");
        System.out.printf("\n");
        for (int i = 0; i < pa.r; i++) {
            for (int j = 0; j < pa.s; j++) {
                System.out.printf("\"%d,%d\"", i, j);
                if (pa.quant_tipos > 4) {
                    System.out.printf("[fontsize=15,label=\"(");
                } else {
                    System.out.printf("[label=\"("); //descommentar aqui se quiser imprimir tb a matriz de contagem junto
                }
                teste = pa.quant_tipos - 1; //so para ver se chegou no final dos tipos
                for (di = 0; di < pa.quant_tipos; di++) {
                    int z = (int) mat_cont.getMatriz3(i, j, di);
                    if (di != teste) {
                        System.out.printf("%d,", z); //descommentar aqui se quiser imprimir tb a matriz de contagem junto
                    } else {
                        System.out.printf("%d", z); //descommentar aqui se quiser imprimir tb a matriz de contagem junto
                    }
                }

                //** se estiver trabalhando com o IDH  **
                if (pa.tabelaCores.startsWith("idh")) {
                    System.out.printf(")");
                    String a = String.valueOf(i).concat(String.valueOf(j));
                    Double x = redeneural.corNeuronio.get(a);
                    if (x == null) {
                        x = 0.0;
                    }
                    String p[] = descobrirPais(redeneural, vetoresPadrao, a, paises);
                    if (p != null) {
                        System.out.printf("\\n Média IDH:%.2f \\n Países:", x);
                        int tamanho = p.length;
                        for (int z = 0; z < tamanho; z++) {
                            if (z < tamanho - 1) {
                                System.out.printf("%s,\\n ", p[z]);
                            } else {
                                System.out.printf("%s", p[z]);
                            }
                        }
                    }
                    System.out.printf("\"");
                } else {
                    System.out.printf(")\"");  //descommentar aqui se quiser imprimir tb a matriz de contagem junto
                }
                String s = String.valueOf(i).concat(String.valueOf(j));
                //for (Double c : hash.get(s)) {
                Double H = redeneural.corNeuronio.get(s);
                Double V = redeneural.corV.get(s);
                if (H == null) {
                    //System.out.println("fiz uma alteracao em:"+s);
                    System.out.printf(",fillcolor=\"");
                    NumberFormat form = NumberFormat.getInstance(Locale.US);
                    System.out.print(form.format(0) + " ");
                    System.out.print(form.format(0) + " ");
                    System.out.print(form.format(100) + " ");
                } else {
                    if (V == null) {
                        V = new Double(1.0);
                    }
                    System.out.printf(",color=\"");
                    NumberFormat form = NumberFormat.getInstance(Locale.US);
                    System.out.print(form.format(H) + " ");
                    System.out.print(form.format(1) + " ");
                    System.out.print(form.format(V) + " ");
                }
                System.out.printf("\"];\n");
                cont++;
            }
        }
        
        cont = 0;
        System.out.printf("\n");
        for (int i = 0; i < pa.r; i++) {
            for (int j = 0; j < pa.s; j++) {
                if (i == pa.r - 1 && j == pa.s - 1) //para ignorar última posição da matriz
                {
                    continue;
                }
                System.out.printf("\"%d,%d\" ", i, j);
                System.out.printf(" -- ");
                System.out.printf("{");
                ArrayList<Vizinho> v = vizinho.vizinhanca(pa.r, pa.s, i, j, pa.OP);
                cont = v.size();
                cont--;//para tirar do cálculo próprio neurônio

                for (Vizinho viz : v) {
                    if (viz.linha == i && viz.coluna == j) {
                        // para não imprimir o prórpio neurônio
                        continue;
                    } else {
                        if ((viz.linha <= i && viz.coluna <= j) || (viz.linha < i && viz.coluna >= j)) {
                            teste = vizinho.ver_seeh_vizinho(pa.r, pa.s, i, j, viz.linha, viz.coluna, pa.OP);
                            if (teste == 1) {
                                continue;
                            }
                        } else {
                            cont2++;
                            if (cont2 == cont) {
                                System.out.printf("\"%d,%d\" ", viz.linha, viz.coluna);
                            } else {
                                System.out.printf("\"%d,%d\" ; ", viz.linha, viz.coluna);
                            }
                        }
                    }
                }
                System.out.printf("};\n");
                cont = cont2 = 0;
            }
        }
        
        String x = clusterCores(pa.tabelaCores);
        System.out.println(x);

        //chamar aqui a parte das cores e talz
        System.out.printf("\nsubgraph cluster_1 {");
        System.out.printf("\nlabel=\"\";");
        System.out.printf("\nrank = sink;");
        System.out.println("\nnode [shape=record,fontcolor=black];");
        System.out.printf("\nLegend [shape=none, margin=0, label=<");
        System.out.printf("\n<TABLE ALIGN=\"LEFT\" BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">");
        System.out.printf("\n<TR>");
        System.out.printf("\n<TD>Erro Topológico: %.2f</TD>", redeneural.erroTopografico);
        System.out.printf("\n</TR>");
        System.out.printf("\n<TR>");
        System.out.printf("\n<TD>Erro de Quantização: %.2f</TD>", redeneural.erroQuantizacao);
        System.out.printf("\n</TR>");
        System.out.printf("\n</TABLE>");
        System.out.printf("\n>];");
        System.out.printf("\n}");
        
        System.out.printf("\n}\n");
    }
    
    public void organiza_grafo(Rede neu) {
// vetores para obter o menor e maior valor de cada dimensão D da matriz neu
        Double menor[] = new Double[neu.dimensoes];
        Double maior[] = new Double[neu.dimensoes];

//inicializando os vetores
        for (int di = 0; di < neu.dimensoes; di++) {
            maior[di] = menor[di] = neu.rede[0][0][0];
        }
// procurando o menor e maior valor de cada dimensão da matriz neu
        for (int i = 0; i < neu.linhas; i++) {
            for (int j = 0; j < neu.colunas; j++) {
                for (int di = 0; di < neu.dimensoes; di++) {
                    if (neu.rede[i][j][di] < menor[di]) {
                        menor[di] = neu.rede[i][j][di];
                    }
                    if (neu.rede[i][j][di] > maior[di]) {
                        maior[di] = neu.rede[i][j][di];
                    }
                }
            }
        }
// alterando os valores da matriz neu para ficarem entre 0 e 1
//seguindo formula proposta pelo orientador (x[i]-min(x))/(max(x)-min(x))
        for (int i = 0; i < neu.linhas; i++) {
            for (int j = 0; j < neu.colunas; j++) {
                for (int di = 0; di < neu.dimensoes; di++) {
                    if (maior[di] > 1) {
                        neu.rede[i][j][di] = (neu.rede[i][j][di] - menor[di]) / (maior[di] - menor[di]);
                    }
                }
            }
        }
    }
    
}

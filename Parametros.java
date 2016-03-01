public class Parametros {
    double MF, EPSILON, 
           H, min_H, alphaInitial, alphaFinal ,lambdaInitial, lambdaFinal;
    int quantVetoresPadrao, d, r, s, MAX_ITERACOES, usarSammon, OP, D, quant_tipos, quantIteracoes,cont,preencherNeu;
    long seed;
    String tabelaCores,tabelaPaises;

    public Parametros() {
    }    
    
    public void imprimir(){
        System.out.println(" /* -- Parâmetros --");
        System.out.println("Viz:"+OP);
        System.out.println("r:"+r);
        System.out.println("s:"+s);
        System.out.println("quantIteracoes:"+quantIteracoes);
        System.out.println("seed:"+seed);        
        System.out.println("Arquivo de cores:"+tabelaCores);
        if(preencherNeu ==1){
            System.out.println("Rede preenchida aleatoriamente."+"*/");
        }else{
            System.out.println("Rede preenchida com sorteio."+"*/");
        }
    }
    
}

public class Parametros {
    double epsilon, MF, EPSILON, H, min_epsilon, min_H;
    int quantVetoresPadrao, d, r, s, MAX_ITERACOES, usarSammon, OP, D, quant_tipos, quantIteracoes, seed;   

    public Parametros() {
    }   
    
    
    public void imprimir(){
        System.out.println("-- Parâmetros --");
        System.out.println("usarSammon:"+usarSammon);
        System.out.println("epsilon:"+epsilon);
        System.out.println("min_epsilon:"+min_epsilon);
        System.out.println("MF:"+MF);
        System.out.println("Max_Iteracoes:"+MAX_ITERACOES);
        System.out.println("EPSILON:"+EPSILON);
        System.out.println("OP:"+OP);
        System.out.println("quantidade_tipos:"+quant_tipos);
        System.out.println("H:"+H);
        System.out.println("min_H:"+min_H);
        System.out.println("quantidadeIteracoes:"+quantIteracoes);
        System.out.println("r:"+r);
        System.out.println("s:"+s);
        System.out.println("d:"+d);
        System.out.println("quanVetoresPadrao:"+quantVetoresPadrao);
    }
    
}

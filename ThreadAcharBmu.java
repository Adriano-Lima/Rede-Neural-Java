
import java.util.Map;
import java.util.Objects;

public class ThreadAcharBmu extends Thread {

    int padrao;
    Rede rede;
    Padrao vetorespadrao;
    Parametros parametros;
    Calculo calc;

    public ThreadAcharBmu(Parametros parametros, int padrao, Rede rede, Padrao vetorespadrao) {
        this.parametros = parametros;
        this.padrao = padrao;
        this.rede = rede;
        this.vetorespadrao = vetorespadrao;
        this.calc = new Calculo();
    }

    public void run() {
        Bmu bmu = new Bmu();
        if (parametros.usarSammon == 1) {
            bmu = calc.achar_BMU_usando_Sammon(rede, vetorespadrao.padroes[padrao], parametros);
            rede.adicionaBmu(padrao, bmu);
        } else if (parametros.usarSammon == 2) {
            //E_m=CSSOM_2(neu,d_ast,d_Y,X,Y,r,s,d,D,vetorespadrao[padrao],epsilon,MF,EPSILON,MAX_ITERACOES,OP, H);
            //erroQ+= achar_BMU_SOM(neu, vetorespadrao[padrao], linhaBMUS, colunaBMUS, r, s, d, OP);
        } else {
            bmu = calc.achar_BMU_SOM(rede, vetorespadrao.padroes[padrao], parametros);
            rede.adicionaBmu(padrao, bmu);
        }
    }

}


public class AcharBmu extends Thread {

    int padrao;
    Rede rede;
    Double[][] vetoresPadrao;
    Parametros pa;
    Calculo calc;

    public AcharBmu(Parametros parametros, int padrao, Rede rede, Double[][] vetoresPadrao) {
        this.pa = parametros;
        this.padrao = padrao;
        this.rede = rede;
        this.vetoresPadrao = vetoresPadrao;
        this.calc = new Calculo();
    }

    public void run() {
        Bmu bmu = new Bmu();
        if (pa.usarSammon == 1) {
            bmu = calc.achar_BMU_usando_Sammon(rede.rede, vetoresPadrao[padrao], pa);
            rede.map.put(padrao, bmu);
            String s = String.valueOf(bmu.linha[0]).concat(String.valueOf(bmu.coluna[0]));
            rede.identificarRepresentacao(s, padrao);
        } else {
            bmu = calc.achar_BMU_SOM(rede.rede, vetoresPadrao[padrao], pa);
            rede.map.put(padrao, bmu);
            String s = String.valueOf(bmu.linha[0]).concat(String.valueOf(bmu.coluna[0]));
            rede.identificarRepresentacao(s, padrao);
        }
    }
}

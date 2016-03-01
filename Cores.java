
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Cores {

    ArrayList<Double> cores;

    public Cores(int tamanho) {
        cores = new ArrayList<Double>(tamanho);
    }

    public void leitura(String arquivo) {
        try {
            BufferedReader rd = new BufferedReader(new FileReader(new File(arquivo)));
            String linha;
            while ((linha = rd.readLine()) != null) {
                Double x = Double.parseDouble(linha);
                cores.add(x);
            }
            rd.close();
        } catch (Exception e) {
            System.err.println("Erro ao tentar abrir ou manipular o arquivo de cores. " + e.getMessage());
        }
    }

    public void calcularCoresH(Rede redeneural) {
        HashMap<String, ArrayList<Double>> hash = new HashMap<String, ArrayList<Double>>();
        for (Integer i : redeneural.map.keySet()) { //i é o padrao que BMu representou 
            String s = String.valueOf(redeneural.map.get(i).linha[0]).concat(String.valueOf(redeneural.map.get(i).coluna[0]));
            if (!hash.containsKey(s)) {
                ArrayList<Double> array = new ArrayList<Double>();
                array.add(cores.get(i));
                hash.put(s, array);
            } else {
                hash.get(s).add(cores.get(i));
            }
        }

        for (String s : hash.keySet()) {
            double soma = 0.0;
            for (Double d : hash.get(s)) {
                soma+= d;
            }
            soma = soma / (double) hash.get(s).size();
            redeneural.corNeuronio.put(s, soma);
        }

    }

    public void calcularCoresV(Rede redeneural, Parametros pa) {
        //descobrindo quantos padroes o BMU representou 
        for (Integer i : redeneural.map.keySet()) { //i é o padrao que BMu representou 
            String s = String.valueOf(redeneural.map.get(i).linha[0]).concat(String.valueOf(redeneural.map.get(i).coluna[0]));
            if (!redeneural.corV.containsKey(s)) {
                redeneural.corV.put(s, 1.0);
            } else {
                double x = redeneural.corV.get(s);
                x++;
                redeneural.corV.put(s, x);
            }
        }

        //divindo o total de padroes representados pelo BMU pelo total de padroes da base de dados
        for (String s : redeneural.corV.keySet()) {
            double x = redeneural.corV.get(s);
            x = x / (double) pa.quantVetoresPadrao;
            x = 1.0 - x;
            x = x * x;
            redeneural.corV.put(s, x);

        }
    }

    public void imprimir() {
        for (Double d : cores) {
            System.out.println(d);
        }
    }

}

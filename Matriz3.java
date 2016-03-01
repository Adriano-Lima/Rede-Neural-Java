
import java.util.ArrayList;

public class Matriz3 {

    ArrayList<ArrayList<ArrayList<Double>>> matriz;
    int li, co, di;

    public Matriz3(int li, int co, int di) {
        this.li = li;
        this.co = co;
        this.di = di;
        
        matriz = new  ArrayList<ArrayList<ArrayList<Double>>>();
        for (int i = 0; i < li; i++) {
            matriz.add(new ArrayList<ArrayList<Double>>());
            for (int j = 0; j < co; j++) {
                matriz.get(i).add(new ArrayList<Double>());
                for(int d=0;d<di;d++){
                    matriz.get(i).get(j).add(0.0);
                }
            }
        }
    }

    public void setMatriz3(int li, int co, int di, double valor) {
        matriz.get(li).get(co).set(di, valor);
    }

    public double getMatriz3(int li, int co, int di) {
        return matriz.get(li).get(co).get(di);
    }
    
    public void imprimir(){
        for(int i=0;i<li;i++){
            for(int j=0;j<co;j++){
                System.out.printf("[%d][%d]: ",i,j);
                for(int d=0;d<di;d++){
                    System.out.printf("%f ", getMatriz3(i, j, d)); 
                }
                System.out.println("");
            }
        }
    }
}

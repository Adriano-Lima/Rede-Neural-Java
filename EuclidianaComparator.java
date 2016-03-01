
import java.util.Comparator;

public class EuclidianaComparator implements Comparator<Euclidiana> {

    @Override
    public int compare(Euclidiana o1, Euclidiana o2) {
        return o1.distancia.compareTo(o2.distancia);
    }    
}

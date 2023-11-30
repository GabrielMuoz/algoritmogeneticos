import java.util.Arrays;
import java.util.Random;

public class Individuo {
    int numeroIndividuo;
    int[] cromosomas;
    int conteo;

    public Individuo(int numeroIndividuo, int[] cromosomas) {
        this.numeroIndividuo = numeroIndividuo;
        this.cromosomas = cromosomas;
        this.conteo = contarUnos(cromosomas);
    }

    public int contarUnos(int[] cromosomas) {
        int contador = 0;
        for (int cromosoma : cromosomas) {
            contador += cromosoma;
        }
        return contador;
    }

    @Override
    public String toString() {
        return "Individuo{" +
                "numeroIndividuo=" + numeroIndividuo +
                ", cromosomas=" + Arrays.toString(cromosomas) +
                ", conteo=" + conteo +
                '}';
    }

}
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class AlgoritmoGenetico {
    private static final int LONGITUD_GENES = 2;
    private static final int TAMANO_POBLACION = 20;
    private static final int NUM_GENERACIONES = 100;
    private static final double ELITISMO = 0.3;
    private static final double RANGO_MUTACION = 0.2;

    private static Random random = new Random();

    public static void main(String[] args) {
        List<Individuo> poblacion = iniciarPoblacion();

        Individuo mejorSolucion = null;
        int generacionMejorSolucion = 0;

        for (int i = 0; i < NUM_GENERACIONES; i++) {
            List<Individuo> nuevaPoblacion = new ArrayList<>();

            int eliteConteo = (int) (TAMANO_POBLACION * ELITISMO);
            nuevaPoblacion.addAll(obtenerElite(poblacion, eliteConteo));

            while (nuevaPoblacion.size() < TAMANO_POBLACION) {
                Individuo padre1 = seleccionarPadres(poblacion);
                Individuo padre2 = seleccionarPadres(poblacion);

                Individuo descendencia = cruzar(padre1, padre2);
                nuevaPoblacion.add(descendencia);
            }

            for (int j = eliteConteo; j < nuevaPoblacion.size(); j++) {
                mutar(nuevaPoblacion.get(j));
            }

            poblacion = nuevaPoblacion;


            Individuo mejorSolucionGeneracion = obtenerMejorSolucion(poblacion);
            if (mejorSolucion == null || mejorSolucionGeneracion.getAptitud() > mejorSolucion.getAptitud()) {
                mejorSolucion = mejorSolucionGeneracion;
                generacionMejorSolucion = i + 1;
            }
        }

        double mejorMSE = mejorSolucion.calcularMSE(mejorSolucion.getGenes(), Dataset.x, Dataset.y);


        System.out.println("Mejor solución en la generación " + generacionMejorSolucion + ":");
        System.out.println("Mejor individuo: " + java.util.Arrays.toString(mejorSolucion.getGenes()));
        System.out.println("Porcentaje del error: " + mejorSolucion.getAptitud());

    }

    private static List<Individuo> iniciarPoblacion() {
        List<Individuo> poblacionInicial = new ArrayList<>();
        for (int i = 0; i < TAMANO_POBLACION; i++) {
            double[] genes = generarDatosAleatorios(LONGITUD_GENES);
            Individuo individuo = new Individuo(genes);
            poblacionInicial.add(individuo);
        }
        return poblacionInicial;
    }

    private static double[] generarDatosAleatorios(int dimension) {
        double[] vector = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            vector[i] = random.nextDouble();
        }
        return vector;
    }

    private static List<Individuo> obtenerElite(List<Individuo> poblacion, int eliteConteo) {
        poblacion.sort(Comparator.comparingDouble(Individuo::getAptitud).reversed());
        return new ArrayList<>(poblacion.subList(0, eliteConteo));
    }

    private static Individuo seleccionarPadres(List<Individuo> poblacion) {
        return poblacion.get(random.nextInt(poblacion.size()));
    }

    private static Individuo cruzar(Individuo padre1, Individuo padre2) {
        double[] genesPadre1 = padre1.getGenes();
        double[] genesPadre2 = padre2.getGenes();

        int puntoCruce = random.nextInt(genesPadre1.length);

        double[] nuevosGenes = new double[genesPadre1.length];
        for (int i = 0; i < genesPadre1.length; i++) {
            nuevosGenes[i] = (i < puntoCruce) ? genesPadre1[i] : genesPadre2[i];
        }

        return new Individuo(nuevosGenes);
    }

    private static void mutar(Individuo individuo) {
        for (int i = 0; i < individuo.getGenes().length; i++) {
            if (random.nextDouble() < RANGO_MUTACION) {
                individuo.getGenes()[i] = random.nextDouble();
            }
        }
    }

    private static Individuo obtenerMejorSolucion(List<Individuo> poblacion) {
        return poblacion.stream().max(Comparator.comparingDouble(Individuo::getAptitud)).orElseThrow();
    }

    static class Individuo {
        private double[] genes;

        public Individuo(double[] genes) {
            this.genes = genes;
        }

        public double[] getGenes() {
            return genes;
        }

        public double calcularMSE(double[] parametros, double[] x, double[] y) {
            int n = x.length;
            double mse = 0.0;

            for (int i = 0; i < n; i++) {
                double prediccion = 0.0;
                for (int j = 0; j < parametros.length; j++) {
                    prediccion += parametros[j] * Math.pow(x[i], j);
                }

                mse += Math.pow(prediccion - y[i], 2);
            }

            return mse / n;
        }

        public double getAptitud() {
            double mse = calcularMSE(genes, Dataset.x, Dataset.y);
            return 1.0 / mse;
        }
    }
}
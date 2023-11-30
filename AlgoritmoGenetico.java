import java.util.ArrayList;
import java.util.Random;

public class AlgoritmoGenetico {

    private static final int NUM_CROMOSOMAS = 10;
    private static final int MAX_TAMANO_POBLACION = 6;
    private static int contadorIndividuos = 0;
    private static final Random random = new Random();

    public static void main(String[] args) {

        ArrayList<Individuo> poblacion = inicializarPoblacion();
        int generacion = 0;


        while (true) {

            ArrayList<Individuo> padres = seleccionarPadres(poblacion);

            ArrayList<Individuo> descendencia = cruzarPadres(padres);

            aplicarMutacion(descendencia);

            reemplazarPoblacion(poblacion, descendencia);

            System.out.println("Generación " + generacion);
            mostrarInformacionPoblacion(poblacion);

            Individuo mejorIndividuo = obtenerMejorIndividuo(poblacion);


            if (mejorIndividuo.conteo >= 9) {
                System.out.println("Generación " + generacion + ": Mejor Individuo = " + mejorIndividuo);
                break;
            }

            generacion++;
        }
    }

    // Función para mostrar información detallada de la población
    private static void mostrarInformacionPoblacion(ArrayList<Individuo> poblacion) {
        for (Individuo individuo : poblacion) {
            System.out.println(individuo);
        }
        System.out.println("--------------------------");
    }

    // Función para inicializar la población con individuos aleatorios
    private static ArrayList<Individuo> inicializarPoblacion() {
        ArrayList<Individuo> poblacion = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < MAX_TAMANO_POBLACION; i++) {
            int[] cromosomas = new int[NUM_CROMOSOMAS];
            for (int j = 0; j < NUM_CROMOSOMAS; j++) {
                cromosomas[j] = random.nextInt(2); // 0 o 1
            }
            poblacion.add(new Individuo(i, cromosomas));
        }

        return poblacion;
    }


    // Función para seleccionar padres utilizando el método de la ruleta
    private static ArrayList<Individuo> seleccionarPadres(ArrayList<Individuo> poblacion) {
        ArrayList<Individuo> padresSeleccionados = new ArrayList<>();

        int indicePadre1 = random.nextInt(poblacion.size());
        padresSeleccionados.add(poblacion.get(indicePadre1));

        int sumaTotal = 0;
        for (Individuo individuo : poblacion) {
            sumaTotal += individuo.conteo;
        }

        double numeroAleatorio = random.nextDouble();
        double acumulado = 0;

        for (Individuo individuo : poblacion) {
            double probabilidadSeleccion = (double) individuo.conteo / sumaTotal;
            acumulado += probabilidadSeleccion;

            if (acumulado >= numeroAleatorio) {
                padresSeleccionados.add(individuo);
                break;
            }
        }

        return padresSeleccionados;
    }

    // Función para cruzar padres y crear nueva descendencia
    private static ArrayList<Individuo> cruzarPadres(ArrayList<Individuo> padres) {
        ArrayList<Individuo> descendencia = new ArrayList<>();

        for (int i = 0; i < padres.size(); i += 2) {
            Individuo padre1 = padres.get(i);
            Individuo padre2 = padres.get(i + 1);

            int numeroAleatorioCrossover = random.nextInt(10) + 1;
            if (numeroAleatorioCrossover <= 2) {

                int puntoCorte = random.nextInt(NUM_CROMOSOMAS);

                int[] descendiente1 = new int[NUM_CROMOSOMAS];
                System.arraycopy(padre1.cromosomas, 0, descendiente1, 0, puntoCorte);
                System.arraycopy(padre2.cromosomas, puntoCorte, descendiente1, puntoCorte, NUM_CROMOSOMAS - puntoCorte);

                int[] descendiente2 = new int[NUM_CROMOSOMAS];
                System.arraycopy(padre2.cromosomas, 0, descendiente2, 0, puntoCorte);
                System.arraycopy(padre1.cromosomas, puntoCorte, descendiente2, puntoCorte, NUM_CROMOSOMAS - puntoCorte);

                descendencia.add(new Individuo(obtenerNuevoNumeroIndividuo(), descendiente1));
                descendencia.add(new Individuo(obtenerNuevoNumeroIndividuo(), descendiente2));
            } else {

                descendencia.add(padre1);
                descendencia.add(padre2);
            }
        }

        return descendencia;
    }

    private static void aplicarMutacion(ArrayList<Individuo> descendencia) {
        Random random = new Random();

        for (Individuo individuo : descendencia) {

            double probabilidadMutacion = 0.2;

            for (int i = 0; i < NUM_CROMOSOMAS; i++) {
                if (random.nextDouble() < probabilidadMutacion) {
                    individuo.cromosomas[i] = 1 - individuo.cromosomas[i];
                    individuo.conteo = individuo.contarUnos(individuo.cromosomas);
                }
            }
        }
    }

    // Función para reemplazar la población con la nueva descendencia
    private static void reemplazarPoblacion(ArrayList<Individuo> poblacion, ArrayList<Individuo> descendencia) {

        poblacion.sort((a, b) -> Integer.compare(b.conteo, a.conteo));
        descendencia.sort((a, b) -> Integer.compare(b.conteo, a.conteo));


        for (int i = 0; i < descendencia.size(); i++) {
            poblacion.set(i, descendencia.get(i));
        }
    }

    // Función para obtener el mejor individuo en términos de aptitud
    private static Individuo obtenerMejorIndividuo(ArrayList<Individuo> poblacion) {
        poblacion.sort((a, b) -> Integer.compare(b.conteo, a.conteo));
        return poblacion.get(0);
    }

    private static int obtenerNuevoNumeroIndividuo() {
        return contadorIndividuos++;
    }
}
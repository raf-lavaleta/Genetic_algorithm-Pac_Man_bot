package rs.edu.raf.mtomic.paclike;

import rs.edu.raf.mtomic.paclike.agent.genetic.Gene;
import rs.edu.raf.mtomic.paclike.agent.genetic.Population;
import rs.edu.raf.mtomic.paclike.agent.player.PlayerOne;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Test klasa:
 * <p>
 * Ovde možete ubaciti proizvoljni sistem za optimizaciju.
 * <p>
 * Zadatak je naslediti klasu Player i implementirati metodu generateNextMove,
 * koja će za svaki poziv vratiti jednu od sledećih metoda:
 * this::goUp, this::goLeft, this::goDown, this::goRight
 * te na taj način upravljati igračem u lavirintu.
 * <p>
 * Može se i (umesto nove klase) izmeniti klasa PlayerOne, koja sada samo ide levo.
 * <p>
 * U metodi se smeju koristiti svi dostupni geteri, ali ne smeju se
 * koristiti metode koje na bilo koji način menjaju stanje protivničkih
 * agenata ili stanje igre.
 * <p>
 * Matrica fields iz gameState: prvi indeks je kolona (X), drugi je vrsta (Y)
 * <p>
 * Zadatak: koristiti genetski algoritam za optimizaciju parametara
 * koji mogu odlučivati o sledećem potezu igrača. Generisanje poteza se
 * vrši na svaki frejm. Cilj je pokupiti svih 244 tačkica iz lavirinta.
 * Ako igrač naleti na protivnika, igra se prekida.
 * <p>
 * Savet: pogledajte kako Ghost agenti odlučuju o tome kada treba napraviti
 * skretanje (mada oni imaju jednostavna ponašanja) u njihovoj metodi Ghost::playMove.
 * <p>
 * Takođe, u implementacijama njihove metode calculateBest mogu se videti
 * primeri korišćenja GameState, iz koga se čitaju svi parametri.
 * <p>
 * Konačno stanje igre generiše se pokretanjem igre preko konstruktora i
 * pozivom join(), pa onda getTotalPoints().
 * <p>
 * Igrač se inicijalizuje GameState-om null, a PacLike će obezbediti
 * odgovarajuće stanje.
 * <p>
 * Ukoliko želite da pogledate simulaciju igre, promenite polje render
 * u klasi PacLike na true, a fps podesite po želji (ostalo ne treba
 * dirati).
 * <p>
 * Ograničenja:
 * - Svi parametri koji se koriste u generateNextMove() moraju biti
 * ili nepromenjeni (automatski generisani i menjani od strane igre),
 * ili inicijalizovani pomoću genetskog algoritma, ili eventualno
 * ako dodajete nove promenljive u klasu inicijalizovani u konstruktoru.
 * <p>
 * - Kalkulacije pomoću tih parametara i na osnovu onoga što igrač vidi
 * na osnovu GameState klase (i svih getera odatle i od objekata do
 * kojih odatle može da se dospe) su dozvoljene i poželjne; nije
 * dozvoljeno oslanjati se na unutrašnju logiku drugih agenata i hardkodovati
 * ponašanja ili šablone koji postoje za ovu igru (mada je engine
 * dosta promenjen u odnosu na original, iako liči, tako da je
 * većina šablona u suštini neupotrebljiva).
 **/
public class RunSimulation {

    private static int popSize = 200;            // 200
    private static int chromLen = 5000;        // 10000
    private static int tourniSize = 2;          // what?
    private static int numOfGenerations = 500;  // 500
    private static double mutRate = 0.01;       // 0.05

    public static void main(String[] args) {
        RunSimulation.geneAlgorithm();
    }

    private static void geneAlgorithm(){
//          First we generate an initial random population.
//          Then for every Gene in the population we choose a random "tourniSize" number of Genes from the population.
//          Tournament returns the Gene with the best fitness.
//          We repeat this operation one more time to get another Gene.
//          Then we mate the two genes and add the to a new pop "newHappyPop"
//          When this is done for the whole pop, we add the Genes form pop to newHappyPop
//          We sort newHappyPop in reversed order and keep the first "popSize", which will form our base pop in the next generation
//          Repeat for "numOfGenerations"


        Population pop = new Population(popSize, chromLen, mutRate);

        for(int i = 0; i < numOfGenerations; i++) {
            System.out.println("++++++++++++++ Generation " + i + " ++++++++++++++");

            List<Gene> newHappyPop = new ArrayList<>();

            for(int g = 0; g < popSize; g++){
                List<Gene> happyFamily = new ArrayList<>();

                for (int j = 0; j < 2; j++) {

                    List<Gene> inputGenes = new ArrayList<>();
                    for (int k = 0; k < tourniSize; k++) {
                        inputGenes.add(pop.getPopulation().get((int) (Math.random() * popSize)));
                    }
                    happyFamily.add(tournament(inputGenes));
                }
                newHappyPop.addAll(happyFamily.get(0).mate(happyFamily.get(1)));
            }
            newHappyPop.addAll(pop.getPopulation());
            newHappyPop.sort(Comparator.comparingDouble(Gene::getFit).reversed());
            pop.setPopulation(newHappyPop.subList(0, popSize));
            double sum = pop.getPopulation().stream().mapToDouble(Gene::getFit).sum();
            System.out.println("Average fit:" + new DecimalFormat("#0.00").format(sum/popSize) + " | Best fit:" + newHappyPop.get(0).getFit() + " | Points:" + newHappyPop.get(0).getPoints() + " | Total sum:" + sum);
        }
    }


    private static Gene tournament(List<Gene> inputGenes){
//      Gets a list of Genes and returns the one with the best fitness

        double bestFit = -1000;
        Gene bestGene = null;
        for(Gene single : inputGenes){
            if(single.getFit() > bestFit){
                bestGene = single;
                bestFit = single.getFit();
            }
        }

        return bestGene;
    }
}

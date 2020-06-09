package rs.edu.raf.mtomic.paclike.agent.genetic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Population implements Serializable {

    private List<Gene> population;
    private int popSize;

    public Population(int popSize, int chromLen, double mutRate){
        this.popSize = popSize;

        this.population = genPop(popSize, chromLen, mutRate);
    }

    private List<Gene> genPop(int popSize, int chromLen, double muteRate){
        List<Gene> newPop  = new ArrayList<>();

        for(int i = 0; i<popSize; i++) {
            newPop.add(new Gene(chromLen, true, muteRate));
        }
        return newPop;
    }

    public List<Gene> getPopulation() {
        return population;
    }

    public void setPopulation(List<Gene> population) {
        this.population = population;
        this.popSize = population.size();
    }
}

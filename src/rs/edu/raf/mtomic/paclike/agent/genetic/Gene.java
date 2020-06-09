package rs.edu.raf.mtomic.paclike.agent.genetic;

import rs.edu.raf.mtomic.paclike.PacLike;
import rs.edu.raf.mtomic.paclike.agent.player.PlayerOne;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gene implements Serializable {

    private ArrayList<Integer> chromosome = new ArrayList<>();
    private int chromLen;
    private double fit = Double.MAX_VALUE;
    private double mutRate = 0.025;
    private int lifeSpan = 0;
    private int points = 0;

    public Gene(int chromLen, boolean genNewGene, double mutRate){
//      So we only run the game once per Gene, we do it when the Gene is created.
//      Since we don't always want this to be the case, we add a boolean "genNewGene" to regulate this.

        this.chromLen = chromLen;
        this.mutRate = mutRate;
        if(genNewGene) {
//          If we are making a new Gene we need to make a new random chromosome so we call genGene().

            this.chromosome = genGene(chromLen);

//          Now that we have a chromosome we can run the game with it.
//          We do this so we can gen getTotalPoints() and getElapsedIterations() from the game engine,
//          so we can set the "fit" of the Gene.
            PacLike pacLike = new PacLike(new PlayerOne(null, this));
            try {
                pacLike.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            this.points = pacLike.getTotalPoints();
            this.lifeSpan = pacLike.getElapsedIterations();
            this.setFit(this.points, this.lifeSpan);
        }
    }

    private ArrayList<Integer> genGene(int chromLen){
//      Random list of ones and zeros of length "chromLen".

        Random rand = new Random();

        for(int i = 0; i < chromLen; i+=2){
            int next = rand.nextInt(4);

            if(next == 0){              // left
                this.chromosome.add(0);
                this.chromosome.add(0);
            }else if(next == 1){        // right
                this.chromosome.add(0);
                this.chromosome.add(1);
            }else if(next == 2){        // up
                this.chromosome.add(1);
                this.chromosome.add(0);
            }else{                      // down
                this.chromosome.add(1);
                this.chromosome.add(1);
            }
        }
        return this.chromosome;
    }

    public void mutate(double mutRate){
//      Every index of the chromosome has a "mutRate" chance to mutate, that is change it's value from one to zero and vice versa.
//      Once the index is 100 away from the place the Gene "died", we increase the "mutRate" in increments of .000675.
//      When the index reaches the place of "death" we stop increasing the "mutRate".

        double increase = .005;
        for(int i = 0; i < this.chromLen; i++){
            if(this.lifeSpan - 100 > i){
                if(Math.random() < mutRate)
                    this.chromosome.set(i, (this.chromosome.get(i)+1)%2);
            } else if(this.lifeSpan > i){
                if(Math.random() < mutRate + increase)
                    this.chromosome.set(i, (this.chromosome.get(i)+1)%2);
                increase += .000675;
            } else {
                if(Math.random() < mutRate + increase)
                    this.chromosome.set(i, (this.chromosome.get(i)+1)%2);
            }
        }
    }

    public List<Gene> mate(Gene inputGene){
//      We make two new Genes, but don't generate their chromosomes, hence "false" is passed as the second argument in Gene().


        Gene child1 = new Gene(this.chromLen, false, inputGene.mutRate);
        Gene child2 = new Gene(this.chromLen, false, inputGene.mutRate);

        ArrayList<Integer> chromChild1 = new ArrayList<>();
        ArrayList<Integer> chromChild2 = new ArrayList<>();

//      Mating is done by Uniform crossover method.

        for(int i = 0; i < this.chromLen; i++){
            if(Math.random() < .5){
                chromChild1.add(this.chromosome.get(i));
            } else {
                chromChild1.add(inputGene.getChromosome().get(i));
            }
            if(Math.random() < .5){
                chromChild2.add(this.chromosome.get(i));
            } else {
                chromChild2.add(inputGene.getChromosome().get(i));
            }
        }
        child1.setChromosome(chromChild1);
        child2.setChromosome(chromChild2);

        child1.mutate(child1.mutRate);
        child2.mutate(child2.mutRate);

        child1.calcFit(child1);
        child2.calcFit(child2);

        List<Gene> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);

        return children;
    }

    public void calcFit(Gene inputGene){
//      Since we don't have the fitness of our new Genes at this moment we have to calculate it.
//      This ensures that the game will be run once per Gene.

        PacLike pacLike = new PacLike(new PlayerOne(null, inputGene));
        try {
            pacLike.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        inputGene.setPoints(pacLike.getTotalPoints());
        inputGene.setLifeSpan(pacLike.getElapsedIterations());
        inputGene.setFit(pacLike.getTotalPoints(), inputGene.getLifeSpan());
    }

    public void setFit(int totalPoints, int elapsedIterations) {
//      The fitness function has two arguments, totalPoint (points eaten by the PacMan),
//      and elapsedIterations (the number of iterations a PacMan has survived before being eaten himself).
//      We use the following formula:
//      (totalPoints*3)^2 - sqrt(elapsedIterations)

        this.fit = (int) (Math.pow(totalPoints*3, 2) - Math.sqrt(elapsedIterations));
//        this.fit = (int) (Math.pow(totalPoints*3, 2));
    }
    public ArrayList<Integer> getChromosome() { return chromosome; }

    public void setChromosome(ArrayList<Integer> chromosome) {
        this.chromosome = chromosome;
    }

    public double getFit() { return fit; }

    public void setLifeSpan(int lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public int getLifeSpan() {
        return lifeSpan;
    }

    public void setPoints(int points) { this.points = points; }

    public int getPoints() { return points; }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.ga;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author calum
 */
public class Population {

    private List<Individual> population;

    private int averageFitness;

    private Individual dataSet;

    public Population(Individual dataSet) {
        this.dataSet = dataSet;
        this.population = new ArrayList<>();
    }

    public Population(List<Individual> population, Individual dataSet) {
        this.dataSet = dataSet;
        this.population = population;
    }

    public Population(int p, int ruleCount, int ruleLength, Individual dataSet) {
        this.dataSet = dataSet;
        this.population = new ArrayList<>();

        for (int i = 0; i < p; i++) {
            Individual newIndividual = new Individual(ruleCount, ruleLength);
            newIndividual.calculateFitness(dataSet);
            population.add(newIndividual);
        }
    }
    
    public void printBest() {
        Individual i = getHighestFitnessIndividual();
        int ruleLength = i.getRuleLength();
        System.out.println();
        System.out.println("Fitness: " + i.getFitness());
        for(String s : i.getRuleList()) {
            System.out.println(s.substring(0, ruleLength - 1) + " " + s.substring(ruleLength - 1));
        }
    }

    public int calculateTotalFitnessOfPopulation() {
        int totalFitness = 0;
        for (Individual i : population) {
            i.calculateFitness(dataSet);
            totalFitness += i.getFitness();
        }
        return totalFitness;
    }

    public Individual getHighestFitnessIndividual() {
        int maxFitness = 0;
        Individual max = null;

        for (Individual i : population) {
            if (i.getFitness() > maxFitness) {
                maxFitness = i.getFitness();
                max = i;
            }
        }

        return max;
    }

    public Individual getLowestFitnessIndividual() {
        int minFitness = 1000000000;
        Individual min = null;

        for (Individual i : population) {
            if (i.getFitness() < minFitness) {
                minFitness = i.getFitness();
                min = i;
            }
        }

        return min;
    }

    public double getAverageFitness() {
        int average = 0;

        for (Individual i : population) {
            i.calculateFitness(dataSet);
            average += i.getFitness();
        }

        averageFitness = average / population.size();

        return averageFitness;
    }

    public void replaceWorstIndividual(Individual replacement) {
        Individual worst = getLowestFitnessIndividual();

        if (worst.getFitness() < replacement.getFitness()) {
            getPopulation().remove(worst);
            addIndividual(replacement);
        }
    }

    public void printGeneration(int g) {
        System.out.println("Gen: " + g + " - Average: " + getAverageFitness() + " - Best: " + getHighestFitnessIndividual().getFitness());
    }

    public Population copy() {
        Population copy = new Population(dataSet);

        for (Individual i : this.population) {
            copy.addIndividual(i.copy());
        }

        return copy;
    }

    public Individual getIndividual(int i) {
        return getPopulation().get(i);
    }

    public void addIndividual(Individual i) {
        getPopulation().add(i);
    }

    public List<Individual> getPopulation() {
        return population;
    }

    public void setPopulation(List<Individual> population) {
        this.population = population;
    }

    @Override
    public String toString() {
        String s = "";
        for(Individual i : population) {
            s += i.getFitness() + " - ";
        }
        return s;
    }
}

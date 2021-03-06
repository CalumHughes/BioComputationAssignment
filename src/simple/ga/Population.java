/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.ga;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static simple.ga.SimpleGA.testSet;

/**
 *
 * @author calum
 */
public class Population {

    private List<Individual> population;

    private double averageFitness;

    private List<Rule> dataSet;

    public Population(List<Rule> dataSet) {
        this.dataSet = dataSet;
        this.population = new ArrayList<>();
    }

    public Population(List<Individual> population, List<Rule> dataSet) {
        this.dataSet = dataSet;
        this.population = population;
    }

    public Population(int p, int ruleCount, int ruleLength, List<Rule> dataSet) {
        this.dataSet = dataSet;
        this.population = new ArrayList<>();

        for (int i = 0; i < p; i++) {
            Individual newIndividual = new Individual(ruleCount, ruleLength);
            newIndividual.calculateFitness(dataSet);
            population.add(newIndividual);
        }
    }

    public void printIndividual(Individual i) {
        if (i == null) {
            i = getHighestFitnessIndividual();
        }
        System.out.println();
        System.out.println("Fitness: " + i.getFitness());
        for (Rule s : i.getRuleList(true)) {
            System.out.println(s.toString());
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
        Individual max = null;
        int maxFitness = 0;
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
        double average = 0;

        for (Individual i : population) {
            average += i.getFitness();
        }

        if (average == 0.0) {
            return 0.0;
        }

        averageFitness = average / population.size();

        return averageFitness;
    }

    public void replaceWorstIndividual(Individual replacement) {
        Individual worst = getLowestFitnessIndividual();

        if (worst.getFitness() < replacement.getFitness()) {
            List<Individual> p = getPopulation();
            p.remove(worst);
            p.add(replacement.copy());
            setPopulation(p);
        }
    }

    public List<String> printGeneration(int g) {
        Individual best = getHighestFitnessIndividual().copy();
        String bestTrainingFitness = String.valueOf(best.getFitness());
        best.calculateFitness(testSet);
        String average = String.valueOf(getAverageFitness());
        String bestTestFitness = String.valueOf(best.getFitness());
        System.out.println("Gen: " + g + " - Average: " + average + " - Best in Training: " + bestTrainingFitness + " - Best in Test: " + bestTestFitness);
        return Arrays.asList(String.valueOf(g), average, bestTrainingFitness, bestTestFitness);
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
        for (Individual i : population) {
            s += i.getFitness() + " - ";
        }
        return s;
    }
}

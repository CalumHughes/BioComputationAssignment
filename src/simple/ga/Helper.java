/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.ga;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author calum
 */
public class Helper {

    private int P;
    private int N;
    private double mutationRate;
    private Individual dataSet;
    private Random r;

    public Helper(int p, int n, double mutationRate) {
        this.P = p;
        this.N = n;
        this.mutationRate = mutationRate;
        r = new Random();
    }

    public Helper() {
        r = new Random();
    }

    public Individual getIndividualFromFile(String fileName) {

        String genes = "";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String currentLine;
            String[] splitLine;
            while ((currentLine = br.readLine()) != null) {
                splitLine = currentLine.split("\\s+");

                if (splitLine.length > 2) {
                    P = Integer.parseInt(splitLine[0]);
                    N = Integer.parseInt(splitLine[3]) + 1;
                } else {
                    genes = genes.concat(splitLine[0].concat(splitLine[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Exception reading file...");
            return null;
        }

        this.dataSet = new Individual(genes, N, P);
        return dataSet;
    }

    public Population tournamentSelection(Population population) {
        Population offspring = new Population(dataSet);
        int p = population.getPopulation().size();

        for (int i = 0; i < p; i++) {
            Individual parent1 = population.getIndividual(r.nextInt(p));
            Individual parent2 = population.getIndividual(r.nextInt(p));

            if (parent1.getFitness() >= parent2.getFitness()) {
                offspring.addIndividual(parent1.copy());
            } else {
                offspring.addIndividual(parent2.copy());
            }
        }

        offspring.calculateTotalFitnessOfPopulation();
        return offspring;
    }


    public Population bitwiseMutation(Population population) {
        Population mutatedOffspring = new Population(dataSet);

        for (Individual i : population.getPopulation()) {
            mutatedOffspring.addIndividual(mutateGenes(i.getGenes(), N));
        }

        mutatedOffspring.calculateTotalFitnessOfPopulation();
        return mutatedOffspring;
    }

    public Individual mutateGenes(int[] genes, int n) {
        int[] mutatedGenes = new int[genes.length];
        int index = 0;

        for (int i : genes) {
            Double d = r.nextDouble();
            if (d < mutationRate) {
                mutatedGenes[index] = 1 - i;
            } else {
                mutatedGenes[index] = i;
            }
            index++;
        }

        return new Individual(mutatedGenes, n);
    }

    public Population singlePointCrossover(Population population) {
        Population mutatedPopulation = new Population(dataSet);

        for (int i = 0; i < P; i++) {
            if (!(i >= population.getPopulation().size() - 1)) {
                Individual parent1 = population.getIndividual(i);
                Individual parent2 = population.getIndividual(i + 1);
                mutatedPopulation.getPopulation().addAll(performCrossover(r.nextInt(parent1.getGenes().length), parent1.getGenes(), parent2.getGenes()));
                i++;
            }
        }

        mutatedPopulation.calculateTotalFitnessOfPopulation();
        return mutatedPopulation;
    }

    public List<Individual> performCrossover(int crossoverPoint, int genesP1[], int genesP2[]) {

        List<Individual> children = new ArrayList<>();

        int[] child1Genes = genesP1;
        int[] child2Genes = genesP2;

        for (int i = crossoverPoint; i < genesP1.length; i++) {
            int tempGene = child1Genes[i];
            child1Genes[i] = child2Genes[i];
            child2Genes[i] = tempGene;
        }

        children.add(new Individual(child1Genes, N));
        children.add(new Individual(child2Genes, N));

        return children;
    }

    public int getP() {
        return P;
    }

    public int getN() {
        return N;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }
}

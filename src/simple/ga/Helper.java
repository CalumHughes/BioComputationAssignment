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
    private Random r;

    public Helper(int p, int n, double mutationRate) {
        this.P = p;
        this.N = n;
        this.mutationRate = mutationRate;
        r = new Random();
    }
    
    public Helper(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public Population getDataListFromFile(String fileName) {
        Population dataSet = new Population();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String currentLine;
            String[] splitLine;

            while ((currentLine = br.readLine()) != null) {
                splitLine = currentLine.split("\\s+");

                if (splitLine.length > 2) {
                    P = Integer.parseInt(splitLine[0]);
                    N = Integer.parseInt(splitLine[3]);
                } else {
                    dataSet.addIndividual(new Individual(splitLine[0], splitLine[1], N));
                }
            }
        } catch (IOException e) {
            System.out.println("Exception reading file...");
            return null;
        }

        if (dataSet.getPopulation().size() != P) {
            System.out.println("Error reading file, incorrect row amount...");
            return null;
        }

        
        
        return dataSet;
    }
    
    public Population tournamentSelection(Population population) {
        Population offspring = new Population();

        for (int i = 0; i <= P; i++) {
            Individual parent1 = population.getIndividual(r.nextInt(P));
            Individual parent2 = population.getIndividual(r.nextInt(P));

            if (parent1.getFitness() >= parent2.getFitness()) {
                offspring.addIndividual(parent1.copy());
            } else {
                offspring.addIndividual(parent2.copy());
            }
        }

        return offspring;
    }

    public Population bitwiseMutation(Population population) {
        Population mutatedOffspring = new Population();

        for (Individual i : population.getPopulation()) {
            mutatedOffspring.addIndividual(mutateGenes(i.getGenes()));
//            mutatedOffspring.printGeneration(1);
        }

        return mutatedOffspring;
    }

    public Individual mutateGenes(int[] genes) {
        int[] mutatedGenes = new int[N];
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

        return new Individual(mutatedGenes, N);
    }

    public Population singlePointCrossover(Population population) {
        Population mutatedPopulation = new Population();

        for (int i = 0; i < P; i++) {
            if (i != population.getPopulation().size() - 1) {
                Individual parent1 = population.getIndividual(i);
                Individual parent2 = population.getIndividual(i + 1);
                mutatedPopulation.getPopulation().addAll(performCrossover(r.nextInt(N), parent1.getGenes(), parent2.getGenes()));
                i++;
            }
        }

        return mutatedPopulation;
    }

    public List<Individual> performCrossover(int crossoverPoint, int genesP1[], int genesP2[]) {

        List<Individual> children = new ArrayList<>();

        int[] child1Genes = genesP1;
        int[] child2Genes = genesP2;

        for (int i = crossoverPoint; i < N; i++) {
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
}

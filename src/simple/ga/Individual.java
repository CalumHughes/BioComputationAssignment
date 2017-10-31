/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.ga;

import java.util.Random;

/**
 *
 * @author c38-hughes
 */
public class Individual {
    private Random r = new Random();
    
    private int genes[];
    
    private int fitness;
    
    private int n;
    
    public Individual(int n) {
        genes = new int[n];
        
        for(int i = 0; i < n; i++) {
            
            int randValue = r.nextInt(2);
            genes[i] = randValue;
            
            if(randValue == 1) {
                fitness++;
            }
        }
        
        this.n = n;
    }
    
    public Individual(int genes[], int n) {
        this.genes = genes;

        for(int i : genes) {
            if(i == 1) {
                fitness++;
            }
        }
        
        if(genes.length != n) {
            System.out.println("Incorrect amount of Genes...");
        }
        
        this.n = n;
    }
    
    public void mutateGenes(double mutationRate) {
        for(int i = 0; i < genes.length; i++) {
            Double d = r.nextDouble();
            if (d < mutationRate) {
                genes[i] = 1 - i;
            }
        }
        
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public int[] getGenes() {
        return genes;
    }

    public void setGenes(int[] genes) {
        this.genes = genes;
    }
    
    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public String printGenes() {
       String genes = "";
       
       for(int i : getGenes()) {
           genes += i;
       }
       
       return genes;
    }


    public Individual copy() {
        return new Individual(getGenes(), getN());
    }
    
    @Override
    public String toString() {
        return "Individual{" + "genes=" + printGenes() + ", fitness=" + fitness + '}';
    }
}

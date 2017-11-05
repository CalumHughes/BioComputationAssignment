/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.ga;

/**
 *
 * @author calum
 */
public class Rule {
    private double[] input;
    private double output;
    
    public Rule(double[] rule) {
        input = new double[rule.length - 1];
        for(int i = 0; i < rule.length - 1; i++) {
            input[i] = rule[i];
        }
        output = rule[rule.length - 1];
    }

    public double[] getInput() {
        return input;
    }

    public void setInput(double[] input) {
        this.input = input;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(double d : input) {
           s.append(d + " ");
        }
        s.append(output);
        return s.toString();
    }
}

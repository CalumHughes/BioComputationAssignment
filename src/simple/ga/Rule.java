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
        for (int i = 0; i < rule.length - 1; i++) {
            input[i] = rule[i];
        }
        output = rule[rule.length - 1];
    }

    public void sortInputPairs() {
        double[] sortedIn = new double[input.length];
        for (int i = 0; i < sortedIn.length; i++) {
            if (input[i] > input[i + 1]) {
                sortedIn[i] = input[i + 1];
                sortedIn[i + 1] = input[i];
            } else {
                sortedIn[i] = input[i];
                sortedIn[i + 1] = input[i + 1];
            }
            i++;
        }
        setInput(sortedIn);
    }

    public double[] getSortedInput() {
        sortInputPairs();
        return input;
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
        int count = 0;
        StringBuilder s = new StringBuilder("{");
        for (double d : input) {
            switch (count) {
                case 0:
                    s.append("[ ").append(d);
                    count++;
                    break;
                case 1:
                    s.append(" - ").append(d).append(" ], ");
                    count = 0;
                    break;
            }
        }
        s.append(output).append("}");
        return s.toString();
    }
}

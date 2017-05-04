
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author shadowalker
 */

class MyFitnessFunction extends FitnessFunction {
    
    private final int railength;
    private int totalLength,intersections;
    
    public MyFitnessFunction(int RAILLENGTH) {
        railength = RAILLENGTH;
    }
    
    @Override
    protected double evaluate(IChromosome a_subject) {
        
        totalLength = 0;
        intersections = 0;
        
        int value;
        
        for(int i=0;i<a_subject.size();i++){
            value = ((Integer)a_subject.getGene(i).getAllele()).intValue();
            intersections+= value;
            totalLength+=value*GeneticAlgorithm.getRailSizes().get(i);  //.railSize[i];
        }

        int lengthDiff = Math.abs(railength-totalLength);
        intersections--;
        
        return (1/(lengthDiff*0.9+intersections*0.1));
    }
}
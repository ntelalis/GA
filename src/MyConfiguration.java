
import org.jgap.Configuration;
import org.jgap.DefaultFitnessEvaluator;
import org.jgap.InvalidConfigurationException;
import org.jgap.audit.EvolutionMonitor;
import org.jgap.event.EventManager;
import org.jgap.impl.ChromosomePool;
import org.jgap.impl.CrossoverOperator;
import org.jgap.impl.GABreeder;
import org.jgap.impl.MutationOperator;
import org.jgap.impl.StockRandomGenerator;
import org.jgap.impl.TournamentSelector;
import org.jgap.impl.WeightedRouletteSelector;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author shadowalker
 */
public class MyConfiguration extends Configuration{
    
    public MyConfiguration(Selection sMethod, double recChance, int mutChance){
        this("","",sMethod,recChance,mutChance);
    }
    
    public MyConfiguration(String id, String name, Selection sMethod, double recChance, int mutChance){
        super(id,name);
        try{
            setBreeder(new GABreeder());
            setRandomGenerator(new StockRandomGenerator());
            //setMonitor(new EvolutionMonitor());
            setEventManager(new EventManager());
            if(sMethod==Selection.Tournament){
                TournamentSelector tournamentSelector=new TournamentSelector(this,5,1);
                tournamentSelector.setDoubletteChromosomesAllowed(false);
                addNaturalSelector(tournamentSelector, false);
            }
            else{
                WeightedRouletteSelector weightedRouletteSelector=new WeightedRouletteSelector(this);
                addNaturalSelector(weightedRouletteSelector, false);
            }
            setMinimumPopSizePercent(0);
            setSelectFromPrevGen(1.0d);
            setKeepPopulationSizeConstant(true);
            setFitnessEvaluator(new DefaultFitnessEvaluator());
            setChromosomePool(new ChromosomePool());
      
            addGeneticOperator(new CrossoverOperator(this, recChance));
            addGeneticOperator(new MutationOperator(this, mutChance));
        }
        catch(InvalidConfigurationException e){
            throw new RuntimeException();
        }
        
        
    }
 
    public Object clone(){
        return super.clone();
    }
}

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author shadowalker
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import org.jgap.InvalidConfigurationException;

public class Menu {
    public static void main(String[] args)throws IOException, InvalidConfigurationException{
        
        
        
        
        String path = Paths.get(".").toAbsolutePath().normalize().toString();
        if(System.getProperty("os.name").contains("indows"))
            path +="\\src\\sources\\test.txt";
        else
            path += "/src/sources/test.txt";
        
        GeneticAlgorithm ga = new GeneticAlgorithm(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        
        System.out.print("Do you want to use Adamidis's knowledge?? xD(T / R)[R]: ");
        String selection;
        do{
            selection=br.readLine().toUpperCase();
            if(selection.equals("T"))
                ga.setSelection(Selection.Tournament);
            else if(selection.equals("R"))
                ga.setSelection(Selection.Roulette);
            else
                ga.setSelection(Selection.Roulette);
            //System.out.println("Give your choice again:");
        }while(!(selection.equals("T") || selection.equals("R") || selection.equals("")));
        //}while(!(selection.equals("T") || selection.equals("R")));
        
        //boolean check1=false;
        boolean check1 = ga.guideRailSelection();
        
        
        System.out.print("Set the generation Limit(1000): ");
        String check=br.readLine();
        if(check.equals(""))
            ga.setGenerationLimit(1000);
        else{
            int generationLimit = Integer.parseInt(check);
            ga.setGenerationLimit(generationLimit);
        }
        
        
        System.out.print("Set the population size(50): ");
        check=br.readLine();
        if(check.equals(""))
            ga.setPopulationSize(50);
        else{
            int populationSize=Integer.parseInt(check);
            ga.setPopulationSize(populationSize);
        }
        
        System.out.print("Which selection method you want to use?(T / R)[R]: ");
        String selection;
        do{
            selection=br.readLine().toUpperCase();
            if(selection.equals("T"))
                ga.setSelection(Selection.Tournament);
            else if(selection.equals("R"))
                ga.setSelection(Selection.Roulette);
            else
                ga.setSelection(Selection.Roulette);
            //System.out.println("Give your choice again:");
        }while(!(selection.equals("T") || selection.equals("R") || selection.equals("")));
        //}while(!(selection.equals("T") || selection.equals("R")));
        
        System.out.print("Do you want to enable elitism?(Y / N)[N]: ");
        String elitism;
        
        do{
            elitism=br.readLine().toUpperCase();
            if(elitism.equals("Y"))
                ga.setElitismEnabled(true);
            else if(elitism.equals("N"))
                ga.setElitismEnabled(false);
            else
                ga.setElitismEnabled(false);
            //System.out.print("Give your choice again:");
        }while(!(elitism.equals("Y") || elitism.equals("N") || elitism.equals("")));
        //}while(!(elitism.equals("Y") || elitism.equals("N")));
        
        
        ga.evolve();
        
        if(check1){
            ga.setRailLength(ga.getFittestChromosomeRailLength()+ga.getLengthOfMyUsedRails());
            GeneticAlgorithm.setRailAmountElement(ga.getIndex(), ga.getForcedTimesToBeUsed());
            
            
            
            System.out.println("Fitness="+ga.getFittestChromosomeFitness());
            System.out.println("Total Length="+ga.getRailLength());
            System.out.println("Intersections="+(ga.getFittestChromosomeRailPoints()+ga.getForcedTimesToBeUsed()));
            System.out.print("Chromosome=[ ");
            for(int i=0;i<ga.getFittestChromosome().length;i++){
                if(i==ga.getIndex()){
                    System.out.print((ga.getFittestChromosome()[i]+ga.getForcedTimesToBeUsed())+" ");
                }
                else{
                    System.out.print(ga.getFittestChromosome()[i]+" ");
                }
            }
            System.out.println("]");
        }
        else{
            System.out.println("Fitness="+ga.getFittestChromosomeFitness());
            System.out.println("Total Length="+ga.getFittestChromosomeRailLength());
            System.out.println("Intersections="+(ga.getFittestChromosomeRailPoints()));
            System.out.print("Chromosome=[ ");
            for(int i=0;i<ga.getFittestChromosome().length;i++){
                System.out.print(ga.getFittestChromosome()[i]+" ");
            }
            System.out.println("]");
            
        }
        
        
    }
}


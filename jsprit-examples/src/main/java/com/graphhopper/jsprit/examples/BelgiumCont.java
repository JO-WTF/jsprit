package com.graphhopper.jsprit.examples;
import com.graphhopper.jsprit.analysis.toolbox.AlgorithmSearchProgressChartListener;
import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.SchrimpfFactory;
import com.graphhopper.jsprit.core.algorithm.listener.AlgorithmStartsListener;
import com.graphhopper.jsprit.core.analysis.SolutionAnalyser;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.util.GreatCircleCosts;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.io.problem.VrpXMLReader;

import java.util.Collection;


public class BelgiumCont {

    public static void main(String[] args) {
        String probName="belgium-cont";
        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();

        VrpXMLReader xmlReader = new VrpXMLReader(vrpBuilder);
        xmlReader.read("output/belgium2750.xml");
        // The problem should be built exactly the same as the one constructed the initial solution.
        VehicleRoutingProblem vrp = vrpBuilder.setRoutingCost(new GreatCircleCosts()).build();
        // Read problem definition from output of the last problem.
        Collection<VehicleRoutingProblemSolution> initialSolutions = xmlReader.readSolutions(vrp,"output/belgium2750.xml");

        // Set up VRP algorithm
        VehicleRoutingAlgorithm vra = new SchrimpfFactory().createAlgorithm(vrp);
        /*
        * It takes long time to solve the problem.
        * SaveSolutionOnExit listener will print and save solutions found whenever the algorithm is stopped.
         */
        vra.addListener((AlgorithmStartsListener) (problem, algorithm, solutions) -> new SaveSolutionOnExit(problem, solutions,probName));
        vra.addInitialSolution(Solutions.bestOf(initialSolutions));
        vra.setMaxIterations(64);
        vra.getAlgorithmListeners().addListener(new AlgorithmSearchProgressChartListener("output/"+probName+"sol_progress.png"));
        vra.searchSolutions();
    }

}

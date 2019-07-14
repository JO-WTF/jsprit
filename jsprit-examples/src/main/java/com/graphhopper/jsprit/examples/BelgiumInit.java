package com.graphhopper.jsprit.examples;

/*
 * Licensed to GraphHopper GmbH under one or more contributor
 * license agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * GraphHopper GmbH licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.graphhopper.jsprit.analysis.toolbox.AlgorithmSearchProgressChartListener;
import com.graphhopper.jsprit.analysis.toolbox.Plotter;
import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.listener.AlgorithmStartsListener;
import com.graphhopper.jsprit.core.analysis.SolutionAnalyser;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.DistanceUnit;
import com.graphhopper.jsprit.core.util.GreatCircleCosts;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.io.algorithm.VehicleRoutingAlgorithms;
import com.graphhopper.jsprit.io.problem.VrpXMLReader;
import com.graphhopper.jsprit.io.problem.VrpXMLWriter;
import com.graphhopper.jsprit.util.Examples;

import java.util.Collection;


public class BelgiumInit {

    public static void main(String[] args) {
        /*
         * This is an example showing setting up and solving a very large scale problem to get an initial solution.
         * The solution is saved to xml file, and will be read from BelgiumCont.java to carry on solving.
         */
        String probName="belgium2750";

        /*
         * some preparation - create output folder
         */
        Examples.createOutputFolder();

        /*
         * Build the problem.
         *
         * But define a problem-builder first.
         */
        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();

        /*
         * Read beilgium instance files, and stores the required information in the builder.
         */
        new VrpXMLReader(vrpBuilder).read("input/belgium-2750.xml");

        /*
         * Finally, the problem can be built. GreatCircleCosts is used in this case.
         */
        final VehicleRoutingProblem vrp = vrpBuilder.setRoutingCost(new GreatCircleCosts(0.5,1., DistanceUnit.Kilometer)).build();

        new Plotter(vrp).plot("output/"+probName+".png", probName);

        /*
         * Define the required vehicle-routing algorithms to solve the above problem.
         *
         * The algorithm can be defined and configured in an xml-file.
         *
         * Use best insertion method to construct initial solution for large scale problems like this one (configured in the algorithm config xml file)
         */

        VehicleRoutingAlgorithm vra = VehicleRoutingAlgorithms.readAndCreateAlgorithm(vrp, "input/algorithmConfig_solomon.xml");


        vra.setMaxIterations(0);
        vra.getAlgorithmListeners().addListener(new AlgorithmSearchProgressChartListener("output/"+probName+"sol_progress.png"));
        /*
         * Solve the problem.
         *
         */
        Collection<VehicleRoutingProblemSolution >solutions= vra.searchSolutions();
        VehicleRoutingProblemSolution solution = Solutions.bestOf(solutions);

        SolutionPrinter.print(vrp, solution, SolutionPrinter.Print.VERBOSE);
        SolutionAnalyser analyser = new SolutionAnalyser(vrp, solution, vrp.getTransportCosts());
        System.out.println("tp_distance: " + analyser.getDistance());
        System.out.println("tp_time: " + analyser.getTransportTime());
        System.out.println("waiting: " + analyser.getWaitingTime());
        System.out.println("service: " + analyser.getServiceTime());
        System.out.println("#picks: " + analyser.getNumberOfPickups());
        System.out.println("#deliveries: " + analyser.getNumberOfDeliveries());
        System.out.println("#cost: "+solution.getCost());
        VrpXMLWriter xmlWriter = new VrpXMLWriter(vrp,solutions,true);
        xmlWriter.write("output/"+probName+".xml");
    }

}

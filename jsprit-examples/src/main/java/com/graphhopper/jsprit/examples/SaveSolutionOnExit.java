package com.graphhopper.jsprit.examples;

import com.graphhopper.jsprit.analysis.toolbox.GraphStreamViewer;
import com.graphhopper.jsprit.core.analysis.SolutionAnalyser;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.analysis.toolbox.Plotter;
import com.graphhopper.jsprit.analysis.toolbox.Plotter.Label;
import com.graphhopper.jsprit.io.problem.VrpXMLWriter;

import java.util.Collection;

public class SaveSolutionOnExit {
    private VehicleRoutingProblem vrp;
    private Collection<VehicleRoutingProblemSolution> solutions;
    private VehicleRoutingProblemSolution solution;
    private String probName;

    // TODO: more parameters to be added
    public SaveSolutionOnExit(VehicleRoutingProblem vrp, Collection<VehicleRoutingProblemSolution> solutions, String probName) {
        this.vrp = vrp;
        this.solutions=solutions;
        solution = Solutions.bestOf(solutions);
        this.probName=probName;
        doShutDownWork();
    }

    private void doShutDownWork() {
        Runtime run = Runtime.getRuntime();//当前 Java 应用程序相关的运行时对象。
        run.addShutdownHook(new Thread() { //注册新的虚拟机来关闭钩子
            @Override
            public void run() {
                //程序结束时进行的操作
                System.out.println("Programme Terminated.");
                SolutionPrinter.print(vrp, solution, SolutionPrinter.Print.VERBOSE);

                Plotter plotter = new Plotter(vrp, solution);
                plotter.setLabel(Label.SIZE);

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

                new GraphStreamViewer(vrp, solution).labelWith(GraphStreamViewer.Label.ID).setRenderDelay(100).setGraphStreamFrameScalingFactor(1).display();
            }
        });
    }

}

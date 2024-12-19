/**
 ********************************************************************************
 * Copyright (c) 2018 Robert Bosch GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Robert Bosch GmbH - initial API and implementation
 ********************************************************************************
 */

package app4mc.example.tool.java;

import java.io.File;

import org.eclipse.app4mc.amalthea.model.ActivityGraph;
import org.eclipse.app4mc.amalthea.model.ActivityGraphItem;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.DiscreteValueConstant;
import org.eclipse.app4mc.amalthea.model.DiscreteValueWeibullEstimatorsDistribution;
import org.eclipse.app4mc.amalthea.model.Frequency;
import org.eclipse.app4mc.amalthea.model.FrequencyDomain;
import org.eclipse.app4mc.amalthea.model.Tag;
import org.eclipse.app4mc.amalthea.model.SWModel;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.HWModel;
import org.eclipse.app4mc.amalthea.model.HwDomain;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.Ticks;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaLoader;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaWriter;
import org.eclipse.app4mc.amalthea.model.util.ModelUtil;

//@SuppressWarnings("java:S106") // Example code - Use of standard outputs is intended
public class ModifyModelCar {

	public static void main(String[] args) {

		// example: absolute path
		// final File inputFile = new File("d:/temp/democar.amxmi");
		// final File outputFile = new File("d:/temp/democar_1.amxmi");

		// example: relative path
		final File inputFile = new File("model-input/democar.amxmi");
		final File outputFile = new File("model-output/LoadModifySave/democar_1.amxmi");

		System.out.println("-----------------------------------");
		System.out.println(" Amalthea Load-Modify-Save Example");
		System.out.println("-----------------------------------");

		// ***** Load *****

		Amalthea model = AmaltheaLoader.loadFromFile(inputFile);

		if (model != null) {
			System.out.println("reading file: " + inputFile.getAbsolutePath());
		} else {
			System.err.println("Error: No model loaded!");
			return;
		}

		// ***** Modify *****

		final AmaltheaFactory fac = AmaltheaFactory.eINSTANCE;
		

		/*
		 * Obtain the SW model
		 * Get the list of tasks
		 * Add the task to it
		 */
//		Task newTask = fac.createTask();
//		newTask.setName("DummyTask");
//		model.getSwModel().getTasks().add(newTask);

		/*
		 * Add ticks to new task
		 */
//		ActivityGraph newActGraph = fac.createActivityGraph();
//		newTask.setActivityGraph(newActGraph);
//		
//		Ticks newTick = fac.createTicks();
//		newActGraph.getItems().add(newTick);
//		
//		DiscreteValueConstant newTickConst = fac.createDiscreteValueConstant();
//		newTickConst.setValue(12356);
//		
//		newTick.setDefault(newTickConst);
//		
//		newActGraph.getItems().add(newTick);
//		newTask.setActivityGraph(newActGraph);
		
		/*
		 * Modify the runnables ticks by 20%
		 */
		SWModel swModel = model.getSwModel();
		HWModel hwModel = model.getHwModel();
		
		if(null == swModel)
		{
			System.err.println("Error: No sw model found!");
			return;
		}
		if(null == hwModel)
		{
			System.err.println("Error: No hw model found!");
			return;
		}
		else
		{
			for(HwDomain item : hwModel.getDomains())
			{
				if(item instanceof FrequencyDomain)
				{
					Frequency tempFreq = (Frequency) ((FrequencyDomain) item).getDefaultValue();
					tempFreq.setValue(tempFreq.getValue() * 1.1);
				}
			}
		}
		
		for(Task iTask : model.getSwModel().getTasks())
		{
			if(iTask.getName() == "Timer_10MS")
			{
				iTask.setName("Timer_20MS");		
			}
			if(iTask.getName() == "Timer_20MS")
			{
				iTask.setName("Timer_5MS");
			}
			if(iTask.getName() == "Timer_5MS")
			{
				iTask.setName("Timer_10MS");
			}
			System.out.println(iTask.getName());
			System.out.println(iTask.getStimuli());
		}
		
		
		for(Runnable tempRun: model.getSwModel().getRunnables())
		{
			ActivityGraph activityGraph_var = tempRun.getActivityGraph();
			
			if(null == activityGraph_var)
			{
				System.err.println("Error: No activity graph found!");
				return;	
			}
			
			for(ActivityGraphItem tempActGraph : tempRun.getActivityGraph().getItems())
			{
				if(tempActGraph instanceof Ticks)
				{
					Ticks tempTick = (Ticks)tempActGraph;
					System.out.printf("Ticket Found in... \t%s \n", tempRun.getName());
					//System.out.printf("", tempTick.getDefault().getAverage(), tempTick.getDefault().getLowerBound(), tempTick.getDefault().getUpperBound());
					
					if(null == tempTick.getDefault())
					{
						System.err.println("Error: No ticks model found!");
						return;
					}
					DiscreteValueWeibullEstimatorsDistribution tempDvc = fac.createDiscreteValueWeibullEstimatorsDistribution();
					//((Ticks) tempActGraph).getDefault();
					tempDvc.setAverage((double) (tempTick.getDefault().getAverage() * 0.80));
					tempDvc.setLowerBound((long) (tempTick.getDefault().getLowerBound() * 0.80));
					tempDvc.setUpperBound((long) (tempTick.getDefault().getUpperBound() * 0.80));
					tempTick.setDefault(tempDvc);
//					((Ticks) tempActGraph).getDefault().getLowerBound();
//					((Ticks) tempActGraph).getDefault().getUpperBound()();
				}
				else
				{
					
				}
//				System.out.printf("%s \n", tempActGraph.getClass().getName());
			}			
		}
		
		
		// ***** Save *****

		boolean success = AmaltheaWriter.writeToFile(model, outputFile);

		if (success) {
			System.out.println("writing file: " + outputFile.getAbsolutePath());
		} else {
			System.err.println("Error: No model saved!");
			return;
		}

		System.out.println("\ndone.");
	}

}

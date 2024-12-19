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
import java.util.Set;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.SWModel;
import org.eclipse.app4mc.amalthea.model.Tag;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaLoader;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaWriter;
import org.eclipse.app4mc.amalthea.model.util.DeploymentUtil;
import org.eclipse.app4mc.amalthea.model.util.ModelUtil;
import org.eclipse.app4mc.amalthea.model.util.RuntimeUtil;
import org.eclipse.app4mc.amalthea.model.util.RuntimeUtil.TimeType;
import org.eclipse.app4mc.amalthea.model.util.TimeUtil;

//@SuppressWarnings("java:S106") // Example code - Use of standard outputs is intended
public class RunTimingAnalysis {

	public static void main(String[] args) {
		
		System.out.println("-----------------------------------");
		System.out.println(" Amalthea Load-Modify-Save Example");
		System.out.println("-----------------------------------");
		
		// example: relative path
//		final File inputFile = new File("model-input/democar_1.amxmi");
		final File inputFile = new File("model-output/LoadModifySave/democar_1.amxmi");

		Amalthea model = AmaltheaLoader.loadFromFile(inputFile);

		if (model != null) {
			System.out.println("reading file: " + inputFile.getAbsolutePath());
		} else {
			System.err.println("Error: No model loaded!");
			return;
		}
		
		SWModel swModel = model.getSwModel();
		
		
		if(swModel == null)
		{
			System.err.println("Error: No software model found!");
			return;
		}
		
		System.out.println("No. of task... " + model.getSwModel().getTasks().size());
		double util = 0.0;
		//Set<ProcessingUnit> processingUnit = new HashSet<>();
		for(Task task : swModel.getTasks())
		{
			System.out.printf("%s %15s %15s %15s%n", "Task", "ExecTime", "Period", "Util");
			Time executionTime = RuntimeUtil.getExecutionTimeForProcess(task, null, TimeType.WCET);
			Time period = RuntimeUtil.getPeriodsOfProcess(task, TimeType.WCET, null).get(0);
			//executionTime = executionTime.multiply(0.8);
			double utilizationTask = executionTime.divide(period);
			
			Time timeInMs = TimeUtil.convertToTimeUnit(executionTime, TimeUnit.MS);
			Time timeInUs = TimeUtil.convertToTimeUnit(executionTime, TimeUnit.US);
			Time timeInPs = TimeUtil.convertToTimeUnit(executionTime, TimeUnit.PS);
			
			util += utilizationTask;

			//System.out.printf("Core: %s\n", DeploymentUtil.getAssignedCoreForProcess(task, model).getClass());
			//System.out.printf("%s: \t%s%n", task.getName(), timeInUs);
			System.out.printf("%s %15s %15s %15s%n \n", task.getName(), executionTime, period, utilizationTask*100);
		}
		System.out.printf("Total Utilization: %f", util);

	}

}

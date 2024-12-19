/**
 ********************************************************************************
 * Copyright (c) 2021 Robert Bosch GmbH.
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.OSModel;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaWriter;
import org.eclipse.app4mc.amalthea.model.predefined.AmaltheaTemplates;
import org.eclipse.app4mc.amalthea.model.predefined.StandardSchedulers.Algorithm;
import org.eclipse.app4mc.amalthea.model.predefined.StandardSchedulers.Parameter;
import org.eclipse.app4mc.amalthea.model.util.ModelUtil;

//@SuppressWarnings("java:S106") // Example code - Use of standard outputs is intended
public class StandardElementsExample {

	public static void main(String[] args) {

		// example: relative path
		final File outputFile1 = new File("model-output/StandardElements/schedulers.amxmi");
		final File outputFile2 = new File("model-output/StandardElements/schedulers-all.amxmi");
		final File outputFile3 = new File("output/StandardElements/scheduler-descriptions.txt");

		System.out.println("-----------------------------------");
		System.out.println(" Standard Elements Example");
		System.out.println("-----------------------------------");

		final AmaltheaFactory fac = AmaltheaFactory.eINSTANCE;

		// ***** Model 1 *****

		Amalthea model1 = fac.createAmalthea();
		OSModel osModel1 = ModelUtil.getOrCreateOsModel(model1);

		// add some standard scheduler definitions
		AmaltheaTemplates.addStandardSchedulerDefinition(osModel1, Algorithm.FIXED_PRIORITY_PREEMPTIVE);
		AmaltheaTemplates.addStandardSchedulerDefinition(osModel1, Algorithm.OSEK);
		AmaltheaTemplates.addStandardSchedulerDefinition(osModel1, Algorithm.P_FAIR_PD2);

		AmaltheaWriter.writeToFile(model1, outputFile1);

		System.out.println("Created file: " + outputFile1.getAbsolutePath());

		// ***** Model 2 *****

		Amalthea model2 = fac.createAmalthea();
		OSModel osModel2 = ModelUtil.getOrCreateOsModel(model2);

		// add all standard scheduling parameter definitions (in alphabetic order)
		Arrays.stream(Parameter.values())
			.sorted(Comparator.comparing(Parameter::getParameterName))
			.forEachOrdered(p -> AmaltheaTemplates.addStandardSchedulingParameterDefinition(osModel2, p));

		// add all standard scheduler definitions (in alphabetic order)
		Arrays.stream(Algorithm.values())
		.sorted(Comparator.comparing(Algorithm::getAlgorithmName))
		.forEachOrdered(a -> AmaltheaTemplates.addStandardSchedulerDefinition(osModel2, a));


		AmaltheaWriter.writeToFile(model2, outputFile2);

		System.out.println("Created file: " + outputFile2.getAbsolutePath());

		// ***** Scheduler descriptions *****

		// create output directory

		try {
			Files.createDirectories(Paths.get(outputFile3.getAbsoluteFile().getParent()));
		} catch (IOException e1) {
			System.err.println("Error: Output directory could not be created!");
			return;
		}

		// write to file

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try (PrintStream ps = new PrintStream(new FileOutputStream(outputFile3.getAbsoluteFile(), false)))
		{
			ps.println(dateFormat.format(new Date()));

			ps.println("\n*** Standard scheduler descriptions ***\n");

			// print all standard scheduler definitions (in alphabetic order)
			Arrays.stream(Algorithm.values())
			.sorted(Comparator.comparing(Algorithm::getAlgorithmName))
			.forEachOrdered(a -> ps.println(
					"----------------------------------------\n" +
					a.getAlgorithmName() +
					"\n----------------------------------------\n\n" +
					a.getDescription()));

		} catch (IOException e) {
			System.err.println("Error: Info file could not be created!");
		}

		System.out.println("Created file: " + outputFile3.getAbsolutePath());

		System.out.println("\ndone.");
	}

}

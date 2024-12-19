/**
 ********************************************************************************
 * Copyright (c) 2022 Robert Bosch GmbH.
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
import java.util.Collection;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.check.ModelStructureCheck;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaLoader;
import org.eclipse.emf.ecore.resource.ResourceSet;

//@SuppressWarnings("java:S106") // Example code - Use of standard outputs is intended
public class LoaderExample {

	public static void main(String[] args) {

		final File inputFolder = new File("model-input/SplitModelDirectory");
		final File outputFile = new File("output/SplitModelLoader/ModelCheckResults.txt");

		System.out.println("-------------------------");
		System.out.println(" Amalthea Loader Example");
		System.out.println("-------------------------");


		// ***** Load *****

		ResourceSet resourceSet = AmaltheaLoader.loadFromDirectory(inputFolder);

		if (resourceSet != null) {
			System.out.println("Read files from directory: " + inputFolder.getAbsolutePath());
		} else {
			System.err.println("Error: No model loaded!");
			return;
		}

		Collection<Amalthea> models = AmaltheaLoader.collectAmaltheaModels(resourceSet);


		// ***** Check Model Consistency *****

		// create output directory

		try {
			Files.createDirectories(Paths.get(outputFile.getAbsoluteFile().getParent()));
		} catch (IOException e1) {
			System.err.println("Error: Output directory could not be created!");
			return;
		}

		// write check report to file

		try (PrintStream ps = new PrintStream(new FileOutputStream(outputFile.getAbsoluteFile(), false)))
		{
			ModelStructureCheck.checkModels(models, ps, false);

		} catch (IOException e) {
			System.err.println("Error: Info file could not be created!");
		}

		System.out.println("Created file: " + outputFile.getAbsolutePath());

		System.out.println("\ndone.");
	}

}

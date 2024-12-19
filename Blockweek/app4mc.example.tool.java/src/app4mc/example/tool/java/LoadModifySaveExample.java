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

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.Tag;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaLoader;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaWriter;
import org.eclipse.app4mc.amalthea.model.util.ModelUtil;

//@SuppressWarnings("java:S106") // Example code - Use of standard outputs is intended
public class LoadModifySaveExample {

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

		Tag tag = fac.createTag();
		tag.setName("The new tag!");
		ModelUtil.getOrCreateCommonElements(model).getTags().add(tag);

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

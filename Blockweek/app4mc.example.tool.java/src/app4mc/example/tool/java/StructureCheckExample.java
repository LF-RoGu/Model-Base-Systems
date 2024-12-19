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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.eclipse.app4mc.amalthea.model.ActivityGraph;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.RunnableCall;
import org.eclipse.app4mc.amalthea.model.SWModel;
import org.eclipse.app4mc.amalthea.model.Tag;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.check.ModelStructureCheck;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaWriter;
import org.eclipse.app4mc.amalthea.model.util.ModelUtil;

//@SuppressWarnings("java:S106") // Example code - Use of standard outputs is intended
public class StructureCheckExample {

	public static void main(String[] args) {

		final File outputFile = new File("model-output/StructureCheck/democar_1.amxmi");

		System.out.println("----------------------------------------");
		System.out.println(" Amalthea Model Structure Check Example");
		System.out.println("----------------------------------------");

		// ***** Create *****

		final AmaltheaFactory fac = AmaltheaFactory.eINSTANCE;

		Amalthea model = fac.createAmalthea();
		SWModel sw = ModelUtil.getOrCreateSwModel(model);

		// Correct task creation
		Task task = fac.createTask();// create object
		task.setName("T1"); // set attribute(s)
		sw.getTasks().add(task); // add to container

		// Incorrect creation (without containment)
		Runnable run = fac.createRunnable();
		run.setName("run-x");
		Tag tag = fac.createTag();
		tag.setName("My tag");

		// some links

		task.getTags().add(tag);
		run.getTags().add(tag);

		ActivityGraph graph = fac.createActivityGraph();
		RunnableCall call = fac.createRunnableCall();
		task.setActivityGraph(graph);
		graph.getItems().add(call);
		call.setRunnable(run);

		// ***** Check *****

		System.out.println("\n\n******** Option 1 ********\n");

		// Option 1: for debugging write output to console

		ModelStructureCheck.checkModel(model, System.out, false);


		System.out.println("\n\n******** Option 2 ********\n");

		// Option 2: check model and create String with log messages
		// (for example result can be written to an error log)

		Charset utf8 = StandardCharsets.UTF_8;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		boolean checkOK = false;
		String checkReport = "no report available";
		try (PrintStream ps = new PrintStream(baos, true, utf8.name())) {
			checkOK = ModelStructureCheck.checkModel(model, ps, false);
			checkReport = new String(baos.toByteArray(), utf8);
		} catch (UnsupportedEncodingException e) {
			// do nothing
		}

		if (!checkOK) {
			System.err.println("Input Amalthea model failed model structure check.");
			System.err.println("----- Report -----\n" + checkReport);
		}

		// ***** Save *****

		// writes an inconsistent model with links to non-existing objects
		boolean success = AmaltheaWriter.writeToFile(model, outputFile);

		if (success) {
			System.out.println("\nwriting file: " + outputFile.getAbsolutePath());
		} else {
			System.err.println("\nError: No model saved!");
			return;
		}

		System.out.println("\ndone.");
	}

}

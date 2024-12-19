/**
 ********************************************************************************
 * Copyright (c) 2018-2020 Robert Bosch GmbH.
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
import java.util.List;

import org.eclipse.app4mc.amalthea.model.ActivityGraphItem;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.LabelAccess;
import org.eclipse.app4mc.amalthea.model.LabelAccessEnum;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.RunnableCall;
import org.eclipse.app4mc.amalthea.model.SWModel;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaWriter;
import org.eclipse.app4mc.amalthea.model.util.SoftwareUtil;

//@SuppressWarnings("java:S106") // Example code - Use of standard outputs is intended
public class CollectorExample {

	public static void main(String[] args) {

		final File outputFile = new File("model-output/Collector/CollectorModel.amxmi");

		System.out.println("----------------------------");
		System.out.println(" Amalthea Collector Example ");
		System.out.println("----------------------------");

		// ***** Create model *****

		System.out.println("creating model");

		Amalthea model = AmaltheaFactory.eINSTANCE.createAmalthea();
		SWModel sw = AmaltheaFactory.eINSTANCE.createSWModel();
		model.setSwModel(sw);
		Runnable run = addNewRunnable(sw, "Runner-1");

		addNewLabelAccess(sw, run, "Label A", LabelAccessEnum.READ);
		addNewLabelAccess(sw, run, "Label B", LabelAccessEnum.READ);
		addNewLabelAccess(sw, run, "Label C", LabelAccessEnum.WRITE);
		addNewLabelAccess(sw, run, "Label D", LabelAccessEnum.WRITE);

		addNewRunnableCall(sw, run, "Service A");
		addNewRunnableCall(sw, run, "Service B");

		// ***** Save model *****

		boolean success = AmaltheaWriter.writeToFile(model, outputFile);

		if (success) {
			System.out.println("writing file: " + outputFile.getAbsolutePath());
		} else {
			System.err.println("Error: No model saved!");
			return;
		}


		// ***** Some tests *****

		List<ActivityGraphItem> items1 = SoftwareUtil.collectActivityGraphItems(run.getActivityGraph());

		System.out.println("\nAll items:");
		for (ActivityGraphItem item : items1) {
			System.out.println(" - " + item.toString());
		}

		List<ActivityGraphItem> items2 = SoftwareUtil.collectActivityGraphItems(run.getActivityGraph(), null,
				a -> a instanceof LabelAccess la && la.getAccess() == LabelAccessEnum.READ);

		System.out.println("\nLabel accesses (read):");
		for (ActivityGraphItem item : items2) {
			System.out.println(" - " + item.toString());
		}

		List<LabelAccess> items3 = SoftwareUtil.collectActivityGraphItems(run.getActivityGraph(), null, LabelAccess.class);

		System.out.println("\nLabel accesses:");
		for (LabelAccess item : items3) {
			System.out.println(" - " + item.eClass().getName() + " " + item.getData().getName());
		}


		List<LabelAccess> items4 = SoftwareUtil.collectActivityGraphItems(
				run.getActivityGraph(),
				null,
				LabelAccess.class,
				CollectorExample::isWritingLabelAccess);

		System.out.println("\nLabel accesses (write):");
		for (LabelAccess item : items4) {
			System.out.println(" - " + item.eClass().getName() + " " + item.getData().getName());
		}

		System.out.println("\ndone.");

	}

	private static boolean isWritingLabelAccess(LabelAccess access) {
		return access.getAccess() == LabelAccessEnum.WRITE;
	}

	private static LabelAccess addNewLabelAccess(SWModel sw, Runnable r, String labelName, LabelAccessEnum rw) {
		Label l = addNewLabel(sw, labelName);

		LabelAccess la = AmaltheaFactory.eINSTANCE.createLabelAccess();
		la.setData(l);
		la.setAccess(rw);
		r.getRunnableItems().add(la);

		return la;
	}

	private static Label addNewLabel(SWModel sw, String labelName) {
		Label lab = AmaltheaFactory.eINSTANCE.createLabel();
		lab.setName(labelName);
		sw.getLabels().add(lab);

		return lab;
	}

	private static RunnableCall addNewRunnableCall(SWModel sw, Runnable r, String runnableName) {
		Runnable r2 = addNewRunnable(sw, runnableName);

		RunnableCall rc = AmaltheaFactory.eINSTANCE.createRunnableCall();
		rc.setRunnable(r2);
		r.getRunnableItems().add(rc);

		return rc;
	}

	private static Runnable addNewRunnable(SWModel sw, String runnableName) {
		Runnable run = AmaltheaFactory.eINSTANCE.createRunnable();
		run.setName(runnableName);
		sw.getRunnables().add(run);

		return run;
	}

}

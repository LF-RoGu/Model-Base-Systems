/**
 ********************************************************************************
 * Copyright (c) 2018-2022 Robert Bosch GmbH.
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
import java.io.IOException;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.AmaltheaPackage;
import org.eclipse.app4mc.amalthea.model.emf.AmaltheaResourceFactory;
import org.eclipse.app4mc.amalthea.model.emf.AmaltheaResourceSetImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

//@SuppressWarnings("java:S106") // Example code - Use of standard outputs is intended
public class SplitModelExample {

	public static void main(String[] args) {

		final String inputName = "model-input/democar.amxmi";

		System.out.println("------------------------------");
		System.out.println(" Amalthea Split-Model Example ");
		System.out.println("------------------------------");

		// ***** Load *****

		Resource resource = loadFromFileNamed(inputName);
		if (resource == null) return;

		final ResourceSet resourceSet = resource.getResourceSet();

		try {
			resource.load(null);
		} catch (IOException e) {
			// ignore
		}

		Amalthea inputModel = null;
		for (final EObject content : resource.getContents()) {
			if (content instanceof Amalthea) {
				inputModel = (Amalthea) content;
			}
		}

		if (inputModel == null) {
			System.err.println("Error: No model loaded!");
			return;
		}

		// ***** Modify and save *****

		Amalthea mCommon		= createNewModelWithResource(resourceSet, "model-output/SplitModel/democar-common.amxmi");
		Amalthea mHardware		= createNewModelWithResource(resourceSet, "model-output/SplitModel/democar-hw.amxmi");
		Amalthea mSoftware		= createNewModelWithResource(resourceSet, "model-output/SplitModel/democar-sw.amxmi");
		Amalthea mOS			= createNewModelWithResource(resourceSet, "model-output/SplitModel/democar-os.amxmi");
		Amalthea mStimuli		= createNewModelWithResource(resourceSet, "model-output/SplitModel/democar-stimuli.amxmi");
		Amalthea mEvents		= createNewModelWithResource(resourceSet, "model-output/SplitModel/democar-events.amxmi");
		Amalthea mConstraints	= createNewModelWithResource(resourceSet, "model-output/SplitModel/democar-constraints.amxmi");
		Amalthea mMapping		= createNewModelWithResource(resourceSet, "model-output/SplitModel/democar-mapping.amxmi");
		Amalthea mComponents	= createNewModelWithResource(resourceSet, "model-output/SplitModel/democar-components.amxmi");

		if (mCommon != null) {
			mCommon.setCommonElements(inputModel.getCommonElements());
			inputModel.setCommonElements(null);
			if (mCommon.getCommonElements() != null) {
				writeResource(mCommon.eResource());				
			}
		}

		if (mHardware != null) {
			mHardware.setHwModel(inputModel.getHwModel());
			inputModel.setHwModel(null);
			if (mHardware.getHwModel() != null) {
				writeResource(mHardware.eResource());				
			}
		}

		if (mSoftware != null) {
			mSoftware.setSwModel(inputModel.getSwModel());
			inputModel.setSwModel(null);
			if (mSoftware.getSwModel() != null) {
				writeResource(mSoftware.eResource());				
			}
		}

		if (mOS != null) {
			mOS.setOsModel(inputModel.getOsModel());
			inputModel.setOsModel(null);
			if (mOS.getOsModel() != null) {
				writeResource(mOS.eResource());				
			}
		}

		if (mStimuli != null) {
			mStimuli.setStimuliModel(inputModel.getStimuliModel());
			inputModel.setStimuliModel(null);
			if (mStimuli.getStimuliModel() != null) {
				writeResource(mStimuli.eResource());				
			}
		}

		if (mEvents != null) {
			mEvents.setEventModel(inputModel.getEventModel());
			inputModel.setEventModel(null);
			if (mEvents.getEventModel() != null) {
				writeResource(mEvents.eResource());				
			}
		}

		if (mConstraints != null) {
			mConstraints.setConstraintsModel(inputModel.getConstraintsModel());
			inputModel.setConstraintsModel(null);
			if (mConstraints.getConstraintsModel() != null) {
				writeResource(mConstraints.eResource());				
			}
		}

		if (mMapping != null) {
			mMapping.setMappingModel(inputModel.getMappingModel());
			inputModel.setMappingModel(null);
			if (mMapping.getMappingModel() != null) {
				writeResource(mMapping.eResource());				
			}
		}

		if (mComponents != null) {
			mComponents.setComponentsModel(inputModel.getComponentsModel());
			inputModel.setComponentsModel(null);
			if (mComponents.getComponentsModel() != null) {
				writeResource(mComponents.eResource());				
			}
		}

		System.out.println("\ndone.");
	}

	private static Resource loadFromFileNamed(String pathname) {

		final File file = new File(pathname);
		final URI uri = URI.createFileURI(file.getAbsolutePath());
		if (uri == null) return null;

		System.out.println("reading file: " + uri.toString());

		final ResourceSet resSet = initializeResourceSet();
		final Resource res = resSet.createResource(uri);

		return res;
	}

	private static Amalthea createNewModelWithResource(ResourceSet resSet, String fileName) {

		Amalthea model = AmaltheaFactory.eINSTANCE.createAmalthea();

		final File file = new File(fileName);
		final URI uri = URI.createFileURI(file.getAbsolutePath());
		if (uri == null) return null;

		final Resource resource = resSet.createResource(uri);
		resource.getContents().add(model);

		return model;
	}

	private static boolean writeResource(Resource res) {

		System.out.println("writing file: " + res.getURI().path());

		try {
			res.save(null);
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	private static ResourceSet initializeResourceSet() {
		AmaltheaPackage.eINSTANCE.eClass(); // register the package
		final ResourceSet resSet = new AmaltheaResourceSetImpl();
		resSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("amxmi", new AmaltheaResourceFactory());

		return resSet;
	}

}

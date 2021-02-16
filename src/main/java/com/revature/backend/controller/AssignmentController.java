package com.revature.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.revature.backend.model.Associate;
import com.revature.backend.model.AssociateStatus;
import com.revature.backend.model.Batch;
import com.revature.backend.model.api.ApiAssociateAssignment;
import com.revature.backend.model.api.ApiAssociateTemplate;
import com.revature.backend.model.api.ApiBatchTemplate;
import com.revature.backend.service.ManagerBalancer;
import com.revature.backend.service.ManagerService;
import com.revature.backend.util.BatchRetriever;
import com.revature.backend.util.BatchWriter;
import com.revature.backend.util.StagingListener;
@Component
public class AssignmentController {

	
	private static Logger log = Logger.getLogger(AssignmentController.class);
	
	private ManagerBalancer balancer;
	
	private ManagerService managerService;
	
	private BatchWriter batchWriter;
	
	private BatchRetriever batchRetriever;
	
	public AssignmentController(ManagerBalancer balancer, 
								ManagerService managerService, 
								BatchWriter batchWriter, 
								BatchRetriever batchRetriever) {
		this.balancer = balancer;
		this.batchRetriever = batchRetriever;
		this.batchWriter = batchWriter;
		this.batchRetriever = batchRetriever;
	}

	/**
	 * This method runs after the {@link StagingListener} detects new batches.
	 * Converts Associates from the raw API object into database ready objects.
	 * Sends new Associate list to the {@link ManagerBalancer} for assignment.
	 * {@link Batch} & {@link Associate} are saved to the database using the {@link BatchWriter}
	 */
	public void addNewBatches() {
		log.info("Adding batches");
		List<Batch> convertedBatches = new ArrayList<>();
		List<Associate> convertedAssociates = new ArrayList<>();
		// Retrieve batches from BatchRetriever
		List<ApiBatchTemplate> batches = batchRetriever.retrieveNewlyStagingBatches();
		for (ApiBatchTemplate batch : batches) {
			// Convert batch to Batch Object & add to a list
			Batch b = convertToBatch(batch);
			convertedBatches.add(b);

			// Convert each Associate to an Associate Object, assign them to the batch, give
			// them a status, & add them to a list
			for (ApiAssociateAssignment assignment : batch.getAssociateAssignments()) {
				Associate a = convertToAssociate(assignment.getAssociate());
				a.setBatch(b);
				a.setStatus(AssociateStatus.STAGING);
				convertedAssociates.add(a);
			}

		}
		log.info("batches found:" + convertedBatches.size());
		log.info("associates found:" + convertedAssociates.size());

		if (convertedAssociates.size() == 0 || convertedBatches.size() == 0) {
			// If either size is 0, then the data retrieval failed and should be attempted
			// again.
			log.error("No batches with associates found");

		} else {
			// Balance out associates to each manager, by batch size.
			
			batchWriter.writeNewlyStagingBatches(convertedBatches);
			balancer.balanceNewBatches(managerService.getAllManagersAndAssociates(), convertedAssociates);

			batchWriter.writeNewlyStagingAssociates(convertedAssociates);
			
		}

	}

	// ----------------
	// Helper Methods
	// ----------------

	/**
	 * Converts an {@link ApiBatchTemplate} to a {@link Batch}
	 * 
	 * @param batch
	 * @return {@link Batch}
	 */
	public Batch convertToBatch(ApiBatchTemplate batch) {
		return new Batch(batch.getBatchId(), batch.getName(), batch.getSkill(), batch.getLocation());
	}

	/**
	 * Converts an {@link ApiAssociateTemplate} to an {@link Associate}
	 * 
	 * @param associate
	 * @return {@link Associate}
	 */
	public Associate convertToAssociate(ApiAssociateTemplate associate) {
		return new Associate(associate.getSalesforceId(), associate.getEmail(), associate.getFirstName(),
				associate.getLastName(), null, null, null);
	}

}

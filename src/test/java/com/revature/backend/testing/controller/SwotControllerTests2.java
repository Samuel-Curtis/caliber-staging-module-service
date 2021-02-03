package com.revature.backend.testing.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

/*
 *  created a new test suite to take advantage of spring boot's testing capabilities for sprint 2 tests
 */

import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.revature.backend.controller.SwotController;
import com.revature.backend.model.AnalysisItem;
import com.revature.backend.model.AnalysisType;
import com.revature.backend.model.Associate;
import com.revature.backend.model.Manager;
import com.revature.backend.model.Swot;
import com.revature.backend.service.SwotService;

@SpringBootTest
public class SwotControllerTests2 {
	
	@Mock
	SwotService swotServ;
	
	@InjectMocks
	SwotController swotCon;
	
	private Swot mockSwot1;
	private Swot mockSwot2;
	
	AnalysisItem mockAI1;
	AnalysisItem mockAI2;
	AnalysisItem mockAI3;
	AnalysisItem mockAI4;
	
	List<Swot> swots;
	
	List<AnalysisItem> mockAnalysisItems;
	
	private AnalysisItem item;
	
	private MockMvc mock;
	
	@BeforeEach
	public void setUp() throws Exception {
		mock = MockMvcBuilders.standaloneSetup(swotCon).build();
		mockAnalysisItems = new ArrayList<AnalysisItem>();
		
		mockAI1 = new AnalysisItem(1, "Strength", null, AnalysisType.STRENGTH, "Strength comment");
		mockAI2 = new AnalysisItem(2, "Weakness", null, AnalysisType.WEAKNESS, "Weakness comment");
		mockAI3 = new AnalysisItem(3, "Opportunity", null, AnalysisType.OPPORTUNITY, "Opportunity comment");
		mockAI4 = new AnalysisItem(4, "Threat", null, AnalysisType.THREAT, "Threat comment");
		
		mockAnalysisItems.add(mockAI1);
		mockAnalysisItems.add(mockAI2);
		mockAnalysisItems.add(mockAI3);
		mockAnalysisItems.add(mockAI4);
		
		mockSwot1 = new Swot(1, 
					new Associate(1, null, null, null, null, null, null, null), 
					new Manager(1, null, null, null), 
					new Timestamp(System.currentTimeMillis()), 
					new Timestamp(System.currentTimeMillis()));
		mockSwot1.setAnalysisItems(mockAnalysisItems);
		mockSwot2 = new Swot(2, 
					new Associate(1, null, null, null, null, null, null, null), 
					new Manager(1, null, null, null), 
					new Timestamp(System.currentTimeMillis()), 
					new Timestamp(System.currentTimeMillis()));
		mockSwot2.setAnalysisItems(mockAnalysisItems);
		
		swots = new ArrayList<>();
		swots.add(mockSwot1);
		swots.add(mockSwot2);
		
		when(swotServ.retrieveAllSwotByAssociateId(1)).thenReturn(swots);
	}
	
	/*
	 * Verify that a basic get request pulls the new comment field for all the swots/analysisitems
	 * 
	 */
	
	@Test
	public void getSwotItemCommentSuccess() throws Exception {
		this.mock.perform(get("/swot/view/{associateId}", swots.get(1).getAssociate().getId()))
		.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$[0].analysisItems[0].comment", is(mockAI1.getComment())))
		.andExpect(jsonPath("$[0].analysisItems[1].comment", is(mockAI2.getComment())))
		.andExpect(jsonPath("$[0].analysisItems[2].comment", is(mockAI3.getComment())))
		.andExpect(jsonPath("$[0].analysisItems[3].comment", is(mockAI4.getComment())))
		.andExpect(jsonPath("$[1].analysisItems[0].comment", is(mockAI1.getComment())))
		.andExpect(jsonPath("$[1].analysisItems[1].comment", is(mockAI2.getComment())))
		.andExpect(jsonPath("$[1].analysisItems[2].comment", is(mockAI3.getComment())))
		.andExpect(jsonPath("$[1].analysisItems[3].comment", is(mockAI4.getComment())));

	}
}

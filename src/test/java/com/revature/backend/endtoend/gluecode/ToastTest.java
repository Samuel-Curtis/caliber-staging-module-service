package com.revature.backend.endtoend.gluecode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;

import com.revature.backend.endtoend.page.HomePage;
import com.revature.backend.endtoend.page.LoginPage;
import com.revature.backend.endtoend.page.ViewPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ToastTest {

	public HomePage homePage;
	public LoginPage loginPage;
	public ViewPage viewPage;

	// Scenario 1
	@Given("User is at the login page")
	public void user_is_at_the_login_page() {
		DriverUtility.driver.manage().window().maximize();
		this.loginPage = new LoginPage(DriverUtility.driver);
		assertEquals(DriverUtility.driver.getCurrentUrl(), "http://localhost:4200/login");
	}

	@When("user inputs their email <{string}>")
	public void user_inputs_their_email(String string) {
		this.loginPage.setEmail(string);
	}

	@When("user inputs their password <{string}>")
	public void user_inputs_their_password(String string) {
		this.loginPage.setPassword(string);
	}

	@When("user clicks log in")
	public void user_clicks_log_in() throws InterruptedException {
		TimeUnit.SECONDS.sleep(3);
		this.loginPage.clickLogin();
	}

	@When("user clicks create swot")
	public void user_clicks_create_swot() throws InterruptedException {
		TimeUnit.SECONDS.sleep(3);
		
		DriverUtility.driver.manage().window().maximize();
		this.homePage = new HomePage(DriverUtility.driver);
		assertEquals(DriverUtility.driver.getCurrentUrl(), "http://localhost:4200/home");
		
		this.homePage.clickCreateSwotForAssociate();
	}

	@When("user inputs swot title {string}")
	public void user_inputs_swot_title(String string) throws InterruptedException {
		TimeUnit.SECONDS.sleep(3);
		this.homePage.enterContentField(string);
	}

	@When("user inputs swot item name {string}")
	public void user_inputs_swot_item_name(String string) {
		this.homePage.enterDescriptionField(string);
	}

	@When("user selects swot type {string}")
	public void user_selects_swot_type(String string) {
		this.homePage.selectSwotType(string);
	}

	@When("user inputs swot note {string}")
	public void user_inputs_swot_note(String string) {
		this.homePage.enterSwotNote(string);
	}

	@When("user clicks add item")
	public void user_clicks_add_item() {
		this.homePage.addSwotItem();
		this.homePage.submitSwotItems();
	}

	@When("user clicks view swots")
	public void user_clicks_view_swots() throws InterruptedException {
		TimeUnit.SECONDS.sleep(3);
		this.homePage.clickViewSwotForAssociate(1);
	}

	@When("user clicks add new item")
	public void user_clicks_add_new_item() throws InterruptedException {
		TimeUnit.SECONDS.sleep(3);
		DriverUtility.driver.manage().window().maximize();
		this.viewPage = new ViewPage(DriverUtility.driver);
		assertEquals(DriverUtility.driver.getCurrentUrl(), "http://localhost:4200/view/1");
	
		this.viewPage.clickAddItemStrengthButton();
	}

	@When("user selects type {string}")
	public void user_selects_type(String string) throws InterruptedException {
		TimeUnit.SECONDS.sleep(3);
		this.viewPage.selectUpdateButtonSwotItemType(string);
	}

	@When("user submits item name {string}")
	public void user_submits_item_name(String string) {
		this.viewPage.setUpdateButtonSwotName(string);
	}

	@When("user inputs note {string}")
	public void user_inputs_note(String string) {
		this.viewPage.setUpdateButtonSwotNote(string);
	}

	@Then("user can click add new item again without clearing the message")
	public void user_can_click_add_new_item_again_without_clearing_the_message() throws InterruptedException {
		TimeUnit.SECONDS.sleep(1);
		this.viewPage.clickAddItemStrengthButton();
	}

	// Scenario 2
	@When("user clicks update for recently created item")
	public void user_clicks_update_for_recently_created_item() {
		this.viewPage.clickSpecificItemAddItemStrengthButton(1);
	}

	@When("user changes item type {string}")
	public void user_changes_item_type(String string) throws InterruptedException {
		TimeUnit.SECONDS.sleep(3);
		this.viewPage.selectUpdateButtonSwotItemType(string);
	}

	@When("user clicks update swot button")
	public void user_clicks_update_swot_button() {
		this.viewPage.clickUpdateButtonIndividualSwot();
	}

	@When("user clicks delete for the same item")
	public void user_clicks_delete_for_the_same_item() {
		this.viewPage.clickSpecificItemAddItemStrengthButton(1);
		// wait condition
		this.viewPage.clickDeleteButtonIndividualSwot();
	}
	
	/** THEN CONDITION NOTE
	 This test was supposed to reflect 3 messages occupying the screen
	 at the same time. This condition can be changed if that is 
	 something Selenium can't do
	 **/
	@Then("the three messages should occupy separate locations on the screen")
	public void the_three_messages_should_occupy_separate_locations_on_the_screen() {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
		
		// Is there a way to make sure pixels aren't on top of one another
		
	}

	// Scenario 3
	@When("user selects type two {string}")
	public void user_selects_type_two(String string) {
		this.viewPage.selectUpdateButtonSwotItemType(string);
	}

	@When("user submits item name three {string}")
	public void user_submits_item_name_three(String string) {
		this.viewPage.selectUpdateButtonSwotItemType(string);
	}
	/** THEN CONDITION NOTE
	 This condition should find 4 swot messages on the screen by returning
	 Whether or not the Toast Message array contains 4 elements. The messages
	 might disappear too quickly for this condition to be tested, so the 
	 number can be deceased from 4 to 2
	**/
	@Then("the user should have four toast messages")
	public void the_user_should_have_four_toast_messages() {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
		
		// condition to find 4 different swot messages
	}

}

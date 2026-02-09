package guiAdminHome;

import database.Database;

/*******
 * <p> Title: GUIAdminHomePage Class. </p>
 * 
 * <p> Description: The Java/FX-based Admin Home Page.  This class provides the controller actions
 * basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page contains a number of buttons that have not yet been implemented.  WHen those buttons
 * are pressed, an alert pops up to tell the user that the function associated with the button has
 * not been implemented. Also, be aware that What has been implemented may not work the way the
 * final product requires and there maybe defects in this code.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-17 Initial version
 * @version 1.01		2025-09-16 Update Javadoc documentation *  
 */

public class ControllerAdminHome {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/
	
	/**
	 * Default constructor is not used.
	 */
	public ControllerAdminHome() {
	}
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**********
	 * <p> 
	 * 
	 * Title: performInvitation () Method. </p>
	 * 
	 * <p> Description: Protected method to send an email inviting a potential user to establish
	 * an account and a specific role. </p>
	 */
	protected static void performInvitation () {
		// Verify that the email address is valid - If not alert the user and return
		String emailAddress = ViewAdminHome.text_InvitationEmailAddress.getText();
		if (invalidEmailAddress(emailAddress)) {
			return;
		}
		
		// Check to ensure that we are not sending a second message with a new invitation code to
		// the same email address.  
		if (theDatabase.emailaddressHasBeenUsed(emailAddress)) {
			ViewAdminHome.alertEmailError.setContentText(
					"An invitation has already been sent to this email address.");
			ViewAdminHome.alertEmailError.showAndWait();
			return;
		}
		
		// Inform the user that the invitation has been sent and display the invitation code
		String theSelectedRole = (String) ViewAdminHome.combobox_SelectRole.getValue();
		String invitationCode = theDatabase.generateInvitationCode(emailAddress,
				theSelectedRole);
		String msg = "Code: " + invitationCode + " for role " + theSelectedRole + 
				" was sent to: " + emailAddress;
		System.out.println(msg);
		ViewAdminHome.alertEmailSent.setContentText(msg);
		ViewAdminHome.alertEmailSent.showAndWait();
		
		// Update the Admin Home pages status
		ViewAdminHome.text_InvitationEmailAddress.setText("");
		ViewAdminHome.label_NumberOfInvitations.setText("Number of outstanding invitations: " + 
				theDatabase.getNumberOfInvitations());
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: manageInvitations () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void manageInvitations () {
		System.out.println("\n*** WARNING ***: Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.setTitle("*** WARNING ***");
		ViewAdminHome.alertNotImplemented.setHeaderText("Manage Invitations Issue");
		ViewAdminHome.alertNotImplemented.setContentText("Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.showAndWait();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: setOnetimePassword () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void setOnetimePassword () {
		System.out.println("\n*** WARNING ***: One-Time Password Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.setTitle("*** WARNING ***");
		ViewAdminHome.alertNotImplemented.setHeaderText("One-Time Password Issue");
		ViewAdminHome.alertNotImplemented.setContentText("One-Time Password Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.showAndWait();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: deleteUser () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void deleteUser() {

		    // Defensive check: only admins should be here
		    if (ViewAdminHome.theUser == null || !ViewAdminHome.theUser.getAdminRole()) {
		        ViewAdminHome.alertNotImplemented.setTitle("*** WARNING ***");
		        ViewAdminHome.alertNotImplemented.setHeaderText("Delete User Issue");
		        ViewAdminHome.alertNotImplemented.setContentText("Only an admin can delete users.");
		        ViewAdminHome.alertNotImplemented.showAndWait();
		        return;
		    }

		    // 1) Ask for username (dialog)
		    TextInputDialog input = new TextInputDialog();
		    input.setTitle("Delete a User");
		    input.setHeaderText("Enter the username to remove access");
		    input.setContentText("Username:");

		    Optional<String> entered = input.showAndWait();
		    if (entered.isEmpty()) return; // user cancelled

		    String targetUsername = entered.get().trim();
		    if (targetUsername.isEmpty()) {
		        Alert a = new Alert(AlertType.INFORMATION);
		        a.setTitle("Delete a User");
		        a.setHeaderText("Delete User Issue");
		        a.setContentText("You must enter a username.");
		        a.showAndWait();
		        return;
		    }

		    // 2) Admin cannot delete themself
		    String currentAdminUsername = ViewAdminHome.theUser.getUserName();
		    if (targetUsername.equalsIgnoreCase(currentAdminUsername)) {
		        Alert a = new Alert(AlertType.INFORMATION);
		        a.setTitle("Delete a User");
		        a.setHeaderText("Not Allowed");
		        a.setContentText("An admin is not allowed to remove that admin’s access.");
		        a.showAndWait();
		        return;
		    }

		    // 3) Must exist
		    if (!theDatabase.doesUserExist(targetUsername)) {
		        Alert a = new Alert(AlertType.INFORMATION);
		        a.setTitle("Delete a User");
		        a.setHeaderText("User Not Found");
		        a.setContentText("No user account exists with username: " + targetUsername);
		        a.showAndWait();
		        return;
		    }

		    // 4) Confirmation: must answer "Yes"
		    Alert confirm = new Alert(AlertType.CONFIRMATION);
		    confirm.setTitle("Delete a User");
		    confirm.setHeaderText("Are you sure?");
		    confirm.setContentText("Remove access for user '" + targetUsername + "'?\n\n"
		            + "Click Yes to remove access.");

		    confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

		    Optional<ButtonType> choice = confirm.showAndWait();
		    if (choice.isEmpty() || choice.get() != ButtonType.YES) return;

		    // 5) Remove access in DB


		    //  HARD DELETE:
		    boolean ok = theDatabase.deleteUserAccount(targetUsername);

		   

		    // 6) Result + update UI counts
		    Alert result = new Alert(AlertType.INFORMATION);
		    result.setTitle("Delete a User");

		    if (!ok) {
		        result.setHeaderText("Delete Failed");
		        result.setContentText("Unable to remove access for '" + targetUsername + "'.");
		        result.showAndWait();
		        return;
		    }

		    // Update the count label
		    ViewAdminHome.label_NumberOfUsers.setText("Number of users: " + theDatabase.getNumberOfUsers());

		    result.setHeaderText("User Removed");
		    result.setContentText("Access removed for user '" + targetUsername + "'.");
		    result.showAndWait();
		}
	
	/**********
	 * <p> 
	 * 
	 * Title: listUsers () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void listUsers() {			// Lists the current users in the database along with their username, name, email, and role.

	    String userDetails = theDatabase.listUsersDetails();

	    if (userDetails == null || userDetails.isBlank()) {
	        ViewAdminHome.alertNotImplemented.setTitle("List Users");
	        ViewAdminHome.alertNotImplemented.setHeaderText("No Users Found");
	        ViewAdminHome.alertNotImplemented.setContentText(
	                "There are currently no users in the system.");
	        ViewAdminHome.alertNotImplemented.showAndWait();
	        return;
	    }

	    ViewAdminHome.alertNotImplemented.setTitle("List Users");
	    ViewAdminHome.alertNotImplemented.setHeaderText("User Accounts");
	    ViewAdminHome.alertNotImplemented.setContentText(userDetails);
	    ViewAdminHome.alertNotImplemented.showAndWait();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: addRemoveRoles () Method. </p>
	 * 
	 * <p> Description: Protected method that allows an admin to add and remove roles for any of
	 * the users currently in the system.  This is done by invoking the AddRemoveRoles Page. There
	 * is no need to specify the home page for the return as this can only be initiated by and
	 * Admin.</p>
	 */
	protected static void addRemoveRoles() {
		guiAddRemoveRoles.ViewAddRemoveRoles.displayAddRemoveRoles(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: invalidEmailAddress () Method. </p>
	 * 
	 * <p> Description: Protected method that is intended to check an email address before it is
	 * used to reduce errors.  The code currently only checks to see that the email address is not
	 * empty.  In the future, a syntactic check must be performed and maybe there is a way to check
	 * if a properly email address is active.</p>
	 * 
	 * @param emailAddress	This String holds what is expected to be an email address
	 */
	protected static boolean invalidEmailAddress(String emailAddress) {
		if (emailAddress.length() == 0) {
			ViewAdminHome.alertEmailError.setContentText(
					"Correct the email address and try again.");
			ViewAdminHome.alertEmailError.showAndWait();
			return true;
		}
		return false;
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performLogout () Method. </p>
	 * 
	 * <p> Description: Protected method that logs this user out of the system and returns to the
	 * login page for future use.</p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewAdminHome.theStage);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performQuit () Method. </p>
	 * 
	 * <p> Description: Protected method that gracefully terminates the execution of the program.
	 * </p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}

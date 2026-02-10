package guiUserUpdate;

import entityClasses.User;
import javafx.stage.Stage;

public class ControllerUserUpdate {
	/*-********************************************************************************************

	The Controller for ViewUserUpdate 
	
	**********************************************************************************************/

	/**********
	 * <p> Title: ControllerUserUpdate Class</p>
	 * 
	 * <p> Description: This static class supports the actions initiated by the ViewUserUpdate
	 * class. In this case, there is just one method, no constructors, and no attributes.</p>
	 *
	 */

	/*-********************************************************************************************

	The User Interface Actions for this page
	
	**********************************************************************************************/

	
	/**********
	 * <p> Method: public goToUserHomePage(Stage theStage, User theUser) </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the button to
	 * proceed to the user's home page.
	 * 
	 * @param theStage specifies the JavaFX Stage for next next GUI page and it's methods
	 * 
	 * @param theUser specifies the user so we go to the right page and so the right information
	 */
	private static boolean forceRelogin = false;
	
	public static void requireReloginAfterUpdate() {
		forceRelogin = true;
	}
	protected static void goToUserHomePage(Stage theStage, User theUser) {
		
		// Get the roles the user selected during login
		int theRole = applicationMain.FoundationsMain.activeHomePage;

		// Use that role to proceed to that role's home page
		switch (theRole) {
		case 1:
			guiAdminHome.ViewAdminHome.displayAdminHome(theStage, theUser);
			break;
		case 2:
			guiRole1.ViewRole1Home.displayRole1Home(theStage, theUser);
			break;
		case 3:
			guiRole2.ViewRole2Home.displayRole2Home(theStage, theUser);
			break;
		default: 
			System.out.println("*** ERROR *** UserUpdate goToUserHome has an invalid role: " + 
					theRole);
			System.exit(0);
		}
 	}
	public static void doUpdatePassword(Stage theStage, User theUser) {
		 TextInputDialog dialog = new TextInputDialog();
	        dialog.setTitle("Update Password");
	        dialog.setHeaderText("Enter a new password");
	        dialog.setContentText("New password:");

	        var result = dialog.showAndWait();
	        if (result.isEmpty()) return;

	        String newPassword = result.get().trim();
	        if (newPassword.isEmpty()) return;

	        applicationMain.FoundationsMain.database.updatePassword(
	                theUser.getUserName(), newPassword);

	        // Clear one-time password after successful update
	        applicationMain.FoundationsMain.database
	                .clearOneTimePassword(theUser.getUserName());

	        Alert info = new Alert(Alert.AlertType.INFORMATION);
	        info.setContentText("Password updated. Please log in again.");
	        info.showAndWait();
	        guiUserLogin.ViewUserLogin.displayUserLogin(theStage);
	}
}

}

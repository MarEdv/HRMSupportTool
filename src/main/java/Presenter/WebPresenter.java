package Presenter;

import Model.UserModel;
import View.LoginUI.LoginWindow;

/**
 * Created by Hatem Houssein on 04-May-16.
 * A class that acts as the presenter for the Login window according to the MVP (Model-View-Presenter) pattern.
 */
public class WebPresenter {

    private LoginWindow loginWindow;
    private UserModel loginDb;

    public WebPresenter(LoginWindow currentWindow){
        loginWindow = currentWindow;
    }

    public void loginEmployee(String username, String password){
        loginDb = new UserModel();

        //If the login credentials are correct
        if(loginDb.findUser(username, password) != null){
            loginWindow.showDatabaseSelectionWindow();
        }
        //If the login credentials are incorrect
        else{
            loginWindow.showLoginErrorMessage();
        }
    }
}

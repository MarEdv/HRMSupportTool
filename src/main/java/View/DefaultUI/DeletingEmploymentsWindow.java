package View.DefaultUI;

import Presenter.DeletingEmploymentsPresenter;
import View.DatabaseSelection.DatabaseSelectionWindow;
import View.LogUI.LogGrid;
import View.LoginUI.LoginWindow;
import View.UserUI.UsersWindow;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import View.Buttons.LogoutOption;

import java.io.IOException;
/**
 * Created by Abeer on 5/10/2016.
 * DeletingEmploymentWindow show the main window for deleting employment, show employment info,
 * add  new User  for admin
 * edition and adding employment is only enabled for admin user.
 */
@SpringView(name = DeletingEmploymentsWindow.VIEW_NAME)
public class DeletingEmploymentsWindow extends VerticalLayout implements View {
    public static final String VIEW_NAME = "default";

    private HorizontalLayout buttonsLayout;
    private Panel panel;
    private LogoutOption logoutHLayout;

    private Button viewlogButton;
    private Button viewEmploymentsButton;
    private Button viewDatabaseSelectionButton;
    private Button addNewUser;

    private boolean isAdmin ;

    private DeletingEmploymentsPresenter employmentsPresenter;

    public DeletingEmploymentsWindow() throws IOException {
        buttonsLayout = new HorizontalLayout();
        panel = new Panel();
        panel.setSizeFull();

        // adding view buttons to the window
        viewlogButton = new Button("View Log");
        viewEmploymentsButton = new Button("View Current Employments");
        viewDatabaseSelectionButton = new Button("View Database Selection");
        addNewUser = new Button("Add New User");


        // adding layout for logout option
        logoutHLayout = new LogoutOption(String.valueOf(UI.getCurrent().getSession().getAttribute("user")));
    }
    private void addWindowComponents (){
       // adding employmentGrid as thr default view
        panel.setContent( new EmploymentsGrid(employmentsPresenter));
        viewEmploymentsButton.setVisible(false);
        viewDatabaseSelectionButton.setVisible(true);
        addNewUser.setVisible(isAdmin);

        viewDatabaseSelectionButton.addClickListener(e -> {
            getUI().getNavigator().navigateTo(DatabaseSelectionWindow.VIEW_NAME);
        });
        addNewUser.addClickListener(e -> {
           getUI().getNavigator().navigateTo(UsersWindow.VIEW_NAME);
        });

        viewlogButton.addClickListener(e -> {
            try {
                panel.setContent( new LogGrid(employmentsPresenter));
                viewlogButton.setVisible(false);
                viewEmploymentsButton.setVisible(true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        viewEmploymentsButton.addClickListener(e -> {
            panel.setContent( new EmploymentsGrid(employmentsPresenter));
            viewlogButton.setVisible(true);
            viewEmploymentsButton.setVisible(false);

        });

        buttonsLayout.addComponents(viewDatabaseSelectionButton,viewlogButton,
                viewEmploymentsButton,addNewUser);

        buttonsLayout.setSpacing(true);
        setSpacing(true);
        setMargin(true);
        addComponents( logoutHLayout, buttonsLayout, panel);
        setComponentAlignment(logoutHLayout, Alignment.BOTTOM_RIGHT);
    }


     // method for generating error notification
    public void ShowErrorNotification(String msg){
        new Notification(msg, Notification.TYPE_ERROR_MESSAGE)
                .show(getUI().getPage());
    }

    public void showSuccessNotification(String msg){
        Notification.show(msg);
    }




    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // checking if the user is logged in and the database is selected before showing the window
         if((getUI().getSession().getAttribute("user")==null)){
             System.out.println("Not connected");
             getUI().getNavigator().navigateTo(LoginWindow.VIEW_NAME);
         } else if ((getUI().getSession().getAttribute("databaseName")==null))
         {
             System.out.println("Connected but no DB selected");
             getUI().getNavigator().navigateTo(DatabaseSelectionWindow.VIEW_NAME);
         } else {
             System.out.println("Connected & DB selected");
             try {

                 isAdmin= (boolean)getUI().getSession().getAttribute("isAdmin");
                 employmentsPresenter = new DeletingEmploymentsPresenter(this);
             } catch (IOException e) {
                 e.printStackTrace();
             }
             addWindowComponents();

         }
    }
}

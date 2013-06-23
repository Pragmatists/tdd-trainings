package tdd.veryHumble;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class SwingEmployeeView extends JPanel implements EmployeeView{

    private static final long serialVersionUID = 7799796459989659995L;

    private JTextField firstNameTextField = new JTextField();
    private JTextField lastNameTextField = new JTextField();
    private JButton saveButton = new JButton("Save");
    
    public SwingEmployeeView(final EmployeePresenter employeePresenter) {
        add(firstNameTextField);
        add(lastNameTextField);
        add(saveButton);
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                employeePresenter.onSave();
            }
        });
    }
    
    @Override
    public String getFirstName() {
        return firstNameTextField.getText();
    }

    @Override
    public String getLastName() {
        return lastNameTextField.getText();
    }

    @Override
    public void setFirstName(String firstName) {
        firstNameTextField.setText(firstName);
    }
    
    @Override
    public void setLastName(String lastName) {
        lastNameTextField.setText(lastName);
    }
    
    @Override
    public void showEmployeeAddedMessage() {
        JOptionPane.showMessageDialog(this, "Employee added");
    }

}

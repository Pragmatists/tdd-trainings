package tdd.lessHumble;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tdd.Employee;


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
                
                employeePresenter.onSave(firstNameTextField.getText(), lastNameTextField.getText());
            }
        });
    }
    
    @Override
    public void showEmployeeAddedMessage() {
        JOptionPane.showMessageDialog(this, "Employee added");
    }

    @Override
    public void populateEmployee(Employee employee) {
        firstNameTextField.setText(employee.getFirstName());
        lastNameTextField.setText(employee.getLastName());
    }
}

package tdd.dependencyBreaking.customerDeletion;


public class CustomerController {

    public void deleteCustomer(Long customerId){
        
        boolean authorized = Permissions.hasPermission("DELETE_CUSTOMER");
        
        if(!authorized){
            throw new AuthorizationException();
        }
        
        // ...
        
    }
    
}

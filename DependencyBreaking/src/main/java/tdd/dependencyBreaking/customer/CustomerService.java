package tdd.dependencyBreaking.customer;

public class CustomerService {

    private CustomerDao dao;
    private AuthorizationService authorization;
    
    public CustomerService(CustomerDao dao, AuthorizationService authorization) {
        this.dao = dao;
        this.authorization = authorization;
    }

    public void deleteCustomer(Long customerId){
        
        if(!authorization.hasPermission(Context.currentUser().getId(), "DELETE_CUSTOMER")){
            throw new AuthorizationException("DELETE_CUSTOMER");
        }
        
        dao.deleteById(customerId);
    }
    
}

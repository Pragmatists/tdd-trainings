package tdd.dependencyBreaking.authentication;

import javax.naming.InitialContext;
import javax.naming.NamingException;


public class AuthenticationService {

    private final UserDao dao;
    
    public AuthenticationService() throws NamingException {
        
        InitialContext context = new InitialContext();
        dao = (UserDao) context.lookup("java:comp/env/UserDao");
    }
    
    public void authenticate(String login, String password) throws AuthenticationException{
        
        User user = dao.findByLoginAndPass(login, password);
        
        if(user == null){
            throw new AuthenticationException("Invalid login or password!");
        }
        
        if(!user.isActive()){
            throw new AuthenticationException("User is blocked!");
        }
    }
}

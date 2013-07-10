package beforeAfter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FizzBuzzServlet extends HttpServlet{

    private static final long serialVersionUID = -5898919511683902372L;

    private final FizzBuzz fizzBuzz = new FizzBuzz();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String number = req.getParameter("number");
        
        String answer = fizzBuzz.answerFor(Integer.valueOf(number));
        
        resp.getWriter().write(answer);
    }
    
}

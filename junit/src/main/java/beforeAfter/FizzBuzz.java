package beforeAfter;

public class FizzBuzz {

    public String answerFor(int number) {
        
        String answer = "";
        
        if(isDivisibleBy(number,3)){
            answer += "Fizz";
        }
        
        if(isDivisibleBy(number, 5)){
            answer += "Buzz";
        }
        
        return answer.isEmpty() ? String.valueOf(number) : answer;
    }

    private boolean isDivisibleBy(int number, int base) {
        return number % base == 0;
    }

}

package tdd.dependencyBreaking.testRunner;

public class TestRunner {

    private FileTestReport report;
    
    public TestRunner(FileTestReport report) {
        this.report = report;
    }
    
    public void run(TestCase test){
        
        try{
            
            test.run();
            report.reportSuccess(test);
            
        } catch(Exception e){
            
            report.reportFailure(test);
        }
    }
}

package tdd.dependencyBreaking.loanCalculator;

import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class LoanCalculator {

    private final double percent;
    
    public LoanCalculator(double percent) {
        this.percent = percent;
    }

    public void calculate(int loan, int months){
        
        if(loan <= 0 || months <= 0){
            JOptionPane.showMessageDialog(null, "Kwota pozyczki oraz liczba miesiecy musi byc wieksza od zera.", "Nieprawidlowe dane", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int rate = (int)Math.ceil(loan/months);
        int[] rates = new int[months + 1];
        int[] remainingLoan = new int[months + 1];
        remainingLoan[0] = loan;
        rates[0] = 0;

        for(int i=1; i<months; i++){
            
            rates[i] = rate + (int)Math.ceil(remainingLoan[i-1] * percent / 12);
            remainingLoan[i] = remainingLoan[i-1] - rate;
            
        }
        rates[months] = remainingLoan[months-1];
        remainingLoan[months] = 0;

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i=0; i <= months; i++) {
            Integer m = Integer.valueOf(i);
            dataset.setValue(remainingLoan[i], "Rata malejąca", m);
        }
        
        JFreeChart chart = 
                ChartFactory.createBarChart3D("Kredyt", "Miesiące", "Pozostało do spłaty", dataset, PlotOrientation.VERTICAL, true, true, false);
        ChartFrame frame=new ChartFrame("Kredyt",chart);
        frame.setVisible(true);
        frame.setSize(800, 600);
        
    }
    
    public static void main(String[] args) {

        LoanCalculator loanCalculator = new LoanCalculator(0.1);
        loanCalculator.calculate(300000, 12);
        
    }
}

package pl.pragmatists.atm;

public class FakeCashDispenser implements CashDispenser {

    private int dispensed;

    public int getDispensed() {
        return dispensed;
    }

    @Override
    public void dispense(int amount) {
        this.dispensed = amount;

    }

}

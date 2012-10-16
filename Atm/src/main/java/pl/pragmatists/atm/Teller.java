package pl.pragmatists.atm;

public class Teller {

    private CashDispenser cashDispenser;
    private Account account;

    public Teller(CashDispenser cashDispenser) {
        this.cashDispenser = cashDispenser;
    }

    public void authenticateAs(Account account) {
        this.account = account;

    }

    public void withdraw(int amount) {
        account.withdraw(amount);
        cashDispenser.dispense(amount);
    }

}

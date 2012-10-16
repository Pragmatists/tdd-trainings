package pl.pragmatists.atm;

public class Account {

    private int balance;

    public Account(int amount) {
        this.balance = amount;
    }

    public int getBalance() {
        return balance;
    }

    public void withdraw(int toWithdraw) {
        balance -= toWithdraw;
    }

}

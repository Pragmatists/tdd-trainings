package pl.pragmatists.atm;

import static org.assertj.core.api.Assertions.assertThat;

public class AtmDsl {

    private Account account;
    private FakeCashDispenser cashDispenser = new FakeCashDispenser();

    public void accountHasBeenCreditedWith(int amount) {
        account = new Account(amount);
    }

    public void withdraw(int amount) {
        Teller teller = new Teller(cashDispenser);
        teller.authenticateAs(account);
        teller.withdraw(amount);
    }

    public void assertDispensed(int amount) {
        assertThat(cashDispenser.getDispensed()).isEqualTo(amount);
    }

    public void assertAccountBalance(int balance) {
        assertThat(account.getBalance()).isEqualTo(balance);
    }

}

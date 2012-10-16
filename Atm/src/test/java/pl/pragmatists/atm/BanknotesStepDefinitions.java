package pl.pragmatists.atm;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import cucumber.runtime.PendingException;
import cucumber.table.DataTable;

public class BanknotesStepDefinitions {
    @Given("^Atm has banknotes:$")
    public void Atm_has_banknotes(DataTable arg1) {
        // Express the Regexp above with the code you wish you had
        // For automatic conversion, change DataTable to List<YourType>
        throw new PendingException();
    }

    @When("^\\$(\\d+) is to be dispensed$")
    public void $_is_to_be_dispensed(int arg1) {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @Then("^following banknotes are dispensed:$")
    public void following_banknotes_are_dispensed(DataTable arg1) {
        // Express the Regexp above with the code you wish you had
        // For automatic conversion, change DataTable to List<YourType>
        throw new PendingException();
    }
}

package pl.pragmatists.atm;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


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

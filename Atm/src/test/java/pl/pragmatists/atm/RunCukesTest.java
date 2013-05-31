package pl.pragmatists.atm;

import org.junit.runner.RunWith;

import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@Cucumber.Options(format = {"html:target/cucumber-html-report"}, strict=false)
//to override those options use command line arg e.g. -Dcucumber.options="--format progress"
//due to https://github.com/cucumber/cucumber-jvm/issues/491 we cannot use pretty formatter
public class RunCukesTest {

}

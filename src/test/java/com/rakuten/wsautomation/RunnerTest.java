package com.rakuten.wsautomation;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
       // plugin = {"pretty", "html:target/cucumber.html"},
        plugin = {"pretty", "json:target/cucumber-report/report.json", "html:target/cucumber.html"},
        features = {"src/test/resources"}
)
public class RunnerTest {
}

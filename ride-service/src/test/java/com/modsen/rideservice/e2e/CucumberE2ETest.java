package com.modsen.rideservice.e2e;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        glue = "com/modsen/rideservice/e2e"
)
public class CucumberE2ETest {

}

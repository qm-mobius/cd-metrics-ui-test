package com.thoughtworks.lean;

import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;
import cucumber.api.junit.Cucumber;
/**
 * Created by qmxie on 5/5/16.
 */


@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/resources/feature","html:target/cucumber","json:target/cucumber.json"})
public class TestRunner {

}
package com.cdjmdev.regex;

import com.fnproject.fn.testing.*;
import com.google.gson.Gson;
import org.junit.*;

import static org.junit.Assert.*;

public class RegexControllerTest {

    @Rule
    public final FnTestingRule testing = FnTestingRule.createDefault();

    @Test
    public void shouldReturnRegex() {

        Gson gson = new Gson();
        RegexController.Query query = new RegexController.Query();

        query.authtoken = "c27afdea0c2d14396b48b533ef61e880";
        query.query = "Give me regex to check the validity of a phone number with dashes";

        testing.givenEvent()
            .withBody(gson.toJson(query))
            .enqueue();

        testing.thenRun(RegexController.class, "handleRequest");

        FnResult result = testing.getOnlyResult();
        System.out.println(result.getBodyAsString());
        assertNotNull(result);
    }
}
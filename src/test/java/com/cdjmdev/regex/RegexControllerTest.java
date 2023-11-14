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

        query.authtoken = "a19cc483fae0a357f68c06add3051272";
        query.query = "Give me regex to check the validity of a phone number with dashes";

        testing.givenEvent()
            .withBody(gson.toJson(query))
            .enqueue();

        testing.thenRun(RegexController.class, "handleRequest");

        FnResult result = testing.getOnlyResult();
        System.out.println(result.getBodyAsString());
        assertNotNull(result);
    }

    @Test
    public void shouldReturnRegex2() {

        Gson gson = new Gson();
        RegexController.Query query = new RegexController.Query();

        query.authtoken = "a19cc483fae0a357f68c06add3051272";
        query.query = "Replace all a with bb";

        testing.givenEvent()
            .withBody(gson.toJson(query))
            .enqueue();

        testing.thenRun(RegexController.class, "handleRequest");

        FnResult result = testing.getOnlyResult();
        System.out.println(result.getBodyAsString());
        assertNotNull(result);
    }
}
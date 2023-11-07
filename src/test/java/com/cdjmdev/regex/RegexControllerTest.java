package com.cdjmdev.regex;

import com.fnproject.fn.testing.*;
import org.junit.*;

import static org.junit.Assert.*;

public class RegexControllerTest {

    @Rule
    public final FnTestingRule testing = FnTestingRule.createDefault();

    @Test
    public void shouldReturnRegex() {
        testing.givenEvent()
            .withBody("Give me regex to check the validity of a phone number with dashes")
            .enqueue();

        testing.thenRun(RegexController.class, "handleRequest");

        FnResult result = testing.getOnlyResult();
        System.out.println(result.getBodyAsString());
        assertNotNull(result);
    }

    @Test
    public void shouldReturnRegex2() {
        testing.givenEvent()
            .withBody("Replace all a with bb")
            .enqueue();

        testing.thenRun(RegexController.class, "handleRequest");

        FnResult result = testing.getOnlyResult();
        System.out.println(result.getBodyAsString());
        assertNotNull(result);
    }
}
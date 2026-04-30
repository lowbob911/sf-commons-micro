package eu.sportsfusion.common.validation.constraints.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Igor Vashyst
 */
public class EmailValidatorTest {

    private EmailValidator validator;

    @BeforeEach
    public void init() {
        validator = new EmailValidator();
        validator.initialize(null);
    }

    @Test
    public void validEmails() {
        List<String> validEmails = Arrays.asList(
                "prettyandsimple@example.com",
                "very.common@example.com",
                "disposable.style.email.with+symbol@example.com",
                "other.email-with-dash@example.com",
                "fully-qualified-domain@example.com",
                "user.name+tag+sorting@example.com",  // (will go to user.name@example.com inbox)
                "x@example.com",                      // (one-letter local-part)
//                "\"very.(),:;<>[]\\\".VERY.\\\"very@\\\\ \\\"very\\\".unusual\"@strange.example.com",
                "example-indeed@strange-example.com",
                "#!$%&'*+-/=?^_`{}|~@example.org",
//                "\"()<>[]:,;@\\\\\\\"!#$%&'-/=?^_`{}| ~.a\"@example.org",
                "example@s.solutions",                // (see the List of Internet top-level domains)
                "Test@test.com"
        );

        for (String email : validEmails) {
            assertTrue(validator.isValid(email, null), "Wrong result for " + email);
        }
    }

    @Test
    public void invalidEmails() {
        List<String> invalidEmails = Arrays.asList(
                "Abc.example.com",                        // (no @ character)
                "A@b@c@example.com",                      // (only one @ is allowed outside quotation marks)
                "a\"b(c)d,e:f;g<h>i[j\\k]l@example.com",  // (none of the special characters in this local-part are allowed outside quotation marks)
                "just\"not\"right@example.com",           // (quoted strings must be dot separated or the only element making up the local-part)
                "this is\"not\\allowed@example.com",      // (spaces, quotes, and backslashes may only exist when within quoted strings and preceded by a backslash)
                "this\\ still\"not\\allowed@example.com", // (even if escaped (preceded by a backslash), spaces, quotes, and backslashes must still be contained by quotes)
//                "1234567890123456789012345678901234567890123456789012345678901234+x@example.com", // (too long)
                "john..doe@example.com",                  // (double dot before @)
                "example@localhost",                      // (sent from localhost)
                "john.doe@example..com",                  // (double dot after @); with caveat: Gmail lets this through, Email address#Local-part the dots altogether
                "\" \"@example.org",                      // (space between the quotes)
                "\"very.unusual.@.unusual.com\"@example.com",
                "Duy",
                " example@example.com",
                "example@example.com ",
                "äo@example.com"
        );

        for (String email : invalidEmails) {
            assertFalse(validator.isValid(email, null), "Wrong result for " + email);
        }
    }
}
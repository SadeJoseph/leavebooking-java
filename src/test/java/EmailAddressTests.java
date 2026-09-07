import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.leavebooking.EmailAddress;

import static org.junit.jupiter.api.Assertions.*;

public class EmailAddressTests {

    private EmailAddress createValidEmailAddress() {
        return new EmailAddress("test@example.com");
    }

    @Test
    @DisplayName("Email addresses are considered the same when their values are the same")
    void test01() {
        EmailAddress emailAddress1 = createValidEmailAddress();
        EmailAddress emailAddress2 = createValidEmailAddress();

        assertEquals(emailAddress1, emailAddress2);
    }

    @Test
    @DisplayName("An email address requires a non-blank value to be valid")
    void test02() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                new EmailAddress("")
        );

        assertEquals(
                EmailAddress.EMAIL_ADDRESS_NOT_EMPTY,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("An email address requires a non-null value to be valid")
    void test03() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                new EmailAddress((String) null)
        );

        assertEquals(
                EmailAddress.EMAIL_ADDRESS_NOT_EMPTY,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("An email address can be copied from an existing email address")
    void test04() {
        EmailAddress original = createValidEmailAddress();

        EmailAddress copy = new EmailAddress(original);

        assertEquals(original, copy);
    }

    @Test
    @DisplayName("A null email address cannot be copied")
    void test05() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                new EmailAddress((EmailAddress) null)
        );

        assertEquals(
                EmailAddress.EMAIL_ADDRESS_NOT_NULL,
                exception.getMessage()
        );
    }
}
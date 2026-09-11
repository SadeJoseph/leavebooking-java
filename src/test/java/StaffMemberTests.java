import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.leavebooking.common.FullName;
import com.example.leavebooking.common.Identity;
import com.example.leavebooking.staffmanagement.domain.EmailAddress;
import com.example.leavebooking.staffmanagement.domain.StaffMember;

import static org.junit.jupiter.api.Assertions.*;

public class StaffMemberTests {

    private Identity<StaffMember> identity;
    private FullName fullName;
    private EmailAddress emailAddress;

    @BeforeEach
    void setUp() { // create some stable data for testing.
        identity = Identity.of("12345678-1234-1234-1234-123456789012");
        fullName = new FullName("first", "surname");
        emailAddress = new EmailAddress("first.surname@example.com");
    }

    private StaffMember createValidStaffMember() {
        return new StaffMember(identity, fullName, emailAddress);
    }

    @Test
    @DisplayName("You can create a StaffMember when all arguments are valid")
    void test01() {
        assertDoesNotThrow(() ->
                new StaffMember(identity, fullName, emailAddress)
        );
    }

    @Test
    @DisplayName("You cannot create a StaffMember if the id is null")
    void test02() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                new StaffMember(null, fullName, emailAddress)
        );

        assertEquals(
                StaffMember.IDENTITY_CANNOT_BE_NULL,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("You cannot create a StaffMember if the full name is null")
    void test03() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                new StaffMember(identity, null, emailAddress)
        );

        assertEquals(
                StaffMember.FULL_NAME_CANNOT_BE_NULL,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("You cannot create a StaffMember if the email address is null")
    void test04() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                new StaffMember(identity, fullName, null)
        );

        assertEquals(
                StaffMember.EMAIL_ADDRESS_CANNOT_BE_NULL,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("You can update a full name if that new full name is valid")
    void test05() {
        StaffMember staffMember = createValidStaffMember();
        FullName newFullName = new FullName("first2", "surname2");

        assertDoesNotThrow(() ->
                staffMember.updateFullName(newFullName)
        );
    }

    @Test
    @DisplayName("You cannot change the full name if that new full name is null")
    void test06() {
        StaffMember staffMember = createValidStaffMember();

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                staffMember.updateFullName(null)
        );

        assertEquals(
                StaffMember.FULL_NAME_CANNOT_BE_NULL,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("You can change an email address if that new email address is valid")
    void test07() {
        StaffMember staffMember = createValidStaffMember();
        EmailAddress newEmailAddress =
                new EmailAddress("new.email@example.com");

        assertDoesNotThrow(() ->
                staffMember.changeEmailAddress(newEmailAddress)
        );
    }

    @Test
    @DisplayName("You cannot change an email address if that new email address is null")
    void test08() {
        StaffMember staffMember = createValidStaffMember();

        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                staffMember.changeEmailAddress(null)
        );

        assertEquals(
                StaffMember.EMAIL_ADDRESS_CANNOT_BE_NULL,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("Two StaffMembers with the same id are considered equal")
    void test09() {
        StaffMember staffMember1 = createValidStaffMember();

        FullName differentName =
                new FullName("first2", "surname2");

        EmailAddress differentEmailAddress =
                new EmailAddress("different@example.com");

        StaffMember staffMember2 =
                new StaffMember(identity, differentName, differentEmailAddress);

        assertEquals(staffMember1, staffMember2);
        // id's the same so entities should be equal
    }
}
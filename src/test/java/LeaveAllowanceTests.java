import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.leavebooking.common.FullName;
import com.example.leavebooking.common.Identity;
import com.example.leavebooking.leavemanagement.domain.LeaveAllowance;
import com.example.leavebooking.staffmanagement.domain.StaffMember;

import static org.junit.jupiter.api.Assertions.*;

public class LeaveAllowanceTests {

    private Identity<LeaveAllowance> identity;
    private Identity<StaffMember> staffId;
    private Identity<StaffMember> managerId;
    private FullName staffName;

    @BeforeEach
    void setUp() {
        identity = Identity.of(
                "12345678-1234-1234-1234-123456789012");

        staffId = Identity.of(
                "87654321-4321-4321-4321-210987654321");

        managerId = Identity.of(
                "11111111-2222-3333-4444-555555555555");

        staffName = new FullName("Sade", "Joseph");
    }

    private LeaveAllowance createValidLeaveAllowance() {
        return new LeaveAllowance(
                identity,
                staffId,
                staffName,
                managerId,
                25);
    }

    @Test
    @DisplayName("You can create a LeaveAllowance when all arguments are valid")
    void test01() {
        assertDoesNotThrow(() -> new LeaveAllowance(
                identity,
                staffId,
                staffName,
                managerId,
                25));
    }

    @Test
    @DisplayName("You cannot create a LeaveAllowance if the id is null")
    void test02() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new LeaveAllowance(
                        null,
                        staffId,
                        staffName,
                        managerId,
                        25));

        assertEquals(
                LeaveAllowance.IDENTITY_CANNOT_BE_NULL,
                exception.getMessage());
    }

    @Test
    @DisplayName("You cannot create a LeaveAllowance if the staff id is null")
    void test03() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new LeaveAllowance(
                        identity,
                        null,
                        staffName,
                        managerId,
                        25));

        assertEquals(
                LeaveAllowance.STAFF_ID_CANNOT_BE_NULL,
                exception.getMessage());
    }

    @Test
    @DisplayName("You cannot create a LeaveAllowance if the staff name is null")
    void test04() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new LeaveAllowance(
                        identity,
                        staffId,
                        null,
                        managerId,
                        25));

        assertEquals(
                LeaveAllowance.STAFF_NAME_CANNOT_BE_NULL,
                exception.getMessage());
    }

    @Test
    @DisplayName("You cannot create a LeaveAllowance if the manager id is null")
    void test05() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new LeaveAllowance(
                        identity,
                        staffId,
                        staffName,
                        null,
                        25));

        assertEquals(
                LeaveAllowance.MANAGER_ID_CANNOT_BE_NULL,
                exception.getMessage());
    }

    @Test
    @DisplayName("Annual leave entitlement must be greater than zero")
    void test06() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new LeaveAllowance(
                        identity,
                        staffId,
                        staffName,
                        managerId,
                        0));

        assertEquals(
                LeaveAllowance.ENTITLEMENT_MUST_BE_POSITIVE,
                exception.getMessage());
    }

    @Test
    @DisplayName("A new LeaveAllowance starts with the full entitlement remaining")
    void test07() {
        LeaveAllowance leaveAllowance = createValidLeaveAllowance();

        assertEquals(25, leaveAllowance.remainingBalance());
        assertEquals(0, leaveAllowance.daysUsed());
    }

    @Test
    @DisplayName("Using leave reduces the remaining balance")
    void test08() {
        LeaveAllowance leaveAllowance = createValidLeaveAllowance();

        leaveAllowance.useLeave(5);

        assertEquals(20, leaveAllowance.remainingBalance());
        assertEquals(5, leaveAllowance.daysUsed());
    }

    @Test
    @DisplayName("You cannot use more leave than the remaining balance")
    void test09() {
        LeaveAllowance leaveAllowance = createValidLeaveAllowance();

        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> leaveAllowance.useLeave(26));

        assertEquals(
                LeaveAllowance.INSUFFICIENT_LEAVE_BALANCE,
                exception.getMessage());
    }

    @Test
    @DisplayName("Leave can be restored to the remaining balance")
    void test10() {
        LeaveAllowance leaveAllowance = createValidLeaveAllowance();

        leaveAllowance.useLeave(5);
        leaveAllowance.restoreLeave(5);

        assertEquals(25, leaveAllowance.remainingBalance());
        assertEquals(0, leaveAllowance.daysUsed());
    }

    @Test
    @DisplayName("Changing entitlement preserves leave already used")
    void test11() {
        LeaveAllowance leaveAllowance = createValidLeaveAllowance();

        leaveAllowance.useLeave(5);
        leaveAllowance.amendYearlyEntitlement(30); // ammending the yearly entitlement to 30 days

        assertEquals(30, leaveAllowance.yearlyEntitlement());
        assertEquals(25, leaveAllowance.remainingBalance()); // remaining balance is 25 days because 5 days have already been used
        assertEquals(5, leaveAllowance.daysUsed());
    }

    @Test
    @DisplayName("Two LeaveAllowances with the same id are considered equal")
    void test12() {
        LeaveAllowance leaveAllowance1 = createValidLeaveAllowance();

        LeaveAllowance leaveAllowance2 = new LeaveAllowance(
                identity,
                Identity.of("99999999-9999-9999-9999-999999999999"),
                new FullName("Test", "Person"),
                managerId,
                30);

        assertEquals(leaveAllowance1, leaveAllowance2);

    }
}

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.leavebooking.common.Identity;
import com.example.leavebooking.leavemanagement.domain.DateRange;
import com.example.leavebooking.leavemanagement.domain.LeaveRequest;
import com.example.leavebooking.leavemanagement.domain.LeaveStatus;
import com.example.leavebooking.leavemanagement.domain.LeaveType;
import com.example.leavebooking.staffmanagement.domain.StaffMember;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LeaveRequestTests {
    private Identity<LeaveRequest> identity;
    private Identity<StaffMember> staffId;
    private DateRange dateRange;

    @BeforeEach
    void setUp() { 
        identity = Identity.of(
                "12345678-1234-1234-1234-123456789012");

        staffId = Identity.of(
                "87654321-4321-4321-4321-210987654321");

        dateRange = new DateRange(
                LocalDate.of(2026, 10, 5),
                LocalDate.of(2026, 10, 9));
    }

    private LeaveRequest createValidLeaveRequest() {
        return new LeaveRequest(
                identity,
                staffId,
                dateRange,
                "Annual leave",
                LeaveType.ANNUAL_LEAVE);
    }

    @Test
    @DisplayName("You can create a LeaveRequest when all arguments are valid")
    void test01() {
        assertDoesNotThrow(() -> new LeaveRequest(
                identity,
                staffId,
                dateRange,
                "Annual leave",
                LeaveType.ANNUAL_LEAVE));
    }

    @Test
    @DisplayName("You cannot create a LeaveRequest if the id is null")
    void test02() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new LeaveRequest(
                        null,
                        staffId,
                        dateRange,
                        "Annual leave",
                        LeaveType.ANNUAL_LEAVE));

        assertEquals(
                LeaveRequest.IDENTITY_CANNOT_BE_NULL,
                exception.getMessage());
    }

    @Test
    @DisplayName("You cannot create a LeaveRequest if the staff id is null")
    void test03() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new LeaveRequest(
                        identity,
                        null,
                        dateRange,
                        "Annual leave",
                        LeaveType.ANNUAL_LEAVE));

        assertEquals(
                LeaveRequest.STAFF_ID_CANNOT_BE_NULL,
                exception.getMessage());
    }

    @Test
    @DisplayName("You cannot create a LeaveRequest if the date range is null")
    void test04() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new LeaveRequest(
                        identity,
                        staffId,
                        null,
                        "Annual leave",
                        LeaveType.ANNUAL_LEAVE));

        assertEquals(
                LeaveRequest.DATE_RANGE_CANNOT_BE_NULL,
                exception.getMessage());
    }

    @Test
    @DisplayName("You cannot create a LeaveRequest if the reason is blank")
    void test05() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new LeaveRequest(
                        identity,
                        staffId,
                        dateRange,
                        "",
                        LeaveType.ANNUAL_LEAVE));

        assertEquals(
                LeaveRequest.REASON_CANNOT_BE_EMPTY,
                exception.getMessage());
    }

    @Test
    @DisplayName("You cannot create a LeaveRequest if the leave type is null")
    void test06() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new LeaveRequest(
                        identity,
                        staffId,
                        dateRange,
                        "Annual leave",
                        null));

        assertEquals(
                LeaveRequest.LEAVE_TYPE_CANNOT_BE_NULL,
                exception.getMessage());
    }

    @Test
    @DisplayName("A new LeaveRequest has a pending status")
    void test07() {
        LeaveRequest leaveRequest = createValidLeaveRequest();

        assertEquals(
                LeaveStatus.PENDING,
                leaveRequest.leaveStatus());
    }

    @Test
    @DisplayName("A pending LeaveRequest can be approved")
    void test08() {
        LeaveRequest leaveRequest = createValidLeaveRequest();

        leaveRequest.approveLeaveRequest();

        assertEquals(
                LeaveStatus.APPROVED,
                leaveRequest.leaveStatus());
    }

    @Test
    @DisplayName("A pending LeaveRequest can be rejected")
    void test09() {
        LeaveRequest leaveRequest = createValidLeaveRequest();

        leaveRequest.rejectLeaveRequest();

        assertEquals(
                LeaveStatus.REJECTED,
                leaveRequest.leaveStatus());
    }

    @Test
    @DisplayName("A pending LeaveRequest can be cancelled")
    void test10() {
        LeaveRequest leaveRequest = createValidLeaveRequest();

        leaveRequest.cancelLeaveRequest();

        assertEquals(
                LeaveStatus.CANCELLED,
                leaveRequest.leaveStatus());
    }

    @Test
    @DisplayName("An approved LeaveRequest can be cancelled")
    void test11() {
        LeaveRequest leaveRequest = createValidLeaveRequest();

        leaveRequest.approveLeaveRequest();
        leaveRequest.cancelLeaveRequest();

        assertEquals(
                LeaveStatus.CANCELLED,
                leaveRequest.leaveStatus());
    }

    @Test
    @DisplayName("Two LeaveRequests with the same id are considered equal")
    void test12() {
        LeaveRequest leaveRequest1 = createValidLeaveRequest();

        DateRange differentDateRange = new DateRange(
                LocalDate.of(2026, 11, 2),
                LocalDate.of(2026, 11, 6));

        LeaveRequest leaveRequest2 = new LeaveRequest(
                identity,
                staffId,
                differentDateRange,
                "Different reason",
                LeaveType.ANNUAL_LEAVE);

        assertEquals(leaveRequest1, leaveRequest2);
        // id's are the same so entities should be equal
    }
}

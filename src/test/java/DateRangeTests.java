import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.leavebooking.leavemanagement.domain.DateRange;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class DateRangeTests {

    private DateRange createValidDateRange() {
        return new DateRange(
                LocalDate.of(2026, 10, 5),
                LocalDate.of(2026, 10, 9)
        );
    }

    @Test
    @DisplayName("You can create a DateRange when both dates are valid")
    void test01() {
        assertDoesNotThrow(() ->
                new DateRange(
                        LocalDate.of(2026, 11, 5),
                        LocalDate.of(2026, 11, 9)
                )
        );
    }

    @Test
    @DisplayName("A DateRange cannot have a null start date")
    void test02() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DateRange(
                        null,
                        LocalDate.of(2026, 10, 9)
                )
        );

        assertEquals(
                DateRange.START_DATE_CANNOT_BE_NULL,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("A DateRange cannot have a null end date")
    void test03() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DateRange(
                        LocalDate.of(2026, 10, 5),
                        null
                )
        );

        assertEquals(
                DateRange.END_DATE_CANNOT_BE_NULL,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("The end date cannot be before the start date")
    void test04() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DateRange(
                        LocalDate.of(2026, 10, 9),
                        LocalDate.of(2026, 10, 5)
                )
        );

        assertEquals(
                DateRange.END_DATE_CANNOT_BE_BEFORE_START_DATE,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("A DateRange can start and end on the same date")
    void test05() {
        LocalDate date = LocalDate.of(2026, 10, 5);

        assertDoesNotThrow(() ->
                new DateRange(date, date)
        );
    }

    @Test
    @DisplayName("DateRanges are considered the same when their dates are the same")
    void test06() {
        DateRange dateRange1 = createValidDateRange();
        DateRange dateRange2 = createValidDateRange();

        assertEquals(dateRange1, dateRange2);
    }

    @Test
    @DisplayName("A DateRange can be copied from an existing DateRange")
    void test07() {
        DateRange original = createValidDateRange();

        DateRange copy = new DateRange(original);

        assertEquals(original, copy);
    }

    @Test
    @DisplayName("A null DateRange cannot be copied")
    void test08() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DateRange((DateRange) null)
        );

        assertEquals(
                DateRange.DATE_RANGE_CANNOT_BE_NULL,
                exception.getMessage()
        );
    }
}
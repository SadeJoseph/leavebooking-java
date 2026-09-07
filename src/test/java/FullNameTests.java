import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.leavebooking.FullName;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class FullNameTests {

    private FullName createValidFullName() {
        return new FullName("first1", "surname1");
    }

    private String createTextOfLength(int length) {
        char[] chars = new char[length];
        Arrays.fill(chars, 'a');
        return new String(chars);
    }

    @Test
    @DisplayName("Full names are considered the same when all parts are the same")
    void test01() {
        FullName fullName1 = createValidFullName();
        FullName fullName2 = createValidFullName();

        assertEquals(fullName1, fullName2);
    }

    @Test
    @DisplayName("A full name requires a non-blank surname to be valid")
    void test02() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                new FullName("firstname1", "")
        );

        assertEquals(FullName.SURNAME_NOT_EMPTY, exception.getMessage());
    }

    @Test
    @DisplayName("A full name requires a non-null surname to be valid")
    void test03() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                new FullName("firstname1", null)
        );

        assertEquals(FullName.SURNAME_NOT_EMPTY, exception.getMessage());
    }

    @Test
    @DisplayName("A surname can be up to a specified maximum number of characters in length")
    void test04() {
        assertDoesNotThrow(() -> {
            new FullName(
                    "firstname1",
                    createTextOfLength(FullName.MAX_SURNAME_LENGTH)
            );
        });
    }

    @Test
    @DisplayName("A surname exceeding the specified number of characters is rejected")
    void test05() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            new FullName(
                    "firstname1",
                    createTextOfLength(FullName.MAX_SURNAME_LENGTH + 1)
            );
        });

        assertEquals(FullName.SURNAME_LENGTH, exception.getMessage());
    }

    //
}
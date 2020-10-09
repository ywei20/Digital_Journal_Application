package ProcessFile;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.Assert.*;

public class DigitalEntryTest {

    DigitalEntry digitalEntry, digitalEntryActual, digitalEntryAExpect, digitalEntryDiffId,
        digitalEntryDiffText, digitalEntryDiffPriority, digitalEntryDiffCategory,
        digitalEntryDiffComplete, digitalEntryDiffDate;


    @Before
    public void setUp() throws Exception {
        digitalEntryActual = DigitalEntry.builder().setText("Finished Hw9")
                .setDate(LocalDate.now()).build();
        digitalEntryAExpect = DigitalEntry.builder().setText("Finished Hw9")
                .setDate(LocalDate.now()).build();
        digitalEntry = DigitalEntry.builder()
                .setCategory("environment")
                .setCompleted(true)
                .setText("global warming")
                .setPriority(2)
                .setDate(LocalDate.EPOCH).build();
        digitalEntryDiffId = DigitalEntry.builder().setCategory("environment").setCompleted(true)
            .setText("global warming").setPriority(2).setDate(LocalDate.EPOCH).build();
        digitalEntryDiffText = DigitalEntry.builder().setCategory("environment").setCompleted(true)
            .setText("Water Pollution ").setPriority(2).setDate(LocalDate.EPOCH).build();
        digitalEntryDiffPriority = DigitalEntry.builder().setCategory("environment").setCompleted(true)
            .setText("global warming").setPriority(null).setDate(LocalDate.EPOCH).build(); // pass null to setPriority will set priority to 3
        digitalEntryDiffCategory = DigitalEntry.builder().setCategory("global").setCompleted(true)
            .setText("global warming").setPriority(2).setDate(LocalDate.EPOCH).build();
        digitalEntryDiffComplete = DigitalEntry.builder().setCategory("environment").setCompleted(false)
            .setText("global warming").setPriority(2).setDate(LocalDate.EPOCH).build();
        digitalEntryDiffDate = DigitalEntry.builder().setCategory("environment").setCompleted(true)
            .setText("global warming").setPriority(2).setDate(null).build(); // pass null to setDate will set Date to LocalDate.now()
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderHasNoRequiredTextThrows() {
        DigitalEntry.builder().setCategory("test").setDate(LocalDate.EPOCH)
                .setCompleted(true).build();
    }

    @Test
    public void defaultPriorityIs3() {
        assertEquals(Integer.valueOf(3), DigitalEntry.builder().setText("test").build().getPriority());
    }

    @Test
    public void getterTests() {
        assertEquals("environment", digitalEntry.getCategory());
        assertEquals(true, digitalEntry.isCompleted());
        assertEquals(Integer.valueOf(2), digitalEntry.getPriority());
        assertEquals("global warming", digitalEntry.getText());
        assertEquals(LocalDate.EPOCH, digitalEntry.getDate());
    }

    @Test
    public void setIdTest() {
        digitalEntry.setId(333);
        assertEquals("333,global warming,True,01/01/1970,2,environment", digitalEntry.toString());
    }

    @Test
    public void setCompletedTest() {
        digitalEntry.setCompleted(false);
        assertEquals(false, digitalEntry.isCompleted());
    }

    @Test
    public void toStringTest() {
        assertEquals("0,global warming,True,01/01/1970,2,environment", digitalEntry.toString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(digitalEntry.hashCode(), Objects.hash(
                0, "global warming", true, LocalDate.EPOCH, 2, "environment"
        ));
    }

    @Test
    public void equalTest() {
        digitalEntryDiffId.setId(100);

        assertEquals(digitalEntryActual, digitalEntryAExpect);
        assertEquals(digitalEntry, digitalEntry);

        assertNotEquals(digitalEntry, null);
        assertNotEquals(digitalEntry, new Object());
        assertNotEquals(digitalEntry, digitalEntryDiffId);
        assertNotEquals(digitalEntry, digitalEntryDiffText);
        assertNotEquals(digitalEntry, digitalEntryDiffPriority);
        assertNotEquals(digitalEntry, digitalEntryDiffCategory);
        assertNotEquals(digitalEntry, digitalEntryDiffComplete);
        assertNotEquals(digitalEntry, digitalEntryDiffDate);


    }
}

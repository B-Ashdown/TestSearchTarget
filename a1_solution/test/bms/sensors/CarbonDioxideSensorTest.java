package bms.sensors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class CarbonDioxideSensorTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);  // Fail any test that takes longer than 1 second to execute.

    // **************** Constructor tests ****************

    @Test
    public void constructorThrowsExceptionIdealValueTest() {
        try {
            new CarbonDioxideSensor(new int[]{800}, 1, -1, 30);
            fail();
        } catch (IllegalArgumentException expected) {}
    }

    @Test
    public void constructorThrowsExceptionVariationLimitTest() {
        try {
            new CarbonDioxideSensor(new int[]{800}, 1, 700, 0);
            fail();
        } catch (IllegalArgumentException expected) {}
    }

    @Test
    public void constructorThrowsExceptionIdealMinusVariationTest() {
        try {
            new CarbonDioxideSensor(new int[]{800}, 1, 500, 600);
            fail();
        } catch (IllegalArgumentException expected) {}
    }

    // **************** Get Hazard Level tests ****************
    @Test
    public void getHazardLevelTest() {
        CarbonDioxideSensor sensor = new CarbonDioxideSensor(
                new int[] {500, 1999, 2000, 40000}, 1, 700, 100);
        assertEquals(0, sensor.getHazardLevel());
        sensor.elapseOneMinute();
        assertEquals(25, sensor.getHazardLevel());
        sensor.elapseOneMinute();
        assertEquals(50, sensor.getHazardLevel());
        sensor.elapseOneMinute();
        assertEquals(100, sensor.getHazardLevel());
    }

    // **************** toString tests ****************

    @Test
    public void toStringBasicTest() {
        CarbonDioxideSensor sensor1 = new CarbonDioxideSensor(
                new int[] {750}, 4, 700, 250);

//        assertEquals("TimedSensor: freq=4, readings=750, " +
//                        "type=CarbonDioxideSensor, idealPPM=700, varLimit=250",
//                sensor1.toString());
        assertEquals("TimedSensor:freq=4,readings=750," +
                        "type=CarbonDioxideSensor,idealPPM=700,varLimit=250",
                sensor1.toString().replace(" ", "").replace("\"", ""));
    }

    @Test
    public void toStringAdvancedTest() {
        CarbonDioxideSensor sensor1 = new CarbonDioxideSensor(
                new int[] {750, 780, 670, 720, 700, 730},
                4, 700, 250);

//        assertEquals("TimedSensor: freq=4, readings=750,780,670,720,700,730, " +
//                        "type=CarbonDioxideSensor, idealPPM=700, varLimit=250",
//                sensor1.toString());
        assertEquals("TimedSensor:freq=4,readings=750,780,670,720,700,730," +
                        "type=CarbonDioxideSensor,idealPPM=700,varLimit=250",
                sensor1.toString().replace(" ", "").replace("\"", ""));
    }
}
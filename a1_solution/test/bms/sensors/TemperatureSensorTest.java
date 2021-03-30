package bms.sensors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertEquals;

public class TemperatureSensorTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);  // Fail any test that takes longer than 1 second to execute.

    // **************** getHazardLevel tests ****************

    @Test
    public void getHazardLevelZeroTest() {
        TemperatureSensor sensor1 = new TemperatureSensor(
                new int[] {67});

        assertEquals(0, sensor1.getHazardLevel());
    }

    @Test
    public void getHazardLevel100Test() {
        TemperatureSensor sensor1 = new TemperatureSensor(
                new int[] {68});

        assertEquals(100, sensor1.getHazardLevel());
    }

    // **************** toString tests ****************

    @Test
    public void toStringBasicTest() {
        TemperatureSensor sensor1 = new TemperatureSensor(
                new int[] {24});

//        assertEquals("TimedSensor: freq=1, readings=24, type=TemperatureSensor",
//                sensor1.toString());
        assertEquals("TimedSensor:freq=1,readings=24,type=TemperatureSensor",
                sensor1.toString().replace(" ", "").replace("\"", ""));
    }

    @Test
    public void toStringAdvancedTest() {
        TemperatureSensor sensor1 = new TemperatureSensor(
                new int[] {22, 25, 35, 47, 69, 71});

//        assertEquals("TimedSensor: freq=1, readings=22,25,35,47,69,71, type=TemperatureSensor",
//                sensor1.toString());
        assertEquals("TimedSensor:freq=1,readings=22,25,35,47,69,71,type=TemperatureSensor",
                sensor1.toString().replace(" ", "").replace("\"", ""));
    }
}

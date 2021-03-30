package bms.sensors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class OccupancySensorTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);  // Fail any test that takes longer than 1 second to execute.

    // **************** Constructor tests ****************

    @Test
    public void constructorThrowsExceptionNegativeCapacity() {
        try {
            new OccupancySensor(new int[] {20}, 1, -3);
            fail();
        } catch (IllegalArgumentException expected) {}
    }

    // **************** Get Hazard tests ****************

    @Test
    public void getHazardLevelZeroTest() {
        int[] sensorReadings =  {0}; // one reading for this test
        OccupancySensor sensor1 = new OccupancySensor(
                sensorReadings, 1, 25);
        // round((0/25.0)*100) >> 0
        assertEquals(0, sensor1.getHazardLevel());
    }

    @Test
    public void getHazardLevelFullTest() {
        int[] sensorReadings =  {21}; // one reading for this test
        OccupancySensor sensor1 = new OccupancySensor(
                sensorReadings, 1, 21);
        // round((8/25.0)*100) >> round(38.0952)
        assertEquals(100, sensor1.getHazardLevel(), 0.0001);
    }

    @Test
    public void getHazardLevelOverFullTest() {
        int[] sensorReadings =  {24}; // one reading for this test
        OccupancySensor sensor1 = new OccupancySensor(
                sensorReadings, 1, 21);
        // round((8/25.0)*100) >> round(38.0952)
        assertEquals(100, sensor1.getHazardLevel(), 0.0001);
    }

    @Test
    public void getHazardLevelTest1() {
        int[] sensorReadings =  {8}; // one reading for this test
        OccupancySensor sensor1 = new OccupancySensor(
                sensorReadings, 1, 21);
        // round((8/25.0)*100) >> round(38.0952)
        assertEquals(38, sensor1.getHazardLevel(), 0.0001);
    }

    @Test
    public void getHazardLevelTest2() {
        int[] sensorReadings =  {21}; // one reading for this test
        OccupancySensor sensor1 = new OccupancySensor(
                sensorReadings, 1, 37);
        // round((21/37.0)*100) >> round(56.7568)
        assertEquals(57, sensor1.getHazardLevel(), 0.0001);
    }

    // **************** toString tests ****************

    @Test
    public void toStringBasicTest() {
        OccupancySensor sensor1 = new OccupancySensor(
                new int[] {28}, 1, 30);

//        assertEquals("TimedSensor: freq=1, readings=28, " +
//                        "type=OccupancySensor, capacity=30",
//                sensor1.toString());
        assertEquals("TimedSensor:freq=1,readings=28," +
                        "type=OccupancySensor,capacity=30",
                sensor1.toString().replace(" ", "").replace("\"", ""));
    }

    @Test
    public void toStringAdvancedTest() {
        OccupancySensor sensor1 = new OccupancySensor(
                new int[] {28,22,29,31,30,25}, 3, 30);

//        assertEquals("TimedSensor: freq=3, readings=28,22,29,31,30,25, " +
//                        "type=OccupancySensor, capacity=30",
//                sensor1.toString());
        assertEquals("TimedSensor:freq=3,readings=28,22,29,31,30,25," +
                        "type=OccupancySensor,capacity=30",
                sensor1.toString().replace(" ", "").replace("\"", ""));
    }
}
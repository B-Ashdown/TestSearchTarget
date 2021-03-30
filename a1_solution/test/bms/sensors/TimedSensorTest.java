package bms.sensors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class TimedSensorTest {
    private int[] sensorReadings;
    private int[] sensorReadings2;
    private int limit;
    private int defaultUpdateFrequency = 1;
    private int longerUpdateFrequency = 2;

    TimedSensor sensor;
    TimedSensor sensorLongUF; // long update frequency

    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);  // Fail any test that takes longer than 1 second to execute.

    @Before
    public void setup() {
        sensorReadings = new int[] {12,14,16,11,9};
        sensorReadings2 = new int[] {12,14,16};
        limit = 25;

        // cannot create instance of abstract class so use on that extends it
        // creating the class will initialise the sensors in the constructor
        sensor = new OccupancySensor(sensorReadings, defaultUpdateFrequency, limit);
        sensorLongUF = new OccupancySensor(sensorReadings2, longerUpdateFrequency, limit);
    }

    // **************** Constructor tests ****************

    @Test
    public void constructorThrowsExceptionUpdateFrequencyLowTest() {
        try {
            new OccupancySensor(sensorReadings, 0, limit);
            fail();
        } catch (IllegalArgumentException expected) {}
    }

    @Test
    public void constructorThrowsExceptionUpdateFrequencyHighTest() {
        try {
            new OccupancySensor(sensorReadings, 6, limit);
            fail();
        } catch (IllegalArgumentException expected) {}
    }

    @Test
    public void constructorThrowsExceptionSensorReadingsNullTest() {
        try {
            new OccupancySensor(null, 1, limit);
            fail();
        } catch (IllegalArgumentException expected) {}
    }

    @Test
    public void constructorThrowsExceptionSensorReadingsEmptyTest() {
        try {
            new OccupancySensor(new int[] {}, 1, limit);
            fail();
        } catch (IllegalArgumentException expected) {}
    }

    @Test
    public void constructorThrowsExceptionNegativeSensorReadingTest() {
        try {
            new OccupancySensor(new int[] {1, -2, 4}, 1, limit);
            fail();
        } catch (IllegalArgumentException expected) {}
    }

    // **************** Getter and setter tests (selected) ****************

    @Test
    public void getCurrentReadingTest() {
        assertEquals(sensorReadings[0], sensor.getCurrentReading());
    }

    @Test
    public void getUpdateFrequencyTest() {
        assertEquals(defaultUpdateFrequency, sensor.getUpdateFrequency());
    }

    // **************** Time elapsed tests ****************

    @Test
    public void getTimeElapsedZeroTest() {
        assertEquals(0, sensor.getTimeElapsed());
    }

    @Test
    public void getTimeElapsedSomeTimeTest() {
        for (int i = 1; i <= 5; i++) {
            sensor.elapseOneMinute();
        }
        assertEquals(5, sensor.getTimeElapsed());
    }

    // **************** Get readings over time tests ****************

    @Test
    public void getCurrentReadingZeroMinutesTest() {
        assertEquals(sensor.getCurrentReading(), sensorReadings[0]);
    }

    @Test
    public void getCurrentReadingFourMinuteTest() {
        for (int i = 1; i <= 4; i++) {
            sensor.elapseOneMinute();
        }
        assertEquals(sensorReadings[4], sensor.getCurrentReading());
    }

    @Test
    public void getCurrentReadingArrayLoopTest() {
        for (int i = 1; i <= 7; i++) {
            sensor.elapseOneMinute();
        }
        // array is only 5 long, so the values should wrap around to start
        // of array
        assertEquals(sensorReadings[2], sensor.getCurrentReading());
    }

    @Test
    public void getCurrentReadingUpdateFrequencyTest() {
        assertEquals(sensorReadings[0], sensorLongUF.getCurrentReading());
        sensorLongUF.elapseOneMinute(); // after this time is 1
        assertEquals(sensorReadings[0], sensorLongUF.getCurrentReading());
        sensorLongUF.elapseOneMinute();// after this time is 2
        assertEquals(sensorReadings[1], sensorLongUF.getCurrentReading());
        sensorLongUF.elapseOneMinute(); // after this time is 3
        assertEquals(sensorReadings[1], sensorLongUF.getCurrentReading());
    }

    @Test
    public void getCurrentReadingUpdateFrequencyArrayLoopTest() {
        assertEquals(sensorReadings[0], sensorLongUF.getCurrentReading());
        sensorLongUF.elapseOneMinute(); // after this time is 1
        assertEquals(sensorReadings[0], sensorLongUF.getCurrentReading());
        sensorLongUF.elapseOneMinute();// after this time is 2
        assertEquals(sensorReadings[1], sensorLongUF.getCurrentReading());
        sensorLongUF.elapseOneMinute(); // after this time is 3
        assertEquals(sensorReadings[1], sensorLongUF.getCurrentReading());
        sensorLongUF.elapseOneMinute(); // after this time is 4
        assertEquals(sensorReadings[2], sensorLongUF.getCurrentReading());
        sensorLongUF.elapseOneMinute();// after this time is 5
        assertEquals(sensorReadings[2], sensorLongUF.getCurrentReading());
        sensorLongUF.elapseOneMinute(); // after this time is 6
        // array is 3 long and update frequency is 2, so the values should
        // wrap around to start of array
        assertEquals(sensorReadings[0], sensorLongUF.getCurrentReading());
        sensorLongUF.elapseOneMinute();// after this time is 7
        assertEquals(sensorReadings[0], sensorLongUF.getCurrentReading());
        sensorLongUF.elapseOneMinute(); // after this time is 8
    }

    /*
    Bare-bones implementation of TimedSensor, used to test default
    implementation of equals, hashCode and toString in the abstract class.
     */
    private static class SimpleTimedSensor extends TimedSensor {
        public SimpleTimedSensor(int[] sensorReadings, int updateFrequency) {
            super(sensorReadings, updateFrequency);
        }
    }

    // **************** toString tests ****************

    @Test
    public void toStringSimpleTest() {
        TimedSensor sensor1 = new SimpleTimedSensor(new int[] {10}, 2);

//        assertEquals("TimedSensor: freq=2, readings=10", sensor1.toString());
        assertEquals("TimedSensor:freq=2,readings=10",
                sensor1.toString().replace(" ", "").replace("\"", ""));
    }

    @Test
    public void toStringAdvancedTest() {
        TimedSensor sensor1 = new SimpleTimedSensor(new int[] {40, 32, 18}, 4);

//        assertEquals("TimedSensor: freq=4, readings=40,32,18", sensor1.toString());
        assertEquals("TimedSensor:freq=4,readings=40,32,18",
                sensor1.toString().replace(" ", "").replace("\"", ""));
    }

}
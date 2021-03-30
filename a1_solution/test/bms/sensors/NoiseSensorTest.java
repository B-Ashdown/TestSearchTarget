package bms.sensors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class NoiseSensorTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);  // Fail any test that takes longer than 1 second to execute.

    // **************** Calculate relative loudness tests ****************

    @Test
    public void calculateRelativeLoudnessZeroTest() {
        int[] sensorReadings =  {70}; // one reading for this test
        NoiseSensor sensor1 = new NoiseSensor(
                sensorReadings, 1);
        assertEquals(1.0, sensor1.calculateRelativeLoudness(), 0.0001);
    }

    @Test
    public void calculateRelativeLoudnessLowerTest1() {
        int[] sensorReadings =  {61}; // one reading for this test
        NoiseSensor sensor1 = new NoiseSensor(
                sensorReadings, 1);
        // 2^((61 - 70)/10) >> 2^((-10)/10) >> 2 ^(-1) >> 0.5359
        assertEquals(0.5359, sensor1.calculateRelativeLoudness(), 0.0001);
    }

    @Test
    public void calculateRelativeLoudnessLowerTest2() {
        int[] sensorReadings =  {53}; // one reading for this test
        NoiseSensor sensor1 = new NoiseSensor(
                sensorReadings, 1);
        // 2^((53 - 70)/10) >> 2^((-17)/10) >> 2 ^(-1.7) >> 0.3078
        double loudness = sensor1.calculateRelativeLoudness();
        assertEquals(0.3078, sensor1.calculateRelativeLoudness(), 0.0001);
    }

    @Test
    public void calculateRelativeLoudnessHigherTest1() {
        int[] sensorReadings =  {89}; // one reading for this test
        NoiseSensor sensor1 = new NoiseSensor(
                sensorReadings, 1);
        // 2^((89 - 70)/10) >> 2^((19)/10) >> 2 ^(1.9) >> 3.7321
        assertEquals(3.7321, sensor1.calculateRelativeLoudness(), 0.0001);
    }

    @Test
    public void calculateRelativeLoudnessHigherTest2() {
        int[] sensorReadings =  {79}; // one reading for this test
        NoiseSensor sensor1 = new NoiseSensor(
                sensorReadings, 1);
        // 2^((79 - 70)/10) >> 2^((9)/10) >> 2 ^(0.9) >> 1.8661
        assertEquals(1.8661, sensor1.calculateRelativeLoudness(), 0.0001);
    }

    // **************** Get Hazard tests ****************

    @Test
    public void getHazardLevelZeroTest() {
        int[] sensorReadings =  {0}; // one reading for this test
        NoiseSensor sensor1 = new NoiseSensor(
                sensorReadings, 1);
        assertEquals(0, sensor1.getHazardLevel());
    }

    @Test
    public void getHazardLevelRoundingTest1() {
        int[] sensorReadings =  {61}; // one reading for this test
        NoiseSensor sensor1 = new NoiseSensor(
                sensorReadings, 1);
        // 2^((61 - 70)/10) >> 2^((-9)/10) >> 2 ^(-0.9) >> 0.5359 >> 53 (round down)
        assertEquals(53, sensor1.getHazardLevel());
    }

    @Test
    public void getHazardLevelMoreThan100Test() {
        int[] sensorReadings =  {76}; // one reading for this test
        NoiseSensor sensor1 = new NoiseSensor(
                sensorReadings, 1);
        // 2^((76 - 70)/10) >> 2^((6)/10) >> 2 ^(0.6) >> 1.5157 >> 151 >> 100
        // (round down to 100)
        assertEquals(100, sensor1.getHazardLevel());
    }

    @Test
    public void getHazardLevelRoundingTest2() {
        int[] sensorReadings =  {42}; // one reading for this test
        NoiseSensor sensor1 = new NoiseSensor(
                sensorReadings, 1);
        // 2^((42 - 70)/10) >> 2^((-28)/10) >> 2 ^(-2.8) >> 0.1436 >> 14 (round down)
        assertEquals(14, sensor1.getHazardLevel());
    }

    // **************** toString tests ****************

    @Test
    public void toStringBasicTest() {
        NoiseSensor sensor1 = new NoiseSensor(
                new int[] {70}, 1);

//        assertEquals("TimedSensor: freq=1, readings=70, type=NoiseSensor",
//                sensor1.toString());
        assertEquals("TimedSensor:freq=1,readings=70,type=NoiseSensor",
                sensor1.toString().replace(" ", "").replace("\"", ""));
    }

    @Test
    public void toStringAdvancedTest() {
        NoiseSensor sensor1 = new NoiseSensor(
                new int[] {70, 73, 78, 65, 72}, 3);

//        assertEquals("TimedSensor: freq=3, readings=70,73,78,65,72, type=NoiseSensor",
//                sensor1.toString());
        assertEquals("TimedSensor:freq=3,readings=70,73,78,65,72,type=NoiseSensor",
                sensor1.toString().replace(" ", "").replace("\"", ""));
    }
}
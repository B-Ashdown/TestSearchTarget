package bms.room;

import bms.exceptions.DuplicateSensorException;
import bms.sensors.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RoomTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);  // Fail any test that takes longer than 1 second to execute.

    // **************** Get Sensor tests ****************
    @Test
    public void getSensorNoSensorsTest() {
        Room room1 = new Room(1, RoomType.STUDY, 10);
        assertNull(room1.getSensor("CarbonDioxideSensor"));
    }

    @Test
    public void getSensorCO2Test() {
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Sensor sensor1 = new CarbonDioxideSensor(
                new int[] {700, 720, 712}, 1, 700, 300);
        try {
            room1.addSensor(sensor1);
        } catch (DuplicateSensorException e) {
            fail(); // should not generate an exception
        }
        assertEquals(sensor1, room1.getSensor("CarbonDioxideSensor"));
    }

    @Test
    public void getSensorNoiseTest() {
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Sensor sensor1 = new NoiseSensor(
                new int[] {65,67,62,61}, 1);
        try {
            room1.addSensor(sensor1);
        } catch (DuplicateSensorException e) {
            fail(); // should not generate an exception
        }
        assertEquals(sensor1, room1.getSensor("NoiseSensor"));
    }

    @Test
    public void getSensorOccupancyTest() {
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Sensor sensor1 = new OccupancySensor(
                new int[] {15,13,12,17}, 1, 25);
        try {
            room1.addSensor(sensor1);
        } catch (DuplicateSensorException e) {
            fail(); // should not generate an exception
        }
        assertEquals(sensor1, room1.getSensor("OccupancySensor"));
    }

    @Test
    public void getSensorFireTest() {
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Sensor sensor1 = new TemperatureSensor(
                new int[] {20,19,20,21});
        try {
            room1.addSensor(sensor1);
        } catch (DuplicateSensorException e) {
            fail(); // should not generate an exception
        }
        assertEquals(sensor1, room1.getSensor("TemperatureSensor"));
    }

    @Test
    public void getSensorFromListTest1() {
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Sensor sensor1 = new CarbonDioxideSensor(
                new int[] {700, 720, 712}, 1, 700, 300);
        Sensor sensor2 = new OccupancySensor(
                new int[] {15,13,12,17}, 1, 25);
        Sensor sensor3 = new NoiseSensor(
                new int[] {65,67,62,61}, 1);
        try {
            room1.addSensor(sensor1);
            room1.addSensor(sensor2);
            room1.addSensor(sensor3);
        } catch (DuplicateSensorException e) {
            fail(); // should not generate an exception
        }
        assertEquals(sensor3, room1.getSensor("NoiseSensor"));
    }
    @Test
    public void getSensorFromListTest2() {
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Sensor sensor1 = new CarbonDioxideSensor(
                new int[] {700, 720, 712}, 1, 700, 300);
        Sensor sensor2 = new OccupancySensor(
                new int[] {15,13,12,17}, 1, 25);
        try {
            room1.addSensor(sensor1);
            room1.addSensor(sensor2);
        } catch (DuplicateSensorException e) {
            fail(); // should not generate an exception
        }
        assertEquals(sensor2, room1.getSensor("OccupancySensor"));
    }
    @Test
    public void getSensorFromNullTest() {
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Sensor sensor1 = new CarbonDioxideSensor(
                new int[] {700, 720, 712}, 1, 700, 300);
        Sensor sensor2 = new OccupancySensor(
                new int[] {15,13,12,17}, 1, 25);
        try {
            room1.addSensor(sensor1);
            room1.addSensor(sensor2);
        } catch (DuplicateSensorException e) {
            fail(); // should not generate an exception
        }
        assertNull(room1.getSensor("NoiseSensor"));
    }

    // **************** GetSensors tests ***************

    @Test
    public void getSensorsAlphabeticalTest() {
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Sensor sensor1 = new OccupancySensor(
                new int[] {15,13,12,17}, 1, 25);
        Sensor sensor2 = new CarbonDioxideSensor(
                new int[] {700, 720, 712}, 1, 700, 300);
        Sensor sensor3 = new NoiseSensor(
                new int[] {65,67,62,61}, 1);
        Sensor sensor4 = new TemperatureSensor(new int[] {24, 23, 25});

        try {
            room1.addSensor(sensor1);
            room1.addSensor(sensor2);
            room1.addSensor(sensor3);
            room1.addSensor(sensor4);
        } catch (DuplicateSensorException e) {
            fail(); // should not generate an exception
        }
        // Order should be: C, N, O, T (alphabetical)
        assertEquals(List.of(sensor2, sensor3, sensor1, sensor4),
                room1.getSensors());
    }

    // **************** Add Hazard Sensor tests ***************

    @Test
    public void addSensorDuplicateTest() {
        boolean thrown = false;
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Sensor sensor1 = new CarbonDioxideSensor(
                new int[] {700, 720, 712}, 1, 700, 300);
        try {
            room1.addSensor(sensor1);
            room1.addSensor(sensor1);
        } catch (DuplicateSensorException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void addSensorAddOneTest() {
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Sensor sensor1 = new CarbonDioxideSensor(
                new int[] {700, 720, 712}, 1, 700, 300);
        List<Sensor> expected = new ArrayList<>();
        expected.add(sensor1);
        try {
            room1.addSensor(sensor1);
        } catch (DuplicateSensorException e) {
            fail(); // should not generate an exception
        }
        assertEquals(1, room1.getSensors().size());
        assertEquals(sensor1, room1.getSensors().get(0));
    }

    @Test
    public void addSensorAddMultipleTest() {
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Sensor sensor1 = new CarbonDioxideSensor(
                new int[] {700, 720, 712}, 1, 700, 300);
        Sensor sensor2 = new NoiseSensor(
                new int[] {65,67,62,61}, 1);
        List<Sensor> expected = new ArrayList<>();
        expected.add(sensor1);
        expected.add(sensor2);
        try {
            room1.addSensor(sensor1);
            room1.addSensor(sensor2);
        } catch (DuplicateSensorException e) {
            fail(); // should not generate an exception
        }
        assertEquals(2, room1.getSensors().size());
        assertEquals(sensor1, room1.getSensors().get(0));
        assertEquals(sensor2, room1.getSensors().get(1));
    }


    // **************** toString tests ****************

    @Test
    public void toStringBasicTest() {
        Room room1 = new Room(22, RoomType.LABORATORY, 20.25);
//        assertEquals("Room #22: type=LABORATORY, area=20.25m^2, sensors=0",
//                room1.toString());
        assertEquals("Room#22:type=LABORATORY,area=20.25m^2,sensors=0",
                room1.toString().replace(" ", "").replace("\"", ""));
    }

    @Test
    public void toStringAdvancedTest() {
        Room room1 = new Room(22, RoomType.LABORATORY, 36.18923);
        try {
            room1.addSensor(new TemperatureSensor(new int[] {24, 25, 21}));
            room1.addSensor(new CarbonDioxideSensor(new int[] {760, 603}, 1, 700, 300));
            room1.addSensor(new OccupancySensor(new int[] {20, 24}, 1, 30));
        } catch (DuplicateSensorException ignored) {}

//        assertEquals("Room #22: type=LABORATORY, area=36.19m^2, sensors=3",
//                room1.toString());
        assertEquals("Room#22:type=LABORATORY,area=36.19m^2,sensors=3",
                room1.toString().replace(" ", "").replace("\"", ""));
    }
}
package bms.floor;

import bms.exceptions.*;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.CarbonDioxideSensor;
import bms.sensors.OccupancySensor;
import bms.sensors.TemperatureSensor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FloorTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);  // Fail any test that takes longer than 1 second to execute.

    // **************** get floor number tests ****************

    @Test
    public void getFloorNumberTest() {
        Floor floor1 = new Floor(1, 5, 6);
        assertEquals(1, floor1.getFloorNumber());
    }

    // **************** get floor minimum width tests ****************

    @Test
    public void getMinWidthTest() {
        Floor floor1 = new Floor(1, 5, 6);
        // Should be set to 5 by default according to specification
        assertEquals(5, floor1.getMinWidth());
    }

    // **************** get floor minimum length tests ****************

    @Test
    public void getMinLengthTest() {
        Floor floor1 = new Floor(1, 5, 6);
        // Should be set to 5 by default according to specification
        assertEquals(5, floor1.getMinLength());
    }

    // **************** get rooms tests ****************

    @Test
    public void getRoomsEmptyTest() {
        Floor floor1 = new Floor(1, 5, 6);
        assertEquals(new ArrayList<>(), floor1.getRooms());
    }

    @Test
    public void getRoomsOneRoomTest() {
        Floor floor1 = new Floor(1, 5, 6);
        Room room1 = new Room(1, RoomType.STUDY, 10);
        List<Room> expected = new ArrayList<>();
        expected.add(room1);
        try {
            floor1.addRoom(room1);
        } catch (DuplicateRoomException ignore) {
        } catch (InsufficientSpaceException ignored) {}

        assertEquals(expected, floor1.getRooms());
    }

    @Test
    public void getRoomsMultipleRoomsTest() {
        Floor floor1 = new Floor(1, 5, 6);
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Room room2 = new Room(2, RoomType.STUDY, 10);
        List<Room> expected = new ArrayList<>();
        expected.add(room1);
        expected.add(room2);
        try {
            floor1.addRoom(room1);
            floor1.addRoom(room2);
        } catch (DuplicateRoomException ignore) {
        } catch (InsufficientSpaceException ignored) {}

        assertEquals(expected, floor1.getRooms());
    }

    @Test
    public void getRoomsOnlyCopyIsModifiedTest() {
        Floor floor1 = new Floor(1, 5, 6);
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Room room2 = new Room(2, RoomType.STUDY, 10);
        List<Room> expected = new ArrayList<>();
        expected.add(room1);
        try {
            floor1.addRoom(room1);
            // This should only append to the copy passed back, and not the stored data
            floor1.getRooms().add(room2);
        } catch (DuplicateRoomException ignore) {
        } catch (InsufficientSpaceException ignored) {}

        assertEquals(expected, floor1.getRooms());
    }

    // **************** calculate area tests ****************

    @Test
    public void calculateAreaIntegersTest() {
        Floor floor1 = new Floor(1, 5, 6);
        assertEquals(30.0, floor1.calculateArea(), 0.0001);
    }

    @Test
    public void calculateAreaDoubleTest1() {
        Floor floor1 = new Floor(1, 5.92, 6.17);
        assertEquals(36.5264, floor1.calculateArea(), 0.0001);
    }

    @Test
    public void calculateAreaDoubleTest2() {
        Floor floor1 = new Floor(1, 19.7, 24.212);
        assertEquals(476.97639, floor1.calculateArea(), 0.0001);
    }

    // **************** calculate area tests ****************

    @Test
    public void occupiedAreaNoRoomsTest() {
        Floor floor1 = new Floor(1, 5, 6);
        assertEquals(0, floor1.occupiedArea(), 0.0001);
    }

    @Test
    public void occupiedAreaOneRoomTest() {
        Floor floor1 = new Floor(1, 5, 6);
        Room room1 = new Room(1, RoomType.STUDY, 12.0);
        try {
            floor1.addRoom(room1);
        } catch (DuplicateRoomException ignore) {
        } catch (InsufficientSpaceException ignored) {}
        assertEquals(12.0, floor1.occupiedArea(), 0.0001);
    }

    @Test
    public void occupiedAreaMultipleRoomsTest() {
        Floor floor1 = new Floor(1, 5, 6);
        Room room1 = new Room(1, RoomType.LABORATORY, 12.0);
        Room room2 = new Room(2, RoomType.STUDY, 7.1);
        try {
            floor1.addRoom(room1);
            floor1.addRoom(room2);
        } catch (DuplicateRoomException ignore) {
        } catch (InsufficientSpaceException ignored) {}
        assertEquals(19.1, floor1.occupiedArea(), 0.0001);
    }

    // **************** add room tests ****************

    @Test
    public void addRoomAreaLessThanMinimumTest() {
        boolean thrown = false;
        Floor floor1 = new Floor(1, 10, 10);
        Room room1 = new Room(2, RoomType.STUDY, Room.getMinArea() - 1);
        try {
            floor1.addRoom(room1);
        } catch(IllegalArgumentException e) {
            thrown = true;
        } catch (DuplicateRoomException ignore) {
        } catch(InsufficientSpaceException ignore) {
        }
        assertTrue(thrown);
    }

    @Test
    public void addRoomAreaMoreThanMinimumTest() {
        Floor floor1 = new Floor(1, 10, 10);
        Room room1 = new Room(2, RoomType.STUDY, Room.getMinArea() + 1);
        try {
            floor1.addRoom(room1);
        } catch(IllegalArgumentException ignore) {
            fail(); // This test is valid so should not throw an exception
        } catch (DuplicateRoomException ignore) {
        } catch(InsufficientSpaceException ignore) {
        }
    }

    @Test
    public void addRoomDuplicateRoomNumberTest() {
        boolean thrown = false;
        Floor floor1 = new Floor(1, 10, 10);
        Room room1 = new Room(62, RoomType.STUDY, 8);
        Room room2 = new Room(62, RoomType.STUDY, 11);
        try {
            floor1.addRoom(room1);
            floor1.addRoom(room2);
        } catch(IllegalArgumentException ignore) {
        } catch (DuplicateRoomException e) {
            thrown = true;
        } catch(InsufficientSpaceException ignore) {
        }
        assertTrue(thrown);
    }

    @Test
    public void addRoomNonDuplicateRoomNumberTest() {
        Floor floor1 = new Floor(1, 10, 10);
        Room room1 = new Room(62, RoomType.STUDY, 8);
        Room room2 = new Room(46, RoomType.STUDY, 11);
        try {
            floor1.addRoom(room1);
            floor1.addRoom(room2);
        } catch(IllegalArgumentException ignore) {
        } catch (DuplicateRoomException e) {
            fail(); // this sequence is valid so an exception should not be thrown
        } catch(InsufficientSpaceException ignore) {
        }
    }

    @Test
    public void addRoomSufficientSpaceOneRoomTest() {
        Floor floor1 = new Floor(1, 5, 5);
        Room room1 = new Room(2, RoomType.STUDY, 10);
        try {
            floor1.addRoom(room1);
        } catch(IllegalArgumentException ignore) {
        } catch (DuplicateRoomException ignore) {
        } catch(InsufficientSpaceException e) {
            fail(); // This test is valid so should not throw an exception
        }
    }

    @Test
    public void addRoomSufficientSpaceMultipleRoomsTest() {
        Floor floor1 = new Floor(1, 5, 5);
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Room room2 = new Room(2, RoomType.STUDY, 14);
        try {
            floor1.addRoom(room1);
            floor1.addRoom(room2);
        } catch(IllegalArgumentException ignore) {
        } catch (DuplicateRoomException ignore) {
        } catch(InsufficientSpaceException e) {
            fail(); // This test is valid so should not throw an exception
        }
    }

    @Test
    public void addRoomSufficientSpaceAtMaxCapacityTest() {
        Floor floor1 = new Floor(1, 5, 5);
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Room room2 = new Room(2, RoomType.STUDY, 15);
        try {
            floor1.addRoom(room1);
            floor1.addRoom(room2);
        } catch(IllegalArgumentException ignore) {
        } catch (DuplicateRoomException ignore) {
        } catch(InsufficientSpaceException e) {
            fail(); // This test is valid so should not throw an exception
        }
    }

    @Test
    public void addRoomInsufficientSpaceOneRoomTest() {
        boolean thrown = false;
        Floor floor1 = new Floor(1, 5, 5);
        Room room1 = new Room(1, RoomType.STUDY, 26);
        try {
            floor1.addRoom(room1);
        } catch(IllegalArgumentException ignore) {
        } catch (DuplicateRoomException ignore) {
        } catch(InsufficientSpaceException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void addRoomInsufficientSpaceMultipleRoomsTest() {
        boolean thrown = false;
        Floor floor1 = new Floor(1, 5, 5);
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Room room2 = new Room(2, RoomType.STUDY, 17);
        try {
            floor1.addRoom(room1);
            floor1.addRoom(room2);
        } catch(IllegalArgumentException ignore) {
        } catch (DuplicateRoomException ignore) {
        } catch(InsufficientSpaceException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    // **************** fire drill tests ****************

    @Test
    public void fireDrillNoRoomsOfTypeTest() throws InsufficientSpaceException,
            DuplicateRoomException {
        Floor floor1 = new Floor(1, 10, 10);
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Room room2 = new Room(2, RoomType.LABORATORY, 17);
        Room room3 = new Room(3, RoomType.STUDY, 12);
        Room room4 = new Room(4, RoomType.LABORATORY, 23);
        floor1.addRoom(room1);
        floor1.addRoom(room2);
        floor1.addRoom(room3);
        floor1.addRoom(room4);

        floor1.fireDrill(RoomType.OFFICE);

        assertFalse(floor1.getRoomByNumber(1).fireDrillOngoing());
        assertFalse(floor1.getRoomByNumber(2).fireDrillOngoing());
        assertFalse(floor1.getRoomByNumber(3).fireDrillOngoing());
        assertFalse(floor1.getRoomByNumber(4).fireDrillOngoing());
    }

    @Test
    public void fireDrillStudyRoomsOnlyTest() throws InsufficientSpaceException,
            DuplicateRoomException {
        Floor floor1 = new Floor(1, 10, 10);
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Room room2 = new Room(2, RoomType.LABORATORY, 17);
        Room room3 = new Room(3, RoomType.STUDY, 12);
        Room room4 = new Room(4, RoomType.OFFICE, 23);
        Room room5 = new Room(5, RoomType.STUDY, 11);
        floor1.addRoom(room1);
        floor1.addRoom(room2);
        floor1.addRoom(room3);
        floor1.addRoom(room4);
        floor1.addRoom(room5);

        floor1.fireDrill(RoomType.STUDY);

        assertTrue(floor1.getRoomByNumber(1).fireDrillOngoing());
        assertFalse(floor1.getRoomByNumber(2).fireDrillOngoing());
        assertTrue(floor1.getRoomByNumber(3).fireDrillOngoing());
        assertFalse(floor1.getRoomByNumber(4).fireDrillOngoing());
        assertTrue(floor1.getRoomByNumber(5).fireDrillOngoing());
    }

    @Test
    public void fireDrillLaboratoryRoomsOnlyTest() throws InsufficientSpaceException,
            DuplicateRoomException {
        Floor floor1 = new Floor(1, 10, 10);
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Room room2 = new Room(2, RoomType.LABORATORY, 17);
        Room room3 = new Room(3, RoomType.STUDY, 12);
        Room room4 = new Room(4, RoomType.OFFICE, 23);
        Room room5 = new Room(5, RoomType.STUDY, 11);
        floor1.addRoom(room1);
        floor1.addRoom(room2);
        floor1.addRoom(room3);
        floor1.addRoom(room4);
        floor1.addRoom(room5);

        floor1.fireDrill(RoomType.LABORATORY);

        assertFalse(floor1.getRoomByNumber(1).fireDrillOngoing());
        assertTrue(floor1.getRoomByNumber(2).fireDrillOngoing());
        assertFalse(floor1.getRoomByNumber(3).fireDrillOngoing());
        assertFalse(floor1.getRoomByNumber(4).fireDrillOngoing());
        assertFalse(floor1.getRoomByNumber(5).fireDrillOngoing());
    }

    @Test
    public void fireDrillOfficeRoomsOnlyTest() throws InsufficientSpaceException,
            DuplicateRoomException {
        Floor floor1 = new Floor(1, 10, 10);
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Room room2 = new Room(2, RoomType.LABORATORY, 17);
        Room room3 = new Room(3, RoomType.STUDY, 12);
        Room room4 = new Room(4, RoomType.OFFICE, 23);
        Room room5 = new Room(5, RoomType.STUDY, 11);
        floor1.addRoom(room1);
        floor1.addRoom(room2);
        floor1.addRoom(room3);
        floor1.addRoom(room4);
        floor1.addRoom(room5);

        floor1.fireDrill(RoomType.OFFICE);

        assertFalse(floor1.getRoomByNumber(1).fireDrillOngoing());
        assertFalse(floor1.getRoomByNumber(2).fireDrillOngoing());
        assertFalse(floor1.getRoomByNumber(3).fireDrillOngoing());
        assertTrue(floor1.getRoomByNumber(4).fireDrillOngoing());
        assertFalse(floor1.getRoomByNumber(5).fireDrillOngoing());
    }

    @Test
    public void fireDrillAllRoomsTest() throws InsufficientSpaceException,
            DuplicateRoomException {
        Floor floor1 = new Floor(1, 10, 10);
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Room room2 = new Room(2, RoomType.LABORATORY, 17);
        Room room3 = new Room(3, RoomType.STUDY, 12);
        Room room4 = new Room(4, RoomType.OFFICE, 23);
        Room room5 = new Room(5, RoomType.STUDY, 11);
        floor1.addRoom(room1);
        floor1.addRoom(room2);
        floor1.addRoom(room3);
        floor1.addRoom(room4);
        floor1.addRoom(room5);

        floor1.fireDrill(null);

        assertTrue(floor1.getRoomByNumber(1).fireDrillOngoing());
        assertTrue(floor1.getRoomByNumber(2).fireDrillOngoing());
        assertTrue(floor1.getRoomByNumber(3).fireDrillOngoing());
        assertTrue(floor1.getRoomByNumber(4).fireDrillOngoing());
        assertTrue(floor1.getRoomByNumber(5).fireDrillOngoing());
    }

    // **************** cancelFireDrill tests ****************

    @Test
    public void cancelFireDrillTest() throws InsufficientSpaceException,
            DuplicateRoomException {
        Floor floor1 = new Floor(1, 10, 10);
        Room room1 = new Room(1, RoomType.STUDY, 10);
        Room room2 = new Room(2, RoomType.LABORATORY, 17);
        Room room3 = new Room(3, RoomType.STUDY, 12);
        Room room4 = new Room(4, RoomType.OFFICE, 23);
        Room room5 = new Room(5, RoomType.STUDY, 11);
        floor1.addRoom(room1);
        floor1.addRoom(room2);
        floor1.addRoom(room3);
        floor1.addRoom(room4);
        floor1.addRoom(room5);

        floor1.fireDrill(null);

        floor1.cancelFireDrill();

        assertFalse(floor1.getRoomByNumber(1).fireDrillOngoing());
        assertFalse(floor1.getRoomByNumber(2).fireDrillOngoing());
        assertFalse(floor1.getRoomByNumber(3).fireDrillOngoing());
        assertFalse(floor1.getRoomByNumber(4).fireDrillOngoing());
        assertFalse(floor1.getRoomByNumber(5).fireDrillOngoing());
    }

    // **************** toString tests ****************

    @Test
    public void toStringBasicTest() {
        Floor floor1 = new Floor(1, 10.4, 18.26);
//        assertEquals("Floor #1: width=10.40m, length=18.26m, rooms=0",
//                floor1.toString());
        assertEquals("Floor#1:width=10.40m,length=18.26m,rooms=0",
                floor1.toString().replace(" ", "").replace("\"", ""));
    }

    @Test
    public void toStringAdvancedTest() {
        Floor floor1 = new Floor(5, 10.4237, 18.8781);
        try {
            floor1.addRoom(new Room(1, RoomType.STUDY, 20));
            floor1.addRoom(new Room(2, RoomType.OFFICE, 30));
        } catch (InsufficientSpaceException | DuplicateRoomException e) {
            fail();
        }

//        assertEquals("Floor #5: width=10.42m, length=18.88m, rooms=2",
//                floor1.toString());
        assertEquals("Floor#5:width=10.42m,length=18.88m,rooms=2",
                floor1.toString().replace(" ", "").replace("\"", ""));
    }
}
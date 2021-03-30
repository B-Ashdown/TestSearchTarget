package bms.building;

import bms.exceptions.*;
import bms.floor.Floor;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.CarbonDioxideSensor;
import bms.sensors.OccupancySensor;
import bms.sensors.TemperatureSensor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class BuildingTest {
    private Building building1;

    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);  // Fail any test that takes longer than 1 second to execute.

    @Before
    public void setUp() {
        building1 = new Building("GP South");
    }

    // **************** get name tests ****************

    @Test
    public void getNameTest() {
        assertEquals("GP South", building1.getName());
    }

    // **************** get floor by number tests ****************
    @Test
    public void getFloorByNumberNullTest() {
        assertEquals(null, building1.getFloorByNumber(1));
    }

    @Test
    public void getFloorByNumberOneFloorTest() {
        try {
            Floor floor1 = new Floor(1, 10, 10);
            building1.addFloor(floor1);
            assertEquals(floor1, building1.getFloorByNumber(1));
        } catch(IllegalArgumentException ignore) {
        } catch(DuplicateFloorException ignore) {
        } catch(NoFloorBelowException ignore) {
        } catch(FloorTooSmallException ignore) {
        }
    }

    @Test
    public void getFloorByNumberMultipleFloorTest() {
        try {
            Floor floor1 = new Floor(1, 10, 10);
            Floor floor2 = new Floor(2, 10, 10);
            Floor floor3 = new Floor(3, 10, 10);
            building1.addFloor(floor1);
            building1.addFloor(floor2);
            building1.addFloor(floor3);
            assertEquals(floor2, building1.getFloorByNumber(2));
            assertEquals(floor3, building1.getFloorByNumber(3));
        } catch(IllegalArgumentException ignore) {
        } catch(DuplicateFloorException ignore) {
        } catch(NoFloorBelowException ignore) {
        } catch(FloorTooSmallException ignore) {
        }
    }

    // **************** add floor tests ****************

    @Test
    public void addFloorFloorNumberNegativeTest() {
        boolean thrown = false;
        try {
            Floor floor1 = new Floor(-1, 10, 10);
            building1.addFloor(floor1);
        } catch(IllegalArgumentException ignore) {
            thrown = true;
        } catch(DuplicateFloorException ignore) {
        } catch(NoFloorBelowException e) {
        } catch(FloorTooSmallException ignore) {
        }
        assertTrue(thrown);
    }

    @Test
    public void addFloorFloorNumberZeroTest() {
        boolean thrown = false;
        try {
            Floor floor1 = new Floor(0, 10, 10);
            building1.addFloor(floor1);
        } catch(IllegalArgumentException ignore) {
            thrown = true;
        } catch(DuplicateFloorException ignore) {
        } catch(NoFloorBelowException e) {
        } catch(FloorTooSmallException ignore) {
        }
        assertTrue(thrown);
    }

    @Test
    public void addFloorInvalidWidthTest() {
        // Minimum floor width should have been set to 5 in Floor class
        boolean thrown = false;
        try {
            Floor floor1 = new Floor(1, 3, 10);
            building1.addFloor(floor1);
        } catch(IllegalArgumentException ignore) {
            thrown = true;
        } catch(DuplicateFloorException ignore) {
        } catch(NoFloorBelowException e) {
        } catch(FloorTooSmallException ignore) {
        }
        assertTrue(thrown);
    }

    @Test
    public void addFloorInvalidLengthTest() {
        // Minimum floor length should have been set to 5 in Floor class
        boolean thrown = false;
        try {
            Floor floor1 = new Floor(1, 10, 3);
            building1.addFloor(floor1);
        } catch(IllegalArgumentException ignore) {
            thrown = true;
        } catch(DuplicateFloorException ignore) {
        } catch(NoFloorBelowException e) {
        } catch(FloorTooSmallException ignore) {
        }
        assertTrue(thrown);
    }

    @Test
    public void addFloorDuplicateTest() {
        boolean thrown = false;
        try {
            Floor floor1 = new Floor(1, 10, 10);
            building1.addFloor(floor1);
            building1.addFloor(floor1);
        } catch(IllegalArgumentException ignore) {
        } catch(DuplicateFloorException e) {
            thrown = true;
        } catch(NoFloorBelowException ignore) {
        } catch(FloorTooSmallException ignore) {
        }
        assertTrue(thrown);
    }
    @Test
    public void addFloorNoDuplicateTest() {
        try {
            Floor floor1 = new Floor(1, 10, 10);
            Floor floor2 = new Floor(2, 10, 10);
            building1.addFloor(floor1);
            building1.addFloor(floor2);
        } catch(IllegalArgumentException ignore) {
        } catch(DuplicateFloorException e) {
            fail(); // should not throw an exception as the case is valid
        } catch(NoFloorBelowException ignore) {
        } catch(FloorTooSmallException ignore) {
        }
    }

    @Test
    public void addFloorNoFloorBelowNoGroundFloorTest() {
        boolean thrown = false;
        try {
            Floor floor1 = new Floor(2, 10, 10);
            building1.addFloor(floor1);
        } catch(IllegalArgumentException ignore) {
        } catch(DuplicateFloorException ignore) {
        } catch(NoFloorBelowException e) {
            thrown = true;
        } catch(FloorTooSmallException ignore) {
        }
        assertTrue(thrown);
    }

    @Test
    public void addFloorNoFloorBelowHasGroundFloorTest() {
        boolean thrown = false;
        try {
            Floor floor1 = new Floor(1, 10, 10);
            Floor floor2 = new Floor(3, 10, 10);
            building1.addFloor(floor1);
            building1.addFloor(floor2);
        } catch(IllegalArgumentException ignore) {
        } catch(DuplicateFloorException ignore) {
        } catch(NoFloorBelowException e) {
            thrown = true;
        } catch(FloorTooSmallException ignore) {
        }
        assertTrue(thrown);
    }

    @Test
    public void addFloorFloorIsBelowTest() {
        try {
            Floor floor1 = new Floor(1, 10, 10);
            Floor floor2 = new Floor(2, 10, 10);
            building1.addFloor(floor1);
            building1.addFloor(floor2);
        } catch(IllegalArgumentException ignore) {
        } catch(DuplicateFloorException ignore) {
        } catch(NoFloorBelowException e) {
            fail(); // should not throw an exception as the case is valid
        } catch(FloorTooSmallException ignore) {
        }
    }

    @Test
    public void addFloorInsufficientAreaBelowTest() {
        boolean thrown = false;
        try {
            Floor floor1 = new Floor(1, 10, 10);
            Floor floor2 = new Floor(2, 15, 15);
            building1.addFloor(floor1);
            building1.addFloor(floor2);
        } catch(IllegalArgumentException ignore) {
        } catch(DuplicateFloorException e) {
        } catch(NoFloorBelowException ignore) {
        } catch(FloorTooSmallException ignore) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void addFloorSufficientAreaBelowSameAreaTest() {
        try {
            Floor floor1 = new Floor(1, 10, 10);
            Floor floor2 = new Floor(2, 10, 10);
            building1.addFloor(floor1);
            building1.addFloor(floor2);
        } catch(IllegalArgumentException ignore) {
        } catch(DuplicateFloorException e) {
        } catch(NoFloorBelowException ignore) {
        } catch(FloorTooSmallException ignore) {
            fail(); // should not throw an exception as the case is valid
        }
    }

    @Test
    public void addFloorSufficientAreaBelowMoreAreaTest() {
        try {
            Floor floor1 = new Floor(1, 10, 10);
            Floor floor2 = new Floor(2, 8, 9);
            building1.addFloor(floor1);
            building1.addFloor(floor2);
        } catch(IllegalArgumentException ignore) {
        } catch(DuplicateFloorException e) {
        } catch(NoFloorBelowException ignore) {
        } catch(FloorTooSmallException ignore) {
            fail(); // should not throw an exception as the case is valid
        }
    }

    // **************** fire drill tests ****************

    // when there are no floors a FireDrillException must be thrown
    @Test
    public void fireDrillNoFloorsNoRoomsTest() {
        boolean thrown = false;
        try {
            building1.fireDrill(RoomType.STUDY);
        } catch(FireDrillException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    // when there are floors but no rooms a FireDrillException must be thrown
    @Test
    public void fireDrillFloorsButNoRoomsTest() {
        boolean thrown = false;
        try {
            Floor floor1 = new Floor(1, 10, 10);
            Floor floor2 = new Floor(2, 10, 10);
            building1.addFloor(floor1);
            building1.addFloor(floor2);
            building1.fireDrill(RoomType.STUDY);
        } catch(DuplicateFloorException | NoFloorBelowException |
                FloorTooSmallException ignore) {
        } catch(FireDrillException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void fireDrillMultipleFloorsNoRoomOfTypeTest() {
        try {
            Floor floor1 = new Floor(1, 10, 10);
            Floor floor2 = new Floor(2, 10, 10);
            Room room1 = new Room(1, RoomType.STUDY, 20);
            Room room2 = new Room(2, RoomType.LABORATORY, 20);
            Room room3 = new Room(3, RoomType.STUDY, 20);
            Room room4 = new Room(6, RoomType.LABORATORY, 20);
            floor1.addRoom(room1);
            floor1.addRoom(room2);
            floor1.addRoom(room3);
            floor2.addRoom(room4);
            building1.addFloor(floor1);
            building1.addFloor(floor2);

            building1.fireDrill(RoomType.OFFICE);
        } catch(DuplicateFloorException | NoFloorBelowException |
                FloorTooSmallException ignore) {
        } catch (DuplicateRoomException | InsufficientSpaceException ignore) {
        } catch(FireDrillException e) {
            fail(); // should not throw this exception here
        }

        assertFalse(building1.getFloorByNumber(1).getRoomByNumber(1).fireDrillOngoing());
        assertFalse(building1.getFloorByNumber(1).getRoomByNumber(2).fireDrillOngoing());
        assertFalse(building1.getFloorByNumber(1).getRoomByNumber(3).fireDrillOngoing());
        assertFalse(building1.getFloorByNumber(2).getRoomByNumber(6).fireDrillOngoing());
    }

    @Test
    public void fireDrillMultipleFloorsStudyOnlyTest() {
        try {
            Floor floor1 = new Floor(1, 10, 10);
            Floor floor2 = new Floor(2, 10, 10);
            Room room1 = new Room(1, RoomType.STUDY, 20);
            Room room2 = new Room(2, RoomType.LABORATORY, 20);
            Room room3 = new Room(3, RoomType.OFFICE, 20);
            Room room4 = new Room(6, RoomType.LABORATORY, 20);
            Room room5 = new Room(7, RoomType.STUDY, 20);
            floor1.addRoom(room1);
            floor1.addRoom(room2);
            floor1.addRoom(room3);
            floor2.addRoom(room4);
            floor2.addRoom(room5);
            building1.addFloor(floor1);
            building1.addFloor(floor2);

            building1.fireDrill(RoomType.STUDY);
        } catch(DuplicateFloorException | NoFloorBelowException |
                FloorTooSmallException ignore) {
        } catch (DuplicateRoomException | InsufficientSpaceException ignore) {
        } catch(FireDrillException e) {
            fail(); // should not throw this exception here
        }

        assertTrue(building1.getFloorByNumber(1).getRoomByNumber(1).fireDrillOngoing());
        assertFalse(building1.getFloorByNumber(1).getRoomByNumber(2).fireDrillOngoing());
        assertFalse(building1.getFloorByNumber(1).getRoomByNumber(3).fireDrillOngoing());
        assertFalse(building1.getFloorByNumber(2).getRoomByNumber(6).fireDrillOngoing());
        assertTrue(building1.getFloorByNumber(2).getRoomByNumber(7).fireDrillOngoing());
    }

    @Test
    public void fireDrillMultipleFloorsLaboratoryOnlyTest() {
        try {
            Floor floor1 = new Floor(1, 10, 10);
            Floor floor2 = new Floor(2, 10, 10);
            Room room1 = new Room(1, RoomType.STUDY, 20);
            Room room2 = new Room(2, RoomType.LABORATORY, 20);
            Room room3 = new Room(3, RoomType.OFFICE, 20);
            Room room4 = new Room(6, RoomType.LABORATORY, 20);
            Room room5 = new Room(7, RoomType.STUDY, 20);
            floor1.addRoom(room1);
            floor1.addRoom(room2);
            floor1.addRoom(room3);
            floor2.addRoom(room4);
            floor2.addRoom(room5);
            building1.addFloor(floor1);
            building1.addFloor(floor2);

            building1.fireDrill(RoomType.LABORATORY);
        } catch(DuplicateFloorException | NoFloorBelowException |
                FloorTooSmallException ignore) {
        } catch (DuplicateRoomException | InsufficientSpaceException ignore) {
        } catch(FireDrillException e) {
            fail(); // should not throw this exception here
        }

        assertFalse(building1.getFloorByNumber(1).getRoomByNumber(1).fireDrillOngoing());
        assertTrue(building1.getFloorByNumber(1).getRoomByNumber(2).fireDrillOngoing());
        assertFalse(building1.getFloorByNumber(1).getRoomByNumber(3).fireDrillOngoing());
        assertTrue(building1.getFloorByNumber(2).getRoomByNumber(6).fireDrillOngoing());
        assertFalse(building1.getFloorByNumber(2).getRoomByNumber(7).fireDrillOngoing());
    }

    @Test
    public void fireDrillMultipleFloorsOfficeOnlyTest() {
        try {
            Floor floor1 = new Floor(1, 10, 10);
            Floor floor2 = new Floor(2, 10, 10);
            Room room1 = new Room(1, RoomType.STUDY, 20);
            Room room2 = new Room(2, RoomType.LABORATORY, 20);
            Room room3 = new Room(3, RoomType.OFFICE, 20);
            Room room4 = new Room(6, RoomType.LABORATORY, 20);
            Room room5 = new Room(7, RoomType.STUDY, 20);
            floor1.addRoom(room1);
            floor1.addRoom(room2);
            floor1.addRoom(room3);
            floor2.addRoom(room4);
            floor2.addRoom(room5);
            building1.addFloor(floor1);
            building1.addFloor(floor2);

            building1.fireDrill(RoomType.LABORATORY);
        } catch(DuplicateFloorException | NoFloorBelowException |
                FloorTooSmallException ignore) {
        } catch (DuplicateRoomException | InsufficientSpaceException ignore) {
        } catch(FireDrillException e) {
            fail(); // should not throw this exception here
        }

        assertFalse(building1.getFloorByNumber(1).getRoomByNumber(1).fireDrillOngoing());
        assertTrue(building1.getFloorByNumber(1).getRoomByNumber(2).fireDrillOngoing());
        assertFalse(building1.getFloorByNumber(1).getRoomByNumber(3).fireDrillOngoing());
        assertTrue(building1.getFloorByNumber(2).getRoomByNumber(6).fireDrillOngoing());
        assertFalse(building1.getFloorByNumber(2).getRoomByNumber(7).fireDrillOngoing());
    }

    @Test
    public void fireDrillMultipleFloorsAllRoomsTest() {
        try {
            Floor floor1 = new Floor(1, 10, 10);
            Floor floor2 = new Floor(2, 10, 10);
            Room room1 = new Room(1, RoomType.STUDY, 20);
            Room room2 = new Room(2, RoomType.LABORATORY, 20);
            Room room3 = new Room(3, RoomType.OFFICE, 20);
            Room room4 = new Room(6, RoomType.LABORATORY, 20);
            Room room5 = new Room(7, RoomType.STUDY, 20);
            floor1.addRoom(room1);
            floor1.addRoom(room2);
            floor1.addRoom(room3);
            floor2.addRoom(room4);
            floor2.addRoom(room5);
            building1.addFloor(floor1);
            building1.addFloor(floor2);

            building1.fireDrill(null);
        } catch(DuplicateFloorException | NoFloorBelowException |
                FloorTooSmallException ignore) {
        } catch (DuplicateRoomException | InsufficientSpaceException ignore) {
        } catch(FireDrillException e) {
            fail(); // should not throw this exception here
        }

        assertTrue(building1.getFloorByNumber(1).getRoomByNumber(1).fireDrillOngoing());
        assertTrue(building1.getFloorByNumber(1).getRoomByNumber(2).fireDrillOngoing());
        assertTrue(building1.getFloorByNumber(1).getRoomByNumber(3).fireDrillOngoing());
        assertTrue(building1.getFloorByNumber(2).getRoomByNumber(6).fireDrillOngoing());
        assertTrue(building1.getFloorByNumber(2).getRoomByNumber(7).fireDrillOngoing());
    }

    // **************** cancelFireDrill tests ****************

    @Test
    public void cancelFireDrillTest() {
        try {
            Floor floor1 = new Floor(1, 10, 10);
            Floor floor2 = new Floor(2, 10, 10);
            Room room1 = new Room(1, RoomType.STUDY, 20);
            Room room2 = new Room(2, RoomType.LABORATORY, 20);
            Room room3 = new Room(3, RoomType.OFFICE, 20);
            Room room4 = new Room(6, RoomType.LABORATORY, 20);
            Room room5 = new Room(7, RoomType.STUDY, 20);
            floor1.addRoom(room1);
            floor1.addRoom(room2);
            floor1.addRoom(room3);
            floor2.addRoom(room4);
            floor2.addRoom(room5);
            building1.addFloor(floor1);
            building1.addFloor(floor2);

            building1.fireDrill(null);

            building1.cancelFireDrill();
        } catch(DuplicateFloorException | NoFloorBelowException |
                FloorTooSmallException | DuplicateRoomException |
                InsufficientSpaceException ignore) {
        } catch(FireDrillException e) {
            fail(); // should not throw this exception here
        }

        assertFalse(building1.getFloorByNumber(1).getRoomByNumber(1).fireDrillOngoing());
        assertFalse(building1.getFloorByNumber(1).getRoomByNumber(2).fireDrillOngoing());
        assertFalse(building1.getFloorByNumber(1).getRoomByNumber(3).fireDrillOngoing());
        assertFalse(building1.getFloorByNumber(2).getRoomByNumber(6).fireDrillOngoing());
        assertFalse(building1.getFloorByNumber(2).getRoomByNumber(7).fireDrillOngoing());
    }

    // **************** toString tests ****************

    @Test
    public void toStringBasicTest() {
//        assertEquals("Building: name=\"GP South\", floors=0",
//                building1.toString());
            assertEquals("Building:name=GPSouth,floors=0",
                    building1.toString().replace(" ", "").replace("\"", ""));
    }

    @Test
    public void toStringAdvancedTest() {
        try {
            building1.addFloor(new Floor(1, 10, 10));
            building1.addFloor(new Floor(2, 10, 8));
            building1.addFloor(new Floor(3, 8, 5));
        } catch (DuplicateFloorException | NoFloorBelowException | FloorTooSmallException e) {
            fail();
        }
//        assertEquals("Building: name=\"GP South\", floors=3",
//                building1.toString());
        assertEquals("Building:name=GPSouth,floors=3",
                building1.toString().replace(" ", "").replace("\"", ""));
    }
}
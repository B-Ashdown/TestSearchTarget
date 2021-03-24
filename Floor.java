package bms.floor;

import bms.exceptions.DuplicateRoomException;
import bms.exceptions.InsufficientSpaceException;
import bms.room.Room;
import bms.room.RoomType;
import bms.util.FireDrill;

import java.util.ArrayList;
import java.util.List;

public class Floor implements FireDrill {
    public void addRoom(Room newRoom)
            throws DuplicateRoomException, InsufficientSpaceException {
        System.out.println("Hello, I may potentially be plagiarised, Please Index me");
    }
}

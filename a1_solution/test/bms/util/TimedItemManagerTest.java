package bms.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class TimedItemManagerTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);  // Fail any test that takes longer than 1 second to execute.

    private class DummyTimedItem implements TimedItem {
        int counter = 0;

        DummyTimedItem() {
            TimedItemManager.getInstance().registerTimedItem(this);
        }

        @Override
        public void elapseOneMinute() {
            this.counter++;
        }
    }

    @Test
    public void singletonTest() {
        TimedItemManager manager1 = TimedItemManager.getInstance();
        TimedItemManager manager2 = TimedItemManager.getInstance();
        assertSame(manager1, manager2);
    }

    @Test
    public void elapseOneMinuteTest() {
        DummyTimedItem[] items = new DummyTimedItem[4];
        for (int i = 0; i < 4; ++i) {
            items[i] = new DummyTimedItem();
        }
        for (int i = 0; i < 4; ++i) {
            assertEquals(0, items[i].counter);
        }

        TimedItemManager.getInstance().elapseOneMinute();
        for (int i = 0; i < 4; ++i) {
            assertEquals(1, items[i].counter);
        }
        TimedItemManager.getInstance(); // shouldn't affect anything
        for (int i = 0; i < 4; ++i) {
            assertEquals(1, items[i].counter);
        }

        TimedItemManager.getInstance().elapseOneMinute();
        TimedItemManager.getInstance(); // shouldn't affect anything
        TimedItemManager.getInstance(); // shouldn't affect anything
        for (int i = 0; i < 4; ++i) {
            assertEquals(2, items[i].counter);
        }

        // Add a new timed item after elapsing some time
        DummyTimedItem newAddition = new DummyTimedItem();
        TimedItemManager.getInstance().elapseOneMinute();
        for (int i = 0; i < 4; ++i) {
            assertEquals(3, items[i].counter);
        }
        assertEquals(1, newAddition.counter);
    }
}

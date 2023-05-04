package comp3350.ppa.tests.business;

import junit.framework.TestCase;

import comp3350.ppa.application.Services;
import comp3350.ppa.business.GlobalTimerAccess;

public class GlobalTimerTest extends TestCase {
    public void tearDown() {
        Services.closeGlobalTimerAccess();
    }

    public void testTypical() {
        GlobalTimerAccess timer = Services.createGlobalTimerAccess();

        System.out.println("Starting GlobalTimerTest: typical cases");

        assertFalse(timer.isRunning());
        assertEquals(0, timer.getSecondsRemaining());
        assertEquals("00:00", timer.getRelativeTimeString());
        timer.setSeconds(30);
        assertEquals(30, timer.getSecondsRemaining());
        assertEquals("00:30", timer.getRelativeTimeString());

        timer.start(1000);
        assertTrue(timer.isRunning());
        timer.stop();
        assertFalse(timer.isRunning());

        timer.start(0);
        assertEquals(30, timer.getSecondsRemaining(0));
        assertEquals(25, timer.getSecondsRemaining(5000));
        assertEquals(10, timer.getSecondsRemaining(20000));
        assertEquals(0, timer.getSecondsRemaining(30000));
        assertTrue(timer.isRunning());

        timer.setSeconds(2 * 60);
        timer.start(10000); // Start while started updates start time
        assertTrue(timer.isRunning());
        assertEquals(120, timer.getSecondsRemaining(10000));
        assertEquals("02:00", timer.getRelativeTimeString(10000));
        assertEquals(105, timer.getSecondsRemaining(25000));
        assertEquals("01:45", timer.getRelativeTimeString(25000));
        assertEquals(85, timer.getSecondsRemaining(45000));
        assertEquals("01:25", timer.getRelativeTimeString(45000));
        assertEquals(0, timer.getSecondsRemaining(130000));
        assertEquals("00:00", timer.getRelativeTimeString(130000));

        timer.setSeconds(12 * 60); // Seconds remaining shouldn't update until start is called
        assertTrue(timer.isRunning());
        assertEquals(120, timer.getSecondsRemaining(10000));
        assertEquals("02:00", timer.getRelativeTimeString(10000));
        assertEquals(105, timer.getSecondsRemaining(25000));
        assertEquals("01:45", timer.getRelativeTimeString(25000));
        assertEquals(85, timer.getSecondsRemaining(45000));
        assertEquals("01:25", timer.getRelativeTimeString(45000));
        assertEquals(0, timer.getSecondsRemaining(130000));
        assertEquals("00:00", timer.getRelativeTimeString(130000));

        timer.start(20000); // Start with 12 minutes
        assertEquals(12 * 60, timer.getSecondsRemaining(20000));
        assertEquals("12:00", timer.getRelativeTimeString(20000));
        assertEquals(11 * 60, timer.getSecondsRemaining(80000));
        assertEquals("11:00", timer.getRelativeTimeString(80000));
        assertEquals(10 * 60 + 35, timer.getSecondsRemaining(105000));
        assertEquals("10:35", timer.getRelativeTimeString(105000));

        timer.setSeconds(61 * 60); // Over an hour
        timer.start(0);
        assertEquals(61 * 60, timer.getSecondsRemaining(0));
        assertEquals("61:00", timer.getRelativeTimeString(0));
        assertEquals(60 * 60 + 25, timer.getSecondsRemaining(35000));
        assertEquals("60:25", timer.getRelativeTimeString(35000));

        Services.closeGlobalTimerAccess();
        System.out.println("Finished GlobalTimerTest: typical cases");
    }

    public void testPastEnd() {
        GlobalTimerAccess timer = Services.createGlobalTimerAccess();

        System.out.println("Starting GlobalTimerTest: past timer end");

        timer.setSeconds(10);
        timer.start(0);
        assertTrue(timer.isRunning());
        assertEquals(0, timer.getSecondsRemaining(25000));
        assertEquals("00:00", timer.getRelativeTimeString(25000));
        assertEquals(0, timer.getSecondsRemaining(100000));
        assertEquals("00:00", timer.getRelativeTimeString(100000));
        assertTrue(timer.isRunning()); // Timer does not automatically stop

        Services.closeGlobalTimerAccess();
        System.out.println("Finished GlobalTimerTest: past timer end");
    }

    public void testBeforeStart() {
        GlobalTimerAccess timer = Services.createGlobalTimerAccess();

        System.out.println("Starting GlobalTimerTest: before timer start");

        timer.setSeconds(10);
        timer.start(20000);
        assertTrue(timer.isRunning());
        assertEquals(20, timer.getSecondsRemaining(10000));
        assertEquals("00:20", timer.getRelativeTimeString(10000));
        assertEquals(30, timer.getSecondsRemaining(0));
        assertEquals("00:30", timer.getRelativeTimeString(0));

        Services.closeGlobalTimerAccess();
        System.out.println("Finished GlobalTimerTest: before timer start");
    }

    public void testNonPositive() {
        GlobalTimerAccess timer = Services.createGlobalTimerAccess();

        System.out.println("Starting GlobalTimerTest: non-positive inputs");

        try {
            timer.setSeconds(0);
            timer.start();
            fail("Expected IllegalArgumentException from starting timer with 0 value");
        } catch (IllegalArgumentException ignored) {}

        try {
            timer.setSeconds(-1);
            fail("Expected IllegalArgumentException from negative value");
        } catch (IllegalArgumentException ignored) {}

        try {
            timer.getSecondsRemaining(-1);
            fail("Expected IllegalArgumentException from negative value");
        } catch (IllegalArgumentException ignored) {}

        try {
            timer.start(-1);
            fail("Expected IllegalArgumentException from negative value");
        } catch (IllegalArgumentException ignored) {}

        Services.closeGlobalTimerAccess();
        System.out.println("Finished GlobalTimerTest: negative inputs");
    }

    public void testDoubleStop() {
        GlobalTimerAccess timer = Services.createGlobalTimerAccess();

        System.out.println("Starting GlobalTimerTest: double stop");
        timer.setSeconds(10);
        assertFalse(timer.isRunning());
        timer.start();
        assertTrue(timer.isRunning());
        timer.stop();
        assertFalse(timer.isRunning());
        timer.stop();
        assertFalse(timer.isRunning());

        Services.closeGlobalTimerAccess();
        System.out.println("Finished GlobalTimerTest: double stop");
    }
}

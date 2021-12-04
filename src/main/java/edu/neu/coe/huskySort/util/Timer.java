package edu.neu.coe.huskySort.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Timer {

    /**
     * Construct a new Timer and set it running.
     */
    public Timer() {
        resume();
    }

    /**
     * Run the given function n times, once per "lap" and then return the result of calling stop().
     *
     * @param n        the number of repetitions.
     * @param function a function which yields a T (T may be Void).
     * @return the average milliseconds per repetition.
     */
    public <T> double repeat(final int n, final Supplier<T> function) {
        for (int i = 0; i < n; i++) {
            function.get();
            lap();
        }
        pause();
        return meanLapTime();
    }

    /**
     * Run the given functions n times, once per "lap" and then return the result of calling stop().
     *
     * @param n        the number of repetitions.
     * @param supplier a function which supplies a different T value for each repetition.
     * @param function a function T=>U and which is to be timed (U may be Void).
     * @return the average milliseconds per repetition.
     */
    public <T, U> double repeat(final int n, final Supplier<T> supplier, final Function<T, U> function) {
        return repeat(n, supplier, function, null, null);
    }

    /**
     * Pause (without counting a lap); run the given functions n times while being timed, i.e. once per "lap", and finally return the result of calling meanLapTime().
     *
     * @param n            the number of repetitions.
     * @param supplier     a function which supplies a T value.
     * @param function     a function T=>U and which is to be timed.
     * @param preFunction  a function which pre-processes a T value and which precedes the call of function, but which is not timed (may be null).
     * @param postFunction a function which consumes a U and which succeeds the call of function, but which is not timed (may be null).
     * @return the average milliseconds per repetition.
     */
    public <T, U> double repeat(final int n, final Supplier<T> supplier, final Function<T, U> function, final UnaryOperator<T> preFunction, final Consumer<U> postFunction) {
        if (n > 0) logger.trace("repeat: with " + n + " runs");
        else logger.warn("repeat: zero runs");
        pause();
        for (int i = 0; i < n; i++) {
            final T t = supplier.get();
            final T t1 = preFunction != null ? preFunction.apply(t) : t;
            resume();
            final U u = function.apply(t1);
            pauseAndLap();
            if (postFunction != null) postFunction.accept(u);
        }
        return meanLapTime();
    }

    /**
     * Stop this Timer and return the mean lap time in milliseconds.
     *
     * @return the average milliseconds used by each lap.
     * @throws TimerException if this Timer is not running.
     */
    public double stop() {
        pauseAndLap();
        return meanLapTime();
    }

    /**
     * Return the mean lap time in milliseconds for this paused timer.
     *
     * @return the average milliseconds used by each lap.
     * @throws TimerException if this Timer is running.
     */
    public double meanLapTime() {
        if (running) throw new TimerException();
        return toMillisecs(ticks) / laps;
    }

    /**
     * Pause this timer at the end of a "lap" (repetition).
     * The lap counter will be incremented by one.
     *
     * @throws TimerException if this Timer is not running.
     */
    public void pauseAndLap() {
        lap();
        ticks += getClock();
        running = false;
    }

    /**
     * Resume this timer to begin a new "lap" (repetition).
     *
     * @throws TimerException if this Timer is already running.
     */
    public void resume() {
        if (running) throw new TimerException();
        ticks -= getClock();
        running = true;
    }

    /**
     * Increment the lap counter without pausing.
     * This is the equivalent of calling pause and resume.
     *
     * @throws TimerException if this Timer is not running.
     */
    public void lap() {
        if (!running) throw new TimerException();
        laps++;
    }

    /**
     * Pause this timer during a "lap" (repetition).
     * The lap counter will remain the same.
     *
     * @throws TimerException if this Timer is not running.
     */
    public void pause() {
        pauseAndLap();
        laps--;
    }

    /**
     * Method to yield the total number of milliseconds elapsed.
     * NOTE: an exception will be thrown if this is called while the timer is running.
     *
     * @return the total number of milliseconds elapsed for this timer.
     */
    public double millisecs() {
        if (running) throw new TimerException();
        return toMillisecs(ticks);
    }

    @Override
    public String toString() {
        return "Timer{" +
                "ticks=" + ticks +
                ", laps=" + laps +
                ", running=" + running +
                '}';
    }

    private long ticks = 0L;
    private int laps = 0;
    private boolean running = false;

    // NOTE: Used by unit tests
    private long getTicks() {
        return ticks;
    }

    // NOTE: Used by unit tests
    private int getLaps() {
        return laps;
    }

    // NOTE: Used by unit tests
    private boolean isRunning() {
        return running;
    }

    /**
     * Get the number of ticks from the system clock.
     * <p>
     * NOTE: (Maintain consistency) There are two system methods for getting the clock time.
     * Ensure that this method is consistent with toMillisecs.
     *
     * @return the number of ticks for the system clock. Currently defined as nano time.
     */
    private static long getClock() {
        return System.nanoTime();
    }

    /**
     * NOTE: (Maintain consistency) There are two system methods for getting the clock time.
     * Ensure that this method is consistent with getTicks.
     *
     * @param ticks the number of clock ticks -- currently in nanoseconds.
     * @return the corresponding number of milliseconds.
     */
    private static double toMillisecs(final long ticks) {
        return ticks / 1e6;
    }

    final static LazyLogger logger = new LazyLogger(Timer.class);

    static class TimerException extends RuntimeException {
        public TimerException() {
        }

        public TimerException(final String message) {
            super(message);
        }

        public TimerException(final String message, final Throwable cause) {
            super(message, cause);
        }

        public TimerException(final Throwable cause) {
            super(cause);
        }
    }
}
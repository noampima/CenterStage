package org.firstinspires.ftc.teamcode.inversekinematics;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RampLib<T extends Comparable<T>>
{
    private T A;
    private T B;
    private T val;
    private ramp_mode mode;
    private long t;
    private long dur;
    private long pos;
    private long grain;
    private loop_mode loop;
    private speed_mode speed;
    private boolean paused;
    private boolean automated;

    public enum ramp_mode {
        LINEAR,
        QUADRATIC_IN,
        QUADRATIC_OUT,
        QUADRATIC_INOUT,
        CUBIC_IN,
        CUBIC_OUT,
        CUBIC_INOUT,
        NONE
        // Add other ramp modes here
    }

    public enum loop_mode {
        ONCEFORWARD,
        ONCEBACKWARD,
        LOOPFORWARD,
        LOOPBACKWARD,
        FORTHANDBACK,
        BACKANDFORTH
    }

    public enum speed_mode {
        FORWARD,
        BACKWARD
    }

    public RampLib() {
        init(null); // Modify the argument type as needed
    }

    public RampLib(T _val) {
        init(_val);
    }

    public void init(T _val) {
        A = _val;
        B = _val;
        val = _val;
        mode = ramp_mode.NONE;
        t = System.currentTimeMillis();
        dur = 0;
        pos = 0;
        grain = 10;
        loop = loop_mode.ONCEFORWARD;
        speed = speed_mode.FORWARD;
        paused = false;
        automated = true;
    }

    public T update() {
        boolean doUpdate = true;
        long newTime = 0;
        long delta = grain;

        if (automated) {
            newTime = System.currentTimeMillis();
            delta = newTime - t;
            doUpdate = delta >= grain;
        }

        if (mode != ramp_mode.NONE && doUpdate) {
            t = newTime;
            if (!paused) {
                if (speed == speed_mode.FORWARD) {
                    if ((long) pos + delta < dur) {
                        pos += delta;
                    } else {
                        pos = dur;
                    }
                } else if (speed == speed_mode.BACKWARD) {
                    if ((long) pos - delta > 0) {
                        pos -= delta;
                    } else {
                        pos = 0;
                    }
                }

                if (mode != ramp_mode.NONE && dur > 0 && !A.equals(B)) {
                    double k = (double) pos / (double) dur;
                    B.compareTo(A);
                    if (B.compareTo(A) >= 0) {
                        val = calculateValue(A, B, k, mode);
                    } else {
                        val = calculateValue(B, A, 1 - k, mode);
                    }
                } else {
                    val = B;
                }
            }

            if (isFinished()) {
                switch (loop) {
                    case LOOPFORWARD:
                        pos = 0;
                        break;
                    case LOOPBACKWARD:
                        pos = dur;
                        break;
                    case FORTHANDBACK:
                    case BACKANDFORTH:
                        switch (speed) {
                            case FORWARD:
                                speed = speed_mode.BACKWARD;
                                break;
                            case BACKWARD:
                                speed = speed_mode.FORWARD;
                                break;
                        }
                        break;

                    case ONCEBACKWARD:
                    case ONCEFORWARD:
                    default:
                        break;
                }
            }
        }

        return val;
    }

    private T calculateValue(T a, T b, double k, ramp_mode mode) {
        try {
            Method compareTo = a.getClass().getMethod("compareTo", a.getClass());
            int comparison = (int) compareTo.invoke(a, b);

            if (comparison == 0) {
                return a;  // a and b are equal
            } else if (comparison < 0) {
                Method doubleValue = a.getClass().getMethod("doubleValue");
                double aValue = (double) doubleValue.invoke(a);
                double bValue = (double) doubleValue.invoke(b);

                double result = aValue + (bValue - aValue) * rampCalc(k, mode);

                Method valueOf = a.getClass().getMethod("valueOf", double.class);
                return (T) valueOf.invoke(a, result);
            } else {
                Method doubleValue = a.getClass().getMethod("doubleValue");
                double aValue = (double) doubleValue.invoke(a);
                double bValue = (double) doubleValue.invoke(b);

                double result = aValue - (aValue - bValue) * rampCalc(k, mode);

                Method valueOf = a.getClass().getMethod("valueOf", double.class);
                return (T) valueOf.invoke(a, result);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public T go(T _val) {
        return go(_val, 0, ramp_mode.NONE);
    }

    public T go(T _val, long _dur) {
        return go(_val, _dur, ramp_mode.LINEAR);
    }

    public T go(T _val, long _dur, ramp_mode _mode) {
        return go(_val, _dur, _mode, loop_mode.ONCEFORWARD);
    }

    public T go(T _val, long _dur, ramp_mode _mode, loop_mode _loop) {
        A = val;
        B = _val;
        mode = _mode;
        dur = _dur;
        t = System.currentTimeMillis();

        if (_dur == 0) {
            val = B;
        }

        if (_loop.ordinal() < loop_mode.ONCEBACKWARD.ordinal()) {
            pos = 0;
            speed = speed_mode.FORWARD;
        } else {
            pos = dur;
            speed = speed_mode.BACKWARD;
        }
        loop = _loop;
        paused = false;
        return val;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public boolean isFinished() {
        if (speed == speed_mode.FORWARD) {
            return pos == dur;
        } else if (speed == speed_mode.BACKWARD) {
            return pos == 0;
        }
        return false;
    }

    public boolean isRunning() {
        return !isFinished() && !paused;
    }

    public boolean isPaused() {
        return !paused;
    }

    public void setGrain(long _grain) {
        grain = _grain;
    }

    public void setAutomation(boolean _automated) {
        automated = _automated;
    }

    public double getCompletion() {
        double val = pos * 10000.0 / dur;
        val /= 100.0;
        return val;
    }

    public long getDuration() {
        return dur;
    }

    public long getPosition() {
        return pos;
    }

    public T getValue() {
        return val;
    }

    public T getOrigin() {
        return A;
    }

    public T getTarget() {
        return B;
    }

    private double rampCalc(double k, ramp_mode m)
    {
        if (k == 0 || k == 1) {
            return k;
        }

        if(m == ramp_mode.LINEAR)
        {
            return k;
        }

        return k;
    }
}

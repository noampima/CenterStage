package org.firstinspires.ftc.teamcode.testing.inversekinematics;

public class Interpolation
{
    RampLib<Integer> myRamp;
    int interpolationFlag = 0;
    int savedValue;

    int go(int input, int duration) {

        if (input != savedValue) {   // check for new data
            interpolationFlag = 0;
        }
        savedValue = input;          // bookmark the old value

        if (interpolationFlag == 0) {                                        // only do it once until the flag is reset
            myRamp.go(input, duration, RampLib.ramp_mode.LINEAR, RampLib.loop_mode.ONCEFORWARD);              // start interpolation (value to go to, duration)
            interpolationFlag = 1;
        }

        int output = myRamp.update();
        return output;
    }
}

package org.firstinspires.ftc.teamcode.inversekinematics;

import com.ThermalEquilibrium.homeostasis.Controllers.Feedback.BasicPID;
import com.ThermalEquilibrium.homeostasis.Parameters.PIDCoefficients;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Config
public class HandPID extends LinearOpMode {
    Interpolation interpX, interpZ;
    Servo servoHandRight, serverHandLeft;
    Servo servoClaw;
    public static int xTarget = 50, zTarget = 100;

    public static double kPx = 0.0001, kIx = 0, kDx = 0;
    public static double kPz = 0.0001, kIz = 0, kDz = 0;
    public static double upperLength = 50; // hand
    public static double lowerLength = 100; // claw

    double currentMillis, previousMillis;

    double x = 0,z = 0;

    double shoulderAngle2a, shoulderAngle2aDegrees, shoulderAngle2, shoulderAngle2Degrees, shoulderPos2, z2, shoulderAngle1a;
    double shoulderAngle1, elbowAngle, shoulderAngleDegrees, elbowAngleDegrees, elbowPos, shoulderPos;

    @Override
    public void runOpMode() throws InterruptedException {
        ElapsedTime opmodeRunTime = new ElapsedTime();
        PIDCoefficients coefficientsX = new PIDCoefficients(kPx,kIx,kDx);
        PIDCoefficients coefficientsZ = new PIDCoefficients(kPz,kIz,kDz);
        BasicPID controllerX = new BasicPID(coefficientsX);
        BasicPID controllerZ = new BasicPID(coefficientsZ);

        servoHandRight = hardwareMap.get(Servo.class, "sHR");
        serverHandLeft = hardwareMap.get(Servo.class, "sHL");
        servoClaw = hardwareMap.get(Servo.class, "sC");

        waitForStart();

        while (opModeIsActive()) {
            currentMillis = opmodeRunTime.milliseconds();

            if (currentMillis - previousMillis >= 10) {  // start timed loop
                previousMillis = currentMillis;


                z = controllerZ.calculate(zTarget, z);
                x = controllerX.calculate(zTarget, x);

                // *** Inverse Kinematics ***
                // calculate modification to shoulder angle and arm length

                shoulderAngle2a = Math.atan(z / x);
                shoulderAngle2aDegrees = shoulderAngle2a * (180 / Math.PI);    // degrees
                shoulderAngle2 = shoulderAngle2a - 0.7853908;  // take away the default 45' offset (in radians)
                shoulderAngle2Degrees = shoulderAngle2 * (180 / Math.PI);    // degrees
                shoulderPos2 = Range.scale(shoulderAngle2Degrees, -360, 360, -1, 1);

                z2 = x / Math.cos(shoulderAngle2a);     // calc new arm length to feed to the next bit of code below

                // ****************

                // calculate arm length based on upper/lower length and elbow and shoulder angle
                shoulderAngle1a = (Math.pow(upperLength, 2) + Math.pow(z2, 2) - Math.pow(lowerLength, 2)) / (2 * upperLength * z2);
                shoulderAngle1 = Math.acos(shoulderAngle1a);     // radians
                elbowAngle = Math.PI - (shoulderAngle1 * 2);       // radians

                // calc degrees from angles
                shoulderAngleDegrees = shoulderAngle1 * (180 / Math.PI);    // degrees
                elbowAngleDegrees = elbowAngle * (180 / Math.PI);              // degrees

                // calc milliseconds PWM to drive the servo.
                shoulderPos = Range.scale(shoulderAngleDegrees, -360, 360, -270, 270);
                elbowPos = Range.scale(elbowAngleDegrees, -360, 360, -270, 270);

                // *** end of Inverse Kinematics ***

                shoulderPos -= shoulderPos2;
                shoulderPos = Math.abs(shoulderPos) / 270;

                elbowPos = Math.abs(elbowPos) / 270;

                // write to servos, remove 45' and 90' offsets from arm default position
                servoHandRight.setPosition(shoulderPos);    // hand
                serverHandLeft.setPosition(shoulderPos);    // hand

                servoClaw.setPosition(elbowPos);    // claw

                telemetry.addLine("left hand: " + serverHandLeft.getPosition() + " right hand: " + servoHandRight.getPosition());
                telemetry.addLine("claw: " + servoClaw.getPosition());
                telemetry.addLine("x: " + x + " z: " + z);

                telemetry.update();
            }
        }
    }
}

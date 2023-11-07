package org.firstinspires.ftc.teamcode.testing;

import com.ThermalEquilibrium.homeostasis.Controllers.Feedback.BasicPID;
import com.ThermalEquilibrium.homeostasis.Parameters.PIDCoefficients;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

// TODO: CHECK IF MOTION PROFILE IS GOOD
@Config
@TeleOp(name = "Hand IK with PID")
public class HandPID extends LinearOpMode {

    // Define claw dimensions
    double handLength = 1; // Length from the arm intersection point to the claw in CM
    double clawAngle = 0; // default angle of the claw servo

    Servo servoArmRight, servoArmLeft, servoClaw;

    public static int xTarget = 50, zTarget = 100, angleTarget = 45;

    public static double kpArm = 1, kiArm = 0, kdArm = 0;
    public static double kpClaw = 1, kiClaw = 0, kdClaw = 0;

    // Variables for PID control
    double previousErrorArm = 0.0, integralArm = 0.0, previousErrorClaw = 0.0, integralClaw = 0.0;

    @Override
    public void runOpMode() throws InterruptedException {
        ElapsedTime opmodeRunTime = new ElapsedTime();

        servoArmRight = hardwareMap.get(Servo.class, "sHR");
        servoArmLeft = hardwareMap.get(Servo.class, "sHL");
        servoClaw = hardwareMap.get(Servo.class, "sC");

        moveArm(0);
        servoClaw.setPosition(0.0);

        waitForStart();

        while (opModeIsActive()) {
            ikArmAndClaw(xTarget, zTarget, angleTarget);

            telemetry.addLine("left hand: " + servoArmLeft.getPosition() + " right hand: " + servoArmRight.getPosition());
            telemetry.addLine("claw: " + servoClaw.getPosition());

            telemetry.update();
        }
    }

    public void ikArmAndClaw(double targetX, double targetZ, double targetClawAngle) {
        // Calculate the angle for the arm
        double thetaArm = Math.atan2(targetZ, targetX);

        // Calculate the angle fo r the claw servo using 2D trigonometry
        double d = Math.sqrt(targetX * targetX + targetZ * targetZ);
        double thetaClaw = Math.atan2(targetClawAngle, d - handLength);

        // Perform PID control for the arm
        double errorArm = thetaArm - servoPosToRadians(servoArmRight.getPosition());
        integralArm += errorArm;
        double derivativeArm = errorArm - previousErrorArm;
        double outputArm = kpArm * errorArm + kiArm * integralArm + kdArm * derivativeArm;

        // Perform PID control for the claw
        double errorClaw = thetaClaw - clawAngle;
        integralClaw += errorClaw;
        double derivativeClaw = errorClaw - previousErrorClaw;
        double outputClaw = kpClaw * errorClaw + kiClaw * integralClaw + kdClaw * derivativeClaw;

        // Convert PID outputs to servo positions (adjust for your servo's range)
        double armServoPosition = Math.toDegrees(servoPosToRadians(servoArmRight.getPosition()) + outputArm)/270;
        //double armServoPosition = outputArm;
        double clawServoPosition = Math.toDegrees(clawAngle + outputClaw)/270;

        // Set servo positions
        moveArm(armServoPosition);
        servoClaw.setPosition(clawServoPosition);

        // Update previous error values
        previousErrorArm = errorArm;
        previousErrorClaw = errorClaw;

        // Update the current claw angle
        clawAngle = clawServoPosition;
    }

    void moveArm(double pos)
    {
        servoArmLeft.setPosition(pos);
        servoArmRight.setPosition(pos);
    }

    double servoPosToRadians(double pos)
    {
        return Math.toRadians(pos);
    }
}

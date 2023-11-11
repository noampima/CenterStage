package org.firstinspires.ftc.teamcode.Autonoumos;

import static com.acmerobotics.roadrunner.ftc.Actions.runBlocking;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PosePath;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.Vector2dDual;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RobotHardware;
import org.firstinspires.ftc.teamcode.commands.ElevatorCommand;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Elevator;
import org.slf4j.Marker;


public class Blue extends LinearOpMode {
    private final RobotHardware robot = RobotHardware.getInstance();

    //RIGHT SIDE BLUE AUTO ONLY PRELOAD! (UNTESTED)
    double startPoseX = 10 , startPoseY = 60 , startPoseH = Math.toRadians(90);
    double placeFirstPixelX = 10 , placeFirstPixelY = 27,  placeFirstPixelH = Math.toRadians(90);
    double placeSecondPixelX = 42 , placeSecondPixelY = 35,  placeSecondPixelH = Math.toRadians(0);
    Pose2d startPose = new Pose2d(startPoseX, startPoseY , startPoseH);
    Pose2d placeFirstPixel = new Pose2d(placeFirstPixelX , placeFirstPixelY , placeFirstPixelH); // 180 for left tape , default is right which is -90 degrees , 90 degrees for middle
    Vector2d placeSecondPixel = new Vector2d(placeSecondPixelX ,placeSecondPixelY);
    Vector2d parkBlyat = new Vector2d(48 , 60 );

    // LEFT SIDE BLUE AUTO ONLY PRELOAD! (UNTESTED)


    double target;


    public enum autoSide {

        BLUE_RIGHT,
        BLUE_LEFT
    }

    MecanumDrive drive;

    autoSide autoSide;

    @Override
    public void runOpMode() throws InterruptedException {

         drive = new MecanumDrive(hardwareMap , startPose , robot );

        robot.init(hardwareMap, telemetry);


        ProfileAccelConstraint profileAccelConstraint = new ProfileAccelConstraint(30 ,60);

        // TODO: Add markers and actions
        Action runBlueRight = drive.actionBuilder(startPose)
                .lineToYLinearHeading(placeFirstPixelY, placeFirstPixelH)
                .strafeToLinearHeading(placeSecondPixel , placeSecondPixelH)
                .build();


        Action runBlueLeft = drive.actionBuilder(startPose)

                // TODO: add trajectory to blue left.

                .build();


        waitForStart();

       switch (autoSide)
       {
           case BLUE_LEFT:

               runBlocking(runBlueRight);

           case BLUE_RIGHT:

               runBlocking(runBlueLeft);

           default:

               runBlocking(runBlueRight);
       }


    }
}

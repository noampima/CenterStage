package org.firstinspires.ftc.teamcode.Autonoumos;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.sun.tools.javac.comp.Todo;

public class PosesAndDirectionsStorage {

     // Added only poses for first cycle, DO NOT ADD OR CHANGE UNTIL PRELOADS ARE TESTED

    //RIGHT SIDE
    double startPoseX, startPoseY  , startPoseH ;
    double placeFirstPixelX , placeFirstPixelY,  placeFirstPixelH;
    double placeSecondPixelX = 42 , placeSecondPixelY = 35,  placeSecondPixelH = Math.toRadians(0);
    Pose2d startPose = new Pose2d(startPoseX, startPoseY , startPoseH);
    Pose2d placeFirstPixel = new Pose2d(placeFirstPixelX , placeFirstPixelY , placeFirstPixelH); // 180 for left tape , default is right which is -90 degrees , 90 degrees for middle
    Vector2d placeSecondPixel = new Vector2d(placeSecondPixelX ,placeSecondPixelY);


    //LEFT SIDE

    double startPoseLeftX = -34 , startPoseLeftY = 60 , startPoseLeftH =Math.toRadians(-90);
    double placePixelX = 50 , placePixelY = 12 , placePixelH = Math.toRadians(30);
    double stageDoorX = 0 , stageDoorY = 12 , stageDoorH = Math.toRadians(0);

    Pose2d startPoseLeft = new Pose2d(startPoseLeftX , startPoseLeftY , startPoseH);
    Pose2d placePixelPose = new Pose2d(placePixelX, placePixelY, placeFirstPixelH);
    Pose2d stageDoorPose = new Pose2d(stageDoorX, stageDoorY, stageDoorH);
   //  Pose2d stageDoorPose1 = new Pose2d(-18, -10, Math.toRadians(0));

    // TODO: Try and add an automatic conversion btw Blue and Red teams




}



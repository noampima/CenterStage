package org.firstinspires.ftc.teamcode.testing.trclib;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

import TrcCommonLib.trclib.TrcDbgTrace;
import TrcCommonLib.trclib.TrcGyro;
import TrcCommonLib.trclib.TrcPidController;
import TrcCommonLib.trclib.TrcPidDrive;
import TrcCommonLib.trclib.TrcSimpleDriveBase;
import TrcCommonLib.trclib.TrcTriggerThresholdZones;
import TrcFtcLib.ftclib.FtcDashboard;
import TrcFtcLib.ftclib.FtcDcMotor;
import TrcFtcLib.ftclib.FtcImu;
import TrcFtcLib.ftclib.FtcMRColorSensor;
import TrcFtcLib.ftclib.FtcOpMode;
import TrcFtcLib.ftclib.FtcOpticalDistanceSensor;
import TrcFtcLib.ftclib.FtcServo;

/**
 * This class implements a K9 Robot.
 */
public class K9Robot
{

    //
    // Global objects.
    //
    public FtcOpMode opMode;
    public FtcDashboard dashboard;
    public TrcDbgTrace globalTracer;

    public K9Robot()
    {
        //
        // Initialize global objects.
        //
        opMode = FtcOpMode.getInstance();
        opMode.hardwareMap.logDevices();
        dashboard = FtcDashboard.getInstance();
        globalTracer = TrcDbgTrace.getGlobalTracer();
    }   //K9Robot

    /**
     * This method is called once right before the opmode starts (i.e. at the time the "Play" button on the DS is
     * pressed).
     */
    public void startMode()
    {
        dashboard.clearDisplay();
        dashboard.refreshDisplay();
    }   //startMode

    /**
     * This method is called once before the opmode is exiting.
     */
    public void stopMode()
    {
    }   //stopMode




}   //class K9Robot
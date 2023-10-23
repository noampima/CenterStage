package org.firstinspires.ftc.teamcode.trclib;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import TrcCommonLib.command.CmdPurePursuitDrive;
import TrcCommonLib.command.CmdTimedDrive;
import TrcCommonLib.trclib.TrcPose2D;
import TrcCommonLib.trclib.TrcRobot;
import TrcFtcLib.ftclib.FtcChoiceMenu;
import TrcFtcLib.ftclib.FtcOpMode;
import TrcFtcLib.ftclib.FtcValueMenu;

@Autonomous(name="Auto: Menu", group="testing")
public class FtcMenu extends LinearOpMode
{
    K9Robot robot;

    public enum Alliance
    {
        RED_ALLIANCE,
        BLUE_ALLIANCE
    }   //enum Alliance

    private enum AutoStrategy
    {
        TIMED_DRIVE,
        DRIVE_AND_TURN,
        PURE_PURSUIT_DRIVE,
        FOLLOW_LINE,
        SEEK_IR,
        DO_NOTHING
    }   //enum AutoStrategy

    private TrcRobot.RobotCommand autoCommand;
    //
    // Menu choices.
    //
    private double delay = 0.0;
    private AutoStrategy strategy = AutoStrategy.DO_NOTHING;
    private double driveTime = 0.0;
    private double drivePower = 0.0;
    private double driveDistance = 0.0;
    private double turnDegrees = 0.0;
    private Alliance alliance = Alliance.RED_ALLIANCE;

    @Override
    public void runOpMode()
    {
        doMenus();

        switch (strategy)
        {
            case TIMED_DRIVE:
                //autoCommand = new CmdTimedDrive(
                //    robot.driveBase, delay, driveTime, 0.0, drivePower, 0.0);
                break;

            case DRIVE_AND_TURN:
                //autoCommand = new CmdDriveAndTurn(robot, delay, driveDistance*12.0, turnDegrees);
                break;

            case PURE_PURSUIT_DRIVE:
                //autoCommand = new CmdPurePursuitDrive(
                //    robot.driveBase, null, robot.yEncPidCoeff, robot.gyroPidCoeff, robot.velPidCoeff);
                break;

            case FOLLOW_LINE:
                //autoCommand = new CmdFollowLine(robot, delay, alliance);
                break;

            case SEEK_IR:
                //autoCommand = new CmdSeekIR(robot, delay);
                break;

            case DO_NOTHING:
            default:
                autoCommand = null;
                break;
        }

        waitForStart();

        while (opModeIsActive())
        {
            robot.dashboard.refreshDisplay();
        }
    }

    private void doMenus()
    {
        //
        // Create the menus.
        //
        FtcValueMenu delayMenu = new FtcValueMenu(
                "Delay time:", null, 0.0, 10.0, 1.0, 0.0,
                "%.0f sec");
        FtcChoiceMenu<AutoStrategy> strategyMenu = new FtcChoiceMenu<>("Auto Strategies:", delayMenu);
        FtcValueMenu driveTimeMenu = new FtcValueMenu(
                "Drive time:", strategyMenu, 0.0, 10.0, 1.0, 4.0,
                "%.0f sec");
        FtcValueMenu drivePowerMenu = new FtcValueMenu(
                "Drive power:", driveTimeMenu, -1.0, 1.0, 0.1, 0.5,
                "%.1f");
        FtcValueMenu distanceMenu = new FtcValueMenu(
                "Drive distance:", strategyMenu, 1.0, 8.0, 1.0, 1.0,
                "%.0f ft");
        FtcValueMenu degreesMenu = new FtcValueMenu(
                "Turn degrees", strategyMenu, -360.0, 360.0, 90.0, 360.0,
                "%.0f deg");
        FtcChoiceMenu<Alliance> allianceMenu = new FtcChoiceMenu<>("Alliance:", strategyMenu);

        delayMenu.setChildMenu(strategyMenu);
        driveTimeMenu.setChildMenu(drivePowerMenu);

        strategyMenu.addChoice("Do nothing", AutoStrategy.DO_NOTHING, true);
        strategyMenu.addChoice("Timed drive", AutoStrategy.TIMED_DRIVE, false, driveTimeMenu);
        strategyMenu.addChoice("Drive forward", AutoStrategy.DRIVE_AND_TURN, false, distanceMenu);
        strategyMenu.addChoice("Pure Pursuit Drive", AutoStrategy.PURE_PURSUIT_DRIVE, false);
        strategyMenu.addChoice("Follow line", AutoStrategy.FOLLOW_LINE, false, allianceMenu);
        strategyMenu.addChoice("Seek IR", AutoStrategy.SEEK_IR, false);

        allianceMenu.addChoice("Red", Alliance.RED_ALLIANCE, true);
        allianceMenu.addChoice("Blue", Alliance.BLUE_ALLIANCE, false);

        //
        // Walk the menu tree starting with the delay menu as the root
        // menu and get user choices.
        //
        TrcFtcLib.ftclib.FtcMenu.walkMenuTree(delayMenu);
        //
        // Set choices variables.
        //
        delay = delayMenu.getCurrentValue();
        strategy = strategyMenu.getCurrentChoiceObject();
        driveTime = driveTimeMenu.getCurrentValue();
        drivePower = drivePowerMenu.getCurrentValue();
        driveDistance = distanceMenu.getCurrentValue();
        turnDegrees = degreesMenu.getCurrentValue();
        alliance = allianceMenu.getCurrentChoiceObject();

        robot.dashboard.displayPrintf(0, "Auto Strategy: %s", strategyMenu.getCurrentChoiceText());
    }   //doMenus

}   //class FtcMenu
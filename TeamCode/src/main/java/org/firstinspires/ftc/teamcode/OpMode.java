package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.ElevatorCommand;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Elevator;
import org.firstinspires.ftc.teamcode.util.BetterGamepad;
import org.firstinspires.ftc.teamcode.util.Globals;

@Config
@TeleOp(name = "OpMode Red")
public class OpMode extends CommandOpMode {

    private final RobotHardware robot = RobotHardware.getInstance();
    private Drivetrain drivetrain;
    Elevator elevator;

    GamepadEx gamepadEx, gamepadEx2;

    double elevatorTarget = Globals.START_ELEVATOR;

    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();

        telemetry = new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry());
        Globals.IS_AUTO = false;
        Globals.IS_USING_IMU = true;

        gamepadEx = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);

        drivetrain = new Drivetrain(robot, gamepad1, gamepad2, true);
        elevator = new Elevator(hardwareMap, gamepad2);

        robot.init(hardwareMap, telemetry);
        robot.addSubsystem(drivetrain, elevator); // TODO: drivetrain ,intake.....

        gamepadEx.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                    .whenPressed(new SequentialCommandGroup(
                            new ElevatorCommand(elevator, elevatorTarget)
                        ));

        gamepadEx.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new SequentialCommandGroup(
                        new ElevatorCommand(elevator, 0)

                ));

//
//        gamepadEx.getGamepadButton(GamepadKeys.Button.B)
//                .whenPressed(new ConditionalCommand(
//                        new ClawCommand(intake, IntakeSubsystem.ClawState.INTERMEDIATE, ClawSide.BOTH),
//                        new ClawCommand(intake, IntakeSubsystem.ClawState.OPEN, ClawSide.BOTH),
//                        () -> (intake.rightClaw == (IntakeSubsystem.ClawState.CLOSED) || (intake.leftClaw == IntakeSubsystem.ClawState.CLOSED))
//                ));
//
//
//
//        gamepadEx2.getGamepadButton(GamepadKeys.Button.A)
//                .whenPressed(new SequentialCommandGroup(
//                        new InstantCommand(() -> extension.setScoring(false)),
//                        new InstantCommand(() -> extension.setFlip(false)),
//                        new InstantCommand(() -> robot.pitchActuator.setMotionProfileTargetPosition(0.01)),
//                        new InstantCommand(() -> robot.extensionActuator.setMotionProfileTargetPosition(350)),
//                        new InstantCommand(() -> intake.updateState(IntakeSubsystem.PivotState.FLAT)),
//                        new InstantCommand(() -> robot.intakePivotActuator.setTargetPosition(0.515)),
//                        new WaitCommand(250),
//                        new ClawCommand(intake, IntakeSubsystem.ClawState.OPEN, ClawSide.BOTH)
//                ))
//                        .whenPressed(new InstantCommand(() -> robot.pitchActuator.setMotionProfileTargetPosition(-0.05))
//                                .alongWith(new InstantCommand(() -> robot.extensionActuator.setMotionProfileTargetPosition(300))
//                                        .alongWith(new InstantCommand(() -> robot.intakePivotActuator.setTargetPosition(0.495)))));
//        gamepadEx.getGamepadButton(GamepadKeys.Button.A)
//                        .whenPressed(
//                                new ConditionalCommand(
//                                        new SequentialCommandGroup(
//                                                new InstantCommand(() -> extension.setScoring(false)),
//                                                new InstantCommand(() -> extension.setFlip(false)),
//                                                new InstantCommand(() -> robot.pitchActuator.setMotionProfileTargetPosition(0.0)),
//                                                new InstantCommand(() -> robot.extensionActuator.setMotionProfileTargetPosition(0)),
//                                                new WaitCommand(250),
//                                                new ClawCommand(intake, IntakeSubsystem.ClawState.CLOSED, ClawSide.BOTH),
//                                                new InstantCommand(() -> intake.updateState(IntakeSubsystem.PivotState.STORED)),
//                                                new InstantCommand(() -> robot.intakePivotActuator.setTargetPosition(0.0475))),
//                                        new SequentialCommandGroup(
//                                                new InstantCommand(() -> extension.setScoring(false)),
//                                                new InstantCommand(() -> extension.setFlip(false)),
//                                                new ClawCommand(intake, IntakeSubsystem.ClawState.CLOSED, ClawSide.BOTH),
//                                                new WaitCommand(250),
//                                                new InstantCommand(() -> robot.pitchActuator.setMotionProfileTargetPosition(0.0)),
//                                                new InstantCommand(() -> robot.extensionActuator.setMotionProfileTargetPosition(0)),
//                                                new InstantCommand(() -> intake.updateState(IntakeSubsystem.PivotState.STORED)),
//                                                new InstantCommand(() -> robot.intakePivotActuator.setTargetPosition(0.0475))),
//                                        () -> extension.getScoring())
//
//
//                                );
//        gamepadEx2.getGamepadButton(GamepadKeys.Button.B)
//                .whenPressed(
//                        new ConditionalCommand(
//                                new SequentialCommandGroup(
//                                        new InstantCommand(() -> extension.setScoring(false)),
//                                        new InstantCommand(() -> extension.setFlip(false)),
//                                        new InstantCommand(() -> robot.pitchActuator.setMotionProfileTargetPosition(0.0)),
//                                        new InstantCommand(() -> robot.extensionActuator.setMotionProfileTargetPosition(0)),
//                                        new WaitCommand(250),
//                                        new ClawCommand(intake, IntakeSubsystem.ClawState.CLOSED, ClawSide.BOTH),
//                                        new InstantCommand(() -> intake.updateState(IntakeSubsystem.PivotState.STORED)),
//                                        new InstantCommand(() -> robot.intakePivotActuator.setTargetPosition(0.0475))),
//                                new WaitCommand(1),
//                                () -> extension.getScoring())
//
//
//                );
//
////        gamepadEx2.getGamepadButton(GamepadKeys.Button.X)
////                .whenPressed(new SequentialCommandGroup(
////                        new InstantCommand(() -> extension.setScoring(true)),
////                        new InstantCommand(() -> extension.setUpdated(false)),
////                        new WaitCommand(200),
////                        new InstantCommand(() -> intake.updateState(IntakeSubsystem.PivotState.SCORING)),
////                        new WaitCommand(500),
////                        new InstantCommand(() -> extension.setFlip(true))
////                ));
//        gamepadEx2.getGamepadButton(GamepadKeys.Button.Y)
//                        .whenPressed(new SequentialCommandGroup(
//                                new InstantCommand(() -> extension.setScoring(true)),
//                                new InstantCommand(() -> robot.pitchActuator.setMotionProfileTargetPosition(((Double) extension.getPair().first).doubleValue())),
//                                new WaitCommand(200),
//                                new InstantCommand(() -> intake.updateState(IntakeSubsystem.PivotState.SCORING)),
//                                new WaitCommand(400),
//                                new InstantCommand(() -> robot.extensionActuator.setMotionProfileTargetPosition(((Integer) extension.getPair().second).doubleValue())))
//                        );
//        gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_UP)
//                .whenPressed(new SequentialCommandGroup(
//                        new InstantCommand(() -> extension.incrementBackdropHeight(1)),
//                        new ConditionalCommand(
//                                new SequentialCommandGroup(
//                                        new InstantCommand(() -> robot.pitchActuator.setMotionProfileTargetPosition(((Double) extension.getPair().first).doubleValue())),
//                                        new InstantCommand(() -> intake.updateState(IntakeSubsystem.PivotState.SCORING)),
//                                        new InstantCommand(() -> robot.extensionActuator.setMotionProfileTargetPosition(((Integer) extension.getPair().second).doubleValue()))
//                                ),
//                                new WaitCommand(1),
//                                () -> extension.getScoring()
//                        )));
//        gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
//                .whenPressed(new SequentialCommandGroup(
//                        new InstantCommand(() -> extension.incrementBackdropHeight(-1)),
//                        new ConditionalCommand(
//                                new SequentialCommandGroup(
//                                        new InstantCommand(() -> robot.pitchActuator.setMotionProfileTargetPosition(((Double) extension.getPair().first).doubleValue())),
//                                        new InstantCommand(() -> intake.updateState(IntakeSubsystem.PivotState.SCORING)),
//                                        new InstantCommand(() -> robot.extensionActuator.setMotionProfileTargetPosition(((Integer) extension.getPair().second).doubleValue()))
//                                ),
//                                new WaitCommand(1),
//                                () -> extension.getScoring()
//                        )));



        // combination of angle and extension amount, get minimums, get maximums, math.map
        robot.read();
        while (opModeInInit()) {
            telemetry.addLine("Robot Initialized. Mason is very cool and he is the best perosn to ever exist in the owrld and java ois the owrst progmraming kanguage nad ih ate it so so os much LLL + Ratio + cope + cget out of my game L");
            telemetry.update();
        }
    }

    @Override
    public void run() {
        robot.read();

//        if (currentJoystickUp && !lastJoystickUp) {
//            // height go upp
//            extension.incrementBackdropHeight(1);
//            CommandScheduler.getInstance().schedule(
//                    new SequentialCommandGroup(
//                            new InstantCommand(() -> robot.pitchActuator.setMotionProfileTargetPosition(((Double) extension.getPair().first).doubleValue())),
//                            new InstantCommand(() -> intake.updateState(IntakeSubsystem.PivotState.SCORING)),
//                            new InstantCommand(() -> robot.extensionActuator.setMotionProfileTargetPosition(((Integer) extension.getPair().second).doubleValue())))
//            );
//        }
//
//        if (currentJoystickDown && !lastJoystickDown) {
//            // gheight go dwodn
//            extension.incrementBackdropHeight(-1);
//            CommandScheduler.getInstance().schedule(
//                    new SequentialCommandGroup(
//                            new InstantCommand(() -> robot.pitchActuator.setMotionProfileTargetPosition(((Double) extension.getPair().first).doubleValue())),
//                            new InstantCommand(() -> intake.updateState(IntakeSubsystem.PivotState.SCORING)),
//                            new InstantCommand(() -> robot.extensionActuator.setMotionProfileTargetPosition(((Integer) extension.getPair().second).doubleValue())))
//            );
//        }

        // input
        super.run();
        robot.periodic();

        telemetry.update();
        robot.write();
    }
}

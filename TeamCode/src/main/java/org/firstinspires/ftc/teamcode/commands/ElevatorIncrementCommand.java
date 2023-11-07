package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.OpMode;
import org.firstinspires.ftc.teamcode.util.values.Globals;

public class ElevatorIncrementCommand extends InstantCommand {
    public ElevatorIncrementCommand(OpMode opMode, int sign) {
        super(
                () -> opMode.setElevatorTarget(opMode.getElevatorTarget() + (Globals.ELEVATOR_INCREMENT * sign))
        );
    }
}

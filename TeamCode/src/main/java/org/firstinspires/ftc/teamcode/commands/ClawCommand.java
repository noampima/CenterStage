package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.RobotHardware;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.util.values.ClawSide;

public class ClawCommand extends InstantCommand {

    public ClawCommand(Claw claw, Claw.ClawState state, ClawSide side) {
        super(
                ()-> claw.updateState(state, side)
        );
    }
}

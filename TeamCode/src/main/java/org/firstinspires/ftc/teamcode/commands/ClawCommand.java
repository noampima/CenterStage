package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.subsystems.Elevator;

public class ClawCommand extends InstantCommand {
    public ClawCommand(Elevator elevator, double target) {
        super(
                () -> elevator.setTarget(target)
        );
    }
}

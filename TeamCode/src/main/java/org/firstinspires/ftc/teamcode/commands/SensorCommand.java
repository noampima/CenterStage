package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.util.values.ClawSide;

public class SensorCommand extends InstantCommand {

    public  SensorCommand(Claw claw, boolean useSesnor) {
        super(
                () -> claw.setUseSensor(useSesnor)
        );
    }
}

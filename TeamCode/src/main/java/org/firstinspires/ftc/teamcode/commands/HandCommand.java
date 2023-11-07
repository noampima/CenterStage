package org.firstinspires.ftc.teamcode.commands;

import com.acmerobotics.roadrunner.Pose2d;
import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Hand;
import org.firstinspires.ftc.teamcode.util.values.ClawSide;

public class HandCommand extends InstantCommand {

    public HandCommand(Hand hand, Pose2d pose) {
        super(
                () -> hand.setPos(pose.position.x, pose.position.y, pose.heading.log())
        );
    }
}

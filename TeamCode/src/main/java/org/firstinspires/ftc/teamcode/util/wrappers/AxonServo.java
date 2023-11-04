package org.firstinspires.ftc.teamcode.util.wrappers;

import com.outoftheboxrobotics.photoncore.Photon;
import com.outoftheboxrobotics.photoncore.hardware.servo.PhotonServo;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.util.AbsoluteAnalogEncoder;

import javax.annotation.Nonnegative;

public class AxonServo {
    private PhotonServo servo;
    private AbsoluteAnalogEncoder encoder;

    public AxonServo(Servo servo, AnalogInput encoder) {
        this.servo = (PhotonServo) servo;
        this.encoder = new AbsoluteAnalogEncoder(encoder);
    }

    public AxonServo(Servo servo) {
        this.servo = (PhotonServo) servo;
    }

    public void setPosition(double position) {
        servo.setPosition(position);
    }

    public void invert() {
        encoder.setInverted(true);
        servo.setDirection(Servo.Direction.REVERSE);
    }

    @Nonnegative
    public double getPosition() {
        if (hasEncoder()) {
            return encoder.getCurrentPosition();
        } else {
            return servo.getPosition();
        }
    }

    public boolean hasEncoder() {
        return encoder != null;
    }
}

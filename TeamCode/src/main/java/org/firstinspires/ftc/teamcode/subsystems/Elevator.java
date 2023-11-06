package org.firstinspires.ftc.teamcode.subsystems;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.RobotHardware;
import org.firstinspires.ftc.teamcode.util.BetterGamepad;
import org.firstinspires.ftc.teamcode.util.Globals;
import org.firstinspires.ftc.teamcode.util.wrappers.BetterSubsystem;

@Config
public class Elevator extends BetterSubsystem {

    private final RobotHardware robot;
    public static double BASE_LEVEL = 5;
    public static double INCREMENT = 1;
    DcMotorEx motor;
    double target, currentTarget = BASE_LEVEL;
    public static double TICKS_PER_REV = 145.1, SPOOL_RADIUS = 0.75; // in //TODO: CHANGE
    double power = 1;
    boolean usePID = true;
    public static double maxPower = 0.85;
    public static boolean DEBUG = true;

    Gamepad gamepad;
    BetterGamepad cGamepad;

    public Elevator(HardwareMap hardwareMap, Gamepad gamepad)
    {
        this.robot = RobotHardware.getInstance();

        this.motor = hardwareMap.get(DcMotorEx.class, "mE");
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //this.motor.setDirection(DcMotor.Direction.REVERSE);

        if(DEBUG)
        {
            this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        this.gamepad = gamepad;
        this.cGamepad = new BetterGamepad(gamepad);
    }

    public Elevator(HardwareMap hardwareMap)
    {
        this.robot = RobotHardware.getInstance();

        this.motor = hardwareMap.get(DcMotorEx.class, "mE");
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //this.motor.setDirection(DcMotor.Direction.REVERSE);

        if(DEBUG)
        {
            this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    @Override
    public void periodic() {
        if(!Globals.IS_AUTO)
        {
            cGamepad.update();

            if (usePID)
            {
                target = currentTarget;

                motor.setTargetPosition(inchesToEncoderTicks(target));
                motor.setPower(power);
                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            else
            {
                motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                if(gamepad.left_stick_y != 0 && !gamepad.left_stick_button)
                {
                    motor.setPower(Range.clip(-gamepad.left_stick_y, -maxPower, maxPower));
                }
                else if(gamepad.left_stick_y != 0 && gamepad.left_stick_button)
                {
                    motor.setPower(Range.clip(-gamepad.left_stick_y, -maxPower/2, maxPower/2));
                }
                else
                {
                    motor.setPower(0);
                }
            }
        }
        else
        {
            target = currentTarget;

            motor.setTargetPosition(inchesToEncoderTicks(target));
            motor.setPower(power);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    @Override
    public void read() {

    }

    @Override
    public void write() {

    }

    @Override
    public void reset() {

    }

    public static double encoderTicksToInches(int ticks) {
        return SPOOL_RADIUS * 2 * Math.PI * ticks / TICKS_PER_REV;
    }

    public static int inchesToEncoderTicks(double inches) {
        return (int)Math.round((inches * TICKS_PER_REV) / (SPOOL_RADIUS * 2 * Math.PI));
    }

    public void setTarget(double target)
    {
        currentTarget = target;
    }

    public void increment()
    {
        currentTarget += INCREMENT;
    }

    public void decrement()
    {
        currentTarget -= INCREMENT;
    }

}
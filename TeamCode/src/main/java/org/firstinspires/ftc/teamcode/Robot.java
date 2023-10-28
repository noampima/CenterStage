import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.AirplaneLauncher;
import org.firstinspires.ftc.teamcode.subsystems.DrivetrainController;
import org.firstinspires.ftc.teamcode.subsystems.Elevator;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

public abstract class Robot extends LinearOpMode {

    private Elevator _elevator;
    private Intake _intake;
    private DrivetrainController _robotController;
    private AirplaneLauncher _airplaneLauncher;

    //Constructor
    public Robot()
    {
        this._elevator = new Elevator(hardwareMap);
        this._intake = new Intake(hardwareMap);
        this._robotController = new DrivetrainController(hardwareMap, gamepad1, gamepad2, telemetry, false);
        this._airplaneLauncher = new AirplaneLauncher(hardwareMap);
    }

    // Allows you to connect opModes to the base class
    @Override
    public void runOpMode()
    {
        this._robotController.update();
    }

}

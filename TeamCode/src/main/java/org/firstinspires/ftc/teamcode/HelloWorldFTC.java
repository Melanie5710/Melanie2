package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class











HelloWorldFTC extends OpMode {
    private DcMotorController dcMotorController;

    private DcMotor dcDriverLeft;
    private DcMotor dcDriverRight;

    @Override
    public void init() {
        dcMotorController = hardwareMap.dcMotorController.get("drive_controller");
        dcDriverLeft = hardwareMap.dcMotor.get("drive_left");
        dcDriverRight = hardwareMap.dcMotor.get("drive_right");
    }

    @Override
    public void loop() {
        dcDriverLeft.setPower(gamepad1.left_stick_y);
        dcDriverRight.setPower(gamepad1.right_stick_y);
    }
}

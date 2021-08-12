package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp //driver controlled
//@Autonomous
public class MyFIRSTJavaOpMode extends LinearOpMode {

    private Gyroscope imu;
    private DcMotor motorTest;
    private DigitalChannel digitalTouch;
    private DistanceSensor sensorColorRange;
    private Servo servoTest;

    @Override
    public void runOpMode() throws InterruptedException {

        imu = hardwareMap.get(Gyroscope.class, "imu");
        /*
        Note that when you attempt to retrieve a reference to a specific device in your op mode,
        the name that you specify as the second argument of the HardwareMap.get method
        must match the name used to define the device in your configuration file.
        For example, if you created a configuration file that had a DC motor named “motorTest”,
        then you must use this same name (it is case sensitive) to retrieve this motor from the hardwareMap object. If the names do not match, the op mode will throw an exception indicating that it cannot find the device.
         */
        motorTest = hardwareMap.get(DcMotor.class, "motorTest");
        digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");
        sensorColorRange = hardwareMap.get(DistanceSensor.class, "sensorColorRange");
        servoTest = hardwareMap.get(Servo.class, "servoTest");

        // 4. REV Robotics Touch Sensor - set digital channel to input mode.
        digitalTouch.setMode(DigitalChannel.Mode.INPUT);

        //the op mode prompts the user to push the start button to continue
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // 1. Andorid Phone - run until the end of the match (driver presses STOP)
//        while (opModeIsActive()) {
//            telemetry.addData("Status", "Running");
//            telemetry.update();
//
//        }

        // 2. GamePad + Phone - run until the end of the match (driver presses STOP)
//        double tgtPower = 0;
//        while (opModeIsActive()) {
//            // negate the left_stick_y value so that pushing the left joystick
//            // forward will result in a positive power being applied to the motor
//            tgtPower = -this.gamepad1.left_stick_y;
//            motorTest.setPower(tgtPower);
//            telemetry.addData("Target Power", tgtPower);
//            telemetry.addData("Motor Power", motorTest.getPower());
//            telemetry.addData("Status", "Running");
//            telemetry.update();
//
//        }

        // 3. Servo - run until the end of the match (driver presses STOP)
        /*
        This added code will check to see if any of the colored buttons on the F310 gamepad
        are pressed. If the Y button is pressed, it will move the servo to the 0-degree position.
        If either the X button or B button is pressed, it will move the servo to the 90-degree position.
        If the A button is pressed, it will move the servo to the 180-degree position.
        The op mode will also send telemetry data on the servo position to the Driver Station.
         */
        double tgtPower = 0;
        while (opModeIsActive()) {
            tgtPower = -this.gamepad1.left_stick_y;
            motorTest.setPower(tgtPower);
            // check to see if we need to move the servo.
            if(gamepad1.y) {
                // move to 0 degrees.
                servoTest.setPosition(0);
            } else if (gamepad1.x || gamepad1.b) {
                // move to 90 degrees.
                servoTest.setPosition(0.5);
            } else if (gamepad1.a) {
                // move to 180 degrees.
                servoTest.setPosition(1);
            }
            telemetry.addData("Servo Position", servoTest.getPosition());
            telemetry.addData("Target Power", tgtPower);
            telemetry.addData("Motor Power", motorTest.getPower());

            // 4. Color-Distance Sensor - send the distance information (in centimeters) to the Driver Station
            telemetry.addData("Distance (cm)", sensorColorRange.getDistance(DistanceUnit.CM));

            // 4. REV Robotics Touch Sensoris button pressed?
            if (digitalTouch.getState() == false) {
                // button is pressed.
                telemetry.addData("Button", "PRESSED");
            } else {
                // button is not pressed.
                telemetry.addData("Button", "NOT PRESSED");
            }

            telemetry.addData("Status", "Running");
            telemetry.update();

        }
    }
}

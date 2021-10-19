package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "MecanumDrive", group = "Quackology")
public class MecanumTeleop extends OpMode {
    private DcMotorEx frontLeft, frontRight, backLeft, backRight, carousel;
    private Servo clawServo, wristServo;
//    private Servo servo;

    static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int    CYCLE_MS    =   50;     // period of each cycle
    static final double MAX_POS     =  1.0;     // Maximum rotational position
    static final double MIN_POS     =  0.0;     // Minimum rotational position

    double  position = (MAX_POS - MIN_POS) / 2; // Start at halfway position
    double clawposition = 0.5;
    double wristposition = 0.7;
//    boolean rampUp = true;

    LiftM liftm;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing...");
        telemetry.update();

        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        carousel = hardwareMap.get(DcMotorEx.class, "carousel");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // Connect to servo (Assume PushBot Left Hand)
        // Change the text in quotes to match any servo name on your robot.
//        servo = hardwareMap.get(Servo.class, "left_hand");
        clawServo = hardwareMap.get(Servo.class, "clawservo");
        wristServo = hardwareMap.get(Servo.class,"wristservo");
        liftm = new LiftM(hardwareMap);
        liftm.init();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

    }

    @Override
    public void loop() {
        double drive;
        double strafe;
        double turn;
        drive = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        turn = gamepad1.right_stick_x;
        boolean spin = gamepad1.y;



        double frontLeftPower = Range.clip(drive + strafe + turn, -1.0, 1.0);
        double backLeftPower = Range.clip(drive - strafe + turn, -1.0, 1.0);
        double frontRightPower = Range.clip(drive - strafe - turn, -1.0, 1.0);
        double backRightPower = Range.clip(drive + strafe - turn, -1.0, 1.0);

        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backLeft.setPower(backRightPower);

        if(gamepad1.x){
            carousel.setPower(0.5);
        }
        if(gamepad1.a){
            carousel.setPower(0);
        }
        while (spin == true) {
            carousel.setPower(1.0);
        }
//
//        // slew the servo, according to the rampUp (direction) variable.
//        if (rampUp) {
//            // Keep stepping up until we hit the max value.
//            position += INCREMENT ;
//            if (position >= MAX_POS ) {
//                position = MAX_POS;
//                rampUp = !rampUp;   // Switch ramp direction
//            }
//        }
//        else {
//            // Keep stepping down until we hit the min value.
//            position -= INCREMENT ;
//            if (position <= MIN_POS ) {
//                position = MIN_POS;
//                rampUp = !rampUp;  // Switch ramp direction
//            }
//        }
        if(gamepad2.a){
            clawposition = 1.0;
        }
        if(gamepad2.y){
            clawposition = 0.0;
        }
        if(gamepad2.b){
            wristposition = 0.9;
        }
        if(gamepad2.x){
            wristposition = 0.7;
        }
        if(gamepad2.dpad_up){
            liftm.moveByInchTele(2, 0.5);
        }
        if(gamepad2.dpad_down){
            liftm.moveByInchTele(-2, 0.5);
        }
        telemetry.addData("Motors", "frontLeft (%.2f), frontRight (%.2f), backLeft (%.2f), backRight(%.2f)",
                frontLeftPower, frontRightPower, backLeftPower, backRightPower);
        telemetry.addData("Claw Position", "%5.2f", clawposition);
        telemetry.addData("Wrist Position", "%5.2f", wristposition);
        telemetry.addData("lift Position", liftm.liftMotor.getCurrentPosition());
        telemetry.addData("lift Target", liftm.targetPos);
        telemetry.update();

        // Set the servo to the new position and pause;
        clawServo.setPosition(clawposition);
        wristServo.setPosition(wristposition);
//        sleep(CYCLE_MS);
//        idle();
    }


    /**
     * Puts the current thread to sleep for a bit as it has nothing better to do. This allows other
     * threads in the system to run.
     *
     * <p>One can use this method when you have nothing better to do in your code as you await state
     * managed by other threads to change. Calling idle() is entirely optional: it just helps make
     * the system a little more responsive and a little more efficient.</p>
     *
     *
     */
    public final void idle() {
        // Otherwise, yield back our thread scheduling quantum and give other threads at
        // our priority level a chance to run
        Thread.yield();
    }

    /**
     * Sleeps for the given amount of milliseconds, or until the thread is interrupted. This is
     * simple shorthand for the operating-system-provided {@link Thread#sleep(long) sleep()} method.
     *
     * @param milliseconds amount of time to sleep, in milliseconds
     * @see Thread#sleep(long)
     */
    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

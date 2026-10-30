package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.List;

@TeleOp
public class VanguardExemplo extends OpMode {
    public DcMotorEx lF, rF, lB, rB, intoiker;

    /** ordem: [lf, rf, lb, rb] */
    public List<Double> powers;

    @Override
    public void init() {
        lF = hardwareMap.get(DcMotorEx.class, "LeftFront");
        rF = hardwareMap.get(DcMotorEx.class, "RightFront");
        lB = hardwareMap.get(DcMotorEx.class, "LeftBack");
        rB = hardwareMap.get(DcMotorEx.class, "RightBack");
        intoiker = hardwareMap.get(DcMotorEx.class, "intake");

        lF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intoiker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lF.setDirection(DcMotorSimple.Direction.REVERSE);
        lB.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void start() {
        lF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intoiker.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        drive();
        intoiker.setPower(gamepad2.left_stick_y);
    }

    public void drive() {
        double forward = gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x * 1.15;
        double turn = gamepad1.right_stick_x;

        double lf = forward + strafe + turn;
        double rf = forward - strafe - turn;
        double lb = forward - strafe + turn;
        double rb = forward + strafe - turn;

        powers = List.of(lf, rf, lb, rb);

        double max = maximum(powers);

        if (max > 1) {
            List<Double> alt = List.of();
            for (Double val : powers) {
                alt.add(val / max);
            }
            powers = alt;
        }

        lF.setPower(powers.get(0));
        rF.setPower(powers.get(1));
        lB.setPower(powers.get(2));
        rB.setPower(powers.get(3));
    }

    public double maximum(List<Double> vals) {
        double big = 0;
        for (Double val : vals) {
            if (val > big) {
                big = val;
            }
        }

        return big;
    }
}

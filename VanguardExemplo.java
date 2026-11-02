package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class VanguardExemplo extends OpMode {
    public DcMotorEx lF, rF, lB, rB, intoiker, intoiker2, shooter;

    /** ordem: [lf, rf, lb, rb] */
    public double[] powers = new double[4];

    @Override
    public void init() {
        lF = hardwareMap.get(DcMotorEx.class, "LeftFront");
        rF = hardwareMap.get(DcMotorEx.class, "RightFront");
        lB = hardwareMap.get(DcMotorEx.class, "LeftBack");
        rB = hardwareMap.get(DcMotorEx.class, "RightBack");
        intoiker = hardwareMap.get(DcMotorEx.class, "IntakeMotor1");
        intoiker2 = hardwareMap.get(DcMotorEx.class, "Intakemotor2");
        shooter = hardwareMap.get(DcMotorEx.class, "ShooterMotor1");

        lF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intoiker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intoiker2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

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
        intoiker2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        drive();
        // intake: os dois motores juntos, no mesmo sentido
        intoiker.setPower(gamepad2.right_stick_y);
        intoiker2.setPower(gamepad2.right_stick_y);

        // shooter: liga enquanto segura o R2
        shooter.setPower(gamepad2.right_trigger > 0.1 ? 1 : 0);
    }

    public void drive() {
        double forward = gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x * 1.15;
        double turn = gamepad1.right_stick_x;

        powers[0] = forward + strafe + turn; // lf
        powers[1] = forward - strafe - turn; // rf
        powers[2] = forward - strafe + turn; // lb
        powers[3] = forward + strafe - turn; // rb

        double max = maximum(powers);

        if (max > 1) {
            for (int i = 0; i < powers.length; i++) {
                powers[i] /= max;
            }
        }

        lF.setPower(powers[0]);
        rF.setPower(powers[1]);
        lB.setPower(powers[2]);
        rB.setPower(powers[3]);
    }

    public double maximum(double[] vals) {
        double big = 0;
        for (double val : vals) {
            big = Math.max(big, Math.abs(val));
        }

        return big;
    }
}

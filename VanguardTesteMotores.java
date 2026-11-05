package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Teste dos motores do drive, um de cada vez (controle 1):
 * X = frontLeftMotor, Y = frontRightMotor, A = backLeftMotor, B = backRightMotor.
 * Com o robô levantado, cada roda deve girar no sentido de andar PRA FRENTE.
 */
@TeleOp
public class VanguardTesteMotores extends OpMode {
    public DcMotorEx lF, rF, lB, rB;

    static final double POTENCIA = 0.4;

    @Override
    public void init() {
        lF = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
        rF = hardwareMap.get(DcMotorEx.class, "frontRightMotor");
        lB = hardwareMap.get(DcMotorEx.class, "backLeftMotor");
        rB = hardwareMap.get(DcMotorEx.class, "backRightMotor");

        // mesmas direções do TeleOp
        lF.setDirection(DcMotorSimple.Direction.REVERSE);
        lB.setDirection(DcMotorSimple.Direction.REVERSE);

        for (DcMotorEx m : new DcMotorEx[]{lF, rF, lB, rB}) {
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    @Override
    public void loop() {
        lF.setPower(gamepad1.x ? POTENCIA : 0);
        rF.setPower(gamepad1.y ? POTENCIA : 0);
        lB.setPower(gamepad1.a ? POTENCIA : 0);
        rB.setPower(gamepad1.b ? POTENCIA : 0);

        telemetry.addLine("Segure um botão: a roda deve girar PRA FRENTE");
        telemetry.addData("X", "frontLeftMotor  (dianteira esquerda)");
        telemetry.addData("Y", "frontRightMotor (dianteira direita)");
        telemetry.addData("A", "backLeftMotor   (traseira esquerda)");
        telemetry.addData("B", "backRightMotor  (traseira direita)");
        telemetry.update();
    }
}

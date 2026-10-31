package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class VanguardAuto extends LinearOpMode {
    public DcMotorEx lF, rF, lB, rB;

    // ===== AJUSTE AQUI PRO ROBÔ DE VOCÊS =====
    /** ticks por volta do motor (goBILDA 312 RPM = 537.7) */
    static final double TICKS_POR_VOLTA = 537.7;
    /** diâmetro da roda em cm (roda mecanum goBILDA = 10.4) */
    static final double DIAMETRO_RODA_CM = 10.4;
    /** o strafe escorrega mais, então precisa de mais ticks pra mesma distância */
    static final double FATOR_STRAFE = 1.15;
    /** -1 porque no TeleOp potência negativa = frente. Se andar ao contrário, troque pra 1 */
    static final int FRENTE = -1;
    static final double VELOCIDADE = 0.5;
    /** tempo máximo de cada movimento, pra não travar se o robô bater em algo */
    static final double TIMEOUT_S = 5;

    static final double TICKS_POR_CM = TICKS_POR_VOLTA / (Math.PI * DIAMETRO_RODA_CM);

    @Override
    public void runOpMode() {
        lF = hardwareMap.get(DcMotorEx.class, "LeftFront");
        rF = hardwareMap.get(DcMotorEx.class, "RightFront");
        lB = hardwareMap.get(DcMotorEx.class, "LeftBack");
        rB = hardwareMap.get(DcMotorEx.class, "RightBack");

        lF.setDirection(DcMotorSimple.Direction.REVERSE);
        lB.setDirection(DcMotorSimple.Direction.REVERSE);

        for (DcMotorEx m : new DcMotorEx[]{lF, rF, lB, rB}) {
            m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        telemetry.addLine("Pronto");
        telemetry.update();
        waitForStart();

        // ===== SEQUÊNCIA DO AUTÔNOMO =====
        andarDeLado(50);   // 50 cm pra direita
    }

    /** cm positivo = frente, negativo = trás */
    public void andar(double cm) {
        int t = (int) Math.round(cm * TICKS_POR_CM) * FRENTE;
        mover(t, t, t, t);
    }

    /** cm positivo = direita, negativo = esquerda */
    public void andarDeLado(double cm) {
        int t = (int) Math.round(cm * TICKS_POR_CM * FATOR_STRAFE);
        mover(t, -t, -t, t);
    }

    /** move cada motor a quantidade de ticks indicada. ordem: [lf, rf, lb, rb] */
    public void mover(int lf, int rf, int lb, int rb) {
        if (!opModeIsActive()) return;

        lF.setTargetPosition(lF.getCurrentPosition() + lf);
        rF.setTargetPosition(rF.getCurrentPosition() + rf);
        lB.setTargetPosition(lB.getCurrentPosition() + lb);
        rB.setTargetPosition(rB.getCurrentPosition() + rb);

        DcMotorEx[] motores = {lF, rF, lB, rB};
        for (DcMotorEx m : motores) {
            m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            m.setPower(VELOCIDADE);
        }

        ElapsedTime timer = new ElapsedTime();
        while (opModeIsActive() && timer.seconds() < TIMEOUT_S
                && (lF.isBusy() || rF.isBusy() || lB.isBusy() || rB.isBusy())) {
            telemetry.addData("lF", "%d / %d", lF.getCurrentPosition(), lF.getTargetPosition());
            telemetry.addData("rF", "%d / %d", rF.getCurrentPosition(), rF.getTargetPosition());
            telemetry.addData("lB", "%d / %d", lB.getCurrentPosition(), lB.getTargetPosition());
            telemetry.addData("rB", "%d / %d", rB.getCurrentPosition(), rB.getTargetPosition());
            telemetry.update();
        }

        for (DcMotorEx m : motores) {
            m.setPower(0);
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        sleep(100);
    }
}

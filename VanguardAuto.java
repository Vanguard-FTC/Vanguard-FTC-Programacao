package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Base dos autônomos. Não aparece no Driver Station: quem aparece são
 * VanguardAutoAzul (anda pra esquerda) e VanguardAutoVermelho (anda pra direita).
 */
public abstract class VanguardAuto extends LinearOpMode {
    /** versão do autônomo: aumente a cada atualização (ver README, Patch notes) */
    public static final String VERSAO = "0.2.0";

    public DcMotorEx lF, rF, lB, rB, intoiker, intoiker2, shooter;

    /** 1 = anda pra direita (vermelho), -1 = anda pra esquerda (azul) */
    protected abstract int lado();

    // ===== SEQUÊNCIA: AJUSTE AQUI =====
    /** força do shooter no auto (%): 100% = 4000 RPM */
    static final int AUTO_SHOOTER_FORCA = 100;
    /** tempo máximo esperando o shooter chegar na velocidade (s) */
    static final double AUTO_ESPERA_SHOOTER_S = 3;
    /** quanto tempo os intakes ficam ligados lançando (s) */
    static final double AUTO_TEMPO_INTAKE_S = 3;
    /** quanto anda pro lado no fim (cm) */
    static final double AUTO_DISTANCIA_CM = 5;

    // ===== AJUSTE AQUI PRO ROBÔ DE VOCÊS =====
    /** ticks por volta do motor (goBILDA 312 RPM = 537.7) */
    static final double TICKS_POR_VOLTA = 537.7;
    /** diâmetro da roda em cm (roda mecanum goBILDA = 10.4) */
    static final double DIAMETRO_RODA_CM = 10.4;
    /** o strafe escorrega mais, então precisa de mais ticks pra mesma distância */
    static final double FATOR_STRAFE = 1.15;
    /** 1 porque no TeleOp potência positiva = frente. Se andar ao contrário, troque pra -1 */
    static final int FRENTE = 1;
    static final double VELOCIDADE = 0.5;
    /** tempo máximo de cada movimento, pra não travar se o robô bater em algo */
    static final double TIMEOUT_S = 5;

    static final double TICKS_POR_CM = TICKS_POR_VOLTA / (Math.PI * DIAMETRO_RODA_CM);

    @Override
    public void runOpMode() {
        lF = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
        rF = hardwareMap.get(DcMotorEx.class, "frontRightMotor");
        lB = hardwareMap.get(DcMotorEx.class, "backLeftMotor");
        rB = hardwareMap.get(DcMotorEx.class, "backRightMotor");

        intoiker = hardwareMap.get(DcMotorEx.class, "IntakeMotor1");
        intoiker2 = hardwareMap.get(DcMotorEx.class, "IntakeMotor2");
        shooter = hardwareMap.get(DcMotorEx.class, "ShooterMotor");

        lF.setDirection(DcMotorSimple.Direction.REVERSE);
        lB.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter.setDirection(DcMotorSimple.Direction.REVERSE); // igual ao TeleOp
        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        for (DcMotorEx m : new DcMotorEx[]{lF, rF, lB, rB}) {
            m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        telemetry.addLine("VanguardAuto " + (lado() > 0 ? "Vermelho " : "Azul ") + VERSAO + " pronto");
        telemetry.update();
        waitForStart();

        // ===== SEQUÊNCIA DO AUTÔNOMO =====
        // 1. liga o shooter e espera chegar na velocidade
        double rpmAlvo = VanguardTeleop.SHOOTER_RPM_MAX * AUTO_SHOOTER_FORCA / 100.0;
        shooter.setVelocity(rpmAlvo * VanguardTeleop.SHOOTER_TICKS_POR_VOLTA / 60);
        ElapsedTime espera = new ElapsedTime();
        while (opModeIsActive() && espera.seconds() < AUTO_ESPERA_SHOOTER_S) {
            double rpm = shooter.getVelocity() * 60 / VanguardTeleop.SHOOTER_TICKS_POR_VOLTA;
            telemetry.addData("Shooter (RPM)", "%.0f / %.0f", rpm, rpmAlvo);
            telemetry.update();
            if (Math.abs(rpm - rpmAlvo) < VanguardTeleop.SHOOTER_TOLERANCIA_RPM) break;
        }

        // 2. liga os dois intakes pra lançar os pollen
        intoiker.setPower(VanguardTeleop.INTAKE1_FORCA);
        intoiker2.setPower(VanguardTeleop.INTAKE2_FORCA);
        sleep((long) (AUTO_TEMPO_INTAKE_S * 1000));

        // 3. desliga tudo
        intoiker.setPower(0);
        intoiker2.setPower(0);
        shooter.setVelocity(0);

        // 4. anda pro lado (direita no vermelho, esquerda no azul)
        andarDeLado(AUTO_DISTANCIA_CM * lado());
    }

    /** cm positivo = frente, negativo = trás */
    public void andar(double cm) {
        int t = (int) Math.round(cm * TICKS_POR_CM) * FRENTE;
        mover(t, t, t, t);
    }

    /** cm positivo = direita, negativo = esquerda */
    public void andarDeLado(double cm) {
        int t = (int) Math.round(cm * TICKS_POR_CM * FATOR_STRAFE);
        mover(-t, t, t, -t);
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

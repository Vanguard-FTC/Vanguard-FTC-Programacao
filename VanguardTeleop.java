package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name = "VanguardTeleop " + VanguardTeleop.VERSAO)
public class VanguardTeleop extends OpMode {
    /** versão do TeleOp: aumente a cada atualização (ver README, Patch notes) */
    public static final String VERSAO = "0.1.11";

    public DcMotorEx lF, rF, lB, rB, intoiker, intoiker2, shooter;

    /** ticks por volta do motor do shooter (REV HD Hex sem caixa = 28, máx ~6000 RPM) */
    static final double SHOOTER_TICKS_POR_VOLTA = 28;
    /** 100% do shooter = 4000 RPM (o motor aguenta até ~6000) */
    static final double SHOOTER_RPM_MAX = 4000;
    /** força (%) em que o shooter liga no X/A */
    static final int SHOOTER_FORCA_INICIAL = 1;
    /** quanto cada toque no D-pad muda a força (%) */
    static final int SHOOTER_PASSO = 1;

    /** potência dos intakes (troque o sinal se girar ao contrário) */
    static final double INTAKE1_FORCA = 1.0;
    static final double INTAKE2_FORCA = 1.0;

    /** shooter: ligado/desligado e força atual em % (1 a 100) */
    boolean shooterLigado = false;
    int shooterForca = SHOOTER_FORCA_INICIAL;
    boolean xAnterior = false, cimaAnterior = false, baixoAnterior = false;

    /** potência do drive no modo lento (R2 do controle 1) */
    static final double MODO_LENTO = 0.4;

    /** segurar posição: força que as rodas usam pra voltar pro lugar quando empurram o robô */
    static final double SEGURAR_FORCA = 0.5;
    /** abaixo disso o analógico conta como solto */
    static final double ZONA_MORTA = 0.05;
    boolean segurando = false;


    /** ordem: [lf, rf, lb, rb] */
    public double[] powers = new double[4];

    @Override
    public void init() {
        lF = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
        rF = hardwareMap.get(DcMotorEx.class, "frontRightMotor");
        lB = hardwareMap.get(DcMotorEx.class, "backLeftMotor");
        rB = hardwareMap.get(DcMotorEx.class, "backRightMotor");
        intoiker = hardwareMap.get(DcMotorEx.class, "IntakeMotor1");
        intoiker2 = hardwareMap.get(DcMotorEx.class, "IntakeMotor2");
        shooter = hardwareMap.get(DcMotorEx.class, "ShooterMotor");

        lF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intoiker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intoiker2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lF.setDirection(DcMotorSimple.Direction.REVERSE);
        lB.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter.setDirection(DcMotorSimple.Direction.REVERSE); // shooter invertido

        // freio: quando não tem força, os motores do drive travam em vez de ficar soltos
        for (DcMotorEx m : new DcMotorEx[]{lF, rF, lB, rB}) {
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    @Override
    public void start() {
        // cor da luz dos controles de PS5 (vermelho, verde, azul de 0 a 1)
        gamepad1.setLedColor(1, 1, 0, Gamepad.LED_DURATION_CONTINUOUS);   // controle 1: amarelo
        gamepad2.setLedColor(0.6, 0, 1, Gamepad.LED_DURATION_CONTINUOUS); // controle 2: roxo

        lF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intoiker.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intoiker2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // usa o encoder pra manter a velocidade, mesmo com a bateria fraca
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        drive();
        // intake: os dois motores juntos, no mesmo sentido
        // intake 1: L2 pra frente, L1 ao contrário (controle 2, enquanto segura)
        if (gamepad2.left_trigger > 0.1) intoiker.setPower(INTAKE1_FORCA);
        else if (gamepad2.left_bumper) intoiker.setPower(-INTAKE1_FORCA);
        else intoiker.setPower(0);

        // intake 2: R2 pra frente, R1 ao contrário (controle 2, enquanto segura)
        if (gamepad2.right_trigger > 0.1) intoiker2.setPower(INTAKE2_FORCA);
        else if (gamepad2.right_bumper) intoiker2.setPower(-INTAKE2_FORCA);
        else intoiker2.setPower(0);

        // shooter (controle 2): X/A liga em 1% e desliga.
        // D-pad pra cima aumenta 1%, pra baixo diminui 1% (de 1% a 100%).
        if (gamepad2.a && !xAnterior) {
            shooterLigado = !shooterLigado;
            shooterForca = SHOOTER_FORCA_INICIAL;
        }
        if (gamepad2.dpad_up && !cimaAnterior) shooterForca += SHOOTER_PASSO;
        if (gamepad2.dpad_down && !baixoAnterior) shooterForca -= SHOOTER_PASSO;
        shooterForca = Math.max(1, Math.min(100, shooterForca));
        xAnterior = gamepad2.a;
        cimaAnterior = gamepad2.dpad_up;
        baixoAnterior = gamepad2.dpad_down;

        double shooterRpm = shooterLigado ? SHOOTER_RPM_MAX * shooterForca / 100.0 : 0;
        shooter.setVelocity(shooterRpm * SHOOTER_TICKS_POR_VOLTA / 60); // ticks por segundo

        telemetry.addData("Versão", "VanguardTeleop " + VERSAO);
        telemetry.addData("Shooter", shooterLigado ? "LIGADO (%d%%)" : "desligado (%d%%)", shooterForca);
        telemetry.addData("Shooter (RPM)", "%.0f / %.0f",
                shooter.getVelocity() * 60 / SHOOTER_TICKS_POR_VOLTA, shooterRpm);
        telemetry.addData("Modo lento", gamepad1.right_trigger > 0.1);
        telemetry.addData("Analógico esq. (X, Y)", "%.2f, %.2f", gamepad1.left_stick_x, gamepad1.left_stick_y);
        telemetry.addData("Potência lf rf lb rb", "%.2f %.2f %.2f %.2f", powers[0], powers[1], powers[2], powers[3]);
        telemetry.update();
    }

    public void drive() {
        double forward = -gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x * 1.15;
        double turn = gamepad1.right_stick_x;

        // segurar posição: com os analógicos soltos, o robô volta pro lugar se for empurrado
        boolean soltos = Math.abs(forward) < ZONA_MORTA && Math.abs(strafe) < ZONA_MORTA
                && Math.abs(turn) < ZONA_MORTA;
        telemetry.addData("Segurando posição", soltos ? "SIM" : "não");
        if (soltos) {
            segurarPosicao();
            return;
        }
        if (segurando) {
            // o piloto mexeu: volta pro controle normal
            for (DcMotorEx m : new DcMotorEx[]{lF, rF, lB, rB}) {
                m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
            segurando = false;
        }

        // modo lento: enquanto segura o R2 do controle 1
        if (gamepad1.right_trigger > 0.1) {
            forward *= MODO_LENTO;
            strafe *= MODO_LENTO;
            turn *= MODO_LENTO;
        }

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

    /** grava a posição atual das rodas uma vez e fica puxando elas de volta pra ela */
    public void segurarPosicao() {
        if (!segurando) {
            for (DcMotorEx m : new DcMotorEx[]{lF, rF, lB, rB}) {
                m.setTargetPosition(m.getCurrentPosition());
                m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                m.setPower(SEGURAR_FORCA);
            }
            segurando = true;
        }
    }

    public double maximum(double[] vals) {
        double big = 0;
        for (double val : vals) {
            big = Math.max(big, Math.abs(val));
        }

        return big;
    }
}

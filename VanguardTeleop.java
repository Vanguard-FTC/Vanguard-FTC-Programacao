package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "VanguardTeleop " + VanguardTeleop.VERSAO)
public class VanguardTeleop extends OpMode {
    /** versão do TeleOp: aumente a cada atualização (ver README, Patch notes) */
    public static final String VERSAO = "0.1.16";

    public DcMotorEx lF, rF, lB, rB, intoiker, intoiker2, shooter;

    /** ticks por volta do motor do shooter (REV HD Hex sem caixa = 28, máx ~6000 RPM) */
    static final double SHOOTER_TICKS_POR_VOLTA = 28;
    /** 100% do shooter = 4000 RPM (o motor aguenta até ~6000) */
    static final double SHOOTER_RPM_MAX = 4000;
    /** ===== FORÇAS DO SHOOTER (%): mude aqui ===== */
    static final int SHOOTER_FORCA_QUADRADO = 22;   // Quadrado no PlayStation / X no Xbox
    static final int SHOOTER_FORCA_BOLINHA = 50;    // Bolinha no PlayStation / B no Xbox
    static final int SHOOTER_FORCA_TRIANGULO = -100; // Triângulo no PlayStation / Y no Xbox (ao contrário)

    /** potência dos intakes (troque o sinal se girar ao contrário) */
    static final double INTAKE1_FORCA = 1.0;
    static final double INTAKE2_FORCA = 1.0;

    /** força atual do shooter em % (0 = desligado, negativo = ao contrário) */
    int shooterForca = 0;
    /** shooter pronto = velocidade a menos disso do alvo (RPM): o controle 2 vibra */
    static final double SHOOTER_TOLERANCIA_RPM = 100;
    boolean shooterPronto = false;

    /** aviso de fim de partida: os dois controles vibram faltando isso (segundos) */
    static final double TELEOP_DURACAO = 120;
    static final double AVISO_FIM = 15;
    ElapsedTime tempo = new ElapsedTime();
    boolean avisouFim = false;

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
        corDosControles(gamepad1, gamepad2); // luz já no INIT

        // freio: quando não tem força, os motores do drive travam em vez de ficar soltos
        for (DcMotorEx m : new DcMotorEx[]{lF, rF, lB, rB}) {
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    @Override
    public void init_loop() {
        manterLuz();
    }

    @Override
    public void start() {
        tempo.reset();
        corDosControles(gamepad1, gamepad2);
        luzTimer.reset();

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
        manterLuz();
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

        // shooter (controle 2): cada botão escolhe uma força; X (PS) / A (Xbox) desliga
        if (gamepad2.x) shooterForca = SHOOTER_FORCA_QUADRADO;
        if (gamepad2.b) shooterForca = SHOOTER_FORCA_BOLINHA;
        if (gamepad2.y) shooterForca = SHOOTER_FORCA_TRIANGULO;
        if (gamepad2.a) shooterForca = 0;
        boolean shooterLigado = shooterForca != 0;

        double shooterRpm = SHOOTER_RPM_MAX * shooterForca / 100.0;
        shooter.setVelocity(shooterRpm * SHOOTER_TICKS_POR_VOLTA / 60); // ticks por segundo

        // controle 2 vibra uma vez quando o shooter chega na velocidade
        double rpmAtual = shooter.getVelocity() * 60 / SHOOTER_TICKS_POR_VOLTA;
        boolean pronto = shooterLigado && Math.abs(rpmAtual - shooterRpm) < SHOOTER_TOLERANCIA_RPM;
        if (pronto && !shooterPronto) gamepad2.rumble(300);
        shooterPronto = pronto;

        // faltando 15 s: os dois controles vibram 3 vezes pra lembrar do PARK
        double restante = TELEOP_DURACAO - tempo.seconds();
        if (restante <= AVISO_FIM && !avisouFim) {
            gamepad1.rumbleBlips(3);
            gamepad2.rumbleBlips(3);
            avisouFim = true;
        }

        telemetry.addData("Versão", "VanguardTeleop " + VERSAO);
        telemetry.addData("Tempo restante", "%.0f s", Math.max(0, restante));
        telemetry.addData("Shooter pronto", pronto ? "SIM" : "não");
        telemetry.addData("Shooter", shooterForca > 0 ? "LIGADO (%d%%)"
                : shooterForca < 0 ? "AO CONTRÁRIO (%d%%)" : "desligado", Math.abs(shooterForca));
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
        double turn = -gamepad1.right_stick_x; // invertido

        // segurar posição: com os analógicos soltos, o robô volta pro lugar se for empurrado
        boolean soltos = Math.abs(forward) < ZONA_MORTA && Math.abs(strafe) < ZONA_MORTA
                && Math.abs(turn) < ZONA_MORTA;
        telemetry.addData("Segurando posição", soltos ? "SIM" : "não");
        // diagnóstico: posição de cada roda (encoder) / alvo. Se um número não muda ao girar a roda, o encoder não está ligado
        telemetry.addData("lF", "%d / %d", lF.getCurrentPosition(), lF.getTargetPosition());
        telemetry.addData("rF", "%d / %d", rF.getCurrentPosition(), rF.getTargetPosition());
        telemetry.addData("lB", "%d / %d", lB.getCurrentPosition(), lB.getTargetPosition());
        telemetry.addData("rB", "%d / %d", rB.getCurrentPosition(), rB.getTargetPosition());
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

    /** cor da luz dos controles de PS5 (vermelho, verde, azul de 0 a 1). Usada no TeleOp e nos autos */
    static void corDosControles(Gamepad g1, Gamepad g2) {
        g1.setLedColor(1, 1, 0, Gamepad.LED_DURATION_CONTINUOUS);   // controle 1: amarelo
        g2.setLedColor(0.6, 0, 1, Gamepad.LED_DURATION_CONTINUOUS); // controle 2: roxo
    }

    /** reaplica a cor a cada 1 s, pra luz não voltar pro padrão */
    ElapsedTime luzTimer = new ElapsedTime();
    public void manterLuz() {
        if (luzTimer.seconds() > 1) {
            corDosControles(gamepad1, gamepad2);
            luzTimer.reset();
        }
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

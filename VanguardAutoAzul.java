package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/** Autônomo da aliança azul: anda pra esquerda. A sequência fica no VanguardAuto.java. */
// preselectTeleOp: quando o auto acaba, o Driver Station já deixa o TeleOp escolhido
@Autonomous(name = "VanguardAuto Azul " + VanguardAuto.VERSAO,
        preselectTeleOp = "VanguardTeleop " + VanguardTeleop.VERSAO)
public class VanguardAutoAzul extends VanguardAuto {
    @Override
    protected int lado() {
        return -1;
    }
}

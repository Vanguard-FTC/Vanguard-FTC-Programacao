package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/** Autônomo da aliança vermelho: anda pra direita. A sequência fica no VanguardAuto.java. */
// preselectTeleOp: quando o auto acaba, o Driver Station já deixa o TeleOp escolhido
@Autonomous(name = "VanguardAuto Vermelho " + VanguardAuto.VERSAO,
        preselectTeleOp = "VanguardTeleop " + VanguardTeleop.VERSAO)
public class VanguardAutoVermelho extends VanguardAuto {
    @Override
    protected int lado() {
        return 1;
    }
}

# Vanguard FTC

Código da equipe **Vanguard** para a [FIRST Tech Challenge](https://www.firstinspires.org/robotics/ftc).

## Estrutura

```
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/
└── util/
    └── VanguardExemplo.java   # TeleOp com drive mecanum + intake
```

## OpModes

### VanguardExemplo (TeleOp)

Drive mecanum controlado pelo gamepad 1 e intake pelo gamepad 2.

| Gamepad | Controle | Função |
|---|---|---|
| 1 | Analógico esquerdo (Y) | Frente / ré |
| 1 | Analógico esquerdo (X) | Andar de lado (strafe) |
| 1 | Analógico direito (X) | Girar |
| 2 | Analógico esquerdo (Y) | Intake |

**Configuração de hardware** (nomes no Robot Configuration do Driver Station):

| Nome | Dispositivo |
|---|---|
| `LeftFront` | Motor dianteiro esquerdo |
| `RightFront` | Motor dianteiro direito |
| `LeftBack` | Motor traseiro esquerdo |
| `RightBack` | Motor traseiro direito |
| `intake` | Motor do intake |

## Como usar

Este repositório contém só a pasta `TeamCode`. Para rodar no robô:

1. Baixe o [FTC SDK oficial](https://github.com/FIRST-Tech-Challenge/FtcRobotController).
2. Copie a pasta `TeamCode/src/main/java/org/firstinspires/ftc/teamcode` deste repositório para dentro do SDK.
3. Abra o projeto no Android Studio e faça o deploy no Control Hub.

Também dá para colar o arquivo direto no **OnBot Java**, pelo navegador do Control Hub.

# Vanguard FTC

Código da equipe **Vanguard** para a [FIRST Tech Challenge](https://www.firstinspires.org/robotics/ftc).

## Estrutura

```
VanguardExemplo.java   # TeleOp com drive mecanum + intake
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

**OnBot Java (mais fácil):** abra o OnBot Java pelo navegador do Control Hub, crie a pasta `util` e cole o conteúdo de `VanguardExemplo.java` lá dentro.

**Android Studio:**

1. Baixe o [FTC SDK oficial](https://github.com/FIRST-Tech-Challenge/FtcRobotController).
2. Coloque `VanguardExemplo.java` em `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/util/` (a pasta precisa bater com o `package` do arquivo).
3. Faça o deploy no Control Hub.

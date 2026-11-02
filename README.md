# Vanguard FTC

Código da equipe **Vanguard** para a [FIRST Tech Challenge](https://www.firstinspires.org/robotics/ftc).

🌐 **Site da equipe:** https://gabriel-pasi.github.io/Vanguard-FTC---Site/

## Estrutura

```
VanguardExemplo.java   # TeleOp com drive mecanum + intake
VanguardAuto.java      # Autônomo pelos encoders dos motores
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
| 2 | R2 (segurar) | Shooter |

**Configuração de hardware** (nomes no Robot Configuration do Driver Station):

| Nome | Dispositivo |
|---|---|
| `LeftFront` | Motor dianteiro esquerdo |
| `RightFront` | Motor dianteiro direito |
| `LeftBack` | Motor traseiro esquerdo |
| `RightBack` | Motor traseiro direito |
| `intake` | Motor do intake |
| `ShooterMotor1` | Motor do shooter |

### VanguardAuto (Autônomo)

Autônomo que se move pelos encoders dos motores do drive (`RUN_TO_POSITION`), sem odometria. Por enquanto ele só anda 50 cm pra direita. A sequência fica no fim do `runOpMode()` e também dá pra usar `andar(cm)` (positivo = frente):

```java
andarDeLado(50);   // 50 cm pra direita (negativo = esquerda)
```

Antes de usar, confira as constantes no topo do arquivo:

| Constante | O que é | Padrão |
|---|---|---|
| `TICKS_POR_VOLTA` | Ticks por volta do motor | `537.7` (goBILDA 312 RPM) |
| `DIAMETRO_RODA_CM` | Diâmetro da roda | `10.4` (mecanum goBILDA 104 mm) |
| `FATOR_STRAFE` | Correção do andar de lado | `1.15` |
| `FRENTE` | Sentido da frente (`-1` ou `1`) | `-1` |
| `VELOCIDADE` | Potência dos motores | `0.5` |

## Como usar

**OnBot Java (mais fácil):** abra o OnBot Java pelo navegador do Control Hub, crie a pasta `util` e cole os arquivos `.java` lá dentro.

**Android Studio:**

1. Baixe o [FTC SDK oficial](https://github.com/FIRST-Tech-Challenge/FtcRobotController).
2. Coloque os arquivos `.java` em `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/util/` (a pasta precisa bater com o `package` do arquivo).
3. Faça o deploy no Control Hub.

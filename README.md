# Vanguard FTC

Código da equipe **Vanguard** para a [FIRST Tech Challenge](https://www.firstinspires.org/robotics/ftc).

🌐 **Site da equipe:** https://vanguard-ftc.github.io/Vanguard-FTC-Site/

## Estrutura

```
VanguardTeleop.java    # TeleOp com drive mecanum + intake
VanguardAuto.java      # Autônomo pelos encoders dos motores
VanguardTesteMotores.java # Teste: gira cada motor do drive separado
```

## OpModes

### VanguardTeleop (`VanguardTeleop.java`)

Drive mecanum controlado pelo gamepad 1 e intake pelo gamepad 2.

| Gamepad | Controle | Função |
|---|---|---|
| 1 | Analógico esquerdo (Y) | Frente / ré |
| 1 | Analógico esquerdo (X) | Andar de lado (strafe) |
| 1 | Analógico direito (X) | Girar |
| 1 | R2 (segurar) | Modo lento (40%) |
| 1 | A (segurar) | Modo mira: gira sozinho até a AprilTag (Limelight) |
| 2 | L2 (segurar) | Intake 1 |
| 2 | R2 (segurar) | Intake 2 |
| 2 | X (PlayStation) / A (Xbox) | Liga/desliga o shooter (força em `SHOOTER_FORCA`) |

**Configuração de hardware** (nomes no Robot Configuration do Driver Station):

| Nome | Dispositivo |
|---|---|
| `frontLeftMotor` | Motor dianteiro esquerdo |
| `frontRightMotor` | Motor dianteiro direito |
| `backLeftMotor` | Motor traseiro esquerdo |
| `backRightMotor` | Motor traseiro direito |
| `IntakeMotor1` | Motor 1 do intake |
| `IntakeMotor2` | Motor 2 do intake |
| `ShooterMotor` | Motor do shooter |
| `limelight` | Câmera Limelight 3A (pipeline 0 = AprilTag) |

### VanguardAuto (`VanguardAuto.java`)

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
| `FRENTE` | Sentido da frente (`-1` ou `1`) | `1` |
| `VELOCIDADE` | Potência dos motores | `0.5` |

## Patch notes

O número aparece no nome do OpMode no Driver Station (ex: `VanguardTeleop 0.1.0`).

### VanguardTeleop 0.1.3 (2026-11-07)
- 100% do shooter agora é 4000 RPM. Com `SHOOTER_FORCA = 0.87`, ele gira a 3480 RPM.

**Controle 1 (piloto)**
- Analógico esquerdo: andar (frente, trás e lados)
- Analógico direito: girar
- R2 (segurar): modo lento (40%)
- A no Xbox / X no PlayStation (segurar): modo mira com a Limelight

**Controle 2 (mecanismos)**
- L2 (segurar): intake 1
- R2 (segurar): intake 2
- X no PlayStation / A no Xbox: liga/desliga o shooter

**Como mudar a força do shooter**
1. No OnBot Java, abram o `VanguardTeleop.java`.
2. Lá no topo, achem esta linha:
   ```java
   static final double SHOOTER_FORCA = 0.87;
   ```
3. Troquem o número. `1.0` = 100% (4000 RPM), `0.87` = 87% (3480 RPM), `0.5` = 50% (2000 RPM).
4. Cliquem em **Build** e rodem o TeleOp de novo.
5. Liguem o shooter (X) e confiram no Driver Station se o **Shooter (RPM)** chegou perto do alvo.

Dica: mudem de pouco em pouco (ex: `0.87` → `0.90`) e testem alguns lançamentos antes de mudar de novo.

### VanguardTeleop 0.1.2 (2026-11-07)
- Shooter agora liga e desliga no X do controle 2 (aperta uma vez liga, de novo desliga).
- Força do shooter em 87%, na variável `SHOOTER_FORCA`.
- Intake 1 no L2 e intake 2 no R2 do controle 2, separados.

### VanguardTeleop 0.1.1 (2026-11-06)
- Arquivo renomeado de `VanguardExemplo.java` pra `VanguardTeleop.java`.

### VanguardTeleop 0.1.0 / VanguardAuto 0.1.0 (2026-11-06)
- Início da numeração de versões.
- TeleOp: drive mecanum, modo lento (R2 do controle 1), modo mira com Limelight (A do controle 1, câmera opcional), intake com 2 motores (analógico direito do controle 2) e shooter com velocidade fixa (R2 do controle 2).
- Auto: anda 50 cm pra direita, pelos encoders.

## Como usar

**OnBot Java (mais fácil):** abra o OnBot Java pelo navegador do Control Hub, crie a pasta `util` e cole os arquivos `.java` lá dentro.

**Android Studio:**

1. Baixe o [FTC SDK oficial](https://github.com/FIRST-Tech-Challenge/FtcRobotController).
2. Coloque os arquivos `.java` em `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/util/` (a pasta precisa bater com o `package` do arquivo).
3. Faça o deploy no Control Hub.

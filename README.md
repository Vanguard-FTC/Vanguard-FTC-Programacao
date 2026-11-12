# Vanguard FTC

Código da equipe **Vanguard** para a [FIRST Tech Challenge](https://www.firstinspires.org/robotics/ftc).

🌐 **Site da equipe:** https://vanguard-ftc.github.io/Vanguard-FTC-Site/

## Estrutura

```
VanguardTeleop.java       # TeleOp
VanguardAuto.java         # Sequência do autônomo (base, não aparece no Driver Station)
VanguardAutoAzul.java     # Autônomo da aliança azul (anda pra esquerda)
VanguardAutoVermelho.java # Autônomo da aliança vermelha (anda pra direita)
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
| 2 | L2 (segurar) | Intake 1 pra frente |
| 2 | L1 (segurar) | Intake 1 ao contrário |
| 2 | R2 (segurar) | Intake 2 pra frente |
| 2 | R1 (segurar) | Intake 2 ao contrário |
| 2 | Quadrado (PlayStation) / X (Xbox) | Shooter 22% |
| 2 | Bolinha (PlayStation) / B (Xbox) | Shooter 50% |
| 2 | Triângulo (PlayStation) / Y (Xbox) | Shooter ao contrário 100% |
| 2 | X (PlayStation) / A (Xbox) | Desliga o shooter |

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

### VanguardAuto Azul / Vermelho (`VanguardAuto.java`)

No Driver Station aparecem dois autos: **VanguardAuto Azul** e **VanguardAuto Vermelho**. Os dois fazem a mesma sequência, que fica no `runOpMode()` do `VanguardAuto.java`:

1. Liga o shooter (`AUTO_SHOOTER_FORCA`, hoje 100%) e espera chegar na velocidade (no máximo `AUTO_ESPERA_SHOOTER_S` segundos).
2. Liga os dois intakes por `AUTO_TEMPO_INTAKE_S` segundos pra lançar os pollen.
3. Desliga tudo.
4. Anda `AUTO_DISTANCIA_CM` (hoje 5 cm) pro lado: **azul pra esquerda, vermelho pra direita**.

O movimento usa os encoders dos motores do drive (`RUN_TO_POSITION`), sem odometria.

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

### VanguardAuto 0.2.0 (2026-11-12)
- Agora são dois autos: **VanguardAuto Azul** (anda pra esquerda) e **VanguardAuto Vermelho** (anda pra direita).
- Sequência nova: liga o shooter a 100% e espera chegar na velocidade, liga os dois intakes por 3 s pra lançar, desliga tudo e anda 5 cm pro lado.
- Tudo ajustável no topo do `VanguardAuto.java`: `AUTO_SHOOTER_FORCA`, `AUTO_ESPERA_SHOOTER_S`, `AUTO_TEMPO_INTAKE_S` e `AUTO_DISTANCIA_CM`.

### VanguardTeleop 0.1.13 (2026-11-12)
- Forças do shooter definidas: Quadrado = 22%, Bolinha = 50%, Triângulo = ao contrário 100%.
- X (PlayStation) / A (Xbox) desliga o shooter, qualquer que seja a força escolhida.
- D-pad não mexe mais no shooter.

**Controle 1 (piloto) — luz amarela**
- Analógico esquerdo: andar (frente, trás e lados)
- Analógico direito: girar
- R2 (segurar): modo lento (40%)
- Analógicos soltos: o robô segura a posição

**Controle 2 (mecanismos) — luz roxa**
- L2 (segurar): intake 1 pra frente
- L1 (segurar): intake 1 ao contrário
- R2 (segurar): intake 2 pra frente
- R1 (segurar): intake 2 ao contrário
- Quadrado no PlayStation / X no Xbox: shooter 22%
- Bolinha no PlayStation / B no Xbox: shooter 50%
- Triângulo no PlayStation / Y no Xbox: shooter ao contrário 100%
- X no PlayStation / A no Xbox: desliga o shooter

**Como mudar as forças**
No topo do `VanguardTeleop.java`: `SHOOTER_FORCA_QUADRADO`, `SHOOTER_FORCA_BOLINHA` e `SHOOTER_FORCA_TRIANGULO` (em %, 100% = 4000 RPM, negativo = ao contrário). Depois cliquem em **Build**.

### VanguardTeleop 0.1.12 / VanguardAuto 0.1.2 (2026-11-12)
- Controle 2 vibra quando o shooter chega na velocidade certa (pronto pra lançar).
- Faltando 15 segundos, os dois controles vibram 3 vezes pra lembrar do PARK.
- O Driver Station mostra o tempo restante e se o shooter está pronto.
- Quando o auto termina, o Driver Station já deixa o VanguardTeleop escolhido.

**Controle 1 (piloto) — luz amarela**
- Analógico esquerdo: andar (frente, trás e lados)
- Analógico direito: girar
- R2 (segurar): modo lento (40%)
- Analógicos soltos: o robô segura a posição

**Controle 2 (mecanismos) — luz roxa**
- L2 (segurar): intake 1 pra frente
- L1 (segurar): intake 1 ao contrário
- R2 (segurar): intake 2 pra frente
- R1 (segurar): intake 2 ao contrário
- X no PlayStation / A no Xbox: liga o shooter em 1% / desliga
- D-pad cima: shooter +1%
- D-pad baixo: shooter -1%

**Transição do auto pro TeleOp**
1. Antes da partida, escolham o **VanguardAuto** e apertem **INIT**. Confiram se o timer de 30 s está ligado (ícone de relógio).
2. O auto roda e para sozinho nos 30 s.
3. Nos 8 s de transição, o **VanguardTeleop** já aparece escolhido. Apertem **INIT**.
4. Quando o juiz der o início do TeleOp, apertem **Play** (▶). O tempo restante começa a contar nessa hora.
5. Não mexam no robô durante a transição: qualquer movimento é falta.

### VanguardTeleop 0.1.11 (2026-11-12)
- Freio nos motores do drive: quando solta o analógico, o robô para na hora.
- Segurar posição (sempre ligado): com os analógicos do controle 1 soltos, se empurrarem o robô, ele volta pro lugar. Mexeu o analógico, dirige normal.
- A força pra voltar fica em `SEGURAR_FORCA` (hoje 0.5) no topo do arquivo.

**Controle 1 (piloto) — luz amarela**
- Analógico esquerdo: andar (frente, trás e lados)
- Analógico direito: girar
- R2 (segurar): modo lento (40%)
- Analógicos soltos: o robô segura a posição

**Controle 2 (mecanismos) — luz roxa**
- L2 (segurar): intake 1 pra frente
- L1 (segurar): intake 1 ao contrário
- R2 (segurar): intake 2 pra frente
- R1 (segurar): intake 2 ao contrário
- X no PlayStation / A no Xbox: liga o shooter em 1% / desliga
- D-pad cima: shooter +1%
- D-pad baixo: shooter -1%

### VanguardTeleop 0.1.10 (2026-11-12)
- Câmera Limelight tirada da programação. O modo mira (A/X do controle 1) não existe mais, e o botão ficou livre.

**Controle 1 (piloto) — luz amarela**
- Analógico esquerdo: andar (frente, trás e lados)
- Analógico direito: girar
- R2 (segurar): modo lento (40%)

**Controle 2 (mecanismos) — luz roxa**
- L2 (segurar): intake 1 pra frente
- L1 (segurar): intake 1 ao contrário
- R2 (segurar): intake 2 pra frente
- R1 (segurar): intake 2 ao contrário
- X no PlayStation / A no Xbox: liga o shooter em 1% / desliga
- D-pad cima: shooter +1%
- D-pad baixo: shooter -1%

### VanguardTeleop 0.1.9 (2026-11-12)
- Luz dos controles de PS5: controle 1 fica amarelo e controle 2 fica roxo quando o TeleOp começa.

**Controle 1 (piloto) — luz amarela**
- Analógico esquerdo: andar (frente, trás e lados)
- Analógico direito: girar
- R2 (segurar): modo lento (40%)
- A no Xbox / X no PlayStation (segurar): modo mira com a Limelight

**Controle 2 (mecanismos) — luz roxa**
- L2 (segurar): intake 1 pra frente
- L1 (segurar): intake 1 ao contrário
- R2 (segurar): intake 2 pra frente
- R1 (segurar): intake 2 ao contrário
- X no PlayStation / A no Xbox: liga o shooter em 1% / desliga
- D-pad cima: shooter +1%
- D-pad baixo: shooter -1%

### VanguardTeleop 0.1.8 (2026-11-10)
- Sentido do shooter invertido.

**Controle 1 (piloto)**
- Analógico esquerdo: andar (frente, trás e lados)
- Analógico direito: girar
- R2 (segurar): modo lento (40%)
- A no Xbox / X no PlayStation (segurar): modo mira com a Limelight

**Controle 2 (mecanismos)**
- L2 (segurar): intake 1 pra frente
- L1 (segurar): intake 1 ao contrário
- R2 (segurar): intake 2 pra frente
- R1 (segurar): intake 2 ao contrário
- X no PlayStation / A no Xbox: liga o shooter em 1% / desliga
- D-pad cima: shooter +1%
- D-pad baixo: shooter -1%

### VanguardTeleop 0.1.7 (2026-11-09)
- Shooter novo: X/A liga em 1% e desliga. D-pad pra cima aumenta 1%, pra baixo diminui 1% (de 1% a 100%).
- Tirados os botões Quadrado (83%), Bolinha (75%) e Triângulo (ao contrário).
- O Driver Station mostra a força atual do shooter em %.

**Controle 1 (piloto)**
- Analógico esquerdo: andar (frente, trás e lados)
- Analógico direito: girar
- R2 (segurar): modo lento (40%)
- A no Xbox / X no PlayStation (segurar): modo mira com a Limelight

**Controle 2 (mecanismos)**
- L2 (segurar): intake 1 pra frente
- L1 (segurar): intake 1 ao contrário
- R2 (segurar): intake 2 pra frente
- R1 (segurar): intake 2 ao contrário
- X no PlayStation / A no Xbox: liga o shooter em 1% / desliga
- D-pad cima: shooter +1%
- D-pad baixo: shooter -1%

**Como mudar o shooter**
- No topo do `VanguardTeleop.java`: `SHOOTER_FORCA_INICIAL` (em quanto liga, hoje 1%) e `SHOOTER_PASSO` (quanto cada toque muda, hoje 1%).
- 100% = 4000 RPM.

### VanguardTeleop 0.1.6 (2026-11-09)
- Triângulo (Y no Xbox) no controle 2 liga o shooter ao contrário a 100%. Aperta de novo pra desligar.

**Controle 1 (piloto)**
- Analógico esquerdo: andar (frente, trás e lados)
- Analógico direito: girar
- R2 (segurar): modo lento (40%)
- A no Xbox / X no PlayStation (segurar): modo mira com a Limelight

**Controle 2 (mecanismos)**
- L2 (segurar): intake 1 pra frente
- L1 (segurar): intake 1 ao contrário
- R2 (segurar): intake 2 pra frente
- R1 (segurar): intake 2 ao contrário
- X no PlayStation / A no Xbox: shooter 87%
- Quadrado no PlayStation / X no Xbox: shooter 83%
- Bolinha no PlayStation / B no Xbox: shooter 75%
- Triângulo no PlayStation / Y no Xbox: shooter ao contrário 100%

### VanguardTeleop 0.1.5 (2026-11-09)
- Intake 1 ao contrário no L1 e intake 2 ao contrário no R1 (controle 2).

**Controle 1 (piloto)**
- Analógico esquerdo: andar (frente, trás e lados)
- Analógico direito: girar
- R2 (segurar): modo lento (40%)
- A no Xbox / X no PlayStation (segurar): modo mira com a Limelight

**Controle 2 (mecanismos)**
- L2 (segurar): intake 1 pra frente
- L1 (segurar): intake 1 ao contrário
- R2 (segurar): intake 2 pra frente
- R1 (segurar): intake 2 ao contrário
- X no PlayStation / A no Xbox: shooter 87%
- Quadrado no PlayStation / X no Xbox: shooter 83%
- Bolinha no PlayStation / B no Xbox: shooter 75%

### VanguardTeleop 0.1.4 (2026-11-09)
- Shooter com 3 forças, cada uma num botão do controle 2: X = 87%, Quadrado = 83%, Bolinha = 75%.
- Apertar o mesmo botão de novo desliga. Apertar outro botão troca a força na hora.

**Controle 1 (piloto)**
- Analógico esquerdo: andar (frente, trás e lados)
- Analógico direito: girar
- R2 (segurar): modo lento (40%)
- A no Xbox / X no PlayStation (segurar): modo mira com a Limelight

**Controle 2 (mecanismos)**
- L2 (segurar): intake 1
- R2 (segurar): intake 2
- X no PlayStation / A no Xbox: shooter 87%
- Quadrado no PlayStation / X no Xbox: shooter 83%
- Bolinha no PlayStation / B no Xbox: shooter 75%

**Como mudar as forças do shooter**
1. No OnBot Java, abram o `VanguardTeleop.java`.
2. Lá no topo, achem estas linhas:
   ```java
   static final double SHOOTER_FORCA_X = 0.87;
   static final double SHOOTER_FORCA_QUADRADO = 0.83;
   static final double SHOOTER_FORCA_BOLINHA = 0.75;
   ```
3. Troquem o número de qualquer uma. `1.0` = 100% (4000 RPM), `0.5` = 50% (2000 RPM).
4. Cliquem em **Build** e rodem o TeleOp de novo.

### VanguardAuto 0.1.1 (2026-11-07)
- O auto agora anda metade da distância: `andarDeLado(50)` virou `andarDeLado(25)`.

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

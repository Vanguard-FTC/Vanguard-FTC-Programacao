# Vanguard FTC

## Nomes dos motores (NUNCA mudar)

A equipe pediu que estes nomes de hardware sejam mantidos **para sempre**. Eles
precisam bater exatamente com a configuração do robô no Driver Station. Todo
OpMode novo ou alterado deve usar exatamente estes nomes no `hardwareMap.get`:

| Nome | Motor |
|---|---|
| `frontLeftMotor` | Drive dianteiro esquerdo |
| `frontRightMotor` | Drive dianteiro direito |
| `backLeftMotor` | Drive traseiro esquerdo |
| `backRightMotor` | Drive traseiro direito |
| `IntakeMotor1` | Intake 1 |
| `IntakeMotor2` | Intake 2 |
| `ShooterMotor` | Shooter |
| `limelight` | Câmera Limelight 3A |

Só mude esses nomes se a equipe pedir explicitamente.

## Versões e patch notes (SEMPRE)

Toda atualização do TeleOp ou do Auto precisa:

1. **Aumentar a versão** do OpMode alterado, na constante `VERSAO` no topo do arquivo
   (`VanguardExemplo.java` → `VanguardTeleop`, `VanguardAuto.java` → `VanguardAuto`).
   O nome no Driver Station vem dela (ex: `VanguardTeleop 0.1.26`).
   - Mudança normal: aumente o último número (`0.1.0` → `0.1.1`).
   - Mudança grande (ex: novo mecanismo, auto novo): aumente o do meio e zere o último (`0.1.7` → `0.2.0`).
2. **Adicionar uma entrada nas Patch notes do README**, a mais nova no topo, com
   `### VanguardTeleop X.Y.Z (data)` (e/ou `VanguardAuto`) e uma lista curta do que mudou, em português simples.

## Outras notas

- A equipe programa pelo **OnBot Java**: nada de bibliotecas externas (Gradle).
- Os arquivos `.java` ficam na raiz do repositório, com `package org.firstinspires.ftc.teamcode.util;`.
- Responda em português, de forma simples e curta.

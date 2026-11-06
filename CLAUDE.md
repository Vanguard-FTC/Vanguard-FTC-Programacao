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

## Outras notas

- A equipe programa pelo **OnBot Java**: nada de bibliotecas externas (Gradle).
- Os arquivos `.java` ficam na raiz do repositório, com `package org.firstinspires.ftc.teamcode.util;`.
- Responda em português, de forma simples e curta.

# Campo Minado Android

Projeto acadêmico de Campo Minado desenvolvido em Kotlin com Jetpack Compose.

## Requisitos

- Android Studio recente
- JDK 17
- Android SDK 35
- Emulador ou dispositivo Android com API 24 ou superior

## Como executar

1. Clone o repositório.
2. Abra a pasta do projeto no Android Studio.
3. Aguarde a sincronização do Gradle.
4. Selecione um emulador ou conecte um celular com depuração USB habilitada.
5. Clique em **Run**.

## Controles

- Toque em uma casa para revelar.
- Toque e segure em uma casa fechada para marcar/remover uma bandeira.
- Escolha Fácil, Médio ou Difícil para iniciar uma nova partida nessa dificuldade.
- Use **Nova partida** para reiniciar.

## Organização

- `game/`: regras e estado do jogo.
- `viewmodel/`: estado da tela, cronômetro e fluxo da partida.
- `MainActivity.kt`: interface Android com Jetpack Compose.

## Observação sobre persistência

A tela já possui o contrato `PartidaResultSaver`. Nesta etapa é utilizado um salvador temporário sem persistência, para permitir executar o jogo antes da integração com Room. Os resultados ainda não ficam gravados após fechar o aplicativo.

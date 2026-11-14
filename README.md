# Campo Minado Android

Projeto acadÃªmico de Campo Minado desenvolvido em Kotlin com Jetpack Compose.

## Requisitos

- Android Studio recente
- JDK 17
- Android SDK 35
- Emulador ou dispositivo Android com API 24 ou superior

## Como executar

1. Clone o repositÃ³rio.
2. Abra a pasta do projeto no Android Studio.
3. Aguarde a sincronizaÃ§Ã£o do Gradle.
4. Selecione um emulador ou conecte um celular com depuraÃ§Ã£o USB habilitada.
5. Clique em **Run**.

## Controles

- Toque em uma casa para revelar.
- Toque e segure em uma casa fechada para marcar/remover uma bandeira.
- Escolha FÃ¡cil, MÃ©dio ou DifÃ­cil para iniciar uma nova partida nessa dificuldade.
- Use **Nova partida** para reiniciar.

## OrganizaÃ§Ã£o

- `game/`: regras e estado do jogo.
- `viewmodel/`: estado da tela, cronÃ´metro e fluxo da partida.
- `MainActivity.kt`: interface Android com Jetpack Compose.

## Persistencia

Partidas e niveis personalizados sao armazenados localmente no Room.

## Configuracao do Firebase

Para habilitar as dicas do Firestore, baixe o arquivo de configuração do app Android no Firebase Console e coloque-o em `app/google-services.json`. O pacote Android esperado é `com.campominado.app`. No Firestore, crie a coleção `dicas` com documentos contendo os campos `id`, `titulo` e `descricao`.

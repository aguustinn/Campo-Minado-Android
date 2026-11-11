# Módulo do jogo — Agustin Brasileiro

## Responsabilidade

Implementação das regras do Campo Minado e gerenciamento do estado da partida.

## Arquivos

- `game/Difficulty.kt`: níveis de dificuldade e dimensões.
- `game/GameStatus.kt`: estados READY, PLAYING, WON e LOST.
- `game/CellUiState.kt`: dados visuais de cada célula.
- `game/GameUiState.kt`: estado geral consumido pela interface.
- `game/MinesweeperEngine.kt`: regras e processamento do tabuleiro.
- `viewmodel/GameViewModel.kt`: estado reativo, ações, cronômetro e resultado.
- `viewmodel/PartidaResultSaver.kt`: contrato de persistência.

## Regras implementadas

1. As minas são distribuídas aleatoriamente.
2. A primeira casa revelada é protegida.
3. Cada casa segura informa a quantidade de minas nas oito posições vizinhas.
4. Casas vazias revelam automaticamente áreas seguras adjacentes.
5. Bandeiras podem ser adicionadas e removidas em casas fechadas.
6. A partida termina ao revelar uma mina ou todas as casas seguras.
7. O cronômetro inicia na primeira revelação válida.
8. O resultado é enviado ao contrato de persistência uma única vez.

## Integração com a interface

A tela Compose deve observar `GameViewModel.uiState` com `collectAsState()`.

- Toque simples: `revealCell(row, column)`.
- Toque longo: `toggleFlag(row, column)`.
- Nova partida: `startNewGame()`.
- Alterar dificuldade: `selectDifficulty(Difficulty.EASY)`.

## Integração com Room

O responsável pelo banco deve implementar `PartidaResultSaver` utilizando o Repository e a Entity de partida existentes no projeto. Ajustar nomes dos campos conforme a implementação final do grupo.

## Testes

Os testes unitários estão em `app/src/test/java/com/campominado/app/game`.
Para executá-los, o projeto deve possuir configuração Android/Gradle e dependência JUnit.

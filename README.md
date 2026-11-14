# Campo Minado Android

Aplicativo acadêmico de Campo Minado desenvolvido em **Kotlin**, com **Jetpack Compose**, arquitetura **MVVM**, persistência local com **Room** e consulta de dicas no **Firebase Cloud Firestore**.

## Funcionalidades

- Jogo de Campo Minado com primeiro clique protegido, revelação de áreas vazias, bandeiras, cronômetro e estados de vitória/derrota.
- Níveis padrão (Fácil, Médio e Difícil).
- Administração de níveis personalizados: cadastrar, listar, editar e excluir.
- Histórico local de partidas e ranking de vitórias.
- Filtro por dificuldade e ordenação por tempo.
- Dicas carregadas da coleção `dicas` do Firestore.
- Identificação do jogador antes de iniciar uma partida.

## Tecnologias

- Kotlin e Android SDK
- Jetpack Compose e Material 3
- Navigation Compose
- ViewModel, StateFlow e Kotlin Coroutines
- Room / SQLite para persistência local
- Firebase Cloud Firestore para as dicas
- Gradle Kotlin DSL

## Requisitos para executar

- Android Studio compatível com Android Gradle Plugin 8.7.3
- JDK 17
- Android SDK 35
- Emulador ou dispositivo Android com API 24 ou superior
- Acesso à internet para carregar as dicas do Firestore

## Configuração do Firebase

1. Abra o [Firebase Console](https://console.firebase.google.com/) e selecione o projeto Firebase da equipe.
2. Registre um aplicativo Android com o identificador `com.campominado.app`.
3. Baixe o arquivo `google-services.json`.
4. Coloque o arquivo em `app/google-services.json` (dentro da pasta `app`, não na raiz).
5. No Firestore Database, crie a coleção `dicas`. Cada documento deve conter os campos String:
   - `titulo`
   - `descricao`
   - `emoji`
6. Sincronize o Gradle no Android Studio.

O arquivo `google-services.json` é uma configuração específica do projeto Firebase e está ignorado pelo Git. Cada integrante deve obtê-lo no mesmo projeto Firebase da equipe. Não publique credenciais ou arquivos privados no repositório.

## Como executar

1. Clone o repositório.
2. Configure o `google-services.json` conforme a seção anterior.
3. Abra a pasta do projeto no Android Studio.
4. Aguarde a sincronização do Gradle.
5. Selecione um emulador ou conecte um dispositivo Android com depuração USB habilitada.
6. Execute a configuração `app` pelo botão **Run**.

## Controles do jogo

- Toque em uma casa para revelá-la.
- Toque e segure em uma casa fechada para colocar ou remover uma bandeira.
- Informe o nome do jogador na tela do jogo.
- Escolha um nível e inicie uma partida.
- Use **Nova partida** para reiniciar.

## Arquitetura e organização

- `game/`: regras do Campo Minado, níveis, células e estado da interface.
- `data/local/`: entidades Room, DAOs e banco de dados.
- `data/repository/`: acesso aos dados e integração entre ViewModels e DAOs.
- `viewmodel/`: estado das telas e regras de apresentação.
- `ui/`: telas Compose de início, administração, histórico e dicas.
- `navigation/`: rotas e navegação entre telas.
- `MainActivity.kt`: entrada da aplicação e interface principal do jogo.

### Persistência

O Room/SQLite armazena localmente os níveis personalizados e os resultados das partidas. O histórico e o ranking são carregados a partir desses registros, permanecendo disponíveis após fechar e abrir novamente o aplicativo.

O Firestore é utilizado separadamente para consultar as dicas publicadas na coleção `dicas`. O aplicativo não implementa autenticação real nem sincronização em nuvem do histórico de partidas.

## Testes

Os testes unitários do motor do jogo ficam em `app/src/test`. Podem ser executados pelo Android Studio ou pelo terminal:

```bash
./gradlew test
```

No Windows:

```bat
gradlew.bat test
```

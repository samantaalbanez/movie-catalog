# 🎬 Movies Catalog App

Um aplicativo Android moderno para exploração de filmes e tendências do cinema, construído com as melhores práticas de desenvolvimento Android, **Jetpack Compose**, **Clean Architecture** e **Paging 3**.

---

## 📱 Screenshots

| Home (Sucesso) | Carrossel e Grid | Estado de Erro |
| :---: | :---: | :---: |
| *Adicione imagem/gif* | *Adicione imagem/gif* | *Adicione imagem/gif* |

---

## ✨ Funcionalidades

- **Destaques e Tendências:** Visualização dos filmes em alta no formato de carrossel horizontal (`LazyRow`).
- **Filmes Populares:** Navegação em Grid de duas colunas com **paginação infinita** (`Paging 3`).
- **Pull-to-Refresh:** Atualização manual dos dados com suporte a feedback visual otimizado.
- **Tratamento de Erros e Offline:** Telas e estados visuais amigáveis para falhas de rede ou carregamento.
- **Detalhes do Filme:** Navegação para informações completas de cada título.

---

## 🛠️ Arquitetura e Tecnologias

O projeto segue os princípios de **Clean Architecture** combinados com o padrão visual **MVI / Unidirectional Data Flow (UDF)**.

- **Linguagem:** [Kotlin](https://kotlinlang.org/)
- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) com Material Design 3
- **Injeção de Dependência:** [Hilt](https://dagger.dev/hilt/)
- **Paginação:** [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- **Assincronismo & Fluxos:** [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [StateFlow / SharedFlow / Channel]
- **Rede:** [Retrofit](https://square.github.io/retrofit/) / [Gson](https://github.com/google/gson)
- **Carregamento de Imagens:** [Coil](https://coil-kt.github.io/coil/)
- **Testes Unitários:** JUnit 4, MockK, Kotlinx Coroutines Test

---

## 📂 Estrutura do Projeto

```text
br.com.samantaalbanez.moviescatalog/
├── data/           # Camada de Dados (Repositories, Services, DTOs, Mappers, PagingSource)
├── domain/         # Camada de Negócio (Models, Use Cases, Repositories Interfaces)
├── ui/             # Camada de Apresentação (Composables, ViewModels, States, Events, Effects)
└── di/             # Módulos de Injeção de Dependência do Hilt
```

## 🧪 Rodando os Testes Unitários
```agsl
./gradlew test
```

## 🚀 Como Executar o Projeto
1. Clone o repositório:

```
git clone [https://github.com/samantaalbanez/movies-catalog.git](https://github.com/samantaalbanez/movies-catalog.git)```
```

2.  Abra o projeto

3. Adicione sua chave de API do TMDB no arquivo local.properties:

```agsl
TMDB_API_KEY="SUA_CHAVE_AQUI"
```

# Despensa Alerta

Aplicativo Android nativo (Java) para controlar a validade de suprimentos domésticos
(alimentos, produtos de limpeza e higiene) e reduzir o desperdício.

Projeto prático da disciplina de Desenvolvimento para Dispositivos Móveis (UTFPR).

| | |
|---|---|
| **Tema** | Gestão de validade de suprimentos domésticos e controle de desperdício |
| **Tipo** | Utilidade pessoal / uso individual |
| **Plataforma** | Android nativo, Java, Android Studio |
| **Persistência** | 100% local (SQLite/Room, a partir da Entrega 3) |

## Versão atual: 0.2.0 (Entrega Parcial 2: Desenvolvimento Inicial)

Itens exigidos nesta entrega:

- [x] **Estrutura do projeto**: projeto Gradle/Android Studio organizado em pacotes (`model`, `data`, `ui`, `util`)
- [x] **Telas iniciais**: Painel, Itens, Histórico, Detalhes do item e Formulário de cadastro/edição
- [x] **Navegação básica**: barra de navegação inferior entre as abas, botão "Novo item", abertura
      de detalhes ao tocar em um item e botão voltar na barra superior

### Telas

| Tela | Classe | O que mostra |
|---|---|---|
| Painel | `ui/painel/PainelFragment` | Totais de itens **Vencidos**, **Vencendo nos próximos 7 dias** e **Na validade**, mais a lista dos itens que precisam de atenção |
| Itens | `ui/itens/ItensFragment` | Todos os itens disponíveis, ordenados pela validade, com busca por nome e filtro por categoria |
| Histórico | `ui/historico/HistoricoFragment` | Itens marcados como **Consumido** ou **Descartado** |
| Detalhes | `ui/item/DetalheItemActivity` | Dados do item e as ações Editar, Consumido, Descartado e Excluir |
| Formulário | `ui/item/FormularioItemActivity` | Cadastro/edição: nome, categoria, quantidade, local e data de validade (com calendário) |

### Fluxo de navegação

```
MainActivity (barra inferior)
├── Painel ──────┐
├── Itens ───────┼── toque no item ──> DetalheItemActivity ── Editar ──> FormularioItemActivity
├── Histórico ───┘
└── botão "Novo item" ─────────────────────────────────────────────────> FormularioItemActivity
```

### Estrutura do código

```
app/src/main/java/br/edu/utfpr/despensaalerta/
├── MainActivity.java            # Activity principal e troca de abas
├── model/
│   ├── Item.java                # Entidade do item
│   ├── StatusValidade.java      # Vencido / Vencendo (7 dias) / Na validade
│   └── SituacaoItem.java        # Ativo / Consumido / Descartado
├── data/
│   └── ItemRepository.java      # Acesso aos dados (em memória nesta versão)
├── ui/
│   ├── adapter/ItemAdapter.java # RecyclerView compartilhado pelas listas
│   ├── painel/PainelFragment.java
│   ├── itens/ItensFragment.java
│   ├── historico/HistoricoFragment.java
│   └── item/
│       ├── DetalheItemActivity.java
│       └── FormularioItemActivity.java
└── util/
    └── DataUtils.java           # Formatação de datas e prazos
```

> **Observação:** nesta entrega os dados ficam em memória (`ItemRepository`), com alguns itens de
> exemplo para demonstrar as telas. Por isso eles voltam ao estado inicial quando o app é fechado.
> Na Entrega 3 o repositório passa a usar SQLite/Room. As telas continuam iguais, porque só
> a implementação interna do repositório muda.

## Como executar

1. Abra a pasta do projeto no **Android Studio** (Ladybug ou mais recente).
2. Aguarde a sincronização do Gradle.
3. Execute a configuração `app` em um emulador ou dispositivo com **Android 8.0 (API 26)** ou superior.

Pela linha de comando: `./gradlew assembleDebug` (APK gerado em `app/build/outputs/apk/debug/`).
Testes unitários: `./gradlew test`.

## Planejamento das próximas entregas

| Entrega | Prazo | Planejado |
|---|---|---|
| 1. Definição do Projeto | 18/09/2026 | ✅ Proposta (PDF) |
| 2. Desenvolvimento Inicial | 02/10/2026 | ✅ Estrutura, telas iniciais e navegação (esta versão) |
| 3. Desenvolvimento Intermediário | 23/10/2026 | Banco local SQLite/Room, integração entre telas (ex.: tocar num total do Painel abre a lista filtrada), filtro por local de armazenamento |
| 4. Desenvolvimento Avançado | 13/11/2026 | CRUD completo persistido, histórico com filtros, validações refinadas, melhorias de interface |
| Final | 30/11/2026 | App completo, documentação e vídeo de até 5 minutos |

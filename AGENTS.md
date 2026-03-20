# dfinance — agents.md

## Project overview

dfinance is a free, local-first, open source personal finance app built with Kotlin Multiplatform (
KMP) and Jetpack Compose. Currently targeting Android only.

## Architecture — MVVM

The project follows MVVM strictly. Respect this structure at all times:

```
feature/
├── data/
│   ├── dto/          # input models (no id, used for create/update)
│   └── repository/   # repository implementations
├── domain/
│   ├── model/        # domain models (always have an id)
│   └── repository/   # repository interfaces
└── presentation/
    ├── Screen.kt     # Composable screens, only collect state and dispatch events
    ├── ViewModel.kt  # exposes StateFlow<State>, handles business logic
    └── State.kt      # data class holding UI state
```

Rules:

- ViewModels expose a single `StateFlow<State>` and a set of functions for user actions
- Screens only call `viewModel.state.collectAsState()` and dispatch events — no logic in Composables
- Repository interfaces live in `domain`, implementations live in `data`
- Domain models are always valid (no nullable ids) — use DTOs for creation

### Actions

Every screen defines a `sealed interface` of actions the UI can dispatch. This is the only way the
screen communicates with the ViewModel.

```kotlin
// presentation/BankAccountsActions.kt
sealed interface BankAccountsActions {
    object OnAddAccountClick : BankAccountsActions
    data class OnAccountClick(val id: Long) : BankAccountsActions
    data class OnDeleteAccountClick(val id: Long) : BankAccountsActions
}
```

The ViewModel exposes a single `onAction(action: BankAccountsActions)` function:

```kotlin
fun onAction(action: BankAccountsActions) {
    when (action) {
        is BankAccountsActions.OnAddAccountClick -> {
            ...
        }
        is BankAccountsActions.OnAccountClick -> {
            ...
        }
        is BankAccountsActions.OnDeleteAccountClick -> deleteAccount(action.id)
    }
}
```

The screen never calls ViewModel functions directly — it always goes through `onAction`:

```kotlin
BankAccountItem(
    account = account,
    onClick = { viewModel.onAction(BankAccountsActions.OnAccountClick(account.id)) }
)
```

File naming convention per feature:

- `State.kt` — `data class BankAccountsState(...)`
- `Actions.kt` — `sealed interface BankAccountsActions`
- `ViewModel.kt` — `class BankAccountsViewModel(...)`
- `Screen.kt` — `@Composable fun BankAccountsScreen(...)`

## Design system — neobrutalist

### Core principles

- **Sharp borders everywhere** — `RoundedCornerShape(0.dp)` or at most `4.dp`. Never use large
  rounded corners.
- **Bold, visible borders** — use `border(2.dp, Color.Black)` or
  `border(2.dp, MaterialTheme.colorScheme.outline)` on cards and containers
- **Flat, solid colors** — no gradients, no blur, no shadows (use offset solid blocks instead for
  depth if needed)
- **Offset shadow effect** — to suggest elevation, draw a solid block offset by 4dp behind the
  component instead of using `elevation` or `shadow()`
- **High contrast** — text must always be clearly readable against its background

### Typography and colors

- Always follow `Theme.kt` — never hardcode colors or text styles
- Use `MaterialTheme.colorScheme.*` for all colors
- Use `MaterialTheme.typography.*` for all text styles
- Never override the theme with hardcoded hex values or custom font sizes

### Component rules

- Cards: `border(2.dp, MaterialTheme.colorScheme.outline)`, no elevation, sharp corners
- Buttons: filled, sharp corners, high contrast label
- Inputs: bordered, sharp corners, no rounded style
- Icons: use the `AccountIcon` enum — never use arbitrary `Icons.Default.*` directly in screens

### What NOT to do

- Do not add `elevation` or `shadow` to any component
- Do not use `RoundedCornerShape` with values above `4.dp`
- Do not introduce new colors outside of `Theme.kt`
- Do not use `AnimatedVisibility`, shimmer, or decorative animations — keep it snappy and direct
- Do not change `Theme.kt` unless explicitly asked

## Database — SQLDelight

- Schema lives in `commonMain/sqldelight/`
- All tables have: `id INTEGER PRIMARY KEY AUTOINCREMENT`, `created_at`, `updated_at`, `deleted_at`
- Deletions are always soft deletes — set `deleted_at`, never `DELETE FROM`
- Balance is always stored as `INTEGER` in minor units (cents) — never `REAL`
- Colors stored as hex strings (e.g. `#4CAF50`), icons stored as string keys (e.g. `CreditCard`)

## Do not touch

- `Theme.kt` — unless explicitly asked
- `.sq` files — unless explicitly asked to update the schema or queries
- `DatabaseDriverFactory` — platform-specific, do not move or refactor
- Any `expect`/`actual` declarations — do not restructure without being asked
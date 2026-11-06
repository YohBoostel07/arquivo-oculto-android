# Arquivo Oculto

Aplicativo Android nativo desenvolvido em Kotlin e Jetpack Compose. Este projeto não utiliza Godot.

## Meta desta etapa
Gerar e validar um APK Android instalável pelo GitHub Actions antes de avançar nas mecânicas do jogo.

## Compilação
A cada atualização da branch `main`, o workflow **Compilar APK Android** executa `assembleDebug` e publica o artefato **Arquivo-Oculto-APK**.

O APK debug fica em `app/build/outputs/apk/debug/app-debug.apk`.

- Nome: Arquivo Oculto
- Pacote: `com.arquivoculto.game.debug` (build debug)
- Tecnologia: Kotlin + Jetpack Compose
- Min SDK: 23
- Target SDK: 35

## Primeiro caso
**Caso 001 — O Desaparecimento de Blackwood.**

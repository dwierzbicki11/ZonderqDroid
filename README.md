# Zonderq UI 0.1 — bezpłatny prototyp

Pierwszy krok do ZonderqDroid: aplikacja Android z własnym ekranem głównym (HOME launcher), interaktywnym **demonstracyjnym** centrum sterowania oraz **demonstracyjnym** ekranem blokady.

## Co działa naprawdę?

- Android APK i alternatywny ekran główny: można ustawić jako domyślny launcher.
- Lista do 12 zainstalowanych aplikacji oraz otwieranie ich z ekranu głównego.
- Nawigacja między trzema ekranami i zmiany stanów kontrolek w obrębie aplikacji.

## Co na razie jest tylko demonstracją?

- Przełączniki Wi-Fi, Bluetooth i transmisji danych NIE zmieniają stanu urządzenia.
- Suwak jasności nie modyfikuje prawdziwej jasności ekranu (szczególnie ważne przy błędzie sterownika jasności w Androidzie 17 GSI).
- Ekran blokady nie jest systemowym Keyguard i nie zabezpiecza telefonu.
- Systemowy pasek stanu, centrum sterowania i powiadomienia nadal należą do Android SystemUI.

## GitHub Actions, bez Google Cloud

1. Utwórz **publiczne** repozytorium `ZonderqDroid` na GitHubie.
2. Umieść pliki z tego folderu w katalogu głównym repozytorium.
3. Zakładka **Actions** → **Zonderq UI - Build APK** → **Run workflow** (albo push do main).
4. Gdy zakończy się sukcesem, wejdź w wynik workflow i pobierz artefakt `ZonderqUI-0.1-debug`.
5. Rozpakuj archiwum artefaktu i zainstaluj APK na Realme przez `adb install -r app-debug.apk`.
6. Jeśli zechcesz, ustaw Zonderq UI jako domyślną aplikację ekranu głównego w Ustawieniach Androida. Powrót do systemowego launchera również przez ustawienia aplikacji domyślnych.

### Z lokalnego Kali

Gdy zainstalujesz Gradle 8.13, JDK 17 i Android SDK API 36:

```bash
gradle :app:testDebugUnitTest :app:assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Ograniczenia

To nie jest pełny ROM ani podmienione SystemUI. Głębokie zmiany powiadomień, blokady i centrum sterowania wymagają modyfikacji źródeł Androida/LineageOS i osobnej kompilacji GSI. Nie wymieniaj `SystemUI.apk` w uruchomionym GSI na podstawie tego prototypu.

### Wersje narzędzi

AGP 8.13.2, Kotlin 2.2.20, Gradle 8.13, JDK 17, compileSdk 36, Compose BOM 2025.08.00.

Projekt przygotowany jako szablon do skompilowania w GitHub Actions. Wynik builda nie jest jeszcze potwierdzony rzeczywistym przebiegiem CI.

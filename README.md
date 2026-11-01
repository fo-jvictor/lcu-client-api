# lcu-client-api

A local Java backend that connects to the League of Legends Client (LCU API) and the Riot Client, and exposes a simplified REST API plus real-time events so other applications, such as the bundled web UI, can read and control the client. The main use case is controlling the client from your phone: on startup the app prints a QR code with its LAN address.

## Tech stack

- Java 21 (Gradle, Shadow plugin for the fat jar)
- Native HTTP server (`com.sun.net.httpserver.HttpServer`)
- `java.net.http.HttpClient` for LCU / Riot Client requests
- Jackson and `org.json` for JSON
- `Java-WebSocket` for the LCU, Riot Client and Riot Messaging Service websockets
- ZXing for the QR code
- Lombok

## How it works

1. **Client discovery:** the app waits until `LeagueClientUx.exe` is running, then reads the `lockfile` of the League Client and the Riot Client to get each one's port and auth token. The League install folder is resolved dynamically. It checks, in order:
   - the folder of the running client process
   - Riot's metadata in `%PROGRAMDATA%\Riot Games` (`product_settings.yaml` and `RiotClientInstalls.json`)
   - the default folder `C:\Riot Games\League of Legends`

   The Riot Client lockfile is read from `%LOCALAPPDATA%\Riot Games\Riot Client\Config`.
2. **TLS:** both clients use self-signed certificates, so requests go through a trust-all `SSLContext` (`utils/trust_manager`).
3. **Riot Messaging Service:** besides the local clients, the app opens a websocket to Riot's RMS edge (`us.edge.rms.si.riotgames.com`), authenticated with the Riot Client's sign-on token.
4. **Connection lifecycle:** `LeagueConnectionsLifecycleManager` tracks the LCU state (down, available, websocket connected). When the client closes or restarts, it re-reads the lockfiles, reconnects the websocket and notifies the frontend.
5. **Events:** the app subscribes to LCU websocket events and forwards the relevant ones to connected frontends.

## Exposed interfaces

| Interface | Address |
|---|---|
| REST API and web UI | `http://<host>:8080` |
| Frontend websocket | port `200` |

REST routes:

| Base path | Routes |
|---|---|
| `/api/lobby` | `create`, `current-lobby`, `current-game-queue`, `available-queues`, `suggested-players`, `player-slots`, `update-positions-preference`, `invitations`, `invite-summoner`, `invite-summoner-by-id`, `remove-summoner` |
| `/api/matchmaking` | `search-state`, `search`, `cancel`, `accept`, `decline` |
| `/api/summoners` | `myself`, `summoner-by-id`, `profile/background-skin` |
| `/api/gameflow` | `session` |
| `/api/chat` | `me`, `update-league-rank` |

Websocket events sent to the frontend: queue started/ended, match found, LCU connection lost/restored, and lobby member updates.

## Running

Requirements: Windows, JDK 21, and the League of Legends client installed.

```bash
./gradlew build
```

```bash
java -jar build/libs/lcu-client-api.jar
```

Start the League client (before or after the app). Then open the printed URL or scan the QR code from a device on the same network.

The build also compiles the Vue frontend (`lcu-client-api-vue`) and copies it into `src/main/resources/web`. Its location is hardcoded in `build.gradle` (`frontendDir`), so update that path on another machine.

The app adds a system tray icon with an Exit option.

## Future improvements

- More LCU endpoints (ranked info, match history, runes)
- Better error handling and friendlier HTTP error responses
- Further modularization of the project structure

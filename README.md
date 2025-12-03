# Alcatraz Multiplayer Client – Vollständige Dokumentation

## Beispielkonfiguration (alice.yaml, siehe unter src/main/resources/alice.yaml)

yaml
spring:
main:
headless: false    # wichtig: Swing GUI aktivieren

server:
port: 9000           # REST-Port dieses Clients

alcatraz:
self:
name: "Alice"      # Spielername
callbackBaseUrl: "http://192.168.0.80:9000"   # URL, unter der andere Clients Alice erreichen
registry:
nodes:
- "http://localhost:8080"
- "http://localhost:8081"
- "http://localhost:8082"


---

## 🧠 Erklärung der Konfiguration

### spring.main.headless = false
Aktiviert Swing (ohne das keine GUI).

### server.port = 9000
REST-Endpunkte dieses Clients, u. a.:

- /move
- /start
- /state
- /health

Jeder Client braucht einen eigenen Port (z. B. 9000, 9001, 9002).

### alcatraz.self.name
Der Spielername, der im Spiel angezeigt wird.

### alcatraz.self.callbackBaseUrl
Die vollständige URL, über die andere Clients diesen Client erreichen können.  
*Muss im LAN korrekt sein:*

Beispiel:

http://192.168.0.80:9000

*NICHT localhost* (das führt zu Chaos).

### alcatraz.registry.nodes
Liste aller möglichen Registry-Server (Spread-Cluster).  
Einer davon ist der Master.  
Clients registrieren sich dort und bekommen von ihm die vollständige Spielerliste.

---

## 🕹️ Wie das Spiel funktioniert

1. *Client starten*  
   Jeder Client meldet sich automatisch beim Registry-Server an.

2. *Lobby CLI*  
   Im Terminal erscheint:

   Type 'start' to request game start.


3. *Spielstart*  
   Sobald mindestens zwei Clients registriert sind, startet der Master die Runde.

4. *GUI erscheint*  
   Das originale Alcatraz-Brett aus der Java-Bibliothek wird geöffnet.

---

### Spielzüge
- Jeder Zug wird lokal ausgeführt.
- Danach wird der Zug an alle anderen Clients gesendet.
- Alle halten ihren lokalen Spielzustand synchron.

---

### Disconnect/Reconnect Handling
- Wenn ein Spieler offline geht, wird das erkannt.
- Alle anderen Spieler werden informiert.
- Das Spiel läuft für die anderen weiter.
- Wenn der Offline-Spieler an der Reihe wäre → Pause bis er wieder online ist.

*Reconnect:*
- Der Client holt verpasste Züge nach.
- Dann geht es exakt dort weiter, wo das Spiel stehen blieb.

---

## 👥 Mehrspieler-Unterstützung
Das System unterstützt:
- 2 Spieler
- 3 Spieler
- 4 Spieler
- usw.

Die Turn-Logik unterstützt beliebig viele Clients, solange jeder eine gültige Konfiguration hat.

---

## 🛜 Netzwerkvoraussetzungen
Client ↔️ Client Kommunikation läuft über HTTP/POST:

- /move
- /state
- /health
- /event/disconnected
- /event/reconnected

Wenn ein Client auf Linux läuft, muss die lokale Firewall (UFW/Firewalld) den Port erlauben:
bash
sudo ufw allow 9000/tcp


---

## 📦 Projektstruktur (Kurzfassung)
- *ClientApplication* → Startpunkt
- *LobbyCli* → Terminal-Dialog (start / unregister)
- *GameSessionService* → Initialisiert GUI, lädt verpasste Moves
- *PlayerMoveService* → Turn-Logik, Disconnect-/Reconnect-Handling
- *MoveBroadcastService* → Verteilung der Moves
- *PlayerDirectory* → Übersicht aller Spieler + Online-Status
- *GameController* → REST-Endpunkte für P2P-Kommunikation

---

## ✔️ Zusammenfassung
Dieses Projekt ist ein vollständig verteilter Multiplayer-Client, der:
- eigenständig ein Alcatraz-Spielbrett rendert
- Moves peer-to-peer austauscht
- Disconnect/Reconnect sauber toleriert
- automatisch auf den Master-Registry-Server zeigt
- einfach per YAML konfiguriert wird

*Du musst nichts am Code ändern – nur die YAML anpassen und das JAR starten.*

java -jar alcatraz.jar --spring.config.location=file:./bob.yaml
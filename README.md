# Verteilte Systeme
Dieses Projekt ist eine Chatplattform, die über ein verteiltes System betrieben wird. Dieses Projekt ist im Rahmen der Vorlesung "Verteilte Systeme" entstanden und wurde in Java geschrieben.

## Aufgabe:
Ein auf TCP basierendes Chatsystem bauen, welches lediglich die Bibliotheken **java.io.\***, **java.net.\***, **java.util.\*** und **java.swing.\*** benutzt.

**Der User muss sich auf einem Server einloggen können**, um dann mit einem weiteren User chatten zu können. Um hier Ausfälle zu vermeiden, müssen zwei identische Server aufgesetzt werden, welche auch beide die komplette Chat Historie behalten.

Der User verbindet sich zu einem **zufällig ausgewählten Server** (=> Lastenverteilung), welcher ihn zentral einloggt, z.B. mit Username und Passwort. Für den Chat verbindet sich der Client auch mit einem zufälligen Server.

Eine Nachricht die von User A geschickt wurde soll in seiner Chat Historie gespeichert werden und dann zu User B geschickt werden, wo es auch in der Chat Historie gespeichert wird. Die komplette Chat Historie soll für beide Parteien in der richtigen Reinfolge dargestellt werden (=> **Lamport Timestamps** [1]). Der Chat soll nur für eingeloggte User sichtbar sein.

Der Server soll Nachrichten aller User schnell bearbeiten. (=> **Non-Persisten Server** [2]).

Das ganze System soll **Fehlersicher gegen Netzwerkprobleme und -fehler** sein. 

Das System soll **Testcases** besitzen, welche mögliche Fehlerfälle abdecken.

Das System soll dokumentiert sein (5-10 Seiten + Codekommentare)

## Zusatzerweiterungen:
- Emojis (+ 5 points) 
- Mehrere Chat Hsitorien für einen User (+ 10 points)
- GUI (+ 10 points)
- Persistente Chat Historie (+ 10 points)
- Verschlüsselte Speicherung auf dem Server (+ 10 points)
- Gruppenchats (+ 20 points)
- Verschlüsselte Kommunikation (+ 20 points)
- Drei identische Server mit der Major Consensus Strategei(+ 40 points)

## Punkterechnung

|Anforderung      | Punkte           | Summe  |
| ------------- |:-------------:| -----:|
| Basisanforderung      | 60 | 60 |
| Emojis     | 5      |   65 |
| GUI | 10      |    75 |
| Mehrere Chat Historien für einen User | 10 |85|
| Persistente Chat Historie | 10 | 95 |
| Verschlüsselte Speicherung auf dem Server | 10 | 105 |
| ~~Gruppenchats~~ | 20 | - |
| ~~Verschlüsselte Kommunikation~~ | 20 | - |
| ~~Drei identische Server mit der Major Consesus Strategie~~ | 40 | - |

### Gruppe:
* **Anna-Lena Richert** ([aalenaa](https://github.com/aalenaa))
* **Anton Ochel** ([Tony1704](https://github.com/Tony1704))
* **Benno Grimm** ([Grimmig18](https://github.com/Grimmig18))
* **Tizian Groß** ([tizian123](https://github.com/tizian123)) 
* **Tristan Emig** ([TristanMrb](https://github.com/TristanMrb)) 
* **Marcel Mertens** ([Nerdystuff](https://github.com/NerdyStuff)) 

### Quellen & Links
- \[1\] [Lamport-Uhr](https://de.wikipedia.org/wiki/Lamport-Uhr); [Lamport timestamps](https://en.wikipedia.org/wiki/Lamport_timestamps) 
- \[2\] [non-persistent vs persistent connection](https://www.geeksforgeeks.org/http-non-persistent-persistent-connection-set-2/)
- \[3\] [Step-bv-Step Java Chat](https://www.instructables.com/id/Creating-a-Chat-Server-Using-Java/)
- AES Verschlüsselung von Lokesh Gupta [https://howtodoinjava.com/security/java-aes-encryption-example/](https://howtodoinjava.com/security/java-aes-encryption-example/) (Abgerufen am 31.03.2020)
- [Non-Blocking Reader](https://community.oracle.com/message/5318833#5318833)  

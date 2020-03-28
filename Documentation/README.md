# Dokumentation des Projekts
Unsere Ideen können [hier](https://github.com/NerdyStuff/VerteilteSysteme/new/master/Documentation/Ideas) gefunden werden.

[Hier](https://github.com/NerdyStuff/VerteilteSysteme/tree/master/Documentation/Planning) finden sich unsere Planungsnotizen.

## Allgemeines
Zur Kommunikation zwischen den Clients und den Servern werden Objekte der Klasse <code>DataPackage</code> verwendet.
Die Objekte werden direkt durch den Socket versendet.
Ein Client kann dabei immer nur ein Objekt der Klasse an den Server senden. Der Server sendet dem Client eine Liste mit DataPackage-Objekten zurück. Die Liste muss dabei immer mindestens ein Objekt enthalten. Durch das Attribut <code>flag</code> wird definiert, was der Server bzw. der Client an Informationen aus dem DataPackage-Objekt an Daten erhalten kann. Die einzelnen Werte des Flags können in der Schnittstellendefinition angesehen werden.
Zur Registrierung senden Clients ein ihren Nutzernamen, ihr Passwort und das entsprechende Flag an den Server. Der Server legt dann einen neuen User in seiner Nutzerliste an und sendet dem Client eine Liste mit einem DataPackage-Objekt zurück, in dem das Flag für eine erfolgreiche Registrierung oder eine nicht erfolgreiche Registrierung gesetzt ist. Nach einer erfolgreichen Registrierung kann der Nutzer eine Nachricht an einen Server schicken. Die Nachricht ist in einem DataPackage-Objekt verpackt mit dem gestzten Flag für eine Nachricht, dem Sender, dem Empfänger und dem Zeitstempel. Existiert der Empfänger im System wird eine positive Rückmeldung vom Server an den Client gesendet, existiert der Empfänger im System nicht, wird eine negative Rückmeldung mittels Flag zurückgeschickt.
Da Nutzer nicht immer angemeldet sind, werden die Nachrichten für die Nutzer in der Message-Queue für den jeweiligen Empfänger gespeichert. Dazu schaut der Server in seiner Nutzerliste nach und Fügt ein Nachrichten Objekt in der Nachrichten Liste des jeweiligen Benutzers hinzu.
Nach dem sich ein Nutzer angemeldet hat, sendet der Client kontinuierlich in bestimmten Zeitintervallen Anfragen an den Server, ob es neue Nachrichten gibt. Gibt es neue Nachrichten wird eine Liste mit DataPackage-Objekten zurückgesendet, die die Nachrichteninhalte enthalten. Gibt es keine Updates, wird eine Liste mit einem DataPackage-Objekt zurückgesendet, mit dem gestzten flag für keine neue Nachrichten.

Synchronistatiion zwischen den Servern.... (**TBD:** Two-Phase-Commit-Protokoll)<br>
Zeitstempel implementieren (TBD: Laportuhren -> eventuell das DataPacakgeObjet um eine NachrichtenID erweitern!) => NUTZEN VON LISTEN, UM LAPORTUHREN ZU UMGEHEN

## Schnittstellen- und Objektdefinitionen:

### DataPackage
| Attribut | Datentyp | Beschreibung |
| -------- | -------- | ------------ |
| flag:    | Integer  | Mit dem Flag wird den Client oder dem anderen Server mitgeteilt, welche Daten erwartet werden können<br><br><code>Wert: 1</code> Registrierung <br> <code>Wert: 2</code> Nachricht <br> <code>Wert: 3</code> Update-Request <br> <code>Wert: 4</code> Neue Nachrichten für den Client <br> <code>Wert: 5</code> Keine neuen Nachrichten für den Client <br> <code>Wert: 6</code> Registrierung erfolgreich <br> <code>Wert: 7</code> Nachricht erfolgreich angenommen <br> <code>Wert: 8</code> Anfrage der Chathistorie <br> <code>Wert: 9</code> Chathistory <br><br><code>Wert: 20</code> Commit request (prepare)<br><code>Wert: 21</code>Ready Nachricht von anderen Server(n)<br><code>Wert: 22</code>Commit von Koordinator<br><code>Wert: 23</code>Acknowledge von anderen Servern<br><br> <code>Wert: -1</code> Registrierung fehlgeschlagen <br> <code>Wert: -2</code> Falsches Passwort oder Nutzername <br> <code>Wert: -3</code> Empfänger existiert nicht <br> <code>Wert: -4</code> Allgemeiner Fehler <br> <code>Wert: -5</code> Empfänger darf nicht gleicher Nutzer sein wie Sender <br> <code>Wert: -6</code> Keine Chathistorie gefunden / Fehler<br><br><code>Wert: -20</code> Failed von anderen Servern<br><code>Wert: -21</code> Abort von Koordinator|
| syncFlag | Integer | Mit dem Flag wird dem Sync-Server mitgeteilt, welche Daten im Attribut \"object\" zu erwarten sind <br><br><code>Wert: 1</code> User-Objekt für die Registrierung<br><code>Wert: 2</code> User-Objekt mit neue Nachrichtenliste <br> **TBP:** CHATHISTORIE UPDATEN! |
| username | String | Nutzername |
| password | String | Password |
| receiver | String | Empfänger der Nachricht |
| message  | String | Nachrichtentext |
| timestamp| Date   | Zeitstempel der Nachricht|
| object   | Object | Objekt um Daten zwischen den Servern zu synchronisieren |

### Message
| Attribut  | Datentyp | Beschreibung |
| --------- | -------- | ------------ |
| sender    | String   | Sender der Nachricht |
| text      | String   | Text der Nachricht |
| timestamp | Date     | Zeitstempel der Nachricht |

### User
| Attribut | Datentyp | Beschreibung |
| -------- | -------- | ------------ |
| username | String   | Nutzername |
| password | String   | Passwort des Benutzers |
| messages | LinkedList\<Message\> | Liste mit den Nachrichten für den Benutzer |

### Host
| Attribut | Datentyp | Beschreibung |
| -------- | -------- | ------------ |
| hostname | String   | Hostname des Hostsservers |
| port     | Integer  | Port des Hostservers |
  
### Server
| Attribut | Datentyp | Beschreibung |
| -------- | -------- | ------------ |
| serverPort | Integer | Port, auf dem der Server Anfragen annimmt |
| serverSocket | Socket | Socket des Servers |
| users    | HashMap\<String, User\> | Hashmap mit den im System registrierten Nutzern |

### Client
| Attribut | Datentyp | Beschreibung |
| -------- | -------- | ------------ |
| hosts    | HashMap\<Integer, Host\> | Hashmap mit den benutzbaren Servern <br> \(HashMap wird als dynamisches Array verwendet mit Zugriffszeit von 1\) |
| username | String   | Nutzername des Clients |
| password | String   | Passwort des Clients |

### UI
Klasse um eine grafische Oberfläche für den Client zu erzeugen.

### CLI
**TBD:** Klasse um ein Eingabeterminal für den Nutzer zu erzeugen

### WrongDataPackageException
Exception Objekt für Daten-Objekte, die nicht dem DataPackage Format entsprechen.

## UML Klassendiagramm:
Hier sieht man das (vorläufige) Klassen- und Beziehungsdiagramm der Basisversion.
<br>
![UML-Klassendiagramm](https://github.com/NerdyStuff/VerteilteSysteme/blob/master/Documentation/Files/uml_diagramm.png)
Das UML-Diagramm ist [hier](https://github.com/NerdyStuff/VerteilteSysteme/blob/master/Documentation/Files/uml_diagramm.pdf) auch als PDF zu finden.

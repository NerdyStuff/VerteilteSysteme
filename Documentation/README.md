# Dokumentation des Projekts
Unsere Ideen können [hier](https://github.com/NerdyStuff/VerteilteSysteme/new/master/Documentation/Ideas) gefunden werden.

[Hier](https://github.com/NerdyStuff/VerteilteSysteme/tree/master/Documentation/Planning) finden sich unsere Planungsnotizen.

## Allgemeines
Zur Kommunikation zwischen den Clients und den Servern werden Objekte der Klasse <code>DataPackage</code> verwendet.
Die Objekte werden direkt durch den Socket versendet.<br>
Ein Client kann dabei immer nur ein Objekt der Klasse an den Server senden. Der Server sendet dem Client eine Liste mit DataPackage-Objekten zurück. Die Liste muss dabei immer mindestens ein Objekt enthalten. Durch das Attribut <code>flag</code> wird definiert, was der Server bzw. der Client an Informationen aus dem DataPackage-Objekt an Daten erhalten kann. Die einzelnen Werte des Flags können in der Schnittstellendefinition angesehen werden.<br>
Zur Registrierung senden Clients ein DataPackage-Objekt mit ihrem Nutzernamen, ihrem Passwort und dem entsprechende Flag an den Server. Der Server legt dann einen neuen Nutzer in seiner Nutzerliste an und sendet dem Client eine Liste mit einem DataPackage-Objekt zurück, in dem das Flag für eine erfolgreiche Registrierung oder eine nicht erfolgreiche Registrierung gesetzt ist.<br>
Nach einer erfolgreichen Registrierung kann der Nutzer eine Nachricht an einen Server schicken. Die Nachricht ist in einem DataPackage-Objekt verpackt mit dem gesetzten Flag für eine Nachricht, dem Sender, dem Empfänger und dem Zeitstempel.<br>
Existiert der Empfänger im System wird eine positive Rückmeldung vom Server an den Client gesendet, existiert der Empfänger im System nicht, wird eine negative Rückmeldung mittels Flag zurückgeschickt.<br>
Da Nutzer nicht immer angemeldet sind, werden die Nachrichten für die Nutzer in der Message-Queue für den jeweiligen Empfänger gespeichert. Die Message-Queue ist mit einer Linked-List implementiert und enthält Objete des Typs <code>Message</code>.
Der Server durchsucht seine Nutzerliste und Fügt ein Nachrichten Objekt in der Nachrichten Liste des jeweiligen Benutzers hinzu.<br>
Das Login im Client sendet ein DataPackage mit dem Flag für einen Login. Je nachdem, ob die Anmeldedaten valide sind, sendet der Server eine entsprechende Nachricht an den Client zurück.<br>
Nach dem sich ein Nutzer angemeldet hat, sendet der Client kontinuierlich in Zeitintervallen von einer Sekunde Anfragen an den Server, ob es neue Nachrichten gibt.<br>
Sind neue Nachrichten vorhanden, wird eine Liste mit DataPackage-Objekten zurückgesendet, die die Nachrichteninhalte enthalten. Gibt es keine neuen Nachrichten, wird eine Liste mit einem DataPackage-Objekt zurückgesendet, mit dem gesetzten Flag für keine neue Nachrichten.
<br>
Jedes Mal, wenn sich ein neuer Nutzer im System registriert, eine Nachricht versendet, oder ein Update einholt, werden die Daten zwischen den Servern synchronisiert.<br>
Dazu werden zwei Instanzen der Server-Klasse auf verschiedenen Rechnern gestartet.<br>
In die Variable <code>syncHostname</code> wird in jeder Instanz jeweils die IP-Adresse oder der Hostname des jeweils anderen Servers eingetragen.<br>
Die Daten werden mit dem Two-Phase-Commit-Protokoll zwischen den Servern synchronisiert.<br>
Sendet ein Nutzer eine Registrierungsanfrage, nimmt der zufällig gewählte Server die Anfrage an. Das ausgelesene Flag für die Registrierung wird ausgelesen und eine Methode aufgerufen, um die Registrierung zu bearbeiten. In der Methode wird zuerst geprüft, ob alle gesendeten Daten ausgefüllt sind und ob bereits ein Nutzer mit diesen Daten oder dem Nutzernamen existiert.<br>
Existiert kein Nutzer mit diesen Daten, wird eine Verbindung zu dem anderen Server aufgebaut und eine Commit-Request gesendet.<br>
Der andere Server nimmt die Anfrage an und prüft ebenfalls die Daten. Anschließend sendet der Server eine Ready- oder Failed-Nachricht.<br> Der Server der den Commit-Request gesendet hat wartet dann auf diese Antwort.<br>
Je nach empfangener Antwort wird entweder ein Commit oder ein Abort gesendet. Der angefragete Server warte ebenfalls auf diese Nachricht.<br>
Bei einem Commit wird der benutzer persistiert und ein Acknowledge versendet. Bei einem Abort wird der Ursprungszustand beslassen.<br>
Um eine Deadlocksituation zu vermeiden, werden in den <code>while</code>-Schleifen Zähler verwendet.<br>
Jede Schleife wird maximal zehn Mal durchlaufen. Ist bis dahin keine Antwort gekommen, werden Aborts bzw. Fails gesendet und der Vorgang abgebrochen.<br>
In jedem Schleifendurchgang wird eine kurze Zeit gewartet, damit der Zäler nicht zu schnell hochgezählt wird. Dazu werden 500 Millisekunden gewartet, bis der Zähler inkrementiert wird.<br>
Ist der Nutzer erfolgreich auf beiden Servern synchronisiert wird jeweils eine Methode zum Speichern aufgerufen.<br>
In der Methode wird die Nutzerliste, die aus Gründen der Performance als HashMap implementiert ist durch die Serialisierung des Objekts in eine Datei geschrieben. Weitere Informationen zum speichern finden sich im Abschnitt "Persistente Speicherung auf dem Server".<br>
Analog zum Registrierungsvorgang sind auch das Senden und Updaten der Nutzernachrichten implementiert.<br>

Durch die Nutzung von zwei verketteten Listen in jedem Nutzerobjekt werden die Nachrichten in der richtigen Reihenfolge bei den Chatpartnern angezeigt. Es existiert eine Liste mit den neuen Nachrichten für den Nutzer, die bei einem Update-Request gelsen und anschließend gelöscht wird.<br>
Zusätzlich gibt es eine Liste mit allen Nachrichten, die der Nutzer an seine Chatpartner versendet und von ihnen empfangen hat. Diese Chathistorie wird bei einem Login im Client vom Server geladen.<br>
Die Zeitstempel der Nachrichten werden auf den Zeitpunkt gesetzt, zu dem sie beim Server eingegangen sind. So können Probleme beim Senden nicht zu einer falschen Reihenfolge führen.<br>
Da ein Nutzer mit mehreren Nutzern chatten kann, wird jeweils nur der Verlauf im GUI angezeigt bei dem der aktuell eingetragene Chatpartner involviert ist.

## Zusätzliche Features:

### GUI
Im Code für den Client wurde die Klasse <code>GUI</code> implementiert. **TBD** @Tony1704

### Mehrere Chatverläufe pro Nutzer
**TBD**

### Emoji
Während des chattesn können UTF8-Kodierte Emoji Zeichen zwischen den Chatteilnehmern genutzt werden.

### Persistente Speicherung auf dem Server
Die HashMap mit den Nutzerdaten wird in der Datei <code>save.bin</code> gespeichert.
Beim  Neustart des Servers, wird die Datei geladen und die HashMap mit den Nutzern steht zur Verfügung.
Je nachdem, ob das Flag ```ENCRYPT_SAVE_FILE``` gesetzt ist, wird die Datei verschlüsselt oder nicht. Weitere Informationen dazu finden sich unter "Verschlüsselte persistente Speicherung auf dem Server". Die Hashmap wird mit dem Serializeable-Interface in einen Object-Stream verwandelt. dieser kann, wenn die Verschlüsselung nicht aktiviert ist direkt in eine Datei geschrieben und wieder eingelsen werden.

### Verschlüsselte persistente Speicherung auf dem Server
Wird das Flag ```ENCRYPT_SAVE_FILE``` in der Server-Klasse auf ```true``` gesetzt, wird die HashMap mit den Nutzerdaten mit dem AES-Algorithmus verschlüsselt. Der für die Verschlüsselung genutzte Code stammt von Lokesh Gupta und kann unter [https://howtodoinjava.com/security/java-aes-encryption-example/](https://howtodoinjava.com/security/java-aes-encryption-example/) (Abgerufen am 31.03.2020) geunden werden. Da die Methoden nur Strings annehmen, die HashMap mit den Nutzerobjekten aber beim Serialisieren einen Strom erzeugt, werden die Objektdaten in einen <code>ByteArrayOutputStream</code> geschrieben. Aus dem <code>ByteArrayOutputStream</code> kann dann ein Byte-Array erzeugt werden.<br>
Um eine falsche Kodierung beim wiederauslesen der Daten zu verhindern, wird das Byte-Array nicht direkt in einen String umgewandelt, sondern durchläuft eine Codierung mit dem <code>Base64-Encode</code>.
Der daraus erhaltene String kann dann als String mit der Verschlüsselungsmethode verschlüsselt werden.
Anschließend wird der String in einer Datei persistiert.<br>
Das Laden der Daten beim Start des Servers funktionirt umgekehrt, der String wird entschlüsslet, zu einem Byte-Array gemacht und anschließend wieder in einen Objekt-Stream.<br>
Der Objekt-Stream kann dann wieder in eine HashMap geparst werden und verwendet werden.

## Schnittstellen- und Objektdefinitionen:

### DataPackage
| Attribut | Datentyp | Beschreibung |
| -------- | -------- | ------------ |
| flag:    | Integer  | Mit dem Flag wird den Client oder dem anderen Server mitgeteilt, welche Daten erwartet werden können<br><br><code>Wert: 1</code> Registrierung <br> <code>Wert: 2</code> Nachricht <br> <code>Wert: 3</code> Update-Request <br> <code>Wert: 4</code> Neue Nachrichten für den Client <br> <code>Wert: 5</code> Keine neuen Nachrichten für den Client <br> <code>Wert: 6</code> Registrierung erfolgreich <br> <code>Wert: 7</code> Nachricht erfolgreich angenommen <br> <code>Wert: 8</code> Anfrage der Chathistorie <br> <code>Wert: 9</code> Chathistory <br><code>Wert: 10</code> Login Request <br><code>Wert: 11</code> Login successfull <br><br><code>Wert: 20</code> Commit request (prepare)<br><code>Wert: 21</code>Ready Nachricht von anderen Server(n)<br><code>Wert: 22</code>Commit von Koordinator<br><code>Wert: 23</code>Acknowledge von anderen Servern<br><br> <code>Wert: -1</code> Registrierung fehlgeschlagen <br> <code>Wert: -2</code> Falsches Passwort oder Nutzername <br> <code>Wert: -3</code> Empfänger existiert nicht <br> <code>Wert: -4</code> Allgemeiner Fehler <br> <code>Wert: -5</code> Empfänger darf nicht gleicher Nutzer sein wie Sender <br> <code>Wert: -6</code> Keine Chathistorie gefunden / Fehler<br><code>Wert: -7</code> Login Fehlgeschlagen<br><br><code>Wert: -20</code> Failed von anderen Servern<br><code>Wert: -21</code> Abort von Koordinator|
| syncFlag | Integer | Mit dem Flag wird dem Sync-Server mitgeteilt, welche Daten im Attribut \"object\" zu erwarten sind <br><br><code>Wert: 1</code> User-Objekt für die Registrierung<br><code>Wert: 2</code> User-Objekt mit neue Nachrichtenliste <br> <code>Wert: 3</code>User-Objekt für Update-requests|
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

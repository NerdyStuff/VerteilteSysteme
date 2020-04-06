# Dokumentation des Projekts
Unsere Ideen können [hier](https://github.com/NerdyStuff/VerteilteSysteme/new/master/Documentation/Ideas/README.md) gefunden werden.

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

### GUI & CLI
In der Main Klasse des Client-Projekts kann entschieden werden, ob das Command-Line-Interface (CLI) oder das Graphical-User-Interface (GUI) genutzt werden soll. Das kann über die Konstante <code>USE_GUI</code> gesteuert werden. 
#### GUI
Im Code für den Client wurde die Klasse <code>GUI</code> implementiert. 
In dem GUI muss sich zuerst eingeloggt werden, dazu wird Username und Passwort eingetragen und dann Registrieren oder Login gedrückt. Wenn Registrierung gedrückt wird, obwohl man bereits Registriert ist, wird das in der Konsolenausgabe rechts angezeigt. Ebenso ist es mit dem Login ohne angelegtes Profil. Registriert man sich, so ist man bereits im gleichen Schritt eingeloggt. Ein erneutes Login ist somit nicht nötig.
Ist man eingeloggt, so erscheint eine entsprechende Nachricht in der Konsolenausgabe. Anschließend muss ein Chatpartner eingegeben werden. Wenn der Chatpartner richtig angegeben wurde, ist der Chatbereich freigeschaltet.
Nun wird der Chatverlauf mit dem User angezeigt. Anschließend kann unten in dem Chatfeld einen Nachricht eingegeben werden und entweder durch die Entertaste oder durch den Klick auch Senden abgeschickt werden.<br>
Das Chatfeld wird sekündlich aktualisiert.<br>
Wird der Clear Button gedrückt, wird, je nach Kontext, das Chatfeld geleert oder der Chatverlauf.
Wird Leave gedrückt, wird der Chat mit dem Chatteilnehmer verlassen. Wird Logout gedrückt, wird der User ausgeloggt.

#### CLI
Wird die CLI als bevorzugtes Interaktionsmittel ausgewählt, muss der Chat über die Konsole gesteuert werden. Dabei wird zuerst gefragt, ob bereits ein Profil angelegt wurde. Auf diese Frage kann der User mit y oder n antworten und zum Bestätigen die Enter-Taste drücken.
Je nach Antwort wird der User dann dazu aufgefordert sich anzumelden oder zu registrieren.
Anschließend wird der User dazu aufgefordert, seinen gewünschten Chatpartner einzugeben. Anschließend kann über die Konsole mit dem Chatpartner geschrieben werden.
In der Konsole gibt es einige zusätzliche Befehle, um die Abwesenheit der Knöpfe, die das GUI besitzt, auszugleichen. Der Befehl /help zeigt die Übersicht der Befehle an. Der Befehl /exit beendet das Programm. Der Befehl /logout loggt den User aus und fordert ihm zu erneuten anmelden auf.

### Mehrere Chatverläufe pro Nutzer
Wie in der Beschreibung von GUI und CLI zu sehen ist, gibt es mehrere Chatverläufe für verschiedene User.
Jeder User ist ein eigenes Objekt in dem Projekt. Jedes dieser Objekte hat einen eigenen Chatverlauf, wo sich sowohl die eigenen als auch die empfangenen Chatnachrichten von anderen Usern befinden. Diese werden in einer Liste bei dem User serverseitig gespeichert.

### Emoji
Während des chattens können UTF8-Kodierte Emoji Zeichen zwischen den Chatteilnehmern genutzt werden.

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

### Verschlüsselte Kommunikation
Wird das Flag ```ENCRYPT_MESSAGES``` in der Client-Klasse auf true gesetzt, werden die Nachrichtentexte vor dem Versenden mit dem in der Konstante ```MESSAGE_PASSWORD``` gesetzten Passwort verschlüsselt. Das Flag ```SHOW_ENCRYPTED_MESSAGE_IN_TERMINAL``` zeigt die vor dem Entschlüsseln empfangenen Nachrichtentexte in der Konsole an. Damit kann validiert werden, dass die Nachrichtentexte von Client zu Client verschlüsselt übertragen werden.<br>
Die Nachrichten werden auch verschlüsslet in der Chathistorie gespeichert, daher ist es nötig die Speicherdateien auf den Servern zu löschen, wenn nur verschlüsselt kommuniziert werden soll. Wird zwischen Klartextübertragung und verschlüsselter Übertragung gewechselt ohne die Datei(en) zu löschen, werden die Nachrichten falsch dargestellt.<br>
Beide Clients müssen das gleiche Passwort verwenden um zu kommunizieren.

## Schnittstellen- und Objektdefinitionen:

### DataPackage
| Attribut | Datentyp | Beschreibung |
| -------- | -------- | ------------ |
| flag:    | Integer  | Mit dem Flag wird den Client oder dem anderen Server mitgeteilt, welche Daten erwartet werden können<br><br><code>Wert: 1</code> Registrierung <br> <code>Wert: 2</code> Nachricht <br> <code>Wert: 3</code> Update-Request <br> <code>Wert: 4</code> Neue Nachrichten für den Client <br> <code>Wert: 5</code> Keine neuen Nachrichten für den Client <br> <code>Wert: 6</code> Registrierung erfolgreich <br> <code>Wert: 7</code> Nachricht erfolgreich angenommen <br> <code>Wert: 8</code> Anfrage der Chathistorie <br> <code>Wert: 9</code> Chathistory <br><code>Wert: 10</code> Login Request <br><code>Wert: 11</code> Login successfull <br><br><code>Wert: 20</code> Commit request (prepare)<br><code>Wert: 21</code>Ready Nachricht von anderen Server(n)<br><code>Wert: 22</code>Commit von Koordinator<br><code>Wert: 23</code>Acknowledge von anderen Servern<br><br> <code>Wert: -1</code> Registrierung fehlgeschlagen <br> <code>Wert: -2</code> Falsches Passwort oder Nutzername <br> <code>Wert: -3</code> Empfänger existiert nicht <br> <code>Wert: -4</code> Allgemeiner Fehler <br> <code>Wert: -5</code> Empfänger darf nicht gleicher Nutzer sein wie Sender <br> <code>Wert: -6</code> Keine Chathistorie gefunden / Fehler<br><code>Wert: -7</code> Login Fehlgeschlagen<br><br><code>Wert: -20</code> Failed von anderen Servern<br><code>Wert: -21</code> Abort von Koordinator|
| syncFlag | Integer | Mit dem Flag wird dem Sync-Server mitgeteilt, welche Daten im Attribut \"object\" zu erwarten sind <br><br><code>Wert: 1</code> User-Objekt für die Registrierung<br><code>Wert: 2</code> User-Objekt mit neue Nachrichtenliste <br> <code>Wert:  3</code>User-Objekt für Update-requests|
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
| receiver  | String   | Empfänger der Nachricht |
| text      | String   | Text der Nachricht |
| timestamp | Date     | Zeitstempel der Nachricht |

### User
| Attribut | Datentyp | Beschreibung |
| -------- | -------- | ------------ |
| username | String   | Nutzername |
| password | String   | Passwort des Benutzers |
| messages | LinkedList\<Message\> | Liste mit den Nachrichten für den Benutzer |
| chatHistory | LinkedList\<Message\> | Liste mit der Chathistorie des Benutzer |

### Host
| Attribut | Datentyp | Beschreibung |
| -------- | -------- | ------------ |
| hostname | String   | Hostname des Hostsservers |
| port     | Integer  | Port des Hostservers |
  
### Server
| Attribut | Datentyp | Beschreibung |
| -------- | -------- | ------------ |
| ENCRYPT_SAVE_FILE | Boolean | Flag mit dem die Daten verschlüsselt gespeichert werden können |
| SAVE_PATH | String | Pfadangabe zur Speicherdatei mit den Nutzerdaten |
| SAVE_SECRET_PASSWORD | String | Passwort zum verschlüsseln der Nutzerdatendatei | 
| | | |
| serverPort | Integer | Port, auf dem der Server Anfragen annimmt |
| serverSocket | Socket | Socket des Servers |
| syncHostname | String | Hostname des jeweils anderen Servers zum Synchronisieren.
| users    | HashMap\<String, User\> | Hashmap mit den im System registrierten Nutzern |

### Client
| Attribut | Datentyp | Beschreibung |
| -------- | -------- | ------------ |
| ENCRYPT_MESSAGES | boolean | Flag um Nachrichrichtentexte vor dem Senden zu verschlüsseln.|
| SHOW_ENCRYPTED_MESSAGE_IN_TERMINAL | boolean | Flag um die empfangenen verschlüsselten Nachrichten in der Konsole anzuzeigen. |
| MESSAGE_PASSWORD | String | Passwort, mit dem die Nachrichten verschlüsselt werden, wenn das Flag zur Verschlüsselung gesetzt ist. |
| | | |
| hosts    | HashMap\<Integer, Host\> | Hashmap mit den benutzbaren Servern <br> \(HashMap wird als dynamisches Array verwendet mit Zugriffszeit von 1\) |
| username | String   | Nutzername des Clients |
| password | String   | Passwort des Clients |

Im Konstruktor des der Klasse Client müssen die Verfügbaren Server in die HashMap mit den Hosts eingefügt werden. soll nur der localhost verwendet werden, kann die nachfolgende Zeile entfernt werden. Soll zufällig ein Server aus der Liste verwendet werden, müssen die beiden Hostnames der Server angegeben werden. Die Methode <code>selectHost()</code> wählt dann zufällig einen Server aus der HashMap aus, der für die aktuelle Anfrage verwendet wird.

### AES
Klasse mit den Implementierungen des AES Algorithmus von Lokesh Gupta [https://howtodoinjava.com/security/java-aes-encryption-example/](https://howtodoinjava.com/security/java-aes-encryption-example/).

### UI
Klasse um eine grafische Oberfläche für den Client zu erzeugen.

### CLI
Klasse um ein Eingabeterminal für den Nutzer zu erzeugen

### NonBlockingBufferedReader
Klasse um die Eingaben der CLI Klasse non-blocking zu machen.
Diese Klasse wurde aus einem Forum entnommen. Diese findet sich [hier](https://community.oracle.com/message/5318833#5318833).

### WrongDataPackageException
Exception Objekt für Daten-Objekte, die nicht dem DataPackage Format entsprechen.

### Main-Klasse des Clients
Die Klasse erzeugt entweder ein neues Objekt der Klasse **UI** oder der Klasse **CLI** je nachdem ob das Flag <code>USE_GUI</code> den Wert <code>true</code> oder <code>false</code> hat.

### Main-Klasse des Servers
Die Klasse erzeugt ein neues Server-Objekt und führt die Methode <code>acceptClientConnections</code> aus.

## UML Klassendiagramm:
Hier sieht man das (vorläufige) Klassen- und Beziehungsdiagramm der Basisversion.
<br>
![UML-Klassendiagramm](https://github.com/NerdyStuff/VerteilteSysteme/blob/master/Documentation/Files/uml_diagramm.png)
Das UML-Diagramm ist [hier](https://github.com/NerdyStuff/VerteilteSysteme/blob/master/Documentation/Files/uml_diagramm.pdf) auch als PDF zu finden.

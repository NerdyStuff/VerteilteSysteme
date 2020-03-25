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

Synchronistatiion zwischen den Servern.... (TBD: Two-Phase-Protokoll)
Zeitstempel implementieren (TBD: Laportuhren -> eventuell das DataPacakgeObjet um eine NachrichtenID erweitern!)

## Schnittstellen- und Objektdefinitionen:

### DataPackage
| Attribut | Datentyp | Beschreibung |
| -------- | -------- | ------------ |
| flag:    | Integer  | <code>Wert: 1</code> Registrierung <br> <code>Wert: 2</code> Nachricht <br> <code>Wert: 3</code> Update-Request <br> <code>Wert: 4</code> Neue Nachrichten für den Client <br> <code>Wert: 5</code> Keine neuen Nachrichten für den Client <br> <code>Wert: -1</code> Registrierung fehlgeschlagen <br> <code>Wert: -2</code> Falsches Passwort oder Nutzername <br> <code>Wert: -3</code> Empfänger existiert nicht<br> <code>Wert: 6</code> Registrierung erfolgreich|
| username | String | Nutzername |
| password | String | Password |
| receiver | String | Empfänger der Nachricht |
| message  | String | Nachrichtentext |
| timestamp| Date   | Zeitstempel der Nachricht|

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
  
### Server
| Attribut | Datentyp | Beschreibung |
| -------- | -------- | ------------ |

### Client
| Attribut | Datentyp | Beschreibung |
| -------- | -------- | ------------ |

### WrongMessageException
Exception Objekt für falsche Messages.

## UML Klassendiagramm:
Hier sieht man das (vorläufige) Klassen- und Beziehungsdiagramm der Basisversion.
<br>
![UML-Klassendiagramm](https://github.com/NerdyStuff/VerteilteSysteme/blob/master/Documentation/Files/uml_diagramm.png)
Das UML-Diagramm ist [hier](https://github.com/NerdyStuff/VerteilteSysteme/blob/master/Documentation/Files/uml_diagramm.pdf) auch als PDF zu finden.

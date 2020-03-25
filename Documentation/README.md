# Dokumentation des Projekts
Unsere Ideen können [hier](https://github.com/NerdyStuff/VerteilteSysteme/new/master/Documentation/Ideas) gefunden werden.

[Hier](https://github.com/NerdyStuff/VerteilteSysteme/tree/master/Documentation/Planning) finden sich unsere Planungsnotizen.

## Allgemeines
Zur Kommunikation zwischen den Clients und den Servern werden Objekte der Klasse <code>DataPackage</code> verwendet.
Ein Client kann dabei immer nur ein Objekt der Klasse an den Server senden. Der Server sendet dem Client eine Liste mit DataPackage-Objekten zurück. Die Liste muss dabei immer mindestens ein Objekt enthalten. Durch das Attribut <code>flag</code> wird definiert, was der Server bzw. der Client an Informationen aus dem DataPackage-Objekt an Daten erhalten kann. Die einzelnen Werte des Flags können in der Schnittstellendefinition angesehen werden.

## Schnittstellendefinition:

### DataPackage
| Attribut | Datentyp | Beschreibung |
| -------- | -------- | ------------ |
| flag:    | Integer  | <code>Wert: 1</code> Registrierung <br> <code>Wert: 2</code> Nachricht <br> <code>Wert: 3</code> Update-Request <br> <code>Wert: 4</code> Neue Nachrichten für den Client <br> <code>Wert: </code> Keine neuen Nachrichten für den Client <br> <code>Wert: 1</code> Registrierung fehlgeschlagen <br> <code>Wert: -1</ode> Falsches Passwort oder Nutzername <br> <code>-3</code> Empfänger existiert nicht
| username | String | Nutzername |
| password | String | Password |
| receiver | Empfänger der Nachricht |
| message  | Nachrichtentext |
| timestamp| Zeitstempel der Nachricht|

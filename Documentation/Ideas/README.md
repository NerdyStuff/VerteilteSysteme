# Ideas

## Ablauf
- Server wird gestartet, Socket wird aufgemacht
- Client muss ip und port kennen, server accepted dann den client

Daraus ergibt sich dieser Ablauf:
1. Server Socket öffnen
2. Server IP angeben
3. Server Port angeben
4. Login mit User und Passwort

## Umsetzung

&rarr; handshake?
  Login-Server muss anderen Servern den User bekannt machen; token im header der nachricht
  verschlüsselter body
  
jeder server macht zwei sockets auf, einen für client kommunikation und einen für serverkommunikation
das ganze wird über unterscheidliche portranges getrennt 

## Nachrichten Client Server
Messageobjekt hat timestamp, typ und body und wird auf dem server in die datenbank geschrieben 

### Datentyp für Messages
- Eine Message soll den Timestamp, den Usernamen, das Passwort und die Nachricht mitgeschickt werden
- Dafür würde ein json ideal sein, dieser ist aber durch die anfoderungen ohne eine externe library nicht erlaubt
- Alternativ bauen wir uns unseren eigenen String, welcher durch ein Zeichen die verschiedenen daten trennt
  -Dafür muss ein geeigneter trenner gefunden werden, idealerweise ein non printable character
  
# Testszenarien

## Ausfälle
- Komplettausfall von einem Server
- Komplettausfall von zwei Servern
- Komplettausfall des gesamten Systems

## Verbindungsprobleme
- Ein Client hat Verbindungsprobleme beim Senden einer Nachricht

## Unstimmigkeiten
- Ein User ist doppelt angelegt
- Ein User ist nicht auf allen Servern angelegt (->kann nicht passieren wegen 2PC)

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

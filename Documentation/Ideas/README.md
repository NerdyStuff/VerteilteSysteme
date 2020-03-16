# Ideas

# Ablauf
- Server wird gestartet, Socket wird aufgemacht
- Client muss ip und port kennen, server accepted dann den client

1. Server Socket öffnen
2. Server IP angeben
3. Server Port angeben
4. Login mit User und Passwort

-> handshake?
  Login-Server muss anderen Servern den User bekannt machen; token im header der nachricht
  verschlüsselter body
  
jeder server macht zwei sockets auf, einen für client kommunikation und einen für serverkommunikation
das ganze wird über unterscheidliche portranges getrennt 

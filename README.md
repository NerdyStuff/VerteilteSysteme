# VerteilteSysteme
This project is a distributed chat plattform for the course 'Verteilte Systeme' written in java.

## Task:
Build a TCP based chat system in java using **java.io.\***, **java.net.\***, **java.util.\*** and **java.swing.\*** for the base version.

**User need to login to the server**, to chat with exactly one other user. To avoid failures, there is a need of **two identical servers**, which keep track of the complete chat history.

The user connects to a **random choosen server** (=> **load distribution**) for a **central login** with for example username and password. For the chat the client also connects to a random server.

A messages sent by user A shall be **saved in the chat history** and send to user B, where it is also kept in the chat history. The complete chat history for both parties shall be displayed in correct order with time (=> **Lamport timestamps**) if the users are logged in.

The server shall process the messages from both users fast (=> **non-persistent server**[1]).

The whole system shall be **failsafe for network issues and errors** (for the testcase the network partitioning is short).

The whole system needs **testcases** to cover all the scenarios mentioned above.

The system must be documented (5 - 10 pages + commented code listing). It must be documented who of the group did which.

## Additional extensions:
- Using emoji (+ 5 points)
- Multiple chat histories for a user (+ 10 points)
- graphical user interface (+ 10 points)
- Persist chat history (+ 10 points)
- Encrypted storage of chathistory on the server (+ 10 points)
- Group chats (+ 20 points)
- Encrypted communication (+ 20 points)
- Three replicated servers with majority consensus strategy\[MCS\](+ 40 points)

### Group:
* **Anna-Lena Richert** ([aalenaa](https://github.com/aalenaa))
* **Anton Ochel** ([Tony1704](https://github.com/Tony1704))
* **Benno Grimm** ([Grimmig18](https://github.com/Grimmig18))
* **Tizian Groß** ([tizian123](https://github.com/tizian123)) 
* **Tristan Emig** ([TristanMrb](https://github.com/TristanMrb)) 
* **Marcel Mertens** ([Nerdystuff](https://github.com/NerdyStuff)) 

### Sources & Links
- \[1\] [non-persistent vs persistent connection](https://www.geeksforgeeks.org/http-non-persistent-persistent-connection-set-2/)

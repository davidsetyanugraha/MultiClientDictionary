Run Server
==========

Run this command to start server:
> java –jar DictionaryServer.jar [port] [dictionary-file]

Example: 
> java -jar DictionaryServer.jar 8000 resource/dictionary.json

[dictionary-file]: contains path. 
Please bear in mind. It's different between /resource/dictionary.json and resource/dictionary.json.
In this context, "resource" is the example of folder path.


Run Client
==========

Run this command to start Client:
> java –jar DictionaryClient.jar [server-address] [server-port]

Example: 
> java -jar DictionaryClient.jar 127.0.0.1 8000


Class Diagram
=============

Inside src, there is ClassDiagram.ucls. It is a file to generate a class diagram.

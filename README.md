## SEEGIE – Synchronized EEG Information Exchange

#### Real-time EEG data distribution system for OpenBCI

###### Overall description

SEEGIE is a distributed solution based on the OpenBCI platform. It allows real-time EEG data exchange and remote control of an EEG session. The system features a cross-platform client desktop application in Java with simple and user-friendly GUI, and a cloud server application written using ASP.Net Core. In addition an app targeting iOS is in progress. Below the different components are described, as well as their status as per October 2017.

###### Components status

- Web API: Finished with basic functionalities and deployed to Microsoft Azure. Authentication might need to be implemented in future.

- Desktop client: Backend fully finished, frontend supports basic functionalities.

- App: by @olavblj, in progress.

###### Sessions

A session consists of one "seed" (a client connected to an OpenBCI EEG set) and any number of "leeches" (remote users receiving EEG data). The desktop client implements both modes, which has to be selected by the user upon stratup. EEG data coming from the seed is distributed to all leeches in real-time. OpenBCI commands (start, stop, reset) might be sent either from any of the leeches or from the seed. The EEG data includes 8 channels and is visualized on the screen. The server allows any number of sessions which are distinguished by a session id obtained by the seed upon the first connection to the server. In order to join a session, a user has to switch her client to the leech mode and enter the session id of the appropriate session.

#### OpenBCI protocols used

http://docs.openbci.com/Hardware/03-Cyton_Data_Format

http://docs.openbci.com/OpenBCI%20Software/04-OpenBCI_Cyton_SDK

#### Data exchange formats

* OpenBCI command:

```
{
   "type": "&cmd&",
   "content": "x3020011X"
}
```

* info from OpenBCI:

```
{
   "type": "&info&",
   "content": "CHANNEL 2 IS ON$$$"
}
```

* data from OpenBCI:

```
{
   "type": "&data&",
   "content": {
      "sampleNum": 32,
      "timeStamp": 0,
      "channelData": [
         -2049512,
         -7245366,
         306179,
         1824337,
         1796923,
         -1367319,
         1085662,
         5156861
      ],
      "acclX": -12505,
      "acclY": 25934,
      "acclZ": -10989,
      "timeStampSet": false
   }
}
```

#### License

See the license file.

## Sanntidsavlesing og distribuering av OpenBCI EEG-signaler
#### (SEEGIE – Synchronized EEG Information Exchange)

OpenBCI (open-source brain-computer interface) er et grensesnitt som blant annet inkluderer maskinvare for avlesning av elektriske hjernesignaler (EEG). Et spesielt designet kretskort mottar de avleste signalene. Disse kan videre sendes til en datamaskin ved å bruke seriell kommunikasjon eller bluetooth. Felles for disse kommunikasjonstypene er kort rekkevidde, noe som i praksis betyr at kun én datamaskin kan motta data samtidig, og denne datamaskinen må befinne seg like i nærheten av EEG-settet.

Målet med prosjektet er å lage en pålitelig og effektiv kryssplattform programvare som mottar data fra OpenBCI Cyton-kretskortet og distribuerer den mellom flere enheter i sanntid. EEG skal også kunne fjernstyres ved at systemet sørger for toveiskommunikasjon. Et brukervennlig grafisk grensesnitt for Linux og Windows inngår i systemet.

#### Fremdriftsplan:
1. Fastsettelse av krav: 
    - ferdig; se forrige avsnitt.
2. Overordnet programvarearkitektur: 
    - ferdig; server (web api) + client (desktop applikasjon)
3. Valg av teknologier (rammeverk, språk): 
    - ferdig; ASP.Net Core og Microsoft Azure for server, JavaFX og C++ for client
4. Detaljert programvarearkitektur: 
    - TODO Uke 40
5. Implementasjon
  * server (web api for relaying data, handling multiple websockets)
  * client (seriell port avlesing, client websocket, GUI)
    - TODO Uke 41-44
6. Testing: 
    - TODO Uke 45
7. Dokumentasjon: 
    - TODO Uke 46


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
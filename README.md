## Sanntidsavlesing og distribuering av OpenBCI EEG-signaler
### (SEEGIE – Synchronized EEG Information Exchange)

OpenBCI (open-source brain-computer interface) er et grensesnitt plattform som blant annet inkluderer maskinvare for avlesning av elektriske hjernesignaler (EEG). Et spesielt designet kretskort mottar de avleste signalene. For å utføre signalbehandling og dataanalyse på disse signalene må de sendes videre til en datamaskin. Dette kan gjøres på flere ulike måter da kretskortet støtter ulike teknologier, bl.a. seriell port kommunikasjon, bluetooth. Felles for alle disse kommunikasjonstypene er kort rekkevidde, noe som i praksis betyr at kun én datamaskin kan motta data samtidig, og denne datamaskinen må befinne seg like i nærheten av EEG-settet.

Målet med Seegie er å lage en pålitelig kryssplatform programvare som mottar data fra OpenBCI Cyton-kretskortet og distribuerer den mellom flere enheter i sanntid. EEG skal også kunne fjernstyres fra de andre enhetene slik at systemet sørger for toveiskommunikasjon. Et brukervennlig grafisk grensesnitt inngår i systemet.

Fremdriftsplan:
1. Fastsettelse av krav: ferdig; se forrige avsnitt.
2. Overordnet programvarearkitektur: ferdig; server-client
3. Valg av teknologier (rammeverk, språk): ferdig; ASP.Net Core og Microsoft Azure for server, JavaFX og C++ for client
4. Detaljert programvarearkitektur: TODO Uke 40
5. Implementasjon: TODO Uke 41-44
    - server (relaying data, handling multiple websockets)
    - client (seriell port avlesing, client websocket, GUI)
6. Testing: TODO Uke 45
7. Dokumentasjon: TODO Uke 46

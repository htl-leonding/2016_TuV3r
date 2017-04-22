#Hinzufügen eines Angular-Projektes

##Generell
Das Angular-Projekt muss gebuildet werden. Dies kann mithilfe von "ng build -prod" 
erledigt werden. Das generierte dist- Verzeichniss muss schließlich in den 
Webapp folder des Hauptprojektes verschoben werden.

##Pfade
Damit die Skripts richtig geladen werden, müssen die Pfade
angepasst werden. Das heißt, dass im index.html von den 
hinzugefügten dist Verzeichnissen der basePath geändert
werden muss.

Bei der Durchführung muss dieser Pfad angegeben werden,
damit das Routing funktioniert.
```
<base href="/Turnierverwaltung/aus/">
```
Die Auswertung benötigt keinen BasePath, jedoch ist zu 
Beachten dass der "/", der standartmäßig vorhanden ist,
aus dem Pfad gelöscht wird.
```
<base href="">
```
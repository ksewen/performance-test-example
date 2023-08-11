# So führen Sie einen Software-Performance-Test durch

## Hintergrund

Als ich als Senior Java-Entwickler in einem Middleware-Team eines Unternehmens in China gearbeitet habe, habe ich ein
typisches Problem gelöst.  
Es gab ein Leistungsproblem mit Spring Cloud Gateway. Jemand hat bei uns darüber geklagt, dass die QPS im Vergleich zum
Betriebsservice zu niedrig ist. Die Services seines Teams bietet direkt Dienst für Kunden an, daher Leistungsprobleme zu
größeren Problemen führen könnte. Ich habe
durch einen
Performance-Test bei
einigen gängigen
Tools die
Gründe der
Fehler
gefunden. Deshalb konnten wir sie beheben.  
Ich finde, es war ein tolles Beispiel dafür, wie Performance-Test durchgeführt wird und wie die Engpässe gefunden
werden.  
In diesem Projekt habe ich eine Umgebung, die wie solch ein Problem ähnlich ist, ohne sensible Daten erstellt, damit ich
allen vorstellen konnte:

- Welche Probleme sind aufgetreten?
- Wie habe ich die behebt?
- Welche Erfolge wurden erreicht?

## Vorstellung des Projektes

Mit Docker habe ich die Testumgebung eingerichtet. In jedem Modul gibt es Skripten für alle Spring-Boot-Service. Im
Verzeichnis „resources“ vom Hauptverzeichnis gibt es eine „docker-compose“ Datei, mit der man sofort eine Testumgebung
einrichten kann.

Der Code im Branch [**0.0.1**](https://github.com/ksewen/performance-test-example/tree/0.0.1) gibt es Probleme. Ich habe unwichtige und potenziell vertrauliche Unternehmensdaten
entfernt und nur das Kernproblem behalten.

Der Code im Branch **0.0.2** ......  
Der Code im Branch **0.0.3** ......

## Mit Docker ausführen

### Image erstellen

```shell
# Am Anfang klonen Sie den Code auf Ihren Computer und wechseln Sie zum Hauptverzeichnis. Und dann führen Sie den folgenden Code aus.
# Erstellen des Gateway-Services
gateway-for-test/resources/scripts/build-image.sh -d .

# Erstellen des Betriebsservices
service-for-test/resources/scripts/build-image.sh -d .

# Erstellen des Auth-Services
auth-service-for-test/resources/scripts/build-image.sh -d .
```

### Mit docker-compose ausführen

```shell
cd resources && \
docker-compose --compatibility -f docker-compose.yml up
```

## Test durchführen

In diesem Beispiel habe ich nur eine einfache Route im Gateway konfiguriert. Mit folgendem Code kann man auf die
Schnittstelle zugreifen. Einige Performance-Test-Tools wie Jmeter, wrk usw. können natürlich benutzt werden.

```shell
curl http://127.0.0.1:38071/service/hello
```

Meine Wahl ist [**wrk**](https://github.com/wg/wrk). Dieses leichtgewichtige Tool reicht aus, um mir bei der
Lokalisierung von Problemen zu helfen.

# Die dritte Testrunde

## Durchsatzrate mit Gateway

Mit dem AsyncAppender war ich davon enttäuscht, dass die Durchsatzrate sich um **fast 20%** gesenkt hat. Dieses
unerwartete Ergebnis konnte möglicherweise im Zusammenhang damit stehen, dass ich nur ein Single-Core im Docker-Image
eingestellt habe.

```shell
Running 5m test @ http://127.0.0.1:38071/service/hello
  5 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     7.42ms    9.85ms  58.53ms   84.07%
    Req/Sec   535.98     49.80   730.00     82.24%
  Latency Distribution
     50%    2.50ms
     75%    6.08ms
     90%   25.14ms
     99%   38.72ms
  800360 requests in 5.00m, 97.70MB read
Requests/sec:   2667.01
Transfer/sec:    333.38KB
```

Aber ich war überzeugt davon, dass das eine wirksame Optimierung in Multi-Core-Systemen war.

## Änderung der Filterstrategie

Um andere möglicherweise vorhandene Engpässe zu finden, sollte ich eine Änderung der Filterstrategie vom Jprofiler
vornehmen. Derweil sollte ich „Monitors & Locks“ auch überwachen.

In diesem Fall sollte ich zuerst den **AsyncAppender entfernen**, die Konfigurationsdatei befindet sich im Verzeichnis
„resources“ im Hauptverzeichnis.

## Analyse

In dieser Runde habe ich auch mit Jprofiler überwacht. Dann konnte ich auch Snapshot-Datei analysieren, um die
Möglichkeit zur Optimierung der Durchsatzrate zu finden.

Im folgenden Bild kann man sehen, dass es viel Zeit für die Ausgabe von Loge verbracht hat.

![cpu-views-call-tree](https://raw.githubusercontent.com/ksewen/Bilder/main/202308161502704.png "CPU Views - Call Tree")

Zum Glück bietet „Logback“ einen [**AsyncAppender**](https://logback.qos.ch/manual/appenders.html#AsyncAppender) an, mit
dem man die Ausgabe von Loge vom Arbeit-thread trennen kann.
# Die dritte Testrunde

## Durchsatzrate mit Gateway

Mit dem AsyncAppender war ich davon enttäuscht, dass die Durchsatzrate sich um **ca. 4 %** gesenkt hat. Dieses
unerwartete Ergebnis konnte möglicherweise im Zusammenhang damit stehen, dass ich nur ein Zwei-Core im Docker-Image
eingestellt habe.

```shell
Running 5m test @ http://127.0.0.1:38071/service/hello
  5 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.56ms  545.34us  29.72ms   92.53%
    Req/Sec   787.67     33.81     0.89k    77.66%
  Latency Distribution
     50%    2.48ms
     75%    2.69ms
     90%    2.94ms
     99%    4.09ms
  1176033 requests in 5.00m, 143.56MB read
Requests/sec:   3919.34
Transfer/sec:    489.92KB
```

Aber ich war überzeugt davon, dass das eine wirksame Optimierung in mehr als Multicore-Systemen (Quad-Cores oder mehr)
war.

## Änderung der Filterstrategie

Um andere möglicherweise vorhandene Engpässe zu finden, sollte ich eine Änderung der Filterstrategie vom Jprofiler
vornehmen. Derweil sollte ich „Monitors & Locks“ auch überwachen.

In diesem Fall sollte ich zuerst den **AsyncAppender entfernen**, die Konfigurationsdatei befindet sich im Verzeichnis
„resources“ im Hauptverzeichnis.

Außerdem habe ich auch Log-Level auf „Error“ geändert. Es war natürlich eine praktikable Lösung, weil es im
Microservice-Framework des Unternehmens solche Features, mit den man Log-Level zur Laufzeit eine Änderung vornehmen
kann, allgemein bei der Produktion gab.

Durch folgendes Ergebnis lässt sich die Durchsatzrate erkennen, dass sie **etwa 6 %** gestiegen ist.

```shell
Running 5m test @ http://127.0.0.1:38071/service/hello
  5 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.31ms  761.12us  69.92ms   96.95%
    Req/Sec     0.88k    59.38     0.96k    85.50%
  Latency Distribution
     50%    2.21ms
     75%    2.36ms
     90%    2.56ms
     99%    4.36ms
  1311067 requests in 5.00m, 160.04MB read
Requests/sec:   4369.11
Transfer/sec:    546.14KB
```

## Analyse

Um zu beweisen, dass der Gateway-Service derzeit keine offensichtlichen Probleme hatte, habe ich zwei folgende Bilder
gespeichert.

![cpu-views-call-tree](https://github.com/ksewen/Bilder/blob/main/202308190029673.png?raw=true "CPU Views - Call Tree")

![thread](https://raw.githubusercontent.com/ksewen/Bilder/main/202308190030455.png "Thread")

Es gab keine offensichtlichen Methoden, die viel Zeit benötigen, und keine sehr häufigen „Block“ und „Wait“ gab es auch.

## Weitere Erklärungen

### Engpass wegen der Generierung von „UUID“

Wenn man Linux als Betriebssystem für den Service verwendet, die Codes in der Class
„com.github.ksewen.performance.test.gateway.filter.tracing.TracingGlobalFilter“ vielleicht einen Engpass hat, weil in
der Class die Method „UUID.randomUUID()“ verwendet wird.

Zum Beispiel habe ich ohne die Spezifikation von der Class „SecureRandom“ den Gateway-Service ausgeführt, konnte ich im
Test solche Blockierung finden.

![Blockierung-1](https://raw.githubusercontent.com/ksewen/Bilder/main/202308201438817.png)
![Blockierung-2](https://raw.githubusercontent.com/ksewen/Bilder/main/202308201439720.png)

![Statistik der Blockierung](https://raw.githubusercontent.com/ksewen/Bilder/main/202308201439000.png)

Um dieses Problem zu umgehen, habe ich folgende Option verwendet, als ich Docker-Image erstellt habe.

```shell
-Djava.security.egd=file:/dev/urandom
```
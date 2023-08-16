# Die zweite Testrunde

## Durchsatzrate mit Gateway

Nach der Behebung sollte ich die neue Durchsatzrate des Backend-Services über das Gateway hinweg kennen, um die Wirkung
der Optimierung zu demonstrieren.

In dieser Runde des Tests habe ich das folgende Ergebnis erhalten:

```shell
Running 5m test @ http://127.0.0.1:38071/service/hello
  5 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     4.63ms    5.63ms  59.35ms   87.50%
    Req/Sec   666.07     38.83     0.88k    77.55%
  Latency Distribution
     50%    2.37ms
     75%    2.73ms
     90%   13.25ms
     99%   25.87ms
  994697 requests in 5.00m, 121.42MB read
Requests/sec:   3314.59
Transfer/sec:    414.32KB
```

Die Durchsatzrate war über **3300**. Die Zahl hat sich um 83% gestiegen.

## Analyse

In dieser Runde habe ich auch mit Jprofiler überwacht. Dann konnte ich auch Snapshot-Datei analysieren, um die
Möglichkeit zur Optimierung der Durchsatzrate zu finden.

Im folgenden Bild kann man sehen, dass es viel Zeit für die Ausgabe von Loge verbracht hat.

![cpu-views-call-tree](https://raw.githubusercontent.com/ksewen/Bilder/main/202308161502704.png "CPU Views - Call Tree")

Zum Glück bietet „Logback“ einen [**AsyncAppender**](https://logback.qos.ch/manual/appenders.html#AsyncAppender) an, mit
dem man die Ausgabe von Loge vom Arbeit-thread trennen kann.
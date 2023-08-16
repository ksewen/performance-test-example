# Die zweite Testrunde

## Durchsatzrate mit Gateway

Nach der Behebung sollte ich die neue Durchsatzrate des Backend-Services über das Gateway hinweg kennen, um die Wirkung
der Optimierung zu demonstrieren.

In dieser Runde des Tests habe ich das folgende Ergebnis erhalten:

```shell
Running 5m test @ http://127.0.0.1:38071/service/hello
  5 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.47ms  772.41us  43.05ms   96.34%
    Req/Sec   822.41     55.76     0.92k    86.56%
  Latency Distribution
     50%    2.36ms
     75%    2.54ms
     90%    2.80ms
     99%    4.65ms
  1227963 requests in 5.00m, 149.90MB read
Requests/sec:   4091.91
Transfer/sec:    511.49KB
```

Die Durchsatzrate war über **4000**. Die Zahl hat sich mehr als 125 % gestiegen.

## Analyse

In dieser Runde habe ich auch mit Jprofiler überwacht. Dann konnte ich auch Snapshot-Datei analysieren, um die
Möglichkeit zur Optimierung der Durchsatzrate zu finden.

Im folgenden Bild kann man sehen, dass es viel Zeit für die Ausgabe von Loge verbracht hat.

![cpu-views-call-tree](https://raw.githubusercontent.com/ksewen/Bilder/main/202308161502704.png "CPU Views - Call Tree")

Zum Glück bietet „Logback“ einen [**AsyncAppender**](https://logback.qos.ch/manual/appenders.html#AsyncAppender) an, mit
dem man die Ausgabe von Loge vom Arbeit-thread trennen kann.
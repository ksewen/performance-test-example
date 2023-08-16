# Die erste Testrunde

## Betriebssystem-Benchmark

Am Anfang sollte ich die Durchsatzrate des Backend-Services kennen.
Mit folgendem Code habe ich den Test durchgeführt.

```shell
wrk -t5 -c10 -d300s --timeout=10s --latency http://127.0.0.1:38072/hello
```

Derweil sollte ich auch Systemressourcen des Backend-Services überwachen. In diesem Beispiel habe ich direkt „docker
stats“ benutzt.

```shell
docker stats ${CONTAINER}
# oder in eine Datei Ergebnisse schreiben
docker stats ${CONTAINER} | awk '{print strftime("%m-%d-%Y %H:%M:%S",systime()), $0}' >> ${FILE_PATH_AND_NAME}
```

In diesem Test habe ich folgendes Ergebnis erhalten:

```shell
Running 5m test @ http://127.0.0.1:38072/hello
  5 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     3.08ms    6.10ms  52.91ms   87.33%
    Req/Sec     2.61k   219.70     3.65k    79.65%
  Latency Distribution
     50%  583.00us
     75%  779.00us
     90%   12.39ms
     99%   26.01ms
  3899171 requests in 5.00m, 469.24MB read
Requests/sec:  12994.82
Transfer/sec:      1.56MB
```

Und die CPU-Auslastung des Backend-Services lag **über 100%**.

## Durchsatzrate mit Gateway

Danach sollte ich die Durchsatzrate des Backend-Services über das Gateway hinweg kennen.  
Wrk habe ich auch anwendet und mit „docker stats“ habe ich Systemressourcen überwacht.

```shell
wrk -t5 -c10 -d300s --timeout=10s --latency http://127.0.0.1:38071/service/hello
```

In diesem Test habe ich folgendes Ergebnis erhalten:

```shell
Running 5m test @ http://127.0.0.1:38071/service/hello
  5 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     6.29ms    6.75ms 206.86ms   97.07%
    Req/Sec   363.95     67.84   545.00     85.73%
  Latency Distribution
     50%    5.17ms
     75%    6.52ms
     90%    8.22ms
     99%   43.07ms
  543438 requests in 5.00m, 66.34MB read
Requests/sec:   1811.16
Transfer/sec:    226.39KB
```

Die CPU-Auslastung des Gateways lag nur **etwas 60%**.  
Die CPU-Auslastung des Backend-Services lag nur **etwas 20%**.  
Die CPU-Auslastung des Auth-Services lag nur **etwas 30%**.

Das Ergebnis von „Requests/sec“ war nur **1811.16**. Aber es konnte einen Wert **über 12000** einstellen, wenn ich den
Backend-Service direkt zugegriffen habe. Im Zusammenhang mit dem Systemressourcenaufwand des Gateway-Services lässt sich
vermuten, dass möglicherweise Engpässe im Gateway vorhanden sind. Anfragen könnten längere Wartezeiten haben, was
wiederum erheblichen Einfluss auf den Durchsatz der Backend-Services hat.

In der nächsten Testrunde brauchte ich einige Tools, um die Engpässe zu finden.

## Test mit Jprofiler

[**Jprofiler**](https://www.ej-technologies.com/products/jprofiler/overview.html) ist ein Java Profiling und Testing
Tool. Mit dem man eine präzise Überwachung der JVM durchführen kann, wobei Heap-Traversal, CPU-Profiling und
Thread-Profiling effektive Methoden zur Lokalisierung von aktuellen Engpässen im System sind. Aber es ist ein Tool mit
großem Funktionsumfang. Das könnte möglicherweise zu Leistungsproblemen führen und darf nicht in einer
Produktionsumgebung verwendet werden.

Im Image des Gateway-Services habe ich „Jprofiler-Agent“ heruntergeladen. In der Datei „docker-compose.yml“ habe ich das
Java Argument bereits konfiguriert.

### Konfiguration

Mit der Adresse „127.0.0.1:38849“ des Agents des Gateway-Services habe ich mich über die JProfiler-Anwendung
verbindet.  
Dann habe ich einen Call Tree Filter „com.github.ksewen“ hinzugefügt. Weil ich in diesem Fall schon gewissen, welcher
Code am wahrscheinlichsten Probleme verursacht.

**In der Praxis werde ich versuchen, die vermuteten Codeabschnitte so weit wie möglich zu überwachen.**

Nach der Konfiguration habe ich auch „wrk“ benutzt, um Überwachungsdaten zu erhalten.

### Analyse

Im folgenden Bild kann man sehen, dass die Methode „org.springframework.web.client.RestTemplate.postForEntity“ die
meiste Zeit in Anspruch genommen hat.  
![cpu-views-call-tree](https://raw.githubusercontent.com/ksewen/Bilder/main/image-01-0.0.1-gateway-12082023-01.png
"CPU Views - Call Tree")

Es war ein offensichtliches Problem, bei dem im React-Projekt blockierende I/O von RestTemplate (HttpClient) verwendet
wird.

### Behebung

Um dieses Problem zu beheben, wurde ich **WebClient** anstatt „RestTemplate“ anwenden.

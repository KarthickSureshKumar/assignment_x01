Travel API Client 
=================

-Provides the list of airport information. 
-Discounted fare details between origin and destination airport choose.
-Statistics of App(number of request and response time lapse in seconds) to monitor.

Clone this repo and start it (on windows systems use the gradlew.bat file):

`./gradlew bootRun` or './gradlew build' & java -jar /build/libs/destination-finder-client-0.1.0.jar

to list all tasks:

`./gradlew tasks`

To view the assignment (after starting the application) go to:

[http://localhost:9000](http://localhost:9000)

To retrieve the available airports

**Retrieve a list of airports**:

`http://localhost:9000/airport/all`

Query params:

- size: the size of the result
- page: the page to be selected in the paged response
- lang: the language, supported ones are nl and en
- term: A search term that searches through code, name and description.

**Retrieve a fare offer**:

`http://localhost:9000/fares/{origin_code}/{destination_code}`

Query params:

- currency: the requested resulting currency, supported ones are EUR and USD

**Retrieve the metrics endpoint**:

http://localhost:9000/actuator/metrics/http.server.requests

Query params:
- tag: to view with respect to respect request uri e.g uri:/airport/all or specific http status eg: status:400 or status:500

http://localhost:9000/actuator/httptrace

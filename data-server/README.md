# emacs-workshop-app

FIXME

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

Also redis 2.4 or newer is required.

## Running

To start a web server for the application, run:

    lein run [--nrepl] [--config config-file-location.edn]

## Usage

In order for backend app to function properly 3 redis keys must be set properly.

Hash "data-server:arduino-ip-hash" must contain mapping of arduino Id to its id.

Example:

    hset "data-server:arduino-ip-hash" "a1" "192.168.15.182"

Hash "data-server:sensor-hash" must contain serialized Clojure maps mapping sensors id to arduino-id and sensor-type. Setting it works only from REPL currently:

Example:

    data-server.storage> (data-server-sensor-hash-set-key "temp1" "a1" "dht22")

Hash "data-server:device-hash" works analogously, but for devices instead of sensors.

    data-server.storage> (data-server-device-hash-set-key "s1" "a1" "stove")

## License

Copyright © 2014 FIXME

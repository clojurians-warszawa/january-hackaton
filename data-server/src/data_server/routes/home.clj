(ns data-server.routes.home
  (:require [data-server.storage :as storage])
  (:use data-server.server-helpers
        net.cgrand.moustache))

(defn get-current-sensor [req sensor-id]
  (json-response "ok"))

(defn get-all-current-sensors [req]
  (json-response (storage/data-server-sensor-get-historical-state :history-range 0)))

(defn get-historical-sensor [req sensor-id]
  (json-response "ok"))

(defn get-all-historical-sensors [req]
  (json-response (storage/data-server-sensor-get-historical-state)))

(defn create-device [[:keys [params] :as req]]
  (let [{:keys [device-id arduino-id device-type]} params]
   (json-response (storage/data-server-device-hash-set-key device-id arduino-id device-type))))

(defn get-devices [req]
  (json-response (storage/data-server-device-hash-get-all)))

(def routes
  (app
   ["_current" &] [[""] get-all-current-sensors
                   [sensor-id] (delegate get-current-sensor sensor-id)]
   ["_history" &] [[""] get-all-historical-sensors
                   [sensor-id] (delegate get-historical-sensor sensor-id)]
   ["_device" &] [[""] {:get get-devices
                        :post create-device}]
   ["version"] {:get (constantly (json-response "1.0"))}))

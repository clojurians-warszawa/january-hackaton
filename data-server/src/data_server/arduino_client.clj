(ns data-server.arduino-client
  (:require [clj-http.client :as http]
            [data-server.storage :as storage]
            [data-server.util :as util]))

(defn- get-arduino-status [arduino-ip]
  (http/get (str "http://" arduino-ip) {:as :json}))

(defn- update-single-arduino-state [arduino-id]
  (let [ip (storage/data-server-arduino-get-ip-by-id arduino-id)
        {:keys [body] :as result} (get-arduino-status ip)
        timestamp (System/currentTimeMillis)]
    ;; NOTE: might add logging of entire http response
    ;; TODO: format will change from:
    ;; {:sensors [{:DHT22 {:temp 280C, :humidity 310%}}]}
    ;; to:
    ;; {:sensors {:DHT22 {:temp 280C, :humidity 310%}}}
    (doseq [sensor (:sensors body)]
      (storage/data-server-sensor-add-state (-> sensor first first)
                                            (-> sensor first second (assoc :timestamp timestamp))))))

(defn- update-arduino-states [arduino-ids]
  (assert (vector? arduino-ids))
  (doseq [id arduino-ids]
    (update-single-arduino-state id)))

(defn arduino-status-updater [check-interval arduino-ids]
  (assert (vector? arduino-ids))
  (util/execute-at-intervals
   check-interval
   "arduino-state-updater"
   (update-arduino-states arduino-ids)))

;;; TODO: to be expanded
(defn start-arduino-updater-threads []
  (let [arduino-ids (storage/data-server-arduino-get-all-ids)]
   (.start (Thread. (partial arduino-status-updater 60 arduino-ids) "arduino-status-updater"))))

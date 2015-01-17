(ns data-server.storage
  (:require [taoensso.carmine :as redis :refer (wcar)] ; or use wcar*
            [clojure.string :as string]
            [data-server.config :as config]))

(def server-conn (delay {:pool {}, :spec {:host (config/get-config-var :redis-host) :port (config/get-config-var :redis-port)}}))

(defmacro wcar* [& body] `(redis/wcar @server-conn ~@body))

(defn- build-key [& components]
  (string/join ":" components))

(def data-server-arduino-ip-hash (build-key "data-server" "arduino-ip-hash"))

(defn data-server-arduino-get-ip-by-id [id]
  (wcar* (redis/hget data-server-arduino-ip-hash id)))

(defn data-server-arduino-get-all-ids []
  (wcar* (redis/hkeys data-server-arduino-ip-hash)))

;;; might not be used, maybe for diagnostics
(def data-server-sensor-hash (build-key "data-server" "sensor-hash"))

(defn data-server-sensor-hash-set-key [sensor-id arduino-id sensor-type]
  (wcar* (redis/hset data-server-sensor-hash sensor-id {:arduino-id arduino-id
                                                        :sensor-type sensor-type})))

(defn data-server-sensor-hash-get-by-id [sensor-id]
  (wcar* (redis/hget data-server-sensor-hash sensor-id)))

;; (defn data-server-sensor-hash-get-all-ids []
;;   (wcar* (redis/hkeys data-server-sensor-hash)))

(defn data-server-sensor-hash-get-all []
  (let [result (wcar* (redis/hgetall data-server-sensor-hash))]
    (into {} (mapv vec (partition 2 result)))))

;; (def data-server- (build-key "data-server" "arduino-ip-hash"))

(defn data-server-sensor-add-state [sensor-id data]
  (let [redis-key (build-key "data-server" "sensor-states" (name sensor-id))]
   (wcar* (redis/lpush redis-key data))))

#_(defn data-server-sensor-get-current-state []
  (let [sensors (data-server-sensor-hash-get-all)
        sensor-ids (keys sensors)
        result (wcar* (mapv #(redis/lindex (build-key "data-server" "sensor-states" %) 0) sensor-ids))]
    (if (= (count sensor-ids) 1)
      (hash-map (first sensor-ids) {:values [result]})
      (zipmap sensor-ids (mapv vector result)))))

(defn data-server-sensor-get-historical-state [& {:keys [history-range] :or {history-range -1}}]
  (let [sensors (data-server-sensor-hash-get-all)
        sensor-ids (keys sensors)
        result (wcar*
                (mapv #(redis/lrange (build-key "data-server" "sensor-states" %) 0 history-range) sensor-ids))
        result-map (if (= (count sensor-ids) 1)
                     (hash-map (first sensor-ids) result)
                     (zipmap sensor-ids result))]
    (into {} (mapv (fn [[k v]] [k (assoc (sensors k) :values v)]) result-map))))

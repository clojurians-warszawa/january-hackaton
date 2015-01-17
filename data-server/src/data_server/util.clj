(ns data-server.util
  (:require [clojure.tools.logging :as log]))

(defmacro ignoring-exceptions
  "Execute body within a try block. If an Exception is thrown, log an error message and return retval (a single form)."
  [message retval & body]
  `(try
     ~@body
     (catch Exception e#
       (log/error (str ~message ": got exception, ignored it: "
                       (.toString e#) " \nStacktrace: " (.printStackTrace e#)))
       ~retval)))

(defmacro execute-at-intervals
  "Macro used for repeatedly calling some code. Interval is specified in seconds."
  [interval name & body]
  `(loop []
     (ignoring-exceptions ~name (do (Thread/sleep 1000) nil) ; avoid busy-looping
                          ~@body
                          (Thread/sleep (* 1000 ~interval)))
     (recur)))



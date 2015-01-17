(ns data-server.app
    (:require [data-server.routes.home :as home-routes])
    (:use clojure.test
        ring.middleware.params
        ring.middleware.session
        ring.middleware.file-info
        ring.middleware.json
        ring.middleware.keyword-params
        net.cgrand.moustache
        data-server.server-helpers
        [data-server.middleware :only [custom-middleware]]))

(def wrap-custom-middleware (apply comp custom-middleware))

(def main-app
  (app
   wrap-params
   wrap-json-params
   wrap-keyword-params
   wrap-log-exceptions
   wrap-custom-middleware
   
   home-routes/routes))

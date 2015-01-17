(defproject data-server "unused"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.logging "0.2.6"]
                 [org.clojure/data.json "0.2.4"]
                 [org.clojure/tools.cli "0.3.1"]
                 [slingshot "0.10.3"]
                 [ring "1.2.2"]
                 [net.cgrand/moustache "1.1.0"]
                 [lib-noir "0.8.3"]
                 [log4j
                  "1.2.17"
                  :exclusions
                  [javax.mail/mail
                   javax.jms/jms
                   com.sun.jdmk/jmxtools
                   com.sun.jmx/jmxri]]
                 [commons-io/commons-io "2.4"]
                 [commons-codec "1.9"]
                 [org.clojure/tools.nrepl "0.2.5"]
                 [ring/ring-json "0.3.1"]
                 [com.taoensso/encore "1.18.2"]
                 [prismatic/plumbing "0.3.1"]
                 [prismatic/schema "0.2.3"]
                 [com.taoensso/carmine "2.9.0"]
                 [clj-http "1.0.1"]]
  :repl-options {:init-ns data-server.app}
  :plugins [[cider/cider-nrepl "0.7.0"]]
  :global-vars {*warn-on-reflection* true}
  :main data-server.main
  :profiles {:uberjar {:aot :all}
             :production {:ring {:open-browser? false
                                 :stacktraces?  false
                                 :auto-reload?  false}}
             :dev {:dependencies [[ring-mock "0.1.5"]
                                  [criterium "0.4.3"]]}}
  :min-lein-version "2.0.0")

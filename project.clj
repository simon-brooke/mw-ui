(defproject mw-ui "0.1.2-SNAPSHOT"
  :description "Web-based user interface for MicroWorld"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [mw-engine "0.1.2-SNAPSHOT"]
                 [mw-parser "0.1.2-SNAPSHOT"]
                 [lib-noir "0.8.4"]
                 [ring-server "0.3.1"]
                 [selmer "0.6.8"]
                 [com.taoensso/timbre "3.2.1"]
                 [com.taoensso/tower "2.0.2"]
                 [markdown-clj "0.9.44"]
                 [environ "0.5.0"]
                 [noir-exception "0.2.2"]]

  :repl-options {:init-ns mw-ui.repl}
  :plugins [[lein-ring "0.8.11"]
            [lein-environ "0.5.0"]
            [lein-marginalia "0.7.1"]]
  :ring {:handler mw-ui.handler/app
         :init    mw-ui.handler/init
         :destroy mw-ui.handler/destroy
         :resources-path "resources"
         :war-resources-path "war-resources"
         :uberwar-name "microworld.war"
         }
  :profiles
  {:uberjar {:aot :all}
   :production {:ring {:open-browser? false
                       :stacktraces?  false
                       :auto-reload?  false}}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.3.0"]
                        [pjstadig/humane-test-output "0.6.0"]]
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev true}}}
  :min-lein-version "2.0.0")

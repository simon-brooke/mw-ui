(defproject mw-ui "0.1.6-SNAPSHOT"
  :description "Web-based user interface for MicroWorld"
  :url "http://www.journeyman.cc/microworld"
  :manifest {
             "build-signature-version" "unset"
             "build-signature-user" "unset"
             "build-signature-email" "unset"
             "build-signature-timestamp" "unset"
             "Implementation-Version" "unset"
             }
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [mw-engine "0.1.6-SNAPSHOT"]
                 [mw-parser "0.1.6-SNAPSHOT"]
                 [lib-noir "0.9.9"]
                 [ring-server "0.5.0"]
                 [selmer "1.12.25"]
                 [hiccup "1.0.5"]
                 [com.taoensso/timbre "4.10.0"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "1.10.4"]
                 [environ "1.2.0"]
                 [noir-exception "0.2.5"]]

  :repl-options {:init-ns mw-ui.repl}
  :plugins [[lein-ring "0.8.11"]
            [lein-environ "0.5.0"]
            [lein-marginalia "0.7.1"]
            [io.sarnowski/lein-docker "1.1.0"]]
  :docker {:image-name "simonbrooke/microworld"
         :dockerfile "Dockerfile"}
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
                        [ring/ring-devel "1.8.1"]
                        [pjstadig/humane-test-output "0.10.0"]]
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev true}}}
  :min-lein-version "2.0.0")

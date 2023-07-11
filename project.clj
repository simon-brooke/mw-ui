(defproject mw-ui "0.2.0-SNAPSHOT"
  :cloverage {:output "docs/cloverage"}
  :codox {:metadata {:doc "**TODO**: write docs"
                     :doc/format :markdown}
          :output-path "docs/codox"
          :source-uri "https://github.com/simon-brooke/mw-ui/blob/master/{filepath}#L{line}"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [clj-time "0.15.2"] ;; this is a hack. Something in libnoir requires 
                                     ;; JodaTime, but doesn't request it. clj-time does.
                 [mw-engine "0.2.0-SNAPSHOT"]
                 [mw-parser "0.2.0-SNAPSHOT"]
                 [lib-noir "0.9.9"]
                 [ring-server "0.5.0"]
                 [selmer "1.12.59"]
                 [hiccup "1.0.5"]
                 [com.taoensso/timbre "6.2.1"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "1.11.4"]
                 [environ "1.2.0"]
                 [noir-exception "0.2.5"]]
  :description "Web-based user interface for MicroWorld"
  :docker {:image-name "simonbrooke/microworld"
           :dockerfile "Dockerfile"}
  :main mw-ui.repl
  :manifest {"build-signature-version" "0.1.6-SNAPSHOT"
             "build-signature-user" "Simon Brooke"
             "build-signature-email" "unset"
             "build-signature-timestamp" "2021-05-17 13:31:22+01:00"
             "Implementation-Version" "0.1.6-SNAPSHOT built by Simon Brooke on 2021-05-17 13:31:22+01:00"}
  :min-lein-version "2.0.0"

  :plugins [[lein-ring "0.12.6"]
            [lein-cloverage "1.2.2"]
            [lein-codox "0.10.8"]
            [lein-environ "1.2.0"]
            [lein-marginalia "0.9.1"]
            [gorillalabs/lein-docker "1.6.0"]]
  :profiles
  {:uberjar {:aot :all}
   :production {:ring {:open-browser? false
                       :stacktraces?  false
                       :auto-reload?  false}}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.10.0"]
                        [pjstadig/humane-test-output "0.11.0"]]
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev true}}}
  :repl-options {:init-ns mw-ui.repl}
  :ring {:handler mw-ui.handler/app
         :init    mw-ui.handler/init
         :destroy mw-ui.handler/destroy
         :resources-path "resources"
         :war-resources-path "war-resources"
         :uberwar-name "microworld.war"}

  :url "http://www.journeyman.cc/microworld")

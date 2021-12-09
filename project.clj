(defproject mw-ui "0.1.6-SNAPSHOT"
  :description "Web-based user interface for MicroWorld"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [mw-engine "0.1.6-SNAPSHOT"]
                 [mw-parser "0.1.6-SNAPSHOT"]
                 [lib-noir "0.9.9"]
                 [ring-server "0.5.0"]
                 [selmer "1.12.45"]
                 [hiccup "1.0.5"]
                 [com.taoensso/timbre "5.1.2"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "1.10.7"]
                 [environ "1.2.0"]
                 [noir-exception "0.2.5"]]
   :docker {:image-name "simonbrooke/microworld"
            :dockerfile "Dockerfile"}
  :main mw-ui.repl
  :manifest {"build-signature-version" "0.1.6-SNAPSHOT"
              "build-signature-user" "Simon Brooke"
              "build-signature-email" "unset"
              "build-signature-timestamp" "2021-05-17 13:31:22+01:00"
              "Implementation-Version" "0.1.6-SNAPSHOT built by Simon Brooke on 2021-05-17 13:31:22+01:00"}
 :min-lein-version "2.0.0"

  :plugins [[lein-ring "0.8.11"]
            [lein-environ "0.5.0"]
            [lein-marginalia "0.7.1"]
            [io.sarnowski/lein-docker "1.1.0"]]
  :profiles
  {:uberjar {:aot :all}
   :production {:ring {:open-browser? false
                       :stacktraces?  false
                       :auto-reload?  false}}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.9.4"]
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

    :url "http://www.journeyman.cc/microworld"
)

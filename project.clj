(defproject mw-ui "0.1.5-SNAPSHOT"
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
                 [mw-engine "3.0.0-SNAPSHOT"]
                 [mw-parser "3.0.0-SNAPSHOT"]
                 [lib-noir "0.9.9"]
                 [ring-server "0.4.0"]
                 [selmer "1.0.9"]
                 [com.taoensso/timbre "3.2.1"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "0.9.89"]
                 [environ "1.1.0"]
                 [noir-exception "0.2.5"]]

  :source-paths ["src/clj" "src/cljc"]
  :license {:name "GNU General Public License v2"
            :url "http://www.gnu.org/licenses/gpl-2.0.html"}
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
                        [ring/ring-devel "1.5.0"]
                        [pjstadig/humane-test-output "0.8.1"]]
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev true}}}
  :min-lein-version "2.0.0")

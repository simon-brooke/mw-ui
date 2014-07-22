(ns mw-ui.routes.params
  (:use clojure.walk
        compojure.core)
  (:require [hiccup.core :refer [html]]
            [mw-engine.heightmap :as heightmap]
            [mw-parser.bulk :as compiler]
            [mw-ui.layout :as layout]
            [mw-ui.util :as util]
            [mw-ui.render-world :as world]
            [noir.session :as session]))

(defn- send-params []
  {{:title "Choose your world"
    :heightmaps (util/list-resources "resources/public/img/heightmaps" #"([0-9a-z-_]+).png")
    :pause (or (session/get :pause) 5)
    :rulesets (util/list-resources "resources/rulesets" #"([0-9a-z-_]+).txt")
    })

(defn params-page 
  "Handler for params request. If no `request` passed, show empty params form.
   If `request` is passed, put parameters from request into session and show
   the world page."
  ([]
    (layout/render "params.html" (send-params)))
  ([request]
    (try
      (let [params (keywordize-keys (:form-params request))
            map (:heightmap params)
            pause (:pause params)
            rulefile (:ruleset params)
            rulepath (str "resources/rulesets/" rulefile ".txt")]
        (if (not (= map "")) 
          (session/put! :world 
                        (heightmap/apply-heightmap 
                          (str "resources/public/img/heightmaps/" map ".png"))))
        (if (not (= rulefile ""))
          (do
            (session/put! :rule-text (slurp rulepath))
            (session/put! :rules (compiler/compile-file rulepath))))
        (if (not (= pause ""))
          (session/put! :pause pause))
        (layout/render "params.html" 
                       (merge (send-params) 
                              {:r rulefile
                               :h map
                               :message "Your parameters are saved, now look at your world"})))
      (catch Exception e
        (let [params (keywordize-keys (:form-params request))]
          (layout/render "params.html" 
                         (merge (send-params)
                                {:title "Choose your world" 
                                 :r (:ruleset params)
                                 :h (:heightmap params)
                                 :message "Your paramters are not saved"
                                 :error (str (.getName (.getClass e)) ": " (.getMessage e) "; " params)}
                                )))))))

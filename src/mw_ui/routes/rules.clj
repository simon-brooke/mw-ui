(ns mw-ui.routes.rules
  (:use clojure.walk
        compojure.core)
  (:require [hiccup.core :refer [html]]
            [mw-parser.bulk :as compiler]
            [mw-ui.layout :as layout]
            [mw-ui.util :as util]
            [mw-ui.render-world :as world]
            [noir.session :as session]))

(defn process-rules-request
  [request]
  (let [src (:src (keywordize-keys (:form-params request)))]
      (try 
        (cond src 
          (let [rules (compiler/compile-string src)]
            {:rule-text src
             :rules rules
             :message (str "Successfully compiled " 
                           (count rules) 
                           " rules")           })
          true {:rule-text (or 
                             (session/get :rule-text) 
                             (slurp "resources/rulesets/basic.txt"))
                :message "No rules found in request; loading defaults"})
        (catch Exception e 
          {:rule-text src
           :message "An error occurred during compilation"
           :error (str (.getName (.getClass e)) ": " (.getMessage e))}))))

(defn rules-page 
  "Request handler for the `rules` request. If the `request` contains a value
   for `:src`, treat that as rule source and try to compile it. If compilation 
   succeeds, stash the compiled rules and the rule text on the session, and 
   provide feedback; if not, provide feedback. 

   If `request` doesn't contain a value for `:src`, load basic rule source from
   the session or from `resources/rulesets/basic.txt` and pass that back."
  ([request]
    (let [processed (process-rules-request request)]
      (if (:rules processed)
        (session/put! :rules (:rules processed)))
      (if (:rule-text processed)
        (session/put! :rule-text (:rule-text processed)))
      (layout/render "rules.html" 
                     (merge {:title "Edit Rules"} processed))))
  ([]
    (rules-page nil)))

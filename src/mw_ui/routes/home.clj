(ns mw-ui.routes.home
  (:use compojure.core)
  (:require [hiccup.core :refer [html]]
            [mw-parser.bulk :as compiler]
            [mw-ui.layout :as layout]
            [mw-ui.util :as util]
            [mw-ui.render-world :as world]
            [noir.session :as session]))

(defn home-page []
  (layout/render "world.html" {:title "Watch your world grow" 
                               :content (html (world/render-world-table)) 
                               :seconds (or (session/get :seconds) 5) 
                               :maybe-refresh "refresh"}))

(defn world-page []
  (layout/render "world.html" {:title "Watch your world grow" 
                               :content (html (world/render-world-table)) 
                               :seconds (or (session/get :seconds) 5) 
                               :maybe-refresh "refresh"}))

(defn about-page []
  (layout/render "about.html" {:title "About MicroWorld" :content (util/md->html "/md/about.md")}))

(defn list-states []
  (sort
    (filter #(not (nil? %)) 
            (map #(first (rest (re-matches #"([0-9a-z-]+).png" (.getName %))))
                 (file-seq (clojure.java.io/file "resources/public/img/tiles"))))))

(defn docs-page []
  (layout/render "docs.html" {:title "Documentation"
                              :parser (util/md->html "/md/mw-parser.md")
                              :states (list-states)
                              :components ["mw-engine" "mw-parser" "mw-ui"]}))

(defn rules-page 
  ([request]
    (let [rule-text (:src request)
          error 
          (try 
            (do
              (if rule-text
                (session/put! :rules (compiler/compile-string rule-text)))
              (session/put! :rule-text rule-text)
              nil)
            (catch Exception e (.getMessage e)))]
      (layout/render "rules.html" {:title "Edit Rules" 
                                   :rule-text (or (session/get :rule-text) (slurp "resources/public/rulesets/basic.txt"))
                                   :error error})))
  ([]
    (rules-page nil)))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (GET "/docs"  [] (docs-page))
  (GET "/world"  [] (world-page))
  (GET "/rules" request (rules-page request))
  (POST "/rules" request (rules-page request)))

(ns mw-ui.routes.home
  (:use compojure.core
        [mw-ui.routes.rules :as rules]
        [mw-ui.routes.params :as params])
  (:require [hiccup.core :refer [html]]
            [mw-ui.layout :as layout]
            [mw-ui.util :as util]
            [mw-ui.render-world :as world]
            [noir.session :as session]))

(defn home-page []
  (layout/render "trusted-content.html" {:title "Welcome to MicroWorld" 
                              :content (util/md->html "/md/mw-ui.md")}))

(defn world-page []
  (layout/render "trusted-content.html" {:title "Watch your world grow" 
                               :content (html (world/render-world-table)) 
                               :pause (or (session/get :pause) 5) 
                               :maybe-refresh "refresh"}))

(defn about-page []
  (layout/render "trusted-content.html" {:title "About MicroWorld" :content (util/md->html "/md/about.md")}))

(defn list-states []
  (sort
    (filter #(not (nil? %)) 
            (map #(first (rest (re-matches #"([0-9a-z-]+).png" (.getName %))))
                 (file-seq (clojure.java.io/file "resources/public/img/tiles"))))))

(defn docs-page []
  (layout/render "docs.html" {:title "Documentation"
                              :parser (util/md->html "/md/mw-parser.md" )
                              :states (util/list-resources "resources/public/img/tiles" #"([0-9a-z-_]+).png")
                              :components ["mw-engine" "mw-parser" "mw-ui"]}))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (GET "/docs"  [] (docs-page))
  (GET "/world"  [] (world-page))
  (GET "/params" [] (params/params-page))
  (POST "/params" request (params/params-page request))
  (GET "/rules" request (rules/rules-page request))
  (POST "/rules" request (rules/rules-page request)))

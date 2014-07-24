(ns mw-ui.routes.home
  (:use clojure.walk
        compojure.core
        [mw-engine.utils :as engine-utils]
        [mw-ui.routes.rules :as rules]
        [mw-ui.routes.params :as params])
  (:require [hiccup.core :refer [html]]
            [mw-ui.layout :as layout]
            [mw-ui.util :as util]
            [mw-ui.render-world :as world]
            [noir.session :as session]
            [ring.util.response :as response]))

(defn home-page []
  (layout/render "trusted-content.html" {:title "Welcome to MicroWorld" 
                              :content (util/md->html "/md/mw-ui.md")}))

(defn inspect-page [request]
  (let [params (keywordize-keys (:params request))
        xs (:x params)
        ys (:y params)
        x (if (not (empty? xs)) (read-string xs) 0)
        y (if (not (empty? ys)) (read-string ys) 0)
        world (session/get :world)
        cell (engine-utils/get-cell world x y)
        state (:state params)]
    (cond state
      (do
        (session/put! :world (engine-utils/set-property world cell :state (keyword state)))
        (response/redirect "world"))
      true
      (layout/render "inspector.html"
                     {:title (format "Inspect cell at %d, %d" x y)
                      :content (html (world/render-inspector cell world))
                      :cell cell
                      :x (:x cell)
                      :y (:y cell)
                      :states (util/list-resources 
                                "/img/tiles" #"([0-9a-z-_]+).png")}))))
                    
(defn world-page []
  (layout/render "trusted-content.html" 
                 {:title "Watch your world grow" 
                 :world-selected "active" 
                 :content (html (world/render-world-table)) 
                 :pause (or (session/get :pause) 5) 
                 :maybe-refresh "refresh"}))

(defn about-page []
  (layout/render "trusted-content.html" 
                 {:title "About MicroWorld"
                  :about-selected "active"  
                  :content (util/md->html "/md/about.md")}))

(defn md-page [request]
  (let [params (keywordize-keys (:params request))
        content (or (:content params) "missing.md")]
    (layout/render "trusted-content.html" 
                   {:title "Welcome to MicroWorld" 
                    :content (util/md->html (str "/md/" content))})))

(defn list-states []
  (sort
    (filter #(not (nil? %)) 
            (map #(first (rest (re-matches #"([0-9a-z-]+).png" (.getName %))))
                 (file-seq (clojure.java.io/file "resources/public/img/tiles"))))))

(defn docs-page []
  (layout/render "docs.html" {:title "Documentation"
                              :parser (util/md->html "/md/mw-parser.md" )
                              :states (util/list-resources "/img/tiles" #"([0-9a-z-_]+).png")
                              :lessons (util/list-resources "/md/lesson-plans"  #"([0-9a-z-_]+).md")
                              :components ["mw-engine" "mw-parser" "mw-ui"]}))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (GET "/docs"  [] (docs-page))
  (GET "/world"  [] (world-page))
  (GET "/params" [] (params/params-page))
  (GET "/md" request (md-page request))
  (POST "/params" request (params/params-page request))
  (GET "/rules" request (rules/rules-page request))
  (POST "/rules" request (rules/rules-page request))
  (GET "/inspect" request (inspect-page request))
  (POST "/inspect" request (inspect-page request))
  )

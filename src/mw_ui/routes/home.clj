(ns mw-ui.routes.home
  (:use compojure.core)
  (:require [hiccup.core :refer [html]]
            [mw-ui.layout :as layout]
            [mw-ui.util :as util]
            [mw-ui.render-world :as world]
            [noir.session :as session]))

(defn home-page []
  (layout/render
    "home.html" {:title "Welcome to MicroWorld" :content (util/md->html "/md/docs.md")}))

(defn about-page []
  (layout/render "about.html" {:title "About MicroWorld" :content (util/md->html "/md/about.md")}))

(defn world-page []
  (layout/render "world.html" {:title "Watch your world grow" 
                               :content (html (world/render-world-table)) 
                               :seconds (or (session/get :seconds) 5) 
                               :maybe-refresh "refresh"}))

(defn docs-page []
  (layout/render "docs.html" {:title "Documentation"
                              :components ["mw-engine" "mw-parser" "mw-ui"]}))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (GET "/world" [] (world-page))
  (GET "/docs"  [] (docs-page)))

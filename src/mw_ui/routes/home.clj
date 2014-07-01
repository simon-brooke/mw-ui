(ns mw-ui.routes.home
  (:use compojure.core)
  (:require [mw-ui.layout :as layout]
            [mw-ui.util :as util]))

(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))

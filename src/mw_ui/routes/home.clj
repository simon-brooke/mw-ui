(ns ^{:doc "Routes which serve the main pages of the application."
      :author "Simon Brooke"}
  mw-ui.routes.home
  (:use clojure.walk
        compojure.core)
  (:require [clojure.pprint :only [pprint]]
            [hiccup.core :refer [html]]
            [mw-engine.utils :as engine-utils]
            [mw-ui.layout :as layout]
            [mw-ui.render-world :as world]
            [mw-ui.routes.load :as load]
            [mw-ui.routes.rules :as rules]
            [mw-ui.routes.params :as params]
            [mw-ui.routes.save :as save]
            [mw-ui.util :as util]
            [noir.io :as io]
            [noir.session :as session]
            [ring.util.response :as response]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;; mw-ui: a servlet user/visualisation interface for MicroWorld.
;;;;
;;;; This program is free software; you can redistribute it and/or
;;;; modify it under the terms of the GNU General Public License
;;;; as published by the Free Software Foundation; either version 2
;;;; of the License, or (at your option) any later version.
;;;;
;;;; This program is distributed in the hope that it will be useful,
;;;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;;;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;;;; GNU General Public License for more details.
;;;;
;;;; You should have received a copy of the GNU General Public License
;;;; along with this program; if not, write to the Free Software
;;;; Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
;;;; USA.
;;;;
;;;; Copyright (C) 2014 Simon Brooke
;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn list-states []
  (sort
    (filter #(not (nil? %))
            (map #(first (rest (re-matches #"([0-9a-z-]+).png" (.getName %))))
                 (file-seq (clojure.java.io/file "resources/public/img/tiles"))))))


(defn about-page []
  (layout/render "trusted-content.html"
                 {:title "About MicroWorld"
                  :about-selected "active"
                  :content (util/md->html "/md/about.md")
                  :version (System/getProperty "mw-ui.version")}))

(defn docs-page []
  (layout/render "docs.html" {:title "Documentation"
                              :parser (util/md->html "/md/mw-parser.md" )
                              :states (util/list-resources "/img/tiles" #"([0-9a-z-_]+).png")
                              :lessons (util/list-resources "/md/lesson-plans"  #"([0-9a-z-_]+).md")
                              :components ["mw-engine" "mw-parser" "mw-ui"]
                              :version (System/getProperty "mw-ui.version")}))

(defn home-page []
  "Render the home page."
  (layout/render "trusted-content.html" {:title "Welcome to MicroWorld"
                              :content (util/md->html "/md/mw-ui.md")
                              :version (System/getProperty "mw-ui.version")}))

(defn inspect-page [request]
  "Open an inspector on the cell at the co-ordinates specified in this request"
  (let [params (keywordize-keys (:params request))
        xs (:x params)
        ys (:y params)
        x (if (seq xs) (read-string xs) 0)
        y (if (seq ys) (read-string ys) 0)
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

(defn md-page
  "Render the markdown page specified in this request, if any. Probably undesirable,
   should be removed."
  [request]
  (let [params (keywordize-keys (:params request))
        content (or (:content params) "missing.md")]
    (layout/render "trusted-content.html"
                   {:title "Welcome to MicroWorld"
                    :content (util/md->html (str "/md/" content))})))

(defn world-page []
  "Render the world in the current session (or a default one if none)."
  (layout/render "trusted-content.html"
                 {:title "Watch your world grow"
                 :world-selected "active"
                 :content (html (world/render-world-table))
                 :pause (or (session/get :pause) 5)
                 :maybe-refresh "refresh"}))


(defroutes home-routes
  (GET  "/" [] (home-page))
  (GET  "/about" [] (about-page))
  (GET  "/docs"  [] (docs-page))
  (GET  "/inspect" request (inspect-page request))
  (POST "/inspect" request (inspect-page request))
  (GET  "/load" [] (load/load-page))
  (POST "/load" request (load/load-page request))
  (GET  "/md" request (md-page request))
  (GET  "/params" [] (params/params-page))
  (POST "/params" request (params/params-page request))
  (GET  "/rules" request (rules/rules-page request))
  (POST "/rules" request (rules/rules-page request))
  (GET  "/saved-map.mwm" [] (save/save-page))
  (GET  "/world"  [] (world-page))
  )

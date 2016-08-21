(ns ^{:doc "Route which serves and handles the parameters page."
      :author "Simon Brooke"}
  mw-ui.routes.params
  (:use clojure.walk
        clojure.java.io
        compojure.core)
  (:require [hiccup.core :refer [html]]
            [mw-engine.heightmap :as heightmap]
            [mw-parser.bulk :as compiler]
            [mw-ui.layout :as layout]
            [mw-ui.util :as util]
            [mw-ui.render-world :as world]
            [noir.io :as io]
            [noir.session :as session]))

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


(defn- send-params []
  {:title "Choose your world"
   :heightmaps (util/list-resources "/img/heightmaps" #"([0-9a-z-_]+).png")
   :pause (or (session/get :pause) 5)
   :rulesets (util/list-resources "/rulesets" #"([0-9a-z-_]+).txt")
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
            rulepath (str "/rulesets/" rulefile ".txt")]
        (if (not= map "")
          (session/put! :world
                        (heightmap/apply-heightmap
                          (io/get-resource (str "/img/heightmaps/" map ".png")))))
        (when (not= rulefile "")
          (session/put! :rule-text (io/slurp-resource rulepath))
          (session/put! :rules (compiler/compile-file (io/get-resource rulepath))))
        (if (not= pause "")
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

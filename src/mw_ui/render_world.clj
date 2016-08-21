(ns ^{:doc "Render the state of the world as an HTML table."
      :author "Simon Brooke"}
  mw-ui.render-world
  (:require [clojure.java.io :as jio]
            [mw-engine.core :as engine]
            [mw-engine.world :as world]
            [mw-engine.heightmap :as heightmap]
            [mw-parser.bulk :as compiler]
            [hiccup.core :refer [html]]
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


(defn format-css-class [statekey]
  "Format this statekey, assumed to be a keyword indicating a state in the
   world, into a CSS class"
  (subs (str statekey) 1))


(defn format-image-path
  "Render this statekey, assumed to be a keyword indicating a state in the
   world, into a path which should recover the corresponding image file."
  [statekey]
  (format "img/tiles/%s.png" (format-css-class statekey)))


(defn format-mouseover [cell]
  (str cell))


(defn render-cell
  "Render this world cell as a Hiccup table cell."
  [cell]
  (let [state (:state cell)]
    [:td {:class (format-css-class state) :title (format-mouseover cell)}
     [:a {:href (format "inspect?x=%d&y=%d" (:x cell) (:y cell))}
      [:img {:alt (:state cell) :src (format-image-path state)}]]]))


(defn render-world-row
  "Render this world row as a Hiccup table row."
  [row]
  (apply vector (cons :tr (map render-cell row))))


(defn render-world-table
  "Render the world implied by the current session as a complete HTML table in a DIV."
  []
  (let [world (or (session/get :world)
                  (heightmap/apply-heightmap
                      (io/get-resource "/img/heightmaps/small_hill.png")))
        rules (or (session/get :rules)
                  (do (session/put! :rules
                                    (compiler/compile-file
                                      (io/get-resource "/rulesets/basic.txt")))
                    (session/get :rules)))
        generation (inc (or (session/get :generation) 0))
        w2 (engine/transform-world world rules)
        ]
    (session/put! :world w2)
    (session/put! :generation generation)
    [:div {:class "world"}
      (apply vector
                 (cons :table
                       (map render-world-row w2)))
      [:p
       (str "Generation " generation)]]))


(defn render-inspector
  "Render in Hiccup format the HTML content of an inspector on this cell."
  [cell table]
  [:table {:class "music-ruled"}
   [:tr
    [:td {:colspan 2 :style "text-align: center;"}
     [:img {:src (str "img/tiles/" (name (:state cell)) ".png")
            :width 64
            :height 64}]]]
   [:tr [:th "Key"][:th "Value"]]
   (map #(vector :tr (vector :th %)(vector :td (cell %))) (keys cell))])



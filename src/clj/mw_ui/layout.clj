(ns ^{:doc "Layout content as HTML."
      :author "Simon Brooke"}
  mw-ui.layout
  (:require [selmer.parser :as parser]
            [clojure.string :as s]
            [ring.util.response :refer [content-type response]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [compojure.response :refer [Renderable]]))

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


(def template-path "templates/")

(parser/add-tag! :csrf-field (fn [_ _] (anti-forgery-field)))

(deftype RenderableTemplate [template params]
  Renderable
  (render [this request]
    (content-type
      (->> (assoc (merge params {:version (System/getProperty "mw-ui.version")})
                  (keyword (s/replace template #".html" "-selected")) "active"
                  :servlet-context
                  (if-let [context (:servlet-context request)]
                    (.getContextPath context)))
        (parser/render-file (str template-path template))
        response)
      "text/html; charset=utf-8")))


(defn render [template & [params]]
  (RenderableTemplate. template params))


(ns ^{:doc "Utility functions used by other namespaces in this package."
      :author "Simon Brooke"}
  mw-ui.util
  (:require [clojure.java.io :refer [file]]
            [noir.io :as io]
            [noir.session :as session]
            [markdown.core :as md]))

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


(defn md->html
  "reads a markdown file from public/md and returns an HTML string"
  [filename]
  (->>
    (io/slurp-resource filename)
    (md/md-to-html-string)))

;; TODO: The reason we can't list files in a jar file, and what to do about it,
;; is here. Too tired to fix this tonight.
;; https://stackoverflow.com/questions/46488466/clojure-list-subfolders-in-resources-in-uberjar

(defn list-resources 
  "List resource files matching `pattern` in `directory`."
  [directory pattern]
  (let
    [path (str (io/resource-path) directory)]
    (session/put! :list-resources-path path)
    (sort
      (remove nil?
            (map #(first (rest (re-matches pattern (.getName %))))
                 (file-seq (file path)))))))

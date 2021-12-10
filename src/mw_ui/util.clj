(ns ^{:doc "Utility functions used by other namespaces in this package."
      :author "Simon Brooke"}
 mw-ui.util
  (:require [clojure.java.io :refer [file]]
            [clojure.string :refer [starts-with?]]
            [markdown.core :as md]
            [noir.io :as io]
            [noir.session :as session]
            [taoensso.timbre :as timbre]))

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

(def running-from-filesystem (atom true))

(def compile-time-resources
  "The resources which were visible at compile time. If we are running from
   a JAR file, it is highly likely that these are all the resources available
   at run time."
  (let [n (count (io/resource-path))]
    (remove nil?
            (map #(let [s (str %)]
                    (when (> (count s) n)
                      (subs s 56)))
                 (file-seq (file (io/resource-path)))))))


(defn md->html
  "reads a markdown file from public/md and returns an HTML string"
  [filename]
  (->>
   (io/slurp-resource filename)
   (md/md-to-html-string)))


(defn cache-seq-match
  "Do the same processing that list-resources does on names fetched from
   the file system, except on the resource list cached at compile time."
  [path pattern]
  (let [n (count path)]
    (remove nil?
            (map #(when (> (count %) n)
                    (let [name (subs % n)]
                      (first (rest (re-matches pattern name)))))
                 (filter #(starts-with? % path) 
                         compile-time-resources)))))


(defn list-resources
  "List resource files matching `pattern` in `directory`."
  [directory pattern]
  (let
   [path (str (io/resource-path) directory)]
    (session/put! :list-resources-path path)
    (try
      (sort
       (remove nil?
               (if @running-from-filesystem
                 (map #(first (rest (re-matches pattern (.getName %))))
                      (file-seq (file path)))
                 (cache-seq-match directory pattern))))
      (catch Exception any
        (timbre/log (str "Not running from filesystem?"
                         (.getName (.getClass any))))
        (reset! running-from-filesystem false)
        (cache-seq-match directory pattern)))))

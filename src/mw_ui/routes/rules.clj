(ns ^{:doc "Route which serves and handles the rules page."
      :author "Simon Brooke"}
  mw-ui.routes.rules
  (:use clojure.walk
        compojure.core)
  (:require [hiccup.core :refer [html]]
            [mw-parser.bulk :as compiler]
            [mw-ui.layout :as layout]
            [mw-ui.util :as util]
            [mw-ui.render-world :as world]
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


(defn process-rules-request
  [request]
  (let [src (:src (keywordize-keys (:form-params request)))]
      (try
        (cond src
          (let [rules (compiler/compile-string src)]
            {:rule-text src
             :rules rules
             :message (str "Successfully compiled "
                           (count rules)
                           " rules")           })
          true {:rule-text (or
                             (session/get :rule-text)
                             (io/slurp-resource "/rulesets/basic.txt"))
                :message "No rules found in request; loading defaults"})
        (catch Exception e
          {:rule-text src
           :message "An error occurred during compilation"
           :error (str (.getName (.getClass e)) ": " (.getMessage e))}))))


(defn rules-page
  "Request handler for the `rules` request. If the `request` contains a value
   for `:src`, treat that as rule source and try to compile it. If compilation
   succeeds, stash the compiled rules and the rule text on the session, and
   provide feedback; if not, provide feedback.

   If `request` doesn't contain a value for `:src`, load basic rule source from
   the session or from `resources/rulesets/basic.txt` and pass that back."
  ([request]
    (let [processed (process-rules-request request)]
      (if (:rules processed)
        (session/put! :rules (:rules processed)))
      (if (:rule-text processed)
        (session/put! :rule-text (:rule-text processed)))
      (layout/render "rules.html"
                     (merge {:title "Edit Rules"} processed))))
  ([]
    (rules-page nil)))

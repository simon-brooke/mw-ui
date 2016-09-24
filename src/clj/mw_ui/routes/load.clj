(ns ^{:doc "Route which handles the upload of worlds/rules from the client."
      :author "Simon Brooke"}
  mw-ui.routes.load
  (:use clojure.walk
        compojure.core)
  (:require [hiccup.core :refer [html]]
            [noir.io :as io]
            [noir.session :as session]
            [ring.util.response :as response]
            [mw-ui.layout :as layout]
            ))

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


(defn- upload [file]
  (io/upload-file "/tmp/" file)
  (cond
   (session/put! :world
                (with-open [eddi (java.io.FileReader. (:tempfile file))] (read)))
    (str "Successfully loaded your world from " (:filename file))))


(defn load-page
  "If no args, show the load form; with args, load a world file from the client.

   *NOTE* that this reads a Clojure form from an untrusted client and should almost
   certainly NOT be enabled on a public-facing site, especially not on the Internet.

   *TODO* doesn't work yet."
  ([]
   (load-page nil))
  ([request]
     (let [params (keywordize-keys (:params request))
           file (:file request)]
       (try
         (layout/render "load.html"
                        {:title "Load World"
                         :message (upload file)})

         (catch Exception any
               (layout/render "load.html"
                            {:title "Load World"
                             :message "Failed to load your world"
                             :error (str (.getName (.getClass any)) ": " (.getMessage any))}))))))


(ns ^{:doc "Route which handles the saving of world state the client."
      :author "Simon Brooke"}
  mw-ui.routes.save
  (:require [clojure.pprint :refer [pprint]]
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


(defn save-page []
  "Save the current world to the browser, using our own custom mime-type in
   an attempt to prevent the browser trying to do anything clever with it.
   Note that it is saved as a raw Clojure data structure, not as XML or
   any proprietary format."
  (response/header
   (response/response
    (with-out-str (pprint  (session/get :world))))
    "Content-Type" "application/journeyman-mwm; charset=utf-8"))


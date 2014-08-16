(ns mw-ui.routes.save
  (:require [clojure.pprint :as pretty :only [pprint]]
            [noir.session :as session]
            [ring.util.response :as response]))

(defn save-page []
  "Save the current world to the browser, using our own custom mime-type in
   an attempt to prevent the browser trying to do anything clever with it.
   Note that it is saved as a raw Clojure data structure, not as XML or
   any proprietary format."
  (response/header
   (response/response
    (with-out-str (pretty/pprint  (session/get :world))))
    "Content-Type" "application/journeyman-mwm; charset=utf-8"))


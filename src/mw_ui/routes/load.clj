(ns mw-ui.routes.load
  (:use clojure.walk
        compojure.core)
  (:require [hiccup.core :refer [html]]
            [noir.io :as io]
            [noir.session :as session]
            [ring.util.response :as response]))


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


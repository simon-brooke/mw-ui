(ns mw-ui.util
  (:require [noir.io :as io]
            [noir.session :as session]
            [markdown.core :as md]))

(defn md->html
  "reads a markdown file from public/md and returns an HTML string"
  [filename]
  (->>
    (io/slurp-resource filename)
    (md/md-to-html-string)))

(defn list-resources [directory pattern]
  "List resource files matching `pattern` in `directory`."
  (let 
    [path (str (io/resource-path) directory)]
    (session/put! :list-resources-path path)
    (sort
      (filter #(not (nil? %)) 
            (map #(first (rest (re-matches pattern (.getName %))))
                 (file-seq (clojure.java.io/file path)))))))

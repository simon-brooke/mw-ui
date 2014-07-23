(ns mw-ui.render-world
  (:require [mw-engine.core :as engine]
            [mw-engine.world :as world]
            [mw-engine.heightmap :as heightmap]
            [mw-engine.natural-rules :as rules]
            [mw-parser.bulk :as compiler]
            [hiccup.core :refer [html]]
            [noir.session :as session]))


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
     [:a {:href (format "inspect?x=%d&amp;y=%d" (:x cell) (:y cell))}       
      [:img {:alt (:state cell) :src (format-image-path state)}]]]))

(defn render-world-row
  "Render this world row as a Hiccup table row."
  [row]
  (apply vector (cons :tr (map render-cell row))))

(defn render-world-table
  "Render the world implied by the session as a complete HTML page."
  []
  (let [world (or (session/get :world)
                  (engine/transform-world
                   (heightmap/apply-heightmap
                     "resources/public/img/heightmaps/small_hill.png"
                     ;; "resources/public/img/heightmaps/great_britain_and_ireland_small.png"
                     )
                   rules/init-rules))
        rules (or (session/get :rules) 
                  (do (session/put! :rules (compiler/compile-file "resources/rulesets/basic.txt"))
                    (session/get :rules)))
        generation (+ (or (session/get :generation) 0) 1)
        w2 (engine/transform-world world rules)
        ]
    (session/put! :world w2)
    (session/put! :generation generation)
    [:div {:class "world"}
     
      (apply vector
                 (cons :table
                       (map render-world-row w2)))
      [:p (str "Generation " generation)]]))

(defn render-world
  "Render the world implied by the session as a complete HTML page."
  []
  (html
   [:html
    [:head
     [:title "MicroWorld demo"]
     [:link {:media "only screen and (max-device-width: 480px)" :href "css/phone.css" :type  "text/css" :rel "stylesheet"}]
     [:link {:media "only screen and (min-device-width: 481px) and (max-device-width: 1024px)" :href "css/tablet.css" :type "text/css" :rel "stylesheet"}]
     [:link {:media "screen and (min-device-width: 1025px)" :href "css/standard.css" :type "text/css" :rel "stylesheet"}]
     [:link {:media "print" :href "css/print.css" :type "text/css" :rel "stylesheet"}]
     [:link {:href "css/states.css" :type "text/css" :rel "stylesheet"}]
     [:meta {:http-equiv "refresh" :content "5"}]]
    [:body
     (render-world-table)
     ]]))

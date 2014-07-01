(ns mw-ui.render-world
  (:require [mw-engine.core :as engine]
            [mw-engine.world :as world]
            [mw-engine.natural-rules :as rules]
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

(defn render-cell 
  "Render this world cell as a Hiccup table cell."
  [cell]
  (let [state (:state cell)]
    [:td {:class (format-css-class state)} 
            [:img {:alt (world/format-cell cell) :img (format-image-path state)}]]))

(defn render-world-row 
  "Render this world row as a Hiccup table row."
  [row]
  (apply vector (cons :tr (map render-cell row))))

(defn render-world 
  "Render this world as a complete HTML page."
  []
  (let [world (or (session/get :world) (world/make-world 20 20))
        rules (or (session/get :rules) rules/natural-rules)
        w2 (engine/transform-world world rules)]
    (session/put! :world w2)
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
        [:h1 "MicroWorld"]
        (apply vector 
               (cons :table
                     (map render-world-row w2)))]])))
      
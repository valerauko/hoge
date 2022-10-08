(ns hoge.views
  (:require [re-frame.core :as rf]
            [hoge.subs :as subs]))

(defn main
  [& _]
  (js/console.log "main" "called")
  (let [title @(rf/subscribe [::subs/title])]
    (js/console.log "title" title)
    [:h1
     title]))

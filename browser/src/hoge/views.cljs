(ns hoge.views
  (:require [re-frame.core :as rf]
            [hoge.subs :as subs]))

(defn main
  [& _]
  (js/console.log "main" "called")
  (let [route @(rf/subscribe [::subs/current-route])
        title @(rf/subscribe [::subs/title])]
    (js/console.log "title" title)
    (js/console.log (clj->js route))
    [:div
     [:h1
      title]
     (if-let [view (-> route :data :view)]
       [view]
       [:p "Requested page not found"])]))

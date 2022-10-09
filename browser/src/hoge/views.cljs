(ns hoge.views
  (:require [re-frame.core :as rf]
            [hoge.subs :as subs]))

(defn main
  [& _]
  (let [route @(rf/subscribe [::subs/current-route])
        title @(rf/subscribe [::subs/title])]
    [:div
     [:h1
      title]
     [:nav
      [:a
       {:href "/a"}
       "A"]
      [:a
       {:href "/b"}
       "B"]]
     (if-let [view (-> route :data :view)]
       [view]
       [:p "Requested page not found"])]))

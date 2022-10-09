(ns hoge.views
  (:require [re-frame.core :as rf]
            [hoge.subs :as subs]))

(defn main
  [& _]
  (let [title @(rf/subscribe [::subs/title])]
    [:div
     [:h1
      title]
     [:nav
      [:a
       {:href "/a"}
       "A"]
      " "
      [:a
       {:href "/b"}
       "B"]]
     (let [route @(rf/subscribe [::subs/current-route])
           view (-> route :data :view)]
       (if view
         [view]
         [:p "Requested page not found"]))]))

(ns hoge.routes
  (:require [re-frame.core :as rf]
            [reitit.core]
            [reitit.frontend]
            [reitit.frontend.easy :as easy]
            [hoge.events :as events]))

(def router
  (reitit.frontend/router
   ["/"
    ["a"
     {:name ::a
      :view (fn [_] [:p "Welcome to A"])}]
    ["b"
     {:name ::b
      :view (fn [_] [:p "Welcome to B"])}]]))

(defn ^:export navigate
  [path]
  (when-let [match (reitit.core/match-by-path router path)]
    (js/console.log "navigate" (clj->js match))
    (rf/dispatch [::events/navigated match])))

(defn on-navigate
  [new-match]
  (when new-match
    (rf/dispatch [::events/navigated new-match])))

(defn ^:dev/after-load start-router
  []
  (easy/start!
   router
   on-navigate
   {:use-fragment false}))

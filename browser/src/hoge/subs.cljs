(ns hoge.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::title
 :title)

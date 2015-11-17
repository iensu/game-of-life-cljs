(ns ^:figwheel-always gol-cljs.core
  (:require [gol-cljs.view :as v]))

(enable-console-print!)

(let [grid (v/generate-grid {:w 20 :h 20})
      grid' (v/update-cell grid [9 9] :alive)]
  (v/draw-grid! grid'))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

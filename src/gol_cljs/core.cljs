(ns ^:figwheel-always gol-cljs.core
  (:require [gol-cljs.grid :as grid]
            [gol-cljs.game :as game]
            [gol-cljs.worlds :as worlds]
            [cljs.core.async :refer [chan >! <! timeout]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(enable-console-print!)

(def config {:size [20 20]
             :speed 500})

(defn within-bounds?
  [[max-x max-y] [x y]]
  (and (< -1 x max-x)
       (< -1 y max-y)))

(defn alive-coords-t
  [size]
  (comp (filter #(= :alive (:state %)))
        (map :pos)
        (filter (partial within-bounds? size))))

(defn world->grid
  [size world]
  (let [alives (into #{} (alive-coords-t size) world)]
    (grid/generate-grid size alives)))

(defn world-update-channel
  [start-world]
  (let [c (chan)]
    (go-loop [w start-world]
      (>! c w)
      (<! (timeout (:speed config)))
      (recur (game/tick w)))
    c))

(defn draw-grid!
  [g]
  (let [app-elem (.getElementById js/document "app")]
    (->> g
         grid/grid->html
         (aset app-elem "innerHTML"))))

(defn run-game
  [{:keys [world size]}]
  (let [world-channel (world-update-channel world)]
    (go-loop []
      (let [w (<! world-channel)]
        (draw-grid! (world->grid size w))
        (recur)))))

(run-game worlds/world-5)

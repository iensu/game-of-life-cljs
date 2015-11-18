(ns ^:figwheel-always gol-cljs.core
  (:require [gol-cljs.view :refer [generate-grid
                                   draw-grid!]]
            [gol-cljs.game :refer [tick
                                   Cell]]
            [cljs.core.async :refer [chan >! <! timeout]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(enable-console-print!)

(def config {:size [20 20]
             :speed 500})

(defn within-bounds?
  [[max-x max-y] [x y]]
  (and (< -1 x max-x)
       (< -1 y max-y)))

(def alive-coords-t (comp (filter #(= :alive (:state %)))
                          (map :pos)
                          (filter (partial within-bounds? (:size config)))))

(defn world->grid
  ([w grid-size]
   (let [alives (into #{} alive-coords-t w)]
     (generate-grid (:size config) alives)))
  ([w]
   (world->grid w (:size config))))

(defn world-update-channel
  [start-world]
  (let [c (chan)]
    (go-loop [w start-world]
      (>! c w)
      (<! (timeout (:speed config)))
      (recur (tick w)))
    c))

(defn run-game
  [world]
  (let [world-channel (world-update-channel world)]
    (go-loop []
      (let [w (<! world-channel)]
        (draw-grid! (world->grid w))
        (recur)))))

(defn generate-world
  [& args]
  (into #{} (map #(Cell. % :alive) args)))

(def start-world (generate-world [9 8] [9 9] [9 10]))
(def start-world-2 (generate-world [10 10]
                                   [9 11] [10 11] [11 11]
                                   [9 12] [10 12] [11 12]))
(def start-world-3 (generate-world [ 7  7] [ 8  7] [ 9  7] [10  7] [11  7]
                                   [ 7  8]         [ 9  8]         [11  8]
                                   [ 7  9]         [ 9  9]         [11  9]
                                   [ 7 10]         [ 9 10]         [11 10]
                                   [ 7 11] [ 8 11] [ 9 11] [10 11] [11 11]))
(run-game start-world-3)

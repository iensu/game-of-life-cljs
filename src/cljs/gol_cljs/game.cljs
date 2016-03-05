(ns ^:figwheel-load gol-cljs.game
  (:require [clojure.set :refer [union]]))

(defrecord Cell [pos state])

(defn neighbor-coordinates
  [{:keys [pos]}]
  (let [x (first pos)
        y (second pos)]
    (into #{}
          (for [dx [-1 0 1]
                dy [-1 0 1]
                :let [pos' [(+ x dx) (+ y dy)]]
                :when (not= pos pos')]
            pos'))))

(defn alive? [c] (= :alive (:state c)))

(defn alive-neighbors
  [ns]
  (comp (filter #(alive? %))
        (filter #(contains? ns (:pos %)))))

(defmulti will-live? :state)

(defmethod will-live? :alive
  [c w]
  (let [ncoords (neighbor-coordinates c)
        ncount (count (into #{} (alive-neighbors ncoords) w))]
    (< 1 ncount 4)))

(defmethod will-live? :dead
  [c w]
  (let [ncoords (neighbor-coordinates c)
        ncount (count (into #{} (alive-neighbors ncoords) w))]
    (= ncount 3)))

(defn dead-neighbors
  [w]
  (let [alive-coords (into #{}
                           (comp (filter alive?)
                                 (map :pos))
                           w)
        dead? (complement (partial contains? alive-coords))
        dead-neighbors-t (comp (mapcat neighbor-coordinates)
                               (filter dead?)
                               (map #(Cell. % :dead)))]
    (into #{} dead-neighbors-t w)))

(defn tick
  [w]
  (let [revive (fn [c] (assoc c :state :alive))
        do-tick (comp (filter (fn [c]
                                (will-live? c w)))
                      (map revive))]
    (into #{} do-tick (union w
                             (dead-neighbors w)))))

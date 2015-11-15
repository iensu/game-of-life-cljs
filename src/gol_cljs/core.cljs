(ns ^:figwheel-always gol-cljs.core)

(enable-console-print!)

(defn create-cell
  [state x y]
  {:state state :position [x y]})

(defn within-bounds?
  [grid x y]
  (and (< 0 x (count grid))
       (< 0 y (count (nth grid 0)))))

(defn get-cell
  [grid x y]
  (if (within-bounds? grid x y)
    (-> grid
        (nth x)
        (nth y))
    nil))

(defn update-cell
  [grid {:keys [position] :as cell}]
  (let [[x y] position]
    (if (within-bounds? grid x y)
      (update-in grid [x y] (fn [_] cell))
      grid)))

(defn generate-grid
  [{:keys [w h]}]
  (let [column (fn [c] (into [] (for [r (range h)]
                                 (create-cell :dead c r))))]
    (into [] (map column (range w)))))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

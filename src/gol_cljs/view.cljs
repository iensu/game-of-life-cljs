(ns ^:figwheel-load gol-cljs.view)

(defn within-bounds?
  [grid [x y]]
  (and (< -1  x (count grid))
       (< -1 y (count (nth grid 0)))))

(defn get-cell
  [grid [x y]]
  (if (within-bounds? grid [x y])
    (-> grid
        (nth x)
        (nth y))
    nil))

(defn update-cell
  [grid position cell]
  (let [[x y] position]
    (if (within-bounds? grid position)
      (update-in grid position (fn [_] cell))
      grid)))

(defn generate-grid
  [{:keys [w h]}]
  (let [column (fn [c] (into []
                            (for [r (range h)]
                              :dead)))]
    (into [] (map column (range w)))))

(defn create-element
  [{:keys [type classes]}]
  (let [e (.createElement js/document (or type "div"))]
    (when classes
      (doseq [c classes]
        (.. e -classList (add c))))
    e))

(defn create-cell-element
  [grid-size state]
  (create-element {:classes [(str "cell-1-" grid-size) (name state)]}))

(defn create-grid-element
  [grid]
  (let [grid-size (count grid)
        grid-elem (create-element {:classes ["grid"]})
        columns (for [c grid
                      :let [column (create-element {:classes [(str "col-1-" grid-size)]})
                            cells (map (partial create-cell-element grid-size) c)]]
                  (do (doseq [e cells] (.appendChild column e) cells)
                      column))]
    (doseq [c columns] (.appendChild grid-elem c))
    grid-elem))

(defn draw-grid!
  [grid]
  (let [grid-elem (-> (.getElementsByClassName js/document "grid")
                      (aget 0))]
    (aset grid-elem "innerHTML" (-> grid
                                    create-grid-element
                                    (aget "innerHTML")))
    grid-elem))

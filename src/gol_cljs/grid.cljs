(ns ^:figwheel-load gol-cljs.grid)

(defn generate-grid
  ([config]
   (generate-grid config #{}))
  ([[w h] alive-coords]
   (let [column (fn [c] (into []
                             (for [r (range h)]
                               (if (contains? alive-coords [c r])
                                 :alive
                                 :dead))))]
     (into [] (map column (range w))))))

(defn grid->html
  [grid]
  (let [size (count grid)
        cell (fn [c] (str "<div class=\"cell-1-" size " " (name c) "\"></div>"))
        column (fn [col] (str "<div class=\"col-1-" size "\">"
                             (apply str (for [c col] (cell c)))
                             "</div>"))]
    (str "<div class=\"grid\">"
         (apply str (for [col grid] (column col)))
         "</div>")))

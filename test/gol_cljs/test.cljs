(ns ^:figwheel-always gol-cljs.test
  (:require [cljs.test :refer-macros [run-all-tests]]))

(enable-console-print!)

(defn ^:export run
  []
  (run-all-tests #"gol-cljs.*-tests"))

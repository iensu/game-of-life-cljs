(ns ^:figwheel-always gol-cljs.test
  (:require [cljs.test :refer-macros [run-tests is testing deftest]]))

(enable-console-print!)

(defn ^:export run
  []
  (run-tests 'gol-cljs.core-tests))

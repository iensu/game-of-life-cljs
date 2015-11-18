(ns ^:figwheel-load gol-cljs.view-tests
  (:require [gol-cljs.view :refer [generate-grid
                                   get-cell
                                   update-cell]]
            [cljs.test :refer-macros [deftest is testing]]))

(deftest view-tests

  (testing "grid generation"

    (testing "dimensions 0x0 should return an empty list"
      (is (= [] (generate-grid [0 0]))))

    (testing "dimsions 10x10 should return a two-dimensional grid of dimesions 10x10"
      (let [grid (generate-grid [10 10])]
        (is (= 10 (count grid)))
        (is (= 10 (count (get grid 0)))))))

  (testing "grid cells"

    (testing "get cell in position"
      (let [grid [[:dead :alive]
                  [:alive :dead]]]
        (is (= :alive (get-cell grid [0 1])))
        (is (= :alive (get-cell grid [1 0])))))

    (testing "should return nil if out of bounds"
      (let [grid (generate-grid [5 5])]
        (is (= nil (get-cell grid [-1 -1])))
        (is (= nil (get-cell grid [5 5])))))

    (testing "should update the cell"
      (let [grid (generate-grid [2 2])
            grid' [[:dead :dead]
                   [:dead :alive]]]
        (is (= grid' (update-cell grid [1 1] :alive)))))

    (testing "should return the original grid if out of bounds"
      (let [grid (generate-grid [2 2])]
        (is (= grid (update-cell grid [-1 -1] :alive)))
        (is (= grid (update-cell grid [3 3] :alive)))))))

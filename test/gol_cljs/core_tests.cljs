(ns ^:figwheel-load gol-cljs.core-tests
  (:require [gol-cljs.core :refer [generate-grid
                                   get-cell
                                   update-cell]]
            [cljs.test :refer-macros [deftest is testing]]))

(deftest grid-generation-tests

  (testing "dimensions 0x0 should return an empty list"
    (is (= [] (generate-grid {:w 0 :h 0}))))

  (testing "dimsions 10x10 should return a two-dimensional grid of dimesions 10x10"
    (let [grid (generate-grid {:w 10 :h 10})]
      (is (= 10 (count grid)))
      (is (= 10 (count (nth grid 0)))))))


(deftest grid-tests

  (testing "get cell"

    (testing "get cell in position"
      (let [grid [[:dead :alive]
                  [:alive :dead]]]
        (is (= :dead (get-cell grid [0 0])))
        (is (= :alive (get-cell grid [1 1])))))

    (testing "should return nil if out of bounds"
      (let [grid (generate-grid {:w 5 :h 5})]
        (is (= nil (get-cell grid [-1 -1])))
        (is (= nil (get-cell grid [5 5]))))))

  (testing "update cell"

    (testing "should update the cell"
      (let [grid (generate-grid {:w 2 :h 2})
            grid' [[:dead :dead]
                   [:dead :alive]]]
        (is (= grid' (update-cell grid [1 1] :alive)))))

    (testing "should return the original grid if out of bounds"
      (let [grid (generate-grid {:w 2 :h 2})]
        (is (= grid (update-cell grid [-1 -1] :alive)))
        (is (= grid (update-cell grid [3 3] :alive)))))))

(ns ^:figwheel-load gol-cljs.grid-tests
  (:require [gol-cljs.grid :refer [generate-grid
                                   grid->html]]
            [cljs.test :refer-macros [deftest is testing]]))

(deftest grid-tests

  (testing "grid generation"

    (testing "dimensions 0x0 should return an empty list"
      (is (= [] (generate-grid [0 0]))))

    (testing "dimensions 10x10 should return a two-dimensional grid of dimesions 10x10"
      (let [grid (generate-grid [10 10])]
        (is (= 10 (count grid)))
        (is (= 10 (count (get grid 0))))))

    (testing "provided alive coordinates should be marked as alive"
      (let [grid (generate-grid [2 2] #{[0 0] [1 1]})
            expected [[:alive :dead]
                      [:dead :alive]]]
        (is (= expected grid)))))

  (testing "grid to html"
    (let [g (generate-grid [2 2] #{[1 1]})
          expected (str "<div class=\"grid\">"
                        "<div class=\"col-1-2\">"
                        "<div class=\"cell-1-2 dead\"></div>"
                        "<div class=\"cell-1-2 dead\"></div>"
                        "</div>"
                        "<div class=\"col-1-2\">"
                        "<div class=\"cell-1-2 dead\"></div>"
                        "<div class=\"cell-1-2 alive\"></div>"
                        "</div>"
                        "</div>")]
      (is (= expected (grid->html g))))))

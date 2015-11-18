(ns ^:figwheel-load gol-cljs.game-tests
  (:require [gol-cljs.game :refer [tick
                                   neighbor-coordinates
                                   will-live?
                                   alive-neighbors
                                   Cell]]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest game-tests

  (testing "tick"

    (testing "of empty world should return an empty world"

      (is (= #{} (tick #{}))))

    (testing "of Tetris block should be unmodified"

      (let [world #{(Cell. [0 0] :alive) (Cell. [0 1] :alive)
                    (Cell. [1 0] :alive) (Cell. [1 1] :alive)}]
        (is (= world (tick world)))))

    (testing "of three cell line should oscillate"

      (let [world #{(Cell. [0 1] :alive) (Cell. [1 1] :alive) (Cell. [2 1] :alive)}
            world' #{(Cell. [1 0] :alive) (Cell. [1 1] :alive) (Cell. [1 2] :alive)}]
        (is (= world' (tick world))))))


  (testing "neighbor-coordinates"

    (testing "should return a set of the 8 coordinates around the cell"
      (let [ns (neighbor-coordinates (Cell. [1 1] :alive))
            expected #{[0 0] [1 0] [2 0]
                       [0 1]       [2 1]
                       [0 2] [1 2] [2 2]}]
        (is (= 8 (count ns)))
        (is (= expected ns)))))

  (testing "alive-neighbors"
    (let [ncoords (neighbor-coordinates (Cell. [1 1] :alive))
          world #{(Cell. [1 0] :alive) (Cell. [1 1] :alive) (Cell. [1 2] :alive)
                  (Cell. [2 1] :dead)}
          expected #{(Cell. [1 0] :alive) (Cell. [1 2] :alive)}
          alive-ns (into #{} (alive-neighbors ncoords) world)]
      (is (= 2 (count alive-ns)))
      (is (= expected alive-ns))))

  (testing "will-live?"

    (testing "alive cell with between two and three living neighbors will live"
      (let [c (Cell. [1 1] :alive)
            world-with-two-neighbors #{(Cell. [1 1] :alive)
                                       (Cell. [0 1] :alive)
                                       (Cell. [2 1] :alive)}
            world-with-three-neighbors (conj world-with-two-neighbors
                                             (Cell. [0 0] :alive))]
        (is (= true (will-live? c world-with-two-neighbors)))
        (is (= true (will-live? c world-with-three-neighbors)))))

    (testing "alive cell with less than two living neighbors will die"
      (let [c (Cell. [1 1] :alive)
            world #{c}]
        (is (= false (will-live? c world)))
        (is (= false (will-live? c (conj world (Cell. [0 1] :alive)))))))

    (testing "dead cell with exactly three living neighbors will live"
      (let [c (Cell. [1 1] :dead)
            world #{(Cell. [0 1] :alive) c (Cell. [2 1] :alive) (Cell. [1 2] :alive)}]
        (is (= true (will-live? c world)))))))

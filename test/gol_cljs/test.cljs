(ns ^:figwheel-always gol-cljs.test
  (:require [doo.runner :refer-macros [doo-tests]]
            [gol-cljs.game-tests]
            [gol-cljs.grid-tests]))

(doo-tests 'gol-cljs.game-tests
           'gol-cljs.grid-tests)

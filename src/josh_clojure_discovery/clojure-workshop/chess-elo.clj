(ns packt-clj.chess-elo
  (:require
   [clojure.java.io :as io]
   [clojure.data.csv :as csv]
   [semantic-csv.core :as sc]
   [clojure.math.numeric-tower :as math]))

(defn match-probability [player-1-rating player-2-rating]
  (/ 1
     (+ 1
        (math/expt 10 (/ (- player-2-rating player-1-rating)
                         400 )))))

(def k-factor 32)

(defn recalculate-rating [previous-rating expected-outcome real-outcome]
  (+ previous-rating (* k-factor (- real-outcome expected-outcome))))
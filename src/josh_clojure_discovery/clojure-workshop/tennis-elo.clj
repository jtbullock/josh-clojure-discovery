(ns packt-clj.tennis-elo
  (:require
   [clojure.java.io :as io]
   [clojure.data.csv :as csv]
   [semantic-csv.core :as sc]
   [clojure.math.numeric-tower :as math]))

(defn match-probability [player-1-rating player-2-rating]
  (/ 1
     (+ 1
        (math/expt 10 (/ (- player-2-rating player-1-rating)
                         400)))))

(defn recalculate-rating [previous-rating expected-outcome real-outcome k-factor]
  (+ previous-rating (* k-factor (- real-outcome expected-outcome))))

(def csv-path "resources/match_scores_1991-2016_unindexed_csv.csv")

(defn process-ratings
  [csv-path k-factor]
  (with-open [r (io/reader csv-path)]
    (->> (csv/read-csv r)
      sc/mappify
      (sc/cast-with {:winner_sets_won sc/->int :loser_sets_won sc/->int})
      (reduce (fn [acc {:keys [winner_slug loser_slug]}]
                (-> acc
                 (update-in [:player-ratings winner_slug] 
                            (fn [rating] 
                              (let [defaulted-winner-rating (or rating 0)
                                    defaulted-loser-rating (or (get-in acc [:player-ratings loser_slug]) 0)
                                    winner-probability (match-probability defaulted-winner-rating defaulted-loser-rating)]
                                (recalculate-rating defaulted-winner-rating winner-probability 1 k-factor))))
                 (update-in [:player-ratings loser_slug] 
                            (fn [rating] 
                              (let [defaulted-winner-rating (or (get-in acc [:player-ratings winner_slug]) 0)
                                    defaulted-loser-rating (or rating 0)
                                    loser-probability (match-probability defaulted-loser-rating defaulted-winner-rating)]
                                (recalculate-rating defaulted-loser-rating loser-probability 1 k-factor))))

                ;(update-in acc [:player-ratings loser_slug] (fn [rating] (recalculate-rating rating (match-probability rating (get-in acc [:player-ratings winner_slug])) 0 k-factor)))
                 (update :total_matches #(inc %))))
              
       {:player_ratings {}, :success_count 0, :total_matches 0, :prediction_count 0}))))
     
    
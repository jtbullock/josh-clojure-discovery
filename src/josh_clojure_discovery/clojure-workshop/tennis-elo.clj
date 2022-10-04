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

; Activity 5.01
(defn process-ratings
  [csv-path k-factor]
  (with-open [r (io/reader csv-path)]
    (->> (csv/read-csv r)
      sc/mappify
      ;(sc/cast-with {:winner_sets_won sc/->int :loser_sets_won sc/->int})
      (reduce (fn [acc {:keys [winner_slug loser_slug]}]
                (let [winner_rating (get-in acc [:player_ratings winner_slug] 400) 
                      loser_rating (get-in acc [:player_ratings loser_slug] 400)
                      winner_probability (match-probability winner_rating loser_rating) 
                      loser_probability (match-probability loser_rating winner_rating)
                      able_to_predict? (not (= winner_probability loser_probability))
                      predition_correct? ( > winner_probability loser_probability )]
                 (-> acc
                  (assoc-in [:player_ratings winner_slug] (recalculate-rating winner_rating winner_probability 1 k-factor))
                  (assoc-in [:player_ratings loser_slug] (recalculate-rating loser_rating loser_probability 0 k-factor))
                  (update :total_matches inc)
                  (update :prediction_count #(if able_to_predict? (inc %) %))
                  (update :success_count #(if predition_correct? (inc %) %))
                 )))
       {:player_ratings {}, :success_count 0, :total_matches 0, :prediction_count 0}))))
     
(defn query-game-records []
  (with-open [r (io/reader csv-path)]
    (->> (csv/read-csv r)
      sc/mappify
      (sc/cast-with {:winner_sets_won sc/->int :loser_sets_won sc/->int})
      count
         )))
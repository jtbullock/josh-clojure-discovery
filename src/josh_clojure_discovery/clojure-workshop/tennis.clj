#_{:clj-kondo/ignore [:namespace-name-mismatch]}
( ns packt-clj.tennis
  ( :require
     [ clojure.data.csv :as csv]
     [ clojure.java.io :as io]
     [ semantic-csv.core :as sc]))

( defn first-match [ csv]
  ( with-open [ r ( io/reader csv)]
    ( ->> ( csv/read-csv r ) sc/mappify first)))

( defn five-matches [ csv]
 ( with-open [ r ( io/reader csv)]
  ( ->> ( csv/read-csv r)
   sc/mappify
   ( map #( select-keys % [ :tourney_year_id :winner_name :loser_name
                            :winner_sets_won :loser_sets_won]))
   ( sc/cast-with { :winner_sets_won sc/->int :loser_sets_won sc/->int})
   ( take 5)
   doall)))

( def csv-file-name "resources/match_scores_1991-2016_unindexed_csv.csv")

( defn federer-wins [ csv]
 ( with-open [ r ( io/reader csv)]
  ( ->> ( csv/read-csv r)
   sc/mappify
   ( filter #( = "Roger Federer" ( :winner_name %)))
   ( map #( select-keys % [ :tourney_year_id :winner_name :loser_name
                            :winner_sets_won :loser_sets_won]))
   ( sc/cast-with { :winner_sets_won sc/->int :loser_sets_won sc/->int})
   doall)))

( defn match-query [ csv pred ]
 ( with-open [ r ( io/reader csv)]
  ( ->> ( csv/read-csv r)
   sc/mappify
   ( sc/cast-with { :winner_sets_won sc/->int :loser_sets_won sc/->int  
                    :loser_games_won sc/->int :winner_games_won sc/->int})
   ( filter pred)
   ( map #( select-keys % [ :tourney_year_id :winner_name :loser_name
                            :winner_sets_won :loser_sets_won
                            :winner_games_won :loser_games_won
                            :tourney_year_id :tourney_slug ] ) )
   doall ) ) )

( def federer-games-pred #( ( hash-set ( :winner_name % ) ( :loser_name % ) ) "Roger Federer"))

( def federer-nadal-pred #( = ( hash-set ( :winner_name % ) ( :loser_name % ) ) #{ "Roger Federer" "Rafael Nadal"}))

( def close-match-pred #( and ( = ( hash-set  ( :winner_name % ) ( :loser_name % ) ) #{ "Roger Federer" "Rafael Nadal" } )
                              ( = 1 ( - ( :winner_sets_won % ) ( :loser_sets_won % ) ) ) ) )

; Activity 4.02
( defn select-match-fields [ match ] 
 ( select-keys match [ :winner_name :loser_name 
                       :winner_sets_won :loser_sets_won
                       :winner_games_won :loser_games_won
                       :tourney_year_id :tourney_slug ] ) )

( defn rivalry-data
  "Return data about matches between `player-1` and `player-2` from the provided `csv` data."
  [ csv player-1 player-2 ]
  ( with-open [ r ( io/reader csv ) ]
    ( let [ matches ( ->> ( csv/read-csv r )
                          sc/mappify
                          ( map select-match-fields )
                          ( sc/cast-with { :winner_sets_won sc/->int :loser_sets_won sc/->int  
                                           :winner_games_won sc/->int :loser_games_won sc/->int } )
                          ( filter #( = ( hash-set ( :winner_name % ) ( :loser_name % ) ) #{ player-1 player-2 } ) ) ) 
            player-1-victories ( filter #( = ( :winner_name % ) player-1 ) matches )
            player-2-victories ( filter #( = ( :winner_name % ) player-2 ) matches )
          ]
      {
        :first-victory-player-1 ( first player-1-victories )
        :first-victory-player-2 ( first player-2-victories )
        :total-matches ( count matches )
        :total-victories-player-1 ( count player-1-victories)
        :total-victories-player-2 ( count player-2-victories )
        :most-competitive-matches ( filter #( = 1 ( - ( :winner_sets_won % ) ( :loser_sets_won % ) ) ) matches )
      }      
    )
  )
)
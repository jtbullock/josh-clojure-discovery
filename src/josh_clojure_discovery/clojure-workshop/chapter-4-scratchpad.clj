; Exercise 4.03
( def students [ { :name "Eliza" :year 1994 }
                 { :name "Salma" :year 1995 }
                 { :name "Jodie" :year 1997 }
                 { :name "Kaitlyn" :year 2000 }
                 { :name "Alice" :year 2001 }
                 { :name "Pippa" :year 2002 }
                 { :name "Fleur" :year 2002 } ] )

( take-while #( < ( :year % ) 2000 ) students )

; Exercise 4.04
( defn our-range [limit]
  ( take-while #( < % limit ) ( iterate inc 0 ) ) )

; Exercise 4.05
( def our-randoms ( repeatedly ( partial rand-int 100 ) ) )

( defn some-random-integers [ size ]
  ( take size ( repeatedly ( partial rand-int 100 ) ) ) )

; Exercse 4.06
(def game-users
  [{:id 9342
    :username "speedy"
    :current-points 45
    :remaining-lives 2
    :experience-level 5
    :status :active}
   {:id 9854
    :username "stealthy"
    :current-points 1201
    :remaining-lives 1
    :experience-level 8
    :status :speed-boost}
   {:id 3014
    :username "sneaky"
    :current-points 725
    :remaining-lives 7
    :experience-level 3
    :status :active}
   {:id 2051
    :username "forgetful"
    :current-points 89
    :remaining-lives 4
    :experience-level 5
    :status :imprisoned}
   {:id 1032
    :username "wandering"
    :current-points 2043
    :remaining-lives 12
    :experience-level 7
    :status :speed-boost}
   {:id 7213
    :username "slowish"
    :current-points 143
    :remaining-lives 0
    :experience-level 1
    :status :speed-boost}
   {:id 5633
    :username "smarter"
    :current-points 99
    :remaining-lives 4
    :experience-level 4
    :status :terminated}
   {:id 3954
    :username "crafty"
    :current-points 21
    :remaining-lives 2
    :experience-level 8
    :status :active}
   {:id 7213
    :username "smarty"
    :current-points 290
    :remaining-lives 5
    :experience-level 12
    :status :terminated}
   {:id 3002
    :username "clever"
    :current-points 681
    :remaining-lives 1
    :experience-level 8
    :status :active}])

( map ( fn [ player ] ( :current-points player ) ) game-users )

; Can be refactored to...
( map :current-points game-users )

; ------------------
( def remove-words #{"and" "an" "a" "the" "of" "is" } )
( remove ( comp remove-words clojure.string/lower-case clojure.string/trim ) ["February" " THE " "4th" ] )

; Exercise 4.07
( def keep-statuses #{ :active :imprisoned :speed-boost } )

( filter ( comp keep-statuses :status ) )

( ->> game-users
  ( filter ( comp keep-statuses :status ) )
  ( map :current-points ) )

; ---------------
( def letters [ "a" "b" "c" ] )

( mapcat ( fn [letter] [ letter ( clojure.string/upper-case letter ) ] ) letters )

; map can take multiple list inputs.
( def meals [ "breakfast" "lunch" "dinner" "midnight snack" ] )
( map ( fn [ idx meal ] ( str ( inc idx ) ". " meal ) ) ( range ) meals )
( map-indexed ( fn [ idx meal ] ( str ( inc idx ) ". " meal ) ) meals )

; Exercise 4.08: Identifying Weather Trends
( def temperature-by-day
  [ 18 23 24 23 27 24 22 21 21 20 32 33 30 29 35 28 25 24 28 29 30 ] )

( map ( fn [ today yesterday ] 
  ( cond 
    ( > today yesterday ) :warmer
    ( < today yesterday ) :colder
    ( = today yesterday ) :unchanged ) )
  ( rest temperature-by-day ) temperature-by-day )

; Exercise 4.09: Finding the Average Weather Temperature
( let [ total ( apply + temperature-by-day ) 
        number-of-days ( count temperature-by-day ) ]
  ( / total number-of-days ) )

; Activity 4.01
; Write two functions that report the min and max value for the specified
; numeric field for users with the specified status.

; The fns should take three args: the field, the status, and list of users.
; Example calls:
; ( max-value-by-status :current-points :active game-users )
; ( min-value-by-status :remaining-lives :imprisoned game-users )

; Filter statement:
( filter ( fn [ user ] (= ( user :status ) status ) ) )
( filter #( = ( % :status ) :active ) )

( defn max-value-by-status
  [ field status users ]
  ( ->> users
    ( filter #( = ( % :status ) status ) ) 
    ( map field )
    ( apply max 0 ) ) )

( defn min-value-by-status
  [ field status users ]
  ( ->> users
    ( filter #( = ( % :status ) status ) ) 
    ( map field )
    ( apply min 0 ) ) )

; Tests
( max-value-by-status :experience-level :imprisoned game-users )
( min-value-by-status :experience-level :imprisoned game-users )

( max-value-by-status :experience-level :terminated game-users )
( min-value-by-status :experience-level :terminated game-users )

( max-value-by-status :remaining-lives :active game-users )
( min-value-by-status :remaining-lives :active game-users )

( max-value-by-status :current-points :speed-boost game-users )

; Exercise 4.10
( with-open [ r ( io/reader "resources/match_scores_1991-2016_unindexed_csv.csv") ] 
  ( count ( csv/read-csv r ) ) )

; Exercise 4.11
( def stat-file-path "resources/match_scores_1991-2016_unindexed_csv.csv" )

( with-open [ r ( io/reader stat-file-path ) ] 
  ( ->> ( csv/read-csv r )
    ( map #( nth % 7 ) )
    ( take 6 )
    doall ) )

; Exercise 4.12

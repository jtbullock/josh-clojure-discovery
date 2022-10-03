(ns josh-clojure-discovery.clojure-workshop.chapter-5-scratchpad)

; Exercise 5.01
(def weather-days
  [{:max 31
    :min 27
    :description :sunny
    :date "2019-09-24"}
   {:max 28
    :min 25
    :description :cloudy
    :date "2019-09-25"}
   {:max 22
    :min 18
    :description :rainy
    :date "2019-09-26"}
   {:max 23
    :min 16
    :description :stormy
    :date "2019-09-27"}
   {:max 35
    :min 19
    :description :sunny
    :date "2019-09-28"}])

( apply max ( map :max weather-days ) )

( reduce ( fn [ max-day-so-far this-day ]
    ( if ( > ( :max this-day ) ( :max max-day-so-far ) )
      this-day
      max-day-so-far ) )
  weather-days )

( reduce ( fn [ max-day-so-far this-day ]
    ( if ( < ( :max this-day ) ( :max max-day-so-far ) )
      this-day
      max-day-so-far ) )
  weather-days )

; ------------

( def numbers [ 4 9 2 3 7 9 5 2 6 1 4 6 2 3 3 6 1 ] )

( defn parity-totals [ ns ]
 ( :ret ; Only return the return value
  ( reduce ( fn [ { :keys [ current ] :as acc } n ]
            ( if ( and
                  ( seq current )
                  ( or 
                   ( and ( odd? ( last current ) ) ( odd? n ) )
                   ( and ( even? ( last current ) ) ( even? n ) ) ) )
             ( -> acc
              ( update :ret conj [ n ( apply + current ) ] )
              ( update :current conj n ) )
             ( -> acc
              ( update :ret conj [ n 0 ] )
              ( assoc :current [ n ] ) ) ) )
   { :current [] :ret [] }
   ns ) ) )

; Exercise 5.02
(def distance-elevation
  [[0 400]
   [12.5 457]
   [19 622]
   [21.5 592]
   [29 615]
   [35.5 892]
   [39 1083]
   [43 1477]
   [48.5 1151]
   [52.5 999]
   [57.5 800]
   [62.5 730]
   [65 1045]
   [68.5 1390]
   [70.5 1433]
   [75 1211]
   [78.5 917]
   [82.5 744]
   [84 667]
   [88.5 860]
   [96 671]
   [99 584]
   [108 402]
   [115.5 473]])

( defn same-slope-as-current? [ current elevation ]
 ( or
  ( = 1 ( count current ) )
  ( let [ [ [ _ next-to-last ] [ _ the-last ] ] ( take-last 2 current ) ]
   ( or
    ( >= next-to-last the-last elevation )
    ( <= next-to-last the-last elevation ) ) ) ) )

( defn distances-elevation-to-next-peak-or-valley
 [ data ]
 ( -> 
  ( reduce
   ( fn [ { :keys [ current ] :as acc } [ distance elevation :as this-position ] ] 
    ( cond
     ( empty? current )
      { :current [ this-position ]
        :calculated [ { :race-position distance, :elevation elevation,
                        :distance-to-next 0, :elevation-to-next 0 } ] }
     ( same-slope-as-current? current elevation )
      ( -> acc
       ( update :current conj this-position )
       ( update :calculated conj { :race-position distance, :elevation elevation
                                   :distance-to-next ( - ( first ( first current ) ) distance )
                                   :elevation-to-next ( - ( second ( first current ) ) elevation ) } ) )
     :otherwise-slope-change
     ( let [ [ prev-distance prev-elevation :as peak-or-valley ] ( last current ) ]
      ( -> acc
       ( assoc :current [ peak-or-valley this-position ] )
       ( update :calculated conj { :race-position distance, :elevation elevation
                                     :distance-to-next ( - prev-distance distance )
                                     :elevation-to-next ( - prev-elevation elevation ) } ) ) ) ) )
   { :current [] :calculated [] }
   ( reverse data ) )
  :calculated
  reverse ) )

; Exercise 5.04
(def matches
  [{:winner-name "Kvitova P.",
    :loser-name "Ostapenko J.",
    :tournament "US Open",
    :location "New York",
    :date "2016-08-29"}
   {:winner-name "Kvitova P.",
    :loser-name "Buyukakcay C.",
    :tournament "US Open",
    :location "New York",
    :date "2016-08-31"}
   {:winner-name "Kvitova P.",
    :loser-name "Svitolina E.",
    :tournament "US Open",
    :location "New York",
    :date "2016-09-02"}
   {:winner-name "Kerber A.",
    :loser-name "Kvitova P.",
    :tournament "US Open",
    :location "New York",
    :date "2016-09-05"}
   {:winner-name "Kvitova P.",
    :loser-name "Brengle M.",
    :tournament "Toray Pan Pacific Open",
    :location "Tokyo",
    :date "2016-09-20"}
   {:winner-name "Puig M.",
    :loser-name "Kvitova P.",
    :tournament "Toray Pan Pacific Open",
    :location "Tokyo",
    :date "2016-09-21"}])

( def matches-by-date ( zipmap ( map :date matches ) matches ) )

; Exercise 5.05

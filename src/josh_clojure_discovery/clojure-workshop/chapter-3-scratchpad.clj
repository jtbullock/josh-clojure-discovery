; Exercise 3.01 - Destructuring
( def booking [ 
    1425
    "Bob Smith"
    "Allergic to unsalted peanuts only"
    [ [ 48.9615, 2.4372 ], [ 37.742, -25.6976 ] ]
    [ [ 37.742, -25.6976 ], [ 48.9615, 2.4372 ] ] ] )

( let [ [ id customer-name sensitive-info flight1 flight2 flight3 ] booking ] 
    ( println id customer-name flight1 flight2 flight3 ) )

( let [ 
    big-booking ( 
        conj booking 
            [ [ 37.742, -25.6976 ], [ 51.1537, 0.1821 ] ]
            [ [ 51.1537, 0.1821 ], [ 48.9615, 2.4372 ] ] )
    [ id customer-name sensitive-info flight1 flight2 flight3 ] big-booking ]
    ( println id customer-name flight1 flight2 flight3 ) )

( let [ [ _ customer-name _ flight1 flight2 flight3 ] booking ] 
    ( println customer-name flight1 flight2 flight3 ) )

( let [ [ _ customer-name _ & flights ] booking ] 
    ( println ( str customer-name " booked " ( count flights ) " flights." ) ) )

( defn print-flight [ flight ] 
    ( let [ [ [ lat1 lon1 ] [ lat2 lon2 ] ] flight ]
        ( println ( str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat "
            lat2 " Lon " lon2 ) ) ) )

( defn print-flight [ flight ] 
    ( let [
        [ departure arrival ] flight  
        [ lat1 lon1 ] departure
        [ lat2 lon2 ] arrival ]
        ( println ( str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat "
            lat2 " Lon " lon2 ) ) ) )

( defn print-booking [ booking ]
    ( let [ [ _ customer-name _ & flights ] booking ] 
        ( println ( str customer-name " booked " ( count flights ) " flights." ) )
        ( let [ [ flight1 flight2 flight3 ] flights ] 
            ( when flight1 ( print-flight flight1 ) )
            ( when flight2 ( print-flight flight2 ) )
            ( when flight3 ( print-flight flight3 ) ) ) ) )

; Exercise 3.02 - Associative Destructuring
( def mapjet-booking {
    :id 8773
    :customer-name "Alice Smith"
    :catering-notes "Vegetarian on Sundays"
    :flights [
        { :from { :lat 48.9615 :lon 2.4372 :name "Paris Le Bourget Airport" }
          :to { :lat 37.742 :lon -25.6976 :name "Ponta Delgada Airport" } },
        { :from { :lat 37.742 :lon -25.6976 :name "Ponta Delgada Airport" }
          :to { :lat 48.9615 :lon 2.4372 :name "Paris Le Bourget Airport" } } ] } )

( let [ { :keys [ customer-name flights ] } mapjet-booking ]
    ( println ( str customer-name " booked " ( count flights ) " flights." ) ) )

; V1
( defn print-mapjet-flight [ flight ]
    ( let [ { :keys [ from to ] } flight
        { lat1 :lat lon1 :lon } from
        { lat2 :lat lon2 :lon } to ]
    ( println ( str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat "
            lat2 " Lon " lon2 ) ) ) )

; V2
( defn print-mapjet-flight [ flight ]
    ( let [ { { lat1 :lat lon1 :lon } :from, { lat2 :lat lon2 :lon } :to } flight ]
    ( println ( str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat "
            lat2 " Lon " lon2 ) ) ) )

( defn print-mapjet-booking [ booking ]
    ( let [ { :keys [ customer-name flights ] } booking ]
        ( println ( str customer-name " booked " ( count flights ) " flights." ) ) 
        ( let [ [ flight1 flight2 flight3 ] flights ] 
            ( when flight1 ( print-mapjet-flight flight1 ) )
            ( when flight2 ( print-mapjet-flight flight2 ) )
            ( when flight3 ( print-mapjet-flight flight3 ) ) ) ) )

; Exercise 3.03
( def weapon-damage { :fists 10.0 :staff 35.0 :sword 100.0 :cast-iron-saucepan 150.0 } )

; V1
( defn strike
  ( [ target weapon ]
    ( let [ points ( weapon weapon-damage ) ]
      ( if ( = :gnomes ( :camp target ) )
        ( update target :health + points )
        ( let [ 
            armor ( or ( :armor target ) 0 )
            damage ( * points ( - 1 armor ) ) ]
            ( update target :health - damage ) ) ) ) ) )

; V2
( defn strike
  "With one argument, strike a target with a default :fists `weapon`. 
  With two arguments, strike a target with `weapon`.
  Strike will heal a target that belongs to the gnomes camp." 
  ( [ target ] ( strike target :fists ) )
  ( [ { :keys [ camp armor ], :or { armor 0 }, :as target } weapon ]
    ( let [ points ( weapon weapon-damage ) ]
      ( if ( = :gnomes camp )
        ( update target :health + points )
        ( let [ damage ( * points ( - 1 armor ) ) ]
            ( update target :health - damage ) ) ) ) ) )

( def enemy { :name "Zulkaz", :health 250, :armor 0.8, :camp :trolls } )

( def ally { :name "Carla", :health 80, :camp :gnomes } )

; Exercise 3.04
( def weapon-fn-map 
    { :fists ( fn [ health ] ( if ( < health 100 ) ( - health 10 ) health ) ) 
      :staff ( partial + 35 ) 
      :sword #( - % 100 )
      :cast-iron-saucepan #( - % 100 ( rand-int 50 ) ) 
      :sweet-potato identity } )

( defn strike
  "With one argument, strike a target with a default :fists `weapon`.
  With two arguments, strike a target with `weapon` and return the target entity."
  ( [ target ] ( strike target :fists ) )
  ( [ target weapon ]
    ( let [ weapon-fn ( weapon weapon-fn-map ) ]
       ( update target :health weapon-fn ) ) ) )

( update enemy :health ( comp ( :sword weapon-fn-map ) ( :cast-iron-saucepan weapon-fn-map ) ) )

( defn mighty-strike
  "Strike a `target` with all weapons!"
  [ target ]
  ( let [ weapon-fn ( apply comp ( vals weapon-fn-map ) ) ]
    ( update target :health weapon-fn ) ) )

; Exercise 3.05
( def player { :name "Lea" :health 200 :position { :x 10 :y 10 :facing :north } } )

( defmulti move #( :facing ( :position % ) ) )

( ns-unmap 'user 'move )

( defmulti move ( comp :facing :position ) )

( defmethod move :north [ entity ] ( update-in entity [ :position :y ] inc ) )

( defmethod move :south [ entity ] ( update-in entity [ :position :y ] dec ) )

( defmethod move :east [ entity ] ( update-in entity [ :position :x ] inc ) )

( defmethod move :west [ entity ] ( update-in entity [ :position :x ] dec ) )

( defmethod move :default [ entity ] entity )

; Activity 3.01
( def walking-speed 5 ) ; km/h
( def driving-speed 70 ) ; km/h

( def paris { :lat 48.856483 :lon 2.352413 } )
( def bordeaux { :lat 44.834999 :lon -0.575490 } )

; Calculate distance
; take two location parameters
; Use a combo of sequential and associative destructuring in fn params
; Use a let expression to break up the calculation
( defn distance
  "Calculate the distance between two coordinates"
  [ { lat1 :lat lon1 :lon }
    { lat2 :lat lon2 :lon } ]
  ( let [ lat1-cos ( Math/cos lat1 )
          x ( Math/pow ( - lat2 lat1 ) 2 )
          y ( Math/pow ( - lon2 lon1 ) 2 ) ]
    ( * 110.25 ( Math/sqrt ( + x ( * lat1-cos y ) ) ) ) ) )

( def sample-drive { :from paris, :to bordeaux, :transport :driving, :vehicle :sporche } )
( def sample-walk { :from paris, :to bordeaux, :transport :walking } )

( defmulti itinerary :transport )

( defmethod itinerary :walking
  [ { :keys [ :from :to ] } ]
  ( let [ distance ( distance from to )
          duration ( / distance walking-speed ) ]
    { :cost 0, :distance distance, :duration duration } ) )

( def vehicle-cost-fn-map 
  { :sporche #( * ( * % 0.12 ) 1.5 )
    :tayato #( * ( * % 0.07 ) 1.5 )
    :sleta #( * ( * % 0.2 ) 0.1 ) } )

; Simplified from answer key:
( def vehicle-cost-fn-map 
  { :sporche ( partial * 0.12 1.5 )
    :tayato ( partial * 0.07 1.5 )
    :sleta ( partial * 0.2 0.1 ) } )

( defmethod itinerary :driving
  [ { :keys [ :from :to :vehicle ] } ]
  ( let [ distance ( distance from to )
          duration ( / distance walking-speed ) 
          cost ( ( vehicle-cost-fn-map vehicle ) distance ) ]
    { :cost cost, :distance distance, :duration duration } ) )
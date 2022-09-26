(ns josh-clojure-discovery.clojure-workshop.chapter-2-scratchpad)

; Exercise 2.01: The Obfuscation Machine
(defn encode-letter [s x]
  (let [code ( Math/pow ( + x (int (first (char-array s)))) 2)]
    (str "#" (int code))))

(defn encode [s]
  (let [ number-of-words ( count ( clojure.string/split s #" " ) )]
    (clojure.string/replace s #"\w" (fn [s] ( encode-letter s number-of-words)))))

(defn decode-letter [letter x]
  ( str (char (- (Math/sqrt ( Integer/parseInt( subs letter 1 ))) x))))

(defn decode [message]
  (let [ number-of-words ( count ( clojure.string/split message #" " ) )]
    (clojure.string/replace message #"\#\d+" (fn [s] ( decode-letter s number-of-words)))))

; Exercise 2.02: Using Maps
( def favorite-fruit {
    :name "Kiwi"
    :color "Green"
    :kcal_per_100g 61
    :distinguish_mark "Hairy" } )

( assoc favorite-fruit :yearly_production_in_tonnes {
    :china 2025000
    :italy 541000
    :new_zealand 412000
    :iran 311000
    :chile 225000 } )

; Exercise 2.03: Using Sets
( def supported-currencies #{ "Dollar" "Japanese yen" "Euro" "Indian rupee" "British pound" } )

; Exercise 2.04: Using Vectors
( def fibonacci [ 0 1 1 2 3 5 8 ] )

( let [
  size ( count fibonacci )
  last-number ( last fibonacci )
  second-to-last-number ( fibonacci ( - size 2 ) ) ]
  ( conj fibonacci ( + last-number second-to-last-number ) ) )

; Exercise 2.05: Using Lists
( def my-todo ( list "Feed the cat" "Clean the bathroom" "Save the world" ) )

; Exercise 2.06: Working with Nested Data Structures
( def gemstone-db {
  :ruby {
    :name "Ruby"
    :stock 480
      :sales [ 1990 3644 6376 4918 7882 6747 7495 8573 5097 1712 ]
      :properties {
        :dispersion 0.018
        :hardness 9.0
        :refractive-index [ 1.77 1.78 ]
        :color "Red" }}
  :diamond {
    :name "Diamond"
    :stock 10
    :sales [ 8295 329 5960 6118 4189 3436 9833 8870 9700 7182 7061 1579 ]
    :properties {
      :dispersion 0.044
      :hardness 10
      :refractive-index [ 2.417 2.419 ]
      :color "Typically yellow, brown or gray to colorless"}}
  :moissanite {
    :name "Moissanite"
    :stock 45
    :sales [7761 3220]
    :properties {
      :dispersion 0.104
      :hardness 9.5
      :refractive-index [2.65 2.69]
      :color "Colorless, green, yellow"}}})

( defn durability
  [ db gemstone ]
  ( get-in db [ gemstone :properties :hardness ] ) )

( defn change-color
  [ db gem new-color ]
  ( assoc-in db [ gem :properties :color] new-color ))

( defn sell
  [ db gem client-id ]
  ( let [ clients-updated-db ( update-in db [ gem :sales ] conj client-id ) ]
    ( update-in clients-updated-db [ gem :stock ] dec ) ) )

; Activity 2.01
( def memory-db ( atom {} ) )

( defn read-db [] @memory-db )

( defn write-db [ new-db ] ( reset! memory-db new-db ) )

( defn create-table
  [ table-name ]
  ( write-db ( assoc ( read-db ) table-name {:data [] :indexes {}} )))

( defn drop-table
  [ table-name ]
  ( write-db ( dissoc ( read-db ) table-name ) ) )

; / Get DB value
; / Add new record
; / get index of where record was inserted
; / Add index records to :id-key index
; Write to database

; (insert :people {:id 1 :name "Josh"} :id)

( defn insert
  [ table, record, id-key ]
  ( if ( select-*-where table id-key ( record id-key ) )
      ( printf "Record already exists" )
      ( let [ 
          db ( read-db )
          updated-db ( update-in db [ table :data ] conj record )
          new-record-index ( dec ( count ( get-in updated-db [ table :data ] ) ) ) ] 
        ( write-db 
          ( update-in updated-db [ table :indexes id-key ] assoc ( record id-key ) new-record-index ) ) ) ) )

( defn select-* [ table ] ( get-in ( read-db ) [ table :data ] ) )

; (select-*-where :people :id 4)
( defn select-*-where
  [ table-name field field-value ]
  ( let [ record-index ( get-in ( read-db ) [ table-name :indexes field field-value ] ) ]
    ( get-in ( read-db ) [ table-name :data record-index ] ) ) )

{
  :clients {
    :data [
      { :id 1 :name "Bob" :age 30 }
      { :id 2 :name "Alice" :age 24 } ]
    :indexes {
      :id { 1 0, 2 1 } }
  },
  :fruits {
    :data [
      { :name "Lemon" :stock 10 }
      { :name "Coconut" :stock 3 } ]
    :indexes {
      :name { "Lemon" 0, "Coconut" 1 } }
  }
}

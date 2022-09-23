(ns josh-clojure-discovery.core
  (:require [clojure.string :as str])
  (:gen-class))

;; Define a variable
(def randVar 10) ;; long
(def aDouble 1.233) ;; double
(def aBool true)

(type 10) ;; Execute this an see what happens!
(type false)

(pos? 15)
(neg? 15)
(even? 15)
(number? 15)
(integer? 15)
(float? 15)
(nil? 15)

(defn hello_world []
  (println "Hello, World!"))

;; Format print out stuff
(def aString "I'm a string!") 
(def aLong 15)
(format "This is a string: %s" aString)
(format "5 spaces and %5d" aLong)
(format "Leading zeroes %04d" aLong)
(format "%-4d left justified" aLong)
(format "3 decimals %.3f" aDouble)

;; String stuff
(def str1 "This is a 2 test string")
(str/blank? str1)
(str/includes? str1 "test")
(str/index-of str1 "test")
(str/split str1 #" ")
(str/split str1 #"\d")
(str/join " " ["The", "Big", "Cheese"])
(str/replace "I am 42" "42" "43")
(str/trim "Extra spaces   ")
(str/upper-case str1)

;; List - can contain different data types
(println (list "Dog" 1 3.4 true))
(println (first (list 1 2 3)))
(println (rest (list 1 2 3)))
(println (nth (list 1 2 3) 1))
(println (list* 1 2 [3 4])) ;; Add to a list
(println (cons 3 (list 1 2))) ;; Add to beginning of list

;; Set - list of unique values
(println (set '(1 1 2)))
(println (get (set '(1 1 2)) 1)) ;; 1-based! =0
(println (conj (set '(1 1 2)) 3))
(println (contains? (set '(1 1 2)) 2))
(println (disj (set '(1 1 2)) 1))

;; Vector - same data type
(println (vector 1 2))
(println (get (vector 1 2) 1))
(println (conj (vector 1 2) 4))
(println (pop (vector 1 2 3 4)))
(println (subvec (vector 1 2 3 4) 1 3))

;; Maps
(println (hash-map "Name" "Josh" "Age" 34))
(println (sorted-map 3 42 2 "Bullock" 1 "Josh"))
(println (get (hash-map "Name" "Josh" "Age" 34) "Name"))
(println (find (hash-map "Name" "Josh" "Age" 34) "Name"))
(println (contains? (hash-map "Name" "Josh" "Age" 34) "Name"))
(println (keys (hash-map "Name" "Josh" "Age" 34)))
(println (vals (hash-map "Name" "Josh" "Age" 34)))
(println (merge-with + (hash-map "Name" "Josh") (hash-map "Age" 34)))

;; Atoms
(def sample_atom (atom 10))

(add-watch sample_atom :watcher 
     (fn [_key _atom old_state new_state]
       ( println "sample_atom changed from " old_state " to " new_state )))

(println "1st value: " @sample_atom)
(reset! sample_atom 11)
(println "2nd value: " @sample_atom)
(swap! sample_atom inc)

;; Agents
(def tickets-sold ( agent 0 ))
(send tickets-sold + 15)
(await-for 1000 tickets-sold)
println "Tickets sold: " @tickets-sold

;; Math
(println(+ 1 2 3))
(println(- 5 3 2))
(println(* 2 5))
(println(/ 10 5))
(println(mod 10 3))

(println(inc 5))
(println(dec 5))

(println(Math/abs -10))

(println(rand-int 20))

(println(Math/PI))

;; functions


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (hello_world))

(-main)
(ns josh-clojure-discovery.clojure-workshop.chapter-1-scratchpad)

; with ifs
( defn meditate
  "Meditate on the given string `s` with calmness level `level`"
  [ s level ] 
  ( println "Clojure Meditate v2.0" )
  ( if ( <= level 4 )
    ( str ( clojure.string/upper-case s ) ", I TELL YA!" )
    ( if ( <= level 9 )
      ( clojure.string/capitalize s ) 
      ( when ( = level 10 )
        ( clojure.string/reverse s ) 
      )
    )
  )
)

; with cond
( defn meditate
  "Meditate on the given string `s` with calmness level `level`"
  [ s level ]
  ( println "Clojure Meditate v2.0" )
  ( cond
    ( <= level 4 ) (str (clojure.string/upper-case s) ", I TELL YA!")
    ( <= level 9 ) (clojure.string/capitalize s)
    ( = level 10 ) (clojure.string/reverse s)
  )
)


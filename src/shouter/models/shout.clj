(ns shouter.models.shout
	(:require [clojure.java.jdbc :as sql]))
	
(def spec (or (System/getenv "DATABASE_URL")
					"postgresql://localhost:5432/shouter"))
					
(defn all []
	(into [] (sql/query spec ["select * from shouts order by id desc"])))
	
(defn sql_filter [terms]
	(println terms)
		(distinct (reduce into (map 
			(fn [term]
			  (if (not= term "")
				(sql/query spec 
				[(str "select * from shouts WHERE body LIKE '%" term "%'")])))
		(vals (dissoc terms :all?))
))))

(defn sql_and_filter [terms]
(into ()
	(reduce clojure.set/intersection
	  (map 
		  (fn [term]
		    (if (not= term "")
		    (into #{}
				  (sql/query spec 
				  [(str "select * from shouts WHERE body LIKE '%" term "%'")]))))
	(vals (dissoc terms :all?))))))
	
(defn one [target]
	(sql/query spec 
		[(str "select * FROM shouts WHERE id = " target)]))
	
(defn create [shout]
	(sql/insert! spec :shouts [:body] 
			[(shout :shout)]))
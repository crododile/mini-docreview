(ns shouter.controllers.shouts
	(:require [compojure.core :refer [defroutes GET POST]]
						[clojure.string :as str]
						[ring.util.response :as ring]
						[shouter.views.shouts :as view]
						[shouter.models.shout :as model]))
						
(defn index []
	(view/index (model/all)))
	
(defn show [target]
	(view/show (model/one target)))
	
(defn scanned_index [terms]
	(view/scanned_index (model/all) terms))
	
(defn matching_documents [terms]
  (view/only_matches (model/sqlFilter terms) terms))
	
(defn create
	[shout]
	(println shout)
	(def matches (view/parse shout))
	(def shot (conj shout [:match matches]))
	(println shot)
	(model/create shot)
	(ring/redirect "/"))
	
(defroutes routes
	(GET "/" [] (index))
	(GET "/:id" [id] (show id))
	(POST "/" [& shout] (create shout))
	(POST "/terms" [& terms] 
	  (println terms)
		(println (:all? terms))
		(println (terms :all?))
		(if (= (:all? terms) "true")
			(do 
				(println "getting all") 
			  (scanned_index terms))
			(do (println "it was flase")
			(matching_documents terms)))))
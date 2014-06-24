(ns shouter.controllers.shouts
	(:require [compojure.core :refer [defroutes GET POST]]
						[clojure.string :as str]
						[ring.util.response :as ring]
						[shouter.views.shouts :as view]
						[shouter.models.shout :as model]))
						
(defn index []
	(view/index (model/all)))
	
(defn new_doc []
	(view/new_doc))
	
(defn search_form []
	(view/search_form))
	
(defn show [target]
	(view/show (model/one target)))
	
(defn scanned_index [terms]
	(view/scanned_index (model/all) terms))
	
(defn and_matching_documents [terms]
	(println (model/sql_and_filter terms))
	(view/and_matches (model/sql_and_filter terms) terms))
	
(defn or_matching_documents [terms]
  (view/or_matches (model/sql_filter terms) terms))
	
(defn create
	[shout]
	(println shout)
	(model/create shout)
	(ring/redirect "/"))
	
(defroutes routes
	(GET "/" [] (search_form))
	(GET "/index" [] (index))
	(GET "/new_doc" [] (new_doc))

	(POST "/" [& shout] (create shout))
	(POST "/terms" [& terms] 
		(if (= (:all? terms) "all")
			  (scanned_index terms)
				(if (=(:all? terms) "either")
					(or_matching_documents terms)
					(and_matching_documents terms)))))
(ns shouter.controllers.shouts
	(:require [compojure.core :refer [defroutes GET POST]]
						[clojure.string :as str]
						[ring.util.response :as ring]
						[ring.middleware.multipart-params :as mp]
						[shouter.views.shouts :as view]
						[shouter.mytika :as tika]
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
	[doc]
	(println doc)
	(model/create doc)
	(ring/redirect "/"))
	
(defroutes routes
	(GET "/" [] (search_form))
	(GET "/index" [] (index))
	(GET "/new_doc" [] (new_doc))

	(POST "/" [& doc] (create doc))
	
	(mp/wrap-multipart-params
		(POST "/file" {params :params} 
			(create (tika/parser (:tempfile (:file params))))))
	(POST "/terms" [& terms] 
		(if (= (:all? terms) "all")
			  (scanned_index terms)
				(if (=(:all? terms) "either")
					(or_matching_documents terms)
					(and_matching_documents terms)))))
					

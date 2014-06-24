(ns shouter.views.shouts
	(:require [shouter.views.layout :as layout]
						[hiccup.core :refer [h]]
						[hiccup.form :as form]
						[hiccup.element :as el]))
						
(defn shout-form []
	[:div {:id "shout-form" :class "add-form"}
		[:form {:action "/file" :method "post" :enctype "multipart/form-data"}
		 [:input {:name "file" :type "file" :size "20"}]
		 [:input {:type "submit" :name "submit" :value "submit"}]]
	 (form/form-to [:post "/"]
								 (form/label "body" "Add Document to DB")
								 (form/text-area "body")
								 (form/submit-button "Create Document"))])
								 
(defn terms-form []
	[:div {:id "shout-form" :class "search-form"}
	 (form/form-to [:post "/terms"]
				 (form/radio-button "all?" "all" "all")"RETURN ALL<br>"
				 (form/radio-button "all?" "either" "either")
				 "Return docs containing EITHER search term<br>"
				 (form/radio-button "all?" "both" "both")
				 "Return docs containing BOTH search terms<br>"
						 (form/label "first" "first search term")
						 (form/text-area "first")
						 (form/label "second" "second search term")
						 (form/text-area "second")
						 (form/submit-button "Mark it up"))])
								 
(defn display-shouts [shouts]
	[:div {:class "shouts sixteen columns alpha omega"}
	 (map
	  (fn [shout] [:h2 {:class "shout"} (h (:body shout))])
		shouts)])
		
(defn display-doc [[doc]]
	[:div {:class "shouts sixteen columns alpha omega"}
		[:div {:class "shout"} (.replaceAll (h (:body doc)) (h (:target doc))
				 (str "<span style='color:blue'>" (h (:target doc)) "</span>"))]
		[:div {:class "shout"} (h (:match doc)) " " (h(:target doc)) "'s"]])	
	
(defn parseAll 
	"use regexes to replace search terms with spans that have highlighting css"
	[shouts terms]
		(map
		  (fn [shout] [:div {:class "shout"}
				(clojure.string/replace (h (:body shout)) 
				  (re-pattern (str  (terms :first) "|" (terms :second)))
					 {(terms :first) (str "<span style='background-color:yellow'>" (terms :first) "</span>")
					 (terms :second) (str "<span style='background-color:red'>" (terms :second) "</span>")})
					 ])
		shouts))
		
(defn or_matches [shouts terms]
  (def parsed (parseAll shouts terms))
	(layout/common "OR matches"
	[:div (str (count parsed) " documents matched EITHER search term")
	parsed]))
	
(defn and_matches [shouts terms]
	(def parsed (parseAll shouts terms))
	(layout/common "AND matches"
	[:div (str (count parsed) " documents matched BOTH search terms")
	parsed]))
	
(defn scanned_index [shouts terms]
		(layout/common "all documents"
			(parseAll shouts terms)))
			
(defn search_form []
	(layout/common "Search"
		(terms-form)))
		
(defn index [shouts]
	(layout/common "Doc Review Jr."
		(display-shouts shouts)
				(terms-form)))
			
(defn show [shout]
	(layout/common "one shout"
	   (display-doc shout)))
		 
(defn new_doc []
	(layout/common "new doc"
		(shout-form)))
								 

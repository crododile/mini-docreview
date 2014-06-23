(ns shouter.views.shouts
	(:require [shouter.views.layout :as layout]
						[hiccup.core :refer [h]]
						[hiccup.form :as form]))
						
(defn shout-form []
	[:div {:id "shout-form" :class "sixteen columns alpha omega"}
	 (form/form-to [:post "/"]
								 (form/label "shout" "What do you want to shout")
								 (form/text-area "shout")
								 (form/label "target" "parse with what?")
								 (form/text-area "target")
								 (form/submit-button "SHOUT!"))])
								 
(defn terms-form []
	[:div {:id "shout-form" :class "sixteen columns alpha omega"}
	 (form/form-to [:post "/terms"]
				 (form/radio-button "all?" "all")"RETUN ALL <br>"
						 (form/radio-button "all?" "some" "false")"Return some <br>"
								 (form/label "first" "first priority")
								 (form/text-area "first")
								 (form/label "second" "second priority")
								 (form/text-area "second")
								 (form/submit-button "Mark it up"))])
								 
(defn parse 
	[shout]
	(def regx
	 (re-pattern (shout :target)))
		(count (re-seq regx (shout :shout))))
								 
(defn display-shouts [shouts]
	[:div {:class "shouts sixteen columns alpha omega"}
	 (map
	  (fn [shout] [:h2 {:class "shout"} (h (:body shout))
			" matches " (h (:target shout)) " " (:match shout) " times"])
		shouts)])
		
(defn display-doc [[doc]]
	[:div {:class "shouts sixteen columns alpha omega"}
		[:div {:class "shout"} (.replaceAll (h (:body doc)) (h (:target doc))
				 (str "<span style='color:blue'>" (h (:target doc)) "</span>"))]
		[:div {:class "shout"} (h (:match doc)) " " (h(:target doc)) "'s"]])	
	
(defn parseAll 
	"use regexes to replace search terms with spans that have hightlighting css"
	[shouts terms]
	(println shouts)
	(println terms)
	[:div  
		(map
		  (fn [shout] [:div {:class "shout"}
				(clojure.string/replace (h (:body shout)) 
				  (re-pattern (str (terms :first) "|" (terms :second)))
					 {(terms :first) (str "<div style='background-color:blue; display:inline'>" (terms :first) "</div>")
					 (terms :second) (str "<div style='background-color:yellow ; display:inline'>" (terms :second) "</div>")})
					 ])
		shouts)])
		
(defn only_matches [shouts terms]
	(layout/common "matching documents"
	(parseAll shouts terms)))
	
(defn scanned_index [shouts terms]
		(layout/common "all shouts parsed"
			(parseAll shouts terms)))
		
(defn index [shouts]
	(layout/common "SHOUTER"
			(shout-form)
			(terms-form)
			(display-shouts shouts)))
			
(defn show [shout]
	(layout/common "one shout"
	   (display-doc shout)))
								 

(ns tika
  (:import [java.io InputStream]
           [org.apache.tika.metadata Metadata]
           [org.apache.tika Tika])
  (:use [clojure.java.io :as io]))

(defn parser
	[filename]
	(let [metadata (Metadata.)
				tika-obj (Tika.)
				text (.parseToString tika-obj (io/input-stream filename) metadata)
				meta (map 
					(fn [name] {name (first (into () (.getValues metadata name)))}) 
					(.names metadata))]
			{:body text :meta meta})

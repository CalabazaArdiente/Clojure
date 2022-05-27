(ns invoice_item)

(def invoice (clojure.edn/read-string (slurp "invoice.edn")))

(defn invoice_item [invoice]
  (->> (get invoice :invoice/items)
       (filter (fn [item]
                 (->> item 
                      (and (item :taxable/taxes) (item :retentionable/retentions))
                      (not ,)
                  )
       (filter (fn [seq]
                 (or (= 19 (get (first (get seq :taxable/taxes)) :tax/rate))
                     (= 1 (get (first (get seq :retentionable/retentions)) :retention/rate))))))
  )

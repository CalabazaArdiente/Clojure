(in-ns 'invoice-spec)
(use 'clojure.walk)

(defn lector [key value]
  (case key
    :issue_date (.parse (java.text.SimpleDateFormat. "dd/MM/yyyy") value)
    :payment_date (.parse (java.text.SimpleDateFormat. "dd/MM/yyyy") value)
    :tax_category :iva
    :tax_rate ((double value) value)
  )
 )

(defn jsonF [file]
  (let [jsonFile (j/read-str (slurp file)
                             :key-fn keyword
                             :value-fn lector)]
    (->> (postwalk-replace {
                       :issue_date      :invoice/issue-date
                       :customer        :invoice/customer
                       :items           :invoice/items
                       :company_name    :customer/name
                       :email           :customer/email
                       :price           :invoice-item/price
                       :quantity        :invoice-item/quantity
                       :sku             :invoice-item/sku
                       :taxes           :invoice-item/taxes
                       :tax_category    :tax/category
                       :tax_rate        :tax/rate
                       } jsonFile) (:invoice)))
  )

(s/valid? ::invoice (jsonFormatter "src/invoice.json"))

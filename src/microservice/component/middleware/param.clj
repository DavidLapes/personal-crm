(ns microservice.component.middleware.param)

(defn format-params-middleware
  [handler]
  (fn [request]
    (let [query-params (-> (:query-params request) (clojure.walk/keywordize-keys))]
      (handler (assoc request :query-params query-params)))))

(ns crm.lib.api.reitit-helper)

(defn get-request-method [request]
  (assert (:request-method request) "Request method missing in request")
  (:request-method request))

(defn get-route-match [request]
  (assert (:reitit.core/match request) "Reitit-Match missing in request")
  (:reitit.core/match request))

(defn get-route-opts [request]
  (let [match (get-route-match request)
        method (get-request-method request)]
    (get-in match [:data method])))

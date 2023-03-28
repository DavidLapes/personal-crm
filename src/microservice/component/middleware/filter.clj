(ns microservice.component.middleware.filter
  (:require [crm.lib.api.reitit-helper :as reitit-helper]
            [crm.api.schema.filter :as filter-schema]))

(defn- is-whitelisted? [filters-whitelist key]
  (-> (key filters-whitelist)
      nil?
      not))

(defn- get-default-filters []
  filter-schema/general-sql-filters)

(defn- get-filters-config [request]
  (let [route-opts (reitit-helper/get-route-opts request)]
    (:filters route-opts)))

;;TODO: Test this
;;TODO: Maybe rename key to crm.filters instead of filters and use this logic across the app?
;;TODO: Maybe add validation for SQL filters like ordering, pagination and limit and do validation in another middleware for all filters?
(defn extract-filters-middleware
  [handler]
  (fn [request]
    (let [request-params (:query-params request)
          default-filters-whitelist (get-default-filters)
          route-filters-whitelist (-> (get-filters-config request) :parameters :query keys)
          whitelist (merge default-filters-whitelist route-filters-whitelist)
          filters (reduce (fn [params [key value]]
                            (if (is-whitelisted? whitelist key)
                              (assoc params key value)
                              params))
                          {}
                          request-params)]
      (-> request
          (assoc :filters filters)
          (assoc :filters-whitelist whitelist))
      (handler (assoc request :filters filter)))))

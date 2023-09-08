(ns microservice.component.middleware.filter
  (:require [crm.lib.api.reitit-helper :as reitit-helper]
            [crm.api.schema.filter :as filter-schema]
            [schema.core :as s]))

(defn- is-whitelisted? [filters-whitelist key]
  (-> (key filters-whitelist)
      some?))

(defn- get-default-filters []
  filter-schema/general-sql-filters)

(defn- get-filters-config [request]
  (let [route-opts (reitit-helper/get-route-opts request)]
    (get-in route-opts [:parameters :query])))

(defn- get-allowed-filters [request]
  (get-filters-config request))

(defn extract-filters-middleware
  [handler]
  (fn [request]
    (let [request-params (:query-params request)
          default-filters-whitelist (get-default-filters)
          route-filters-whitelist (get-allowed-filters request)
          filters-specs (merge default-filters-whitelist route-filters-whitelist)
          filters-whitelist (into #{} (map #(if (s/optional-key? %) (:k %) %) (keys filters-specs)))
          filters (reduce (fn [params [key value]]
                            (if (is-whitelisted? filters-whitelist key)
                              (assoc params key value)
                              params))
                          {}
                          request-params)]
      (-> request
          (assoc :filters filters)
          (assoc :filters-specs filters-specs)
          (assoc :filters-whitelist filters-whitelist)
          (handler)))))

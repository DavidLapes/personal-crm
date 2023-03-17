(ns crm.auth.middleware
  (:require [buddy.auth.middleware :as auth]
            [crm.auth.core :refer [authentication-config]]))

(defn wrap-with-jwt-middleware
  "Returns Ring middleware for JWT-based authentication."
  [{:keys [datasource]} handler]
  (auth/wrap-authentication handler (authentication-config datasource)))

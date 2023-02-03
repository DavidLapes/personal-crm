(ns microservice.component.middleware.auth
  (:require [buddy.auth :refer [authenticated?]]))

(defn wrap-authentication-check
  [handler]
  (fn [request]
    (when-not (authenticated? request)
      (throw (ex-info "User is not authenticated" {:cause :not-authenticated})))
    (handler request)))

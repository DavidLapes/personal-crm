(ns crm.auth.core
  (:require [buddy.auth.backends :as backends]
            [buddy.sign.jwt :as jwt]
            [clojure.java.jdbc :as jdbc]
            [crm.auth.crypto :as crypto]
            [crm.lib.db.utils :as query]
            [crm.service.user :as user-service]
            [microservice.component.params :as params])
  (:import (java.time LocalDateTime)))

(defn- jwt-secret []
  (params/get-param :crm-jwt-secret))

(def ^:private auth-token-expiration-days 14)

(defn valid-token?
  "Checks if given parsed token is valid"
  [token]
  (let [expiration (:expiration token)
        date-time (LocalDateTime/parse expiration)]
    (.isAfter date-time (LocalDateTime/now))))

(defn authentication-fn
  [datasource]
   (fn [auth-data]
     (let [token-user (:user auth-data)
           email (:email token-user)
           model-user (user-service/get-by-email! datasource email)]
       (if (user-service/deleted? model-user)
         (throw (ex-info "User is deleted" {:cause :deleted}))
         (if (user-service/active? model-user)
           (if (valid-token? auth-data)
             model-user
             (throw (ex-info "Authentication token has expired" {:cause :expired-auth-token})))
           (throw (ex-info "User is inactive" {:cause :inactive})))))))

(defn authentication-config
  [datasource]
  "Configuration for Buddy-based authentication middleware wrapper"
  (backends/jws {:realm "CRM API"
                 :authfn (authentication-fn datasource)
                 :token-name "Bearer"
                 :secret (jwt-secret)
                 :on-error (fn [_ ex]
                             (throw ex))}))

(defn get-login-detail-by-email!
  "Returns user by email."
  [datasource email]
  (jdbc/with-db-connection [connection {:datasource datasource}]
    (query/get-one! connection crm.model.user/table-name {:email email})))

(defn generate-auth-token
  "Generates authentication token for given user."
  [user]
  (jwt/sign {:user       {:email (:email user)}
             :expiration (.plusDays (LocalDateTime/now) auth-token-expiration-days)}
            (jwt-secret)))

(defn sign-in
  "Signs in given user and returns generated token if authentication succeeds."
  [datasource email password]
  (let [email (-> email (.toLowerCase))
        user (get-login-detail-by-email! datasource email)
        encrypted-password (:password user)]
    (if (crypto/check-password password encrypted-password)
      (if (user-service/deleted? user)
        (throw (ex-info "User is deleted" {:cause :deleted}))
        (if (user-service/active? user)
          (generate-auth-token user)
          (throw (ex-info "User is inactive" {:cause :inactive}))))
      (throw (ex-info "Invalid password" {:cause :invalid-credentials})))))

(defn sign-up
  "Signs up new user and returns created user with new token if registration succeeds."
  [datasource user]
  (let [password (:password user)
        encrypted-password (crypto/encrypt password)
        user (merge user {:password encrypted-password})]
    (user-service/create! datasource user)))

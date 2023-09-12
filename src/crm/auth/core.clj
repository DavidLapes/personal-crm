(ns crm.auth.core
  (:require [buddy.auth.backends :as backends]
            [buddy.sign.jwt :as jwt]
            [crm.auth.crypto :as crypto]
            [crm.service.user :as user-service]
            [microservice.component.param :as params])
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
         (throw (ex-info "User is deleted" {:cause :user-deleted}))
         (if (user-service/active? model-user)
           (if (valid-token? auth-data)
             model-user
             (throw (ex-info "Authentication token has expired" {:cause :expired-auth-token})))
           (throw (ex-info "User is inactive" {:cause :user-inactive})))))))

(defn authentication-config
  [datasource]
  "Configuration for Buddy-based authentication middleware wrapper"
  (backends/jws {:realm "CRM API"
                 :authfn (authentication-fn datasource)
                 :token-name "Bearer"
                 :secret (jwt-secret)
                 :on-error (fn [_ ex]
                             (throw ex))}))

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
        user (user-service/get-by-credentials! datasource email password)]
    (if (some? user)
      (if (user-service/deleted? user)
        (throw (ex-info "User is deleted" {:cause :user-deleted}))
        (if (user-service/active? user)
          (generate-auth-token user)
          (throw (ex-info "User is inactive" {:cause :user-inactive}))))
      (throw (ex-info "Invalid credentials" {:cause :invalid-credentials})))))

(defn sign-up
  "Signs up new user and returns created user with new token if registration succeeds."
  [datasource user]
  (let [password (:password user)
        encrypted-password (crypto/encrypt password)
        user (merge user {:password encrypted-password})]
    (user-service/create! datasource user)))

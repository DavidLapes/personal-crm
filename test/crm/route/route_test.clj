(ns crm.route.route-test
  (:require [clojure.test :refer :all]
            [crm.core-test :refer [test-system]]
            [crm.lib.http.client :as client]
            [microservice.component.param :refer [get-param]]
            [reitit.core :as reitit]))

(defn get-router []
  "Obtains router from initialized test system."
  (-> @test-system :crm.component/router :router))

(defn get-match
  "Checks whether router contains a route by given path."
  [path]
  (reitit/match-by-path (get-router) path))

(defn get-endpoint
  "Checks whether matched path contains given HTTP method."
  [path-match method]
  (get-in path-match [:result method]))

(defn build-public-path
  "Constructs path to a public endpoint."
  [path]
  (str "/api/public" path))

(defn build-private-path
  "Constructs path to a private endpoint."
  [path]
  (str "/api/private" path))

(defn build-full-public-path
  "Constructs full path to a public endpoint."
  [path]
  (str "http://localhost:" (get-param :crm-web-server-port) "/api/public" path))

(defn build-full-private-path
  "Constructs full path to a private endpoint."
  [path]
  (str "http://localhost:" (get-param :crm-web-server-port) "/api/private" path))

(def authentication-token
  ^{:doc "Retrieves authentication token."}
  (delay
    (let [response (client/request {:method :post
                                    :uri    (build-full-public-path "/auth/sign-in")
                                    :body   {:email "admin@admin.com"
                                             :password "admin"}})
          token    (-> response :body :token)]
      (str "Bearer " token))))

(defn wrap-authentication
  "Wraps a request with proper authentication."
  [request]
  (update-in request [:headers] merge {"Authorization" @authentication-token}))

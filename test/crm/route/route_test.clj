(ns crm.route.route-test
  (:require [clojure.test :refer :all]
            [crm.core-test :refer [test-system]]
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

(defn get-handler
  "Obtains handler from initialized test system."
  []
  (-> @test-system :crm.component/handler :handler))

(defn call-internal-endpoint
  "Executes request with following configuration

  :request-method
  :uri

  against internal endpoint."
  [request]
  (let [handler (get-handler)]
    (handler request)))

(def authentication-token
  ^{:doc "Retrieves authentication token."}
  (delay
    (let [request  {:request-method :post
                    :uri            (build-public-path "/auth/sign-in")
                    :body-params    {:email "test_admin@gmail.com"
                                     :password "test_admin"}}
          response (call-internal-endpoint request)
          token    (-> response :body :token)]
      (str "Bearer " token))))

(defn wrap-authentication
  "Wraps a request with proper authentication."
  [request]
  (update-in request [:headers] merge {"Authorization" @authentication-token}))

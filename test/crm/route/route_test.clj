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

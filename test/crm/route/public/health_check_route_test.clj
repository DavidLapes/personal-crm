(ns crm.route.public.health-check-route-test
  (:require [clojure.test :refer :all]
            [crm.route.route-test :refer [get-match get-endpoint build-public-path]])
  (:import (reitit.core Match)))

(deftest health-check-endpoint-test
  (let [expected-path   (build-public-path "/health-check")
        path-match      (get-match expected-path)]
    (testing (str "Routes contain " expected-path " endpoint(s)")
      (is (instance? Match path-match)))
    (testing (str "GET " expected-path " endpoint present")
      (let [endpoint              (get-endpoint path-match :get)
            {:keys [method path]} endpoint]
        (is (= path expected-path))
        (is (= method :get))))))

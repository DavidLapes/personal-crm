(ns crm.route.private.ping-route-test
  (:require [clojure.test :refer :all]
            [crm.route.route-test :refer [get-match get-endpoint build-private-path]])
  (:import (reitit.core Match)))

(deftest ping-endpoint-test
  (let [expected-path   (build-private-path "/ping")
        path-match      (get-match expected-path)]
    (testing (str "Routes contain " expected-path " endpoint(s)")
      (is (instance? Match path-match)))
    (testing (str "GET " expected-path " endpoint present")
      (let [endpoint              (get-endpoint path-match :get)
            {:keys [method path]} endpoint]
        (is (= path expected-path))
        (is (= method :get))))))

(ns crm.route.public.ping_route_test
  (:require [clojure.test :refer :all]
            [crm.route.route-test :refer [get-match get-endpoint build-public-path call-internal-endpoint]])
  (:import (reitit.core Match)))

(deftest ping-endpoint-test
  (let [expected-path   (build-public-path "/ping")
        path-match      (get-match expected-path)]
    (testing (str "Routes contain " expected-path " endpoint(s)")
      (is (instance? Match path-match)))
    (testing (str "GET " expected-path " endpoint present")
      (let [endpoint              (get-endpoint path-match :get)
            {:keys [method path]} endpoint]
        (is (= path expected-path))
        (is (= method :get))))
    (testing (str "Execution of " expected-path " returns 200")
      (let [request  {:request-method :get
                      :uri expected-path}
            {status :status} (call-internal-endpoint request)]
        (is (= status 200))))))

(ns crm.route.public.ping_route_test
  (:require [clojure.test :refer :all]
            [crm.lib.http.client :as client]
            [crm.route.route-test :refer [get-match get-endpoint build-public-path build-full-public-path]])
  (:import (reitit.core Match)))

(deftest ping-endpoint-test
  (let [expected-path   (build-public-path "/ping")
        full-path       (build-full-public-path "/ping")
        path-match      (get-match expected-path)]
    (testing (str "Routes contain " expected-path " endpoint(s)")
      (is (instance? Match path-match)))
    (testing (str "GET " expected-path " endpoint present")
      (let [endpoint              (get-endpoint path-match :get)
            {:keys [method path]} endpoint]
        (is (= path expected-path))
        (is (= method :get))))
    (testing (str "Execution of " expected-path " returns 200")
      (let [{status :status} (client/request {:method :get
                                              :uri full-path})]
        (is (= status 200))))))

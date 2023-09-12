(ns crm.route.public.health-check-route-test
  (:require [clojure.test :refer :all]
            [crm.lib.http.client :as client]
            [crm.route.route-test :refer [get-match get-endpoint build-public-path build-full-public-path]])
  (:import (reitit.core Match)))

(deftest health-check-endpoint-test
  (let [expected-path   (build-public-path "/health-check")
        full-path       (build-full-public-path "/health-check")
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

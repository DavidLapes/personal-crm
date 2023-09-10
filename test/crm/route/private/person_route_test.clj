(ns crm.route.private.person-route-test
  (:require [clojure.test :refer :all]
            [crm.route.route-test :refer [get-match get-endpoint build-private-path]])
  (:import (reitit.core Match)))

(deftest person-endpoint-test
  (let [expected-path   (build-private-path "/persons")
        path-match      (get-match expected-path)]
    (testing (str "Routes contain " expected-path " endpoint(s)")
      (is (instance? Match path-match)))
    (testing (str "GET " expected-path " endpoint present")
      (let [endpoint              (get-endpoint path-match :get)
            {:keys [method path]} endpoint]
        (is (= path expected-path))
        (is (= method :get))))
    (testing (str "POST " expected-path " endpoint present")
      (let [endpoint              (get-endpoint path-match :post)
            {:keys [method path]} endpoint]
        (is (= path expected-path))
        (is (= method :post))))))

(deftest person-id-endpoint-test
  (let [expected-path   (build-private-path "/persons/:id")
        path-match      (get-match expected-path)]
    (testing (str "Routes contain " expected-path " endpoint(s)")
      (is (instance? Match path-match)))
    (testing (str "GET " expected-path " endpoint present")
      (let [endpoint              (get-endpoint path-match :get)
            {:keys [method path]} endpoint]
        (is (= path expected-path))
        (is (= method :get))))
    (testing (str "PUT " expected-path " endpoint present")
      (let [endpoint              (get-endpoint path-match :put)
            {:keys [method path]} endpoint]
        (is (= path expected-path))
        (is (= method :put))))
    (testing (str "DELETE " expected-path " endpoint present")
      (let [endpoint              (get-endpoint path-match :delete)
            {:keys [method path]} endpoint]
        (is (= path expected-path))
        (is (= method :delete))))))

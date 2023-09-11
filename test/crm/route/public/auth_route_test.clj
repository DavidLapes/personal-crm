(ns crm.route.public.auth-route-test
  (:require [clojure.test :refer :all]
            [crm.route.route-test :refer [get-match get-endpoint build-public-path call-internal-endpoint]])
  (:import (reitit.core Match)))

(deftest auth-endpoint-test
  (let [expected-path   (build-public-path "/auth/sign-in")
        path-match      (get-match expected-path)]
    (testing (str "Routes contain " expected-path " endpoint(s)")
      (is (instance? Match path-match)))
    (testing (str "POST " expected-path " endpoint present")
      (let [endpoint              (get-endpoint path-match :post)
            {:keys [method path]} endpoint]
        (is (= path expected-path))
        (is (= method :post))))
    (testing (str "Wrong credentials " expected-path " returns 401")
      (let [request  {:request-method :post
                      :uri expected-path
                      :body-params {:email    "NON_EXISTING_EMAIL"
                             :password "NON_EXISTING_PASSWORD"}}
            {status :status} (call-internal-endpoint request)]
        (is (= status 401))))))
